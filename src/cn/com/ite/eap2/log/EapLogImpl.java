package cn.com.ite.eap2.log;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.Value;
import org.springframework.transaction.annotation.Transactional;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.hibernate.ref.HibernateConfigurationHelper;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.common.thread.*;
import cn.com.ite.eap2.domain.baseinfo.LogData;
import cn.com.ite.eap2.domain.baseinfo.LogForign;
import cn.com.ite.eap2.domain.baseinfo.LogMain;
import cn.com.ite.eap2.domain.baseinfo.LogSub;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
/**
 * <p>Title cn.com.ite.eap2.log.EapLogImpl</p>
 * <p>Description 业务日志接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-19 上午09:32:59
 * @version 2.0
 * 
 * @modified records:
 */
public class EapLogImpl extends DefaultServiceImpl implements LogInner{
	/**
	 * 要保存的日志数据,键为事务编号，值为日志数据
	 */
	private static Map<String,LogMain> logMap = new LinkedHashMap<String,LogMain>();
	/**
	 * 已经提交的事务编号组，日志以此为前提来保存日志
	 */
	private static List<String> commitTransactions = new ArrayList<String>();
	/**
	 * 各事务的用时
	 */
	private static List<Long> comminTransactionTime = new ArrayList<Long>();

	/**
	 * 构造，同时启动日志保存线程
	 * @modified
	 */
	public EapLogImpl() throws Exception{
		ThreadManger.openThread(new Run(){
			public void hander() {
				try {
					saveLogThreadFun();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public boolean stop() {
				return false;
			}
			public boolean suspend() {
				return false;
			}
			public boolean start() {
				return false;
			}}, 10000, 30*1000);
	}
	/**
	 * 循环体
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void saveLogThreadFun() throws Exception {
		HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
		for(int x=0;x<commitTransactions.size();x++){
			String ct = commitTransactions.get(x);
			LogMain main = logMap.get(ct);
			if(main!=null){
				Map<String,Object> keyToEntityMap = new HashMap<String,Object>();
				for(Object ob:main.getEntitys()){
					String keyField = helper.getPkProperyName(ob.getClass());
					Serializable key = (Serializable)PropertyUtils.getProperty(ob,keyField);
					keyToEntityMap.put(key.toString()+"_"+ob.getClass().getName(), ob);
				}
				Map<String,LogData> keyToLogDataMap = new HashMap<String,LogData>();
				//属性结构转换
				for(int i=0;i<main.getEntitys().size();i++){
					LogData ld = entityToLogSub(main.getEntitys().get(i)
							,(Integer)main.getOpers().get(i),ct,keyToEntityMap,main.getDateArray(),(Map)main.getOlds().get(i));
					ld.setNo(i);
					keyToLogDataMap.put(ld.getDataKey()+"_"+ld.getDataCode(), ld);
					main.getLogDatas().add(ld);
				}
				//临时对象修正
				for(LogData ld:main.getLogDatas()){
					ld.setYear(main.getYear());
					ld.setMonth(main.getMonth());
					for(LogSub ls:ld.getLogSubs()){
						ls.setYear(main.getYear());
						ls.setMonth(main.getMonth());
						if(ls.getLinkData()!=null&&//判断多对一的临时值
								"-1".equals(ls.getLinkData().getLdId())){
							//获取真实值
							LogData okData = keyToLogDataMap.get(ls.getLinkData().getDataKey()+
									"_"+ls.getLinkData().getDataCode());
							ls.setLinkData(okData);
						}
						//处理一对多的临时值
						for(LogForign lf:ls.getLogForigns()){
							lf.setYear(main.getYear());
							lf.setMonth(main.getMonth());
							if(lf.getLinkData()!=null&&//判断多对一的临时值
									"-1".equals(lf.getLinkData().getLdId())){
								//获取真实值
								LogData okData = keyToLogDataMap.get(lf.getLinkData().getDataKey()+
										"_"+lf.getLinkData().getDataCode());
								lf.setLinkData(okData);
							}
						}
					}
				}
				//保存日志
				main.setUseTime(comminTransactionTime.get(x));//设置用时
				getDao().addEntity(main);
			}
			commitTransactions.remove(ct);
			comminTransactionTime.remove(x);
			logMap.remove(ct);
			x--;
		}
	}
	/**
	 * 实体对象转换成日志数据对象
	 * @param entity 实体对象
	 * @param operaterType 操作类型
	 * @param transactionId 事务
	 * @param keyToEntityMap 主健对实体映射
	 * @param date 时间
	 * @param old 修改前的值
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private LogData entityToLogSub(Object entity,int operaterType,String transactionId,Map<String,Object> keyToEntityMap,int[] date,Map old) throws Exception{
		Set<LogSub> subs = new HashSet<LogSub>();
		HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
		PersistentClass persistentClass = helper.getPersistentClass(entity.getClass());
		Property keyProperty = persistentClass.getIdentifierProperty();
		String keyname = keyProperty.getName();
		LogSub keyLog = new LogSub();int index = 1;//以下存储主健数据
		keyLog.setAttCode(keyname);
		keyLog.setAttName("存储编号");
		keyLog.setAttType(0);
		Serializable key = (Serializable)PropertyUtils.getProperty(entity, keyname);
		keyLog.setAttValue(key.toString());
		keyLog.setAttOldValue(key.toString());
		keyLog.setNo(index);
		subs.add(keyLog);
		Iterator<Property> iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			String cascade = property.getCascade();
			Value v = property.getValue();
			LogSub cLog = new LogSub();
			cLog.setAttCode(property.getName());
			cLog.setAttName(helper.getEntityAttributeDesc(entity.getClass(), property.getName()));
			if( v instanceof org.hibernate.mapping.Collection){//集合对象
				cLog.setAttType(3);//集合类型
				String ec = helper.getCollectionElementClass(v);
				if(ec == null||operaterType==3) continue;
				Set refs = new HashSet();
				boolean isManyToMany = (((org.hibernate.mapping.Collection)v).getElement() 
						instanceof org.hibernate.mapping.ManyToOne);//是否为多对多关联
				boolean isSaveOld = isManyToMany;//是否要保存老数据
				if(!isManyToMany&&!cascade.startsWith("all")&&!cascade.startsWith("save"))
					continue;//非多对多且非级联保存的集合数据不存储
				Collection collection = null;
				int size = 0;
				try{
					collection = (Collection)PropertyUtils.getProperty(entity, property.getName());
				    if(collection!=null)size = collection.size();
				}catch(Exception e){}
				if(collection!=null&&size>0){
					Iterator citer = collection.iterator();
					boolean isSave = true;//是否需要保存此关联集合属性
					while(citer.hasNext()){
						Object ref = citer.next();
						if(StringUtils.isEmpty(cLog.getAttValue()))
							cLog.setAttObject(ref.getClass().getName());
						String refEntityName = ref.getClass().getName();
						String refKeyField = helper.getPkProperyName(ref.getClass());
						String refTitleName = helper.getEntityTitlePropertyName(ref.getClass());
						Serializable refKey = (Serializable)PropertyUtils.getProperty(ref,refKeyField);
						String refTitle = (String)PropertyUtils.getProperty(ref,refTitleName);
						LogForign lf = new LogForign();
						lf.setLdName(refTitle);
						lf.setLinkId(refKey.toString());
						lf.setDataType(1);//新数据
						Object ob = keyToEntityMap.get(refKey.toString()+"_"+refEntityName);
						if(ob!=null){//为同时保存的数据
							cLog.setLinkType(1);//级联关联
							LogData linkData = new LogData();
							linkData.setLdId("-1");//标识为暂时数据，最后需要修正为要保存的关联数据
							linkData.setDataKey(refKey.toString());
							linkData.setDataCode(refEntityName);
							lf.setLinkData(linkData);
						}else{//要查询最近的关联数据，由其它事务保存的
							cLog.setLinkType(2);//引用关联
							if(!isManyToMany) isSave = false;
						}
						refs.add(lf);
					}
					if(!isSave) continue;//不是级联保存或多对多关联时，不保存此属性
				}
				if(isSaveOld&&operaterType==2&&old!=null){
					Collection oldCollection = null;
					int oldsize = 0;
					try{
					   oldCollection = (Collection)old.get(property.getName());
					   oldsize = oldCollection.size();
					}catch(Exception e){}
					if(oldCollection!=null&&oldsize>0){
						Iterator citer = oldCollection.iterator();
						while(citer.hasNext()){
							Object ref = citer.next();
							@SuppressWarnings("unused")
							String refEntityName = ref.getClass().getName();
							String refKeyField = helper.getPkProperyName(ref.getClass());
							String refTitleName = helper.getEntityTitlePropertyName(ref.getClass());
							Serializable refKey = (Serializable)PropertyUtils.getProperty(ref,refKeyField);
							String refTitle = (String)PropertyUtils.getProperty(ref,refTitleName);
							LogForign lf = new LogForign();
							lf.setLdName(refTitle);
							lf.setLinkId(refKey.toString());
							lf.setDataType(2);//老数据
							refs.add(lf);
						}
					}
				}else if(collection==null||size==0)
					break;//没有新老数据时，不保存此属性
				cLog.setLogForigns(refs);
			}else if(v instanceof ToOne){//多对一对象
				if(operaterType==3) continue;
				cLog.setAttType(2);//对象类型
				String refEntityName = ((ToOne) v).getReferencedEntityName();
				String refKeyName = helper.getPkProperyName(Class.forName(refEntityName));
				String refTitleName = helper.getEntityTitlePropertyName(Class.forName(refEntityName));
				Object ref = PropertyUtils.getProperty(entity,property.getName());
				if(ref!=null){
					Serializable refKey = (Serializable)PropertyUtils.getProperty(ref,refKeyName);
					if(refKey==null) continue;
					cLog.setLinkId(refKey.toString());
					String refTitle = null;
					try{
						refTitle = (String)PropertyUtils.getProperty(ref,refTitleName);
					}catch(Exception e){
						continue;
					}
					cLog.setAttValue(refTitle);
					cLog.setAttObject(refEntityName);
					Object ob = keyToEntityMap.get(refKey.toString()+"_"+refEntityName);
					if(ob!=null){//为同时保存的数据
						cLog.setLinkType(1);//级联关联
						cLog.setAttValue(refTitle);
						LogData linkData = new LogData();
						linkData.setLdId("-1");//标识为暂时数据，最后需要修正为要保存的关联数据
						linkData.setDataKey(refKey.toString());
						linkData.setDataCode(refEntityName);
						cLog.setLinkData(linkData);
					}else{//要查询最近的关联数据，由其它事务保存的
						cLog.setLinkType(2);//引用关联
						if(old!=null){//找老数据
					    	Object oldRef = old.get(property.getName());
					    	if(oldRef!=null){
					    	  String refOldTitle = null;
					    	  try{
					    	  refOldTitle = (String)PropertyUtils.getProperty(oldRef,refTitleName);
					    	  }catch(Exception e){continue;}
					    	  cLog.setAttOldValue(refOldTitle);
					    	}
					    }
					}
				}else if(old!=null&&operaterType==2){
					//没有新数据时，找老数据
					Object oldRef = old.get(property.getName());
					if(oldRef!=null){
						 try{
				    	   String refOldTitle = (String)PropertyUtils.getProperty(oldRef,refTitleName);
				    	   cLog.setAttOldValue(refOldTitle);
						 }catch(Exception e){
							 continue;
						 }
				    }else continue;//没有新老数据时，不保存此属性
				}else continue;//修改时没有新老数据，不保存此属性
			}else{//其它类型属性
			    cLog.setAttType(1);//一般类型
			    cLog.setLinkType(0);//无关联
			    Object ob = PropertyUtils.getProperty(entity, property.getName());
			    if(ob instanceof Date)
			    	cLog.setAttValue(DateUtils.convertDateToStr((Date)ob, "yyyy-MM-dd HH:mm:ss"));
			    else if(ob!=null)
			        cLog.setAttValue(ob.toString());
			    if(old!=null){
			    	Object oldOb = old.get(property.getName());
			    	if(oldOb instanceof Date)
				    	cLog.setAttOldValue(DateUtils.convertDateToStr((Date)oldOb, "yyyy-MM-dd HH:mm:ss"));
				    else if(oldOb!=null)
				        cLog.setAttOldValue(oldOb.toString());
			    }
			}
			cLog.setNo(index);
			subs.add(cLog);
			index++;
		}
		LogData logData = new LogData();
		logData.setDataCode(entity.getClass().getName());
		logData.setDataName(helper.getTableComment(entity.getClass()));
		logData.setDataOperaterType(operaterType);
		logData.setDataKey(key.toString());
		logData.setLogSubs(subs);//设置对象属性数据
		return logData;
	}
	/**
	 * 查询最近的关联对象数据
	 * @param key
	 * @param type
	 * @param time
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private LogData findNewData(String key,String type,int year){
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("keyTerm", key);
		term.put("codeTerm", type);
		term.put("yearTerm", year);
		List<LogData> lds = (List<LogData>)getDao().queryConfigQl("findNewDataSql", term, null, LogData.class);
		if(lds.size()>0)
			return lds.get(0);
		else{
			if(year <= 2014)
				return null;
			//再向前推一年查
			return this.findNewData(key, type, year-1);
		}
	}
	/**
	 * 增加数据日志处理
	 * @param entity
	 * @param transactionId 事务ID
	 * @param oper 操作类型
	 * @param old 修改前的值
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	static void makeLog(Object entity,String transactionId,int oper,Map old) throws Exception{
		LogMain main = logMap.get(transactionId);
		HibernateConfigurationHelper helper = HibernateConfigurationHelper.getInstance();
		if(main==null){
			main = createLogMain((oper==1?"增加":(oper==2?"修改":"删除"))+
					helper.getTableComment(entity.getClass()));
			main.setType(3);
			logMap.put(transactionId, main);
		}
		main.getEntitys().add(entity);
		main.getOpers().add(oper);
		main.getOlds().add(old);
	}
	/**
	 * 创建日志对象
	 * @param content
	 * @return
	 * @modified
	 */
	private static LogMain createLogMain(String content){
		LogMain log = new LogMain();
		String userInputContent = (String)logContentLocal.get();
		log.setContent(userInputContent==null?content:userInputContent);
		log.setDateArray(new Date(System.currentTimeMillis()));
		log.setIp(ServletContent.getIP());
		UserSession us = LoginAction.getUserSessionInfo();
		if(us!=null){
			log.setApp(us.getAppName());
			log.setDept(us.getCurrentDeptName());
			log.setOrgan(us.getCurrentOrganName());
			log.setEmployee(us.getEmployeeName());
			log.setModuleName(us.getModuleName());
			log.setUser(us.getAccount());
		}
		return log;
	}
	/**
	 * 事务提交
	 * @param transactionId 事务号
	 * @param time 用时
	 * @modified
	 */
	static void commin(String transactionId,long time){
		commitTransactions.add(transactionId);
		comminTransactionTime.add(time);
	}
	@SuppressWarnings("unchecked")
	private static ThreadLocal logContentLocal=new ThreadLocal();
	/**
	 * 操作日志
	 * @param logContent 日志内容
	 */
	@SuppressWarnings("unchecked")
	public void info(String logContent) {
		logContentLocal.set(logContent);
	}
	/**
	 * 登录日志
	 * @param logContent 日志内容
	 * @modified
	 */
	public void login(String logContent) {
		LogMain lm = createLogMain(logContent);
		lm.setType(1);
		//不需要业务操作，直接保存
		String uuid = UUID.randomUUID().toString();
		logMap.put(uuid, lm);
		commitTransactions.add(uuid);
		comminTransactionTime.add(0l);
	}
	/**
	 * 注销日志
	 * @param logContent 日志内容
	 * @modified
	 */
	public void quit(String logContent) {
		LogMain lm = createLogMain(logContent);
		lm.setType(2);
		//不需要业务操作，直接保存
		String uuid = UUID.randomUUID().toString();
		logMap.put(uuid, lm);
		commitTransactions.add(uuid);
		comminTransactionTime.add(0l);
	}
	/**
	 * 警告日志
	 * @param logContent 日志内容
	 * @modified
	 */
	public void warning(String logContent) {
		LogMain lm = createLogMain(logContent);
		lm.setType(5);
		//不需要业务操作，直接保存
		String uuid = UUID.randomUUID().toString();
		logMap.put(uuid, lm);
		commitTransactions.add(uuid);
		comminTransactionTime.add(0l);
	}	
	/**
	 * 异常日志
	 * @param logContent 日志内容
	 * @modified
	 */
	public void error(String logContent) {
		LogMain lm = createLogMain(logContent);
		lm.setType(4);
		//不需要业务操作，直接保存
		String uuid = UUID.randomUUID().toString();
		logMap.put(uuid, lm);
		commitTransactions.add(uuid);
		comminTransactionTime.add(0l);		
	}
	/**
	 * 查询日志操作，对内提供
	 * @param id 标识
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> findLogHandleInfo(String id) {
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("idTerm", id);
		@SuppressWarnings("unused")
		List<Map> maps = getDao().queryConfigQl("findSql", term, 
				null, HashMap.class);
		List<Map> returns = new ArrayList<Map>();
		//数据结构转变
		/*for(Map map:maps){
			
		}*/
		return returns;
	}
	/**
	 * 日志查询
	 * @param user 账号或姓名
	 * @param app 系统
	 * @param module 模块
	 * @param oper 操作
	 * @param content 内容
	 * @param type 类型 1登录2退出3数据操作4异常5警告
	 * @param year 年
	 * @param month 月
	 * @param startDay 开始日
	 * @param endDay 结束日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LogMain> findLog(String user, String app, String module,
			String oper, String content, int logType, int year, int month,
			int startDay, int endDay) {
		Map term = new HashMap();
		term.put("userTerm", user);
		term.put("yearTerm", year);
		term.put("monthTerm", month);
		term.put("dayStartTerm", startDay);
		term.put("dayEndTerm", endDay);
		term.put("typeTerm", logType);
		term.put("contentTerm", content);
		term.put("appTerm", app);
		term.put("moduleTerm", module);
		term.put("workTerm", oper);
		return getDao().queryConfigQl("querySql", term, null, LogMain.class);
	}
}