package cn.com.ite.workflow.manger;

import java.util.*;

import bsh.Interpreter;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.workflow.domain.WorkFlow;
import cn.com.ite.workflow.domain.WorkFlowExcute;
import cn.com.ite.workflow.domain.WorkFlowNode;
import cn.com.ite.workflow.domain.WorkFlowQuarter;
import cn.com.ite.workflow.domain.WorkFlowTo;
import cn.com.ite.workflow.face.AuditInfo;
import cn.com.ite.workflow.face.IClient;
import cn.com.ite.workflow.face.IWorkFlow;


/**
 * <p>Title cn.com.ite.eap2.module.funres.module.ModuleServiceImpl</p>
 * <p>Description 工作流服务实现类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-7 下午03:02:09
 * @version 2.0
 * 
 * @modified records:
 */
public class WorkFlowServiceImpl extends DefaultServiceImpl implements WorkFlowService,IWorkFlow {
	public void setDao(DefaultDAO dao) {
		super.setDao(dao);
		//threadHandle();
	}
	/**
	 * 定时处理
	 * @modified
	 */
	private void threadHandle(){
		Thread timer = new Thread(){
			public void run() {
				try {
					this.sleep(2*60*1000);//暂停2分钟
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		        while(true){
		        	updateTimerTo();
		        	try {
						this.sleep(30*1000);//暂停30秒
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
			}
		};
		timer.start();
	}
	/**
	 * 处理定时导出的任务
	 * @modified
	 */	
	private void updateTimerTo(){
		WorkFlowService service = (WorkFlowService)SpringContextUtil.getBean("workflowServer");
		List tasks = service.queryData("findTimeOutNoExcuteTaskHql", null, null);
        for(WorkFlowExcute excute:(List<WorkFlowExcute>)tasks){
        	WorkFlowNode node = excute.getWorkFlowNode();
        	String employeeId = excute.getExcuteId();
        	if(employeeId==null){
        		//任取此岗位下的一个员工
        		List ems = service.findEntityByField(Employee.class, "quarter.quarterId", 
        				excute.getQuarterId());
        		if(ems.size()==0)
        			continue;
        		else
        			employeeId = ((Employee)ems.get(0)).getEmployeeId();
        	}
        	try {
        		((IWorkFlow)service).next(excute.getExcuteId(), node.getTimerToCode(), null, 
        				employeeId, null, null, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	/**
	 * 查询任务提醒
	 * @param employeeId 创建者
	 * @return
	 * @modified
	 */
	public List<WorkFlowExcute> findTaskTip(String employeeId){
		return ((WorkFlowDao)getDao()).findTaskTip(employeeId);
	}
	/**
	 * 清除提醒
	 * @param excuteId
	 * @modified
	 */
	public void canelTip(String excuteId)throws Exception{
		WorkFlowExcute wfe = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		wfe.getParam().put("notice", "1");
		this.update(wfe);
	}
	/**
	 * 查询流程监控信息
	 * @param manId
	 * @return
	 * @modified
	 */
	public List<FlowInfo> findFlowInfo(Map term,int startNo,int rowSize){
		List<FlowInfo> fis = getDao().queryConfigQl("flowMonitorSql", 
				term, null, FlowInfo.class,startNo,rowSize);
		if(rowSize!=0)
		for(FlowInfo info:fis){
			term.remove("manId");
			term.put("busId", info.getBusId());
			List<Map> maps = this.queryData("findInMan", term, null,null);
			String inMan=null;
			String lastTask=null;
			for(Map map:maps){
				String en = (String)map.get("EMPLOYEE_NAME");
				if(en!=null&&(inMan==null||inMan.indexOf(en)<0)){
					if(inMan==null)
						inMan = en;
					else
						inMan += ","+en;
				}
				String toId = (String)map.get("TO_ID");
				String taskName = (String)map.get("TASK_NAME");
				if(toId==null){
					if(lastTask==null)
						lastTask = taskName;
					else if(lastTask.indexOf(taskName)<0)
						lastTask += ","+taskName;
				}				
			}
			info.setTask(lastTask);
			info.setInMan(inMan);
		}
		return fis;
	}
	/**
	 * 保存流程
	 * @param flow
	 * @throws Exception
	 */
	public void saveFlow(WorkFlow form) throws Exception{
		int i=1;
		Map<String,WorkFlowNode> codeToNode = new LinkedHashMap<String,WorkFlowNode>();
		boolean exitStart = false;
		if(form.getWorkFlowNodes().size()==0)
			throw new Exception("请定义流程结点！");
		String nodeCodes=",";
		String nodeNames=",";
		for(WorkFlowNode node:form.getWorkFlowNodes()){
			if(nodeCodes.indexOf(","+node.getCode()+",")>0)
				throw new Exception("同一流程中结点编码不能重复！");
			if(nodeNames.indexOf(","+node.getName()+",")>0)
				throw new Exception("同一流程中结点名称不能重复！");
			if(!StringUtils.isEmpty(node.getTimerToCode())){
				boolean timerTo = false;
				for(WorkFlowTo to:form.getTos()){
					if(to.getSrcNodeId().equals(node.getCode())&&
					   node.getTimerToCode().equals(to.getCode())){
						timerTo = true;
						break;
					}
				}
				if(!timerTo)
					throw new Exception("定时导向编码‘"+node.getTimerToCode()
							+"’在结点'"+node.getCode()+"'出口导向中不存在！");
			}
			nodeCodes+=node.getCode()+",";
			nodeNames+=node.getName()+",";
			if((i==1&&!node.getType().equals("START"))||(i>1&&node.getType().equals("START")))
				throw new Exception("流程结点中，‘开始’类型结点只能放在第一位并有且只有一个！");
			if((i==form.getWorkFlowNodes().size()&&!node.getType().equals("END"))||
			   (i<form.getWorkFlowNodes().size()&&node.getType().equals("END")))
				throw new Exception("流程结点中，‘结束’类型结点只能放在最后一位并有且只有一个！");
			//if(node.getType().equals("TASK")&&StringUtils.isEmpty(node.getUrl()))
				//throw new Exception("流程结点中，‘任务’类型结点必须填写‘任务打开地址’！");
			if(node.getType().equals("JUDGE")&&StringUtils.isEmpty(node.getJudgeExpress()))
				throw new Exception("流程结点中，‘判断’类型结点必须填写‘表达式’！");
			if(node.getType().equals("DYNAMIC_FORK")&&StringUtils.isEmpty(node.getJudgeExpress()))
				throw new Exception("流程结点中，‘动态分支’类型结点必须填写‘表达式’！");
			if(node.getType().equals("TASK")){
				if(node.getExcuteType().intValue()==1&&node.getWorkFlowQuarters().size()==0)
					throw new Exception("流程结点中，‘任务’类型结点如果执行者类型为手动选定，则要选择‘执行岗位选择’！");
				if(node.getExcuteType().intValue()==4&&StringUtils.isEmpty(node.getJudgeExpress()))
					throw new Exception("流程结点中，‘任务’类型结点如果执行者类型为参数设定，则要填写表达式！");
			}
			if(node.getType().equals("SUB_FLOW")){
				if(this.findEntityByField(WorkFlow.class, "code", node.getCode()).size()==0)
					throw new Exception("流程结点中，‘子流程’类型结点对应工作流未定义！");
			}
			node.setWorkFlow(form);
			node.setOrderNo(i++);
			codeToNode.put(node.getCode(), node);
		}
		i=1;
		String states = ",";//用于判断状态名称是否重复
		String stateCodes = ",";
		if(form.getTos().size()==0)
			throw new Exception("请定义结点导向！");
		for(WorkFlowTo to:form.getTos()){
			if(stateCodes.indexOf(","+to.getCode()+",")>0)
				throw new Exception("结点导向中，同一流程导向编码不能重复！");
			if(states.indexOf(","+to.getState()+",")>0)
				throw new Exception("结点导向中，同一流程导向状态名不能重复！");
			//if(to.getSrcNodeId().equals(to.getToNodeId()))
				//throw new Exception("结点导向中，源结点编码不能等于目的结点编码！");
			states+=to.getState()+",";
			stateCodes+=to.getCode()+",";
			WorkFlowNode src = codeToNode.get(to.getSrcNodeId());
			if(src==null) throw new Exception(to.getSrcNodeId()+"此编码的结点不存在！");
			to.setSrcNode(src);
			WorkFlowNode toNode = codeToNode.get(to.getToNodeId());
			if(toNode==null) throw new Exception(to.getToNodeId()+"此编码的结点不存在！");
			to.setToNode(toNode);			
			if((i==1&&!src.getType().equals("START"))||(i>1&&src.getType().equals("START")))
				throw new Exception("结点导向中，‘开始’类型结点只能设置为第一个导向的开始位置！");
			//if((i==form.getTos().size()&&!toNode.getType().equals("END"))||
			  // (i<form.getTos().size()&&toNode.getType().equals("END")))
				//throw new Exception("结点导向中，‘结束’类型结点只能放在最后一个导向的结束位置！");
			if(src.getType().equals("END"))//||toNode.getType().equals("START"))
				throw new Exception("结点导向中，’结束‘类型结点只能作为导向的结束位置！");
			if((src.getType().equals("FORK")||src.getType().equals("DYNAMIC_FORK"))&&toNode.getType().equals("JOIN"))
				throw new Exception("结点导向中，分技不能直接导向聚合！");
			if(src.getType().equals("START")&&toNode.getType().equals("END"))
				throw new Exception("结点导向中，开始不能直接导向结束！");
			to.setOrderNo(i++);
		}
		//验证非开始结束的结点必须有个入口和出口
		Iterator iterator = codeToNode.keySet().iterator();
		int z = 1;
		while(iterator.hasNext()){
			String code = (String)iterator.next();
			WorkFlowNode node = codeToNode.get(code);
			if(z==1||z==codeToNode.size()){//除开开始和结束
				z++;
				continue;
			}
			int in = 0,out=0;
			for(WorkFlowTo to:form.getTos()){
				if(code.equals(to.getSrcNodeId())) in++;
				if(code.equals(to.getToNodeId())) out++;
			}
			if(in==0)
				throw new Exception(code+"此编码的结点必须在导向中有入口！");
			if(out==0)
				throw new Exception(code+"此编码的结点必须在导向中有出口！");
			if(out>1&&(node.getType().equals("AUTO")||node.getType().equals("JOIN")
				||node.getType().equals("DYNAMIC_FORK")||node.getType().equals("SUB_FLOW")))
				throw new Exception("自动、聚合、动态分支、子流程四类结点只可能有一个出口！");
			z++;
		}
		//验证结构是否正确
		this.testWorkFlowStruts(form);
		if(!StringUtils.isEmpty(form.getFlowId())){
			String toIds=",";
			for(WorkFlowTo to:form.getTos())
			  if(!StringUtils.isEmpty(to.getToId()))
				toIds+=to.getToId()+",";
			Map delete = new HashMap();
			delete.put("flowId", form.getFlowId());
			//删除无关联的导向
			this.deletes(this.queryData("deleteNoTo", delete,null,WorkFlowTo.class));
		}
		this.save(form);
		for(WorkFlowNode node:form.getWorkFlowNodes()){
			if(StringUtils.isEmpty(node.getNodeId())){
				Map term = new HashMap();
				term.put("flowId", form.getFlowId());
				term.put("nodeCode", node.getCode());
				node = (WorkFlowNode)this.queryData("findWorkNodeByCodeFlowId", term, null,WorkFlowNode.class).get(0);
			}
			codeToNode.put(node.getCode(), node);
		}
		for(WorkFlowTo to:form.getTos()){
			WorkFlowNode src = codeToNode.get(to.getSrcNodeId());
			to.setSrcNode(src);
			WorkFlowNode toNode = codeToNode.get(to.getToNodeId());
			to.setToNode(toNode);	
			to.setToId(null);
			this.save(to);
		}
	}
	/**
	 * 验证工作流的结构是否正常
	 * @param flowId
	 * @throws Exception
	 */
	private void testWorkFlowStruts(WorkFlow wf) throws Exception{
		//获取开始结点
		WorkFlowNode startNode = (WorkFlowNode)wf.getWorkFlowNodes().iterator().next();
		Map<String,String> codeToLevel = new HashMap<String,String>();//存储各结点的令牌
		codeToLevel.put(startNode.getCode(), "1");
		//标识是否找到结束结点,初始未找到为0
		codeToLevel.put("$IS_FIND_END_NODE", "0");
		this.testToNode(startNode, codeToLevel,wf.getTos());
		String isFindEndNode = codeToLevel.get("$IS_FIND_END_NODE");
		if(isFindEndNode.equals("0"))
			throw new Exception("流程中定义的所有线路没有一个能流转到结束结点！");
	}
	
	private void testToNode(WorkFlowNode node,Map<String,String> codeToLevel,List<WorkFlowTo> tos)throws Exception{
		String nodeLevel = codeToLevel.get(node.getCode());
		if(node.getType().equals("DYNAMIC_FORK")&&node.getSrcWorkFlowTos().size()>1)
			throw new Exception(node.getCode()+"此编码对应的动态分支结点只能有一条出口！");
		int i=1;
		for(WorkFlowTo to:tos){
			if(!to.getSrcNodeId().equals(node.getCode()))
				continue;
			WorkFlowNode toNode = to.getToNode();
			String toNodeCodeLevel = codeToLevel.get(toNode.getCode());
			if(toNodeCodeLevel!=null){
				if(node.getType().equals("FORK")||node.getType().equals("DYNAMIC_FORK")){
					String level = toNodeCodeLevel;
					level = level.replaceFirst(nodeLevel+".", "");
					if(level.indexOf(".")>0)
						throw new Exception("分支或聚合结点没有完全上下对应！");
				}else
				if(toNode.getType().equals("JOIN")){
					String level = nodeLevel;
					level = level.replaceFirst(toNodeCodeLevel+".", "");
					if(level.indexOf(".")>0)
						throw new Exception("分支或聚合结点没有完全上下对应！");
				}else
				if(!nodeLevel.equals(toNodeCodeLevel))
					throw new Exception("分支或聚合后结点之间不能建立导向！");
			}else{
				if(node.getType().equals("FORK")||node.getType().equals("DYNAMIC_FORK")){
					codeToLevel.put(toNode.getCode(),nodeLevel+"."+i);
				}else
				if(toNode.getType().equals("JOIN")){
					codeToLevel.put(toNode.getCode(),nodeLevel.substring(0,nodeLevel.lastIndexOf(".")));
				}else
					codeToLevel.put(toNode.getCode(),nodeLevel);
				if(toNode.getType().equals("END"))
					codeToLevel.put("$IS_FIND_END_NODE", "1");
				else//再向下验证
				this.testToNode(toNode, codeToLevel,tos);
			}
			i++;
		}
	}
	/**
	 * 流程复制
	 * @param flowId 源流程
	 * @param update 是否修改产生的
	 * @return 新流程
	 * @throws Exception
	 */
	public WorkFlow saveCopy(String flowId,boolean update) throws Exception{
		WorkFlow wf = (WorkFlow)getDao().findEntityBykey(WorkFlow.class,flowId);
		Map<String, Object> term = new HashMap<String, Object>();
		term.put("code", wf.getCode());
		Integer heightVersion = (Integer)this.queryData("heightVersionHql", term, null).get(0);
		WorkFlow copyWf = new WorkFlow();
		copyWf.setCode(wf.getCode());
		copyWf.setName(wf.getName());
		copyWf.setServiceName(wf.getServiceName());
		copyWf.setThat(wf.getThat());
		if(update)
		   copyWf.setVersion(heightVersion+1);
		else{
		   copyWf.setVersion(1);
		   copyWf.setCode(wf.getCode()+"_COPY");
		}
		Map codeToNode = new HashMap();
		for(WorkFlowNode node:wf.getWorkFlowNodes()){
			WorkFlowNode copyNode = new WorkFlowNode();
			copyNode.setCode(node.getCode());
			copyNode.setExcuteType(node.getExcuteType());
			copyNode.setJudgeExpress(node.getJudgeExpress());
			copyNode.setName(node.getName());
			copyNode.setOrderNo(node.getOrderNo());
			copyNode.setType(node.getType());
			copyNode.setUrl(node.getUrl());
			copyNode.setWorkFlow(copyWf);
			for(WorkFlowQuarter wfq:node.getWorkFlowQuarters()){
				WorkFlowQuarter copyWfq = new WorkFlowQuarter();
				copyWfq.setQuarterId(wfq.getQuarterId());
				copyWfq.setQuarterName(wfq.getQuarterName());
				copyNode.getWorkFlowQuarters().add(copyWfq);
			}
			copyWf.getWorkFlowNodes().add(copyNode);
			codeToNode.put(node.getCode(), copyNode);
		}
		this.save(copyWf);
		//复制导向
		term.put("flowId", flowId);
		for(WorkFlowTo to:(List<WorkFlowTo>)this.queryData("toHql", term, null)){
			WorkFlowTo copyTo = new WorkFlowTo();
			copyTo.setCode(to.getCode());
			copyTo.setName(to.getName());
			copyTo.setOrderNo(to.getOrderNo());
			copyTo.setState(to.getState());
			copyTo.setSrcNode((WorkFlowNode)codeToNode.get(to.getSrcNode().getCode()));
			copyTo.setToNode((WorkFlowNode)codeToNode.get(to.getToNode().getCode()));
			this.add(copyTo);
		}
		return copyWf;
	}
	
	/**
	 * 删除流程
	 * @param flowId
	 * @throws Exception
	 */
	public void deleteFlow(String flowId) throws Exception{		
		Map<String, Object> term = new HashMap<String, Object>();
		term.put("flowId", flowId);
		//判断是否被引用
		long c = (Long)this.queryData("useHql", term, null).get(0);
		if(c>0)
			throw new Exception("此流程已经被使用！");
		List tos = this.queryData("toHql", term, null);
		this.deletes(tos);//先删除导向
		WorkFlow wf = (WorkFlow)getDao().findEntityBykey(WorkFlow.class,flowId);
		for(WorkFlowNode node:wf.getWorkFlowNodes()){
			node.setSrcWorkFlowTos(null);
			node.setToWorkFlowTo(null);
		}
		this.delete(wf);
	}
	
	/******以下为工作接口实现*********/
	
	/**
	 * 获取工作流映射
	 * @return
	 */
	public Map<String,String> getFlowMap(){
		List<WorkFlow> wfs = getDao().findAll(WorkFlow.class);
		Map<String,String> map = new HashMap<String,String>();
		for(WorkFlow wf:wfs){
			map.put(wf.getCode(), wf.getName());
		}
		return map;
	}
	
	/**
	 * 查询执行任务
	 * @param flowCode 流程编码
	 * @param employeeId 员工ID
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param taskName 任务名称
	 * @param param 业务参数
	 * @param complet 是否完成
	 * @param undo 是否撤消
	 * @return
	 */
	public List<WorkFlowExcute> findExcuteTask(String flowCode,
			String employeeId, Date startDate, Date endDate, String taskCode,
			Map<String, String> param, Boolean complet,Boolean undo,int startNo,int rowSize) {
		return ((WorkFlowDao)getDao()).findExcuteTask(flowCode, 
				employeeId, startDate, endDate, taskCode, param, complet,undo,startNo,rowSize);
	}
	/**
	 * 根据业务参数查询审核信息
	 * @param param 业务参数
	 * @return
	 */
	public List<AuditInfo> getAuditInfo(String flowCode,Map<String, String> param) {
		List<WorkFlowExcute> excutes = ((WorkFlowDao)getDao()).findExcuteTask(flowCode, 
				null, null, null, null, param, true,false,0,0);
		List<AuditInfo> returns = new ArrayList<AuditInfo>();
		for(WorkFlowExcute e:excutes){
			AuditInfo info = new AuditInfo();
			info.setTaskName(e.getWorkFlowNode().getName());
			info.setEmployeeName(e.getEmployeeName());
			info.setAdvice(e.getAdvice());
			info.setCreateTime(e.getCreateTime());
			info.setTime(e.getExcuteTiime());
			if(e.getWorkFlowTo()!=null)
			info.setTo(e.getWorkFlowTo().getState());
			returns.add(info);
		}
		return returns;
	}
	/**
	 * 删除流程数据
	 * @param flowCode 流程编码
	 * @param param 业务参数
	 * @modified
	 */
	public void deleteFLowData(String flowCode,Map<String,String> param) throws Exception{
		((WorkFlowDao)getDao()).deleteFlowData(flowCode, param);
	}
	/**
	 * 根据流程实例查询审核信息
	 * @param flowInstanceId
	 * @return
	 * @modified
	 */
	public List<AuditInfo> getAuditInfo(String flowInstanceId){
		List<WorkFlowExcute> excutes = 
			((WorkFlowDao)getDao()).findEntityByField(WorkFlowExcute.class, "busId", flowInstanceId);
		List<AuditInfo> returns = new ArrayList<AuditInfo>();
		for(WorkFlowExcute e:excutes){
			if(e.getWorkFlowNode().getType().equals("TASK")&&e.getWorkFlowTo()!=null){
				AuditInfo info = new AuditInfo();
				info.setTaskName(e.getWorkFlowNode().getName());
				info.setEmployeeName(e.getEmployeeName());
				info.setAdvice(e.getAdvice());
				info.setCreateTime(e.getCreateTime());
				info.setTime(e.getExcuteTiime());
				info.setTo(e.getWorkFlowTo().getState());
				returns.add(info);
			}
		}
		return returns;
	}
	/**
	 * 根据执行ID查询状态（操作或导向)映射,用于操作按钮生成
	 * @param excuteId
	 * @return
	 */
	public Map<String, String> getStateMapByExcuteId(String excuteId) {
		WorkFlowExcute excute = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		Map<String, String> ret = new LinkedHashMap<String, String>();
		for(WorkFlowTo to:excute.getWorkFlowNode().getSrcWorkFlowTos()){
			ret.put(to.getCode(), to.getName());
		}
		return ret;
	}
	/**
	 * 获得操作（状态）映射，包括历史版本的
	 * @param workFlowCode 工作流编码
	 * @return 状态映射
	 * @modified
	 */
	public Map<String, String> getStateMapByFlowCode(String workFlowCode) {
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("code", workFlowCode);
		List<WorkFlowTo> list = (List<WorkFlowTo>)this.queryData("findStateMapHql", term, null);
		Map<String, String> ret = new LinkedHashMap<String, String>();
		Map<String,Integer> version = new HashMap<String,Integer>();//存储版本，以便取版本高的状态名称
		for(WorkFlowTo to:list){
			Integer v = to.getSrcNode().getWorkFlow().getVersion();
			Integer vv = version.get(to.getCode());
			if(vv==null||v>vv){
			   ret.put(to.getCode(), to.getState());
			   version.put(to.getCode(), v);
			}
		}
		return ret;
	}
	/**
	 * 获得工作流的功能映射（要判断权限），包括历史版本的
	 * @param workFlowCode 工作流编码
	 * @param employeeId 员工ID，只取此人有权限的
	 * @return 结点编码与结点名称的映射
	 * @modified
	 */
	public Map<String, String> getTaskMap(String workFlowCode, String employeeId) {
		Map<String, String> ret = new LinkedHashMap<String, String>();
		Map<String,Integer> version = new HashMap<String,Integer>();//存储版本，以便取版本高的状态名称
		if(StringUtils.isEmpty(employeeId)){
			List<WorkFlow> wfs = (List<WorkFlow>)this.findEntityByField(WorkFlow.class, "code", workFlowCode);					
			for(WorkFlow wf:wfs)
			for(WorkFlowNode node:wf.getWorkFlowNodes()){
				if(node.getType().equals("TASK")){
					Integer v = node.getWorkFlow().getVersion();
					Integer vv = version.get(node.getCode());
					if(vv==null||v>vv){
						ret.put(node.getCode(), node.getName());
					    version.put(node.getCode(), v);
					}
				}
			}
		}else{
			Map term = new HashMap();
			term.put("flowCode", workFlowCode);
			term.put("employeeId", employeeId);
		    Employee employee = (Employee)this.findDataByKey(employeeId, Employee.class);
		    String quarterId = "";
		    if(employee!=null&&employee.getQuarter()!=null)
		    	quarterId = employee.getQuarter().getQuarterId();
			term.put("quarterId", quarterId);
			List<WorkFlowNode> nodes = this.queryData("findPopdomNodeHql", term, null,WorkFlowNode.class);
			for(WorkFlowNode node:nodes){
				if(node.getType().equals("TASK")){
					Integer v = node.getWorkFlow().getVersion();
					Integer vv = version.get(node.getCode());
					if(vv==null||v>vv){
						ret.put(node.getCode(), node.getName());
					    version.put(node.getCode(), v);
					}
				}
			}
		}
		return ret;
	}
	/**
	 * 获取任务的打开地址，里面包括结点执行ID（excuteId）等等其它业务参数，用于业务功能中的流程打开操作
	 * @param nodeCode
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getTaskUrl(String nodeCode, Map<String, String> param,String employeeId)
			throws Exception {
		List<WorkFlowNode> nodes = (List<WorkFlowNode>)this.findEntityByField(WorkFlowNode.class, "code", nodeCode);
		if(nodes.size()==0)
			throw new Exception("无此编码的功能:"+nodeCode+"!");
		Integer version = 0;//取最新版本
		WorkFlowNode wfn = nodes.get(0);
		List<WorkFlowExcute> tasks = this.findExcuteTask(null, 
				employeeId, null, null, nodeCode, param, false, false,0,0);
		if(tasks.size()==0)
			throw new Exception("此业务暂时无此功能操作！");
		WorkFlowExcute task = tasks.get(0);
		if(param==null) param = new HashMap<String,String>();
		param.putAll(task.getParam());
		String url = wfn.getUrl();
		if(url==null) 
			return "excuteId="+task.getExcuteId();
		if(url.indexOf("$")>0)
		for(String key:param.keySet()){
			url = url.replaceAll("\\$"+key, param.get(key));
		}else
		for(String key:param.keySet()){
			url += (url.indexOf("?")>0?"&":"?")+key+"="+param.get(key);
		}
		url+="&excuteId="+task.getExcuteId();
		return url;
	}
	/**
	 * 获取执行任务的打开地址
	 * @param excuteId
	 * @return
	 */
	public String getTaskUrl(String excuteId) throws Exception{
		WorkFlowExcute excute = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		if(excute==null) throw new Exception("无此执行任务！");
		if(StringUtils.isEmpty(excute.getEmployeeId()))throw new Exception("此任务需要接收才能操作！");			
		return this.getTaskUrl(excute.getWorkFlowNode().getCode(), excute.getParam(),excute.getEmployeeId());
	}
	/**
	 * 判断结束状态
	 * @param workFlowCode 工作流编码
	 * @param state 状态
	 * @param param 业务参数
	 * @return
	 */
	public boolean judgeEndState(String flowCode, String state,
			Map<String, String> param) throws Exception{
		List<WorkFlow> wfs = (List<WorkFlow>)this.findEntityByField(WorkFlow.class, "code", flowCode);
		if(wfs.size()==0) throw new Exception("无此流程"+flowCode+"定义！");
		WorkFlow wf = null;
		Integer version = 0;//取最新版本
		if(wfs.size()>1){
			List tasks = this.findExcuteTask(flowCode, null, null, null, null, param, null, false,0,0);
			if(tasks.size()>0)
				wf = ((WorkFlowExcute)tasks.get(0)).getWorkFlowNode().getWorkFlow();
			else
			for(WorkFlow item:wfs){
				if(item.getVersion()>version){
					wf = item;
					version = item.getVersion();
				}
			}
		}else
			wf = wfs.get(0);
		WorkFlowNode wfn = null;
		for(WorkFlowNode node:wf.getWorkFlowNodes())
			wfn = node;
		WorkFlowTo to = wfn.getToWorkFlowTo().iterator().next();
		return to.getCode().equals(state);
	}
	/**
	 * 判断开始状态
	 * @param workFlowCode 工作流编码
	 * @param state 状态
	 * @param param 业务参数
	 * @return
	 */
	public boolean judgeStartState(String flowCode, String state,
			Map<String, String> param) throws Exception{
		List<WorkFlow> wfs = (List<WorkFlow>)this.findEntityByField(WorkFlow.class, "code", flowCode);
		if(wfs.size()==0) throw new Exception("无此流程"+flowCode+"定义！");
		WorkFlow wf = null;
		Integer version = 0;//取最新版本
		if(wfs.size()>1){
			List tasks = this.findExcuteTask(flowCode, null, null, null, null, param, null, false,0,0);
			if(tasks.size()>0)
				wf = ((WorkFlowExcute)tasks.get(0)).getWorkFlowNode().getWorkFlow();
			else
			for(WorkFlow item:wfs){
				if(item.getVersion()>version){
					wf = item;
					version = item.getVersion();
				}
			}
		}else
			wf = wfs.get(0);
		WorkFlowNode wfn = wf.getWorkFlowNodes().iterator().next();
		WorkFlowTo to = wfn.getSrcWorkFlowTos().iterator().next();
		return to.getCode().equals(state);
	}
	/**
	 * 结点流转
	 * @param flowCode 流程编码
	 * @param toName 导向（操作）名称
	 * @param term 任务查询的业务条件
	 * @param employeeId 员工ID
	 * @param advice 意见
	 * @param ip 操作IP地址
	 * @param ifCopyBeforeParam 是否复制前一结点的业务参数，如果与param参数冲突，则保存param参数
	 * @return 状态编码和名称，奇数位存储状态编码，偶数位存储状态名称
	 * @throws Exception
	 */
	public String[] nextEx(String flowCode,String toName,Map<String,String> term,
		   String employeeId,String advice,String ip,boolean ifCopyBeforeParam) throws Exception{
		return this.nextEx(flowCode, toName, null,term, employeeId, advice, ip, ifCopyBeforeParam);
	}
	/**
	 * 结点流转
	 * @param flowCode 流程编码
	 * @param toName 导向（操作）名称
	 * @param param 业务参数
	 * @param term 任务查询的业务条件
	 * @param employeeId 员工ID
	 * @param advice 意见
	 * @param ip 操作IP地址
	 * @param ifCopyBeforeParam 是否复制前一结点的业务参数，如果与param参数冲突，则保存param参数
	 * @return 状态编码和名称，奇数位存储状态编码，偶数位存储状态名称
	 * @throws Exception
	 */
	public String[] nextEx(String flowCode,String toName,Map<String,String> param,Map<String,String> term,
		    String employeeId,String advice,String ip,boolean ifCopyBeforeParam) throws Exception{
		List<WorkFlowExcute> wes = this.findExcuteTask(flowCode, employeeId, 
				null, null, null, term, false, null, 0, 0);
		if(wes.size()==0)
			throw new Exception("不存在要流转的任务!");
		if(wes.size()>1)
			throw new Exception("请选择具体要操作的任务!");
		String toCode = null;
		for(WorkFlowTo to:wes.get(0).getWorkFlowNode().getSrcWorkFlowTos()){
			if(to.getName().equals(toName))
				toCode = to.getCode();
		}
		if(StringUtils.isEmpty(toName)&&
		   wes.get(0).getWorkFlowNode().getSrcWorkFlowTos().size()>0)//无导向名称时取缺省第一个导向
			toCode = wes.get(0).getWorkFlowNode().getSrcWorkFlowTos().iterator().next().getCode();
		if(toCode==null)
			throw new Exception("不存在此操作名称'"+toName+"'!");
		return this.next(wes.get(0).getExcuteId(), toCode, param, 
				employeeId, advice, ip, ifCopyBeforeParam);
	}
	/**
	 * 结点流转
	 * @param excuteId 结点执行ID
	 * @param toCode 导向编码
	 * @param param 业务参数
	 * @param employeeId 员工ID
	 * @param advice 意见
	 * @param ip 操作IP地址
	 * @param ifCopyBeforeParam 是否复制前一结点的业务参数，如果与param参数冲突，则保存param参数
	 * @return 状态编码和名称，奇数位存储状态编码，偶数位存储状态名称
	 * @throws Exception
	 */
	public String[] next(String excuteId, String toCode, Map<String, String> para,
			String employeeId, String advice, String ip,
			boolean ifCopyBeforeParam) throws Exception {
		Map param = new HashMap();//参数复制，防止数据变化
	    if(para!=null)
		for(String key:para.keySet()){
			param.put(key, para.get(key));
		}
		Map term = new HashMap();
		term.put("excuteId", excuteId);
		//以前的总状态
		List<Map> maps = this.queryData("stateSql", term, null,null);
		List states = new ArrayList();
		for(Map map:maps){
			states.add(map.get("CODE"));
			states.add(map.get("NAME"));
		}
		WorkFlowExcute excute = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		//以前的状态
		List<Map> mapOlds = this.queryData("stateNodeSql", term, null,null);
		//流转后的新状态
		String[] newStates = this.nexts(excuteId, toCode, param, employeeId, advice, ip, ifCopyBeforeParam);
		if(newStates==null)
			return (String[])states.toArray(new String[]{});
		for(Map map:mapOlds){//清除老状态
			states.remove(map.get("CODE"));
			states.remove(map.get("NAME"));
		}
		for(String state:newStates){//加新状态
			states.add(state);
		}
		return (String[])states.toArray(new String[]{});
	}
	
	private String[] nexts(String excuteId, String toCode, Map<String, String> param,
			String employeeId, String advice, String ip,
			boolean ifCopyBeforeParam) throws Exception {
		WorkFlowExcute excute = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		if(excute==null)
			throw new Exception("不存在此任务操作！");
		WorkFlowNode node = excute.getWorkFlowNode();
		WorkFlow wf = node.getWorkFlow();//工作流对象
		WorkFlowTo to = null;
		if(StringUtils.isEmpty(toCode))
			throw new Exception("导向编码'"+toCode+"'为空！");
		for(WorkFlowTo wft:node.getSrcWorkFlowTos()){
			if(wft.getCode().equals(toCode)){
				to = wft;
				break;
			}
		}
		if(to==null)
			throw new Exception("此功能不存在此操作"+toCode);
		if(param==null) param = new HashMap<String,String>();
		if(ifCopyBeforeParam&&excute.getParam()!=null){
			for(String key:excute.getParam().keySet()){
				if(!param.containsKey(key))
					param.put(key, excute.getParam().get(key));
			}
		}
		excute.setWorkFlowTo(to);		
		if(node.getType().equals("TASK")){
			if(StringUtils.isEmpty(employeeId))
			   throw new Exception("无用户存在，不能执行！");
			Employee employee = (Employee)this.findDataByKey(employeeId, Employee.class);
			if(employee==null)
				throw new Exception("无用户存在，不能执行！");
			Quarter querter = employee.getQuarter();
			if(!employeeId.equals(excute.getEmployeeId())){
			   if(querter==null||(querter!=null&&
				 !querter.getQuarterId().equals(excute.getQuarterId())))
			      throw new Exception("此任务你无权执行！");
			}
			excute.setEmployeeId(employeeId);
			excute.setEmployeeName(employee.getEmployeeName());
			excute.setQuarterId(null);
			excute.setQuarterName(null);
		}
		excute.setExcuteTiime(DateUtils.getCurrDate());
		excute.setIp(ip);
		excute.setAdvice(advice);
		UserSession us = LoginAction.getUserSessionInfo();//在线用户信息
		if(us!=null&&!StringUtils.isEmpty(us.getProxyUserId())){//代理模块下，要设置代理人
			SysUser user = (SysUser)this.findDataByKey(us.getProxyUserId(), SysUser.class);
			if(user!=null){
				Employee proxy = user.getEmployee();
				if(proxy!=null)
				   excute.setAgent(proxy.getEmployeeName()+"("+user.getAccount()+")");
				else
				   excute.setAgent(user.getAccount());
			}
		}
		Map<String,Object> signterm = new HashMap<String,Object>();
		signterm.put("excuteId", excuteId);
		signterm.put("nodeId", node.getNodeId());
		List<WorkFlowExcute> signEs = (List<WorkFlowExcute>)this.queryData("signTaskHql", signterm, null);	
		//如果是会签任务，首先要判断是否全部执行完，如果全部执行完还需要进行表达式来进行跳转
		if(node.getType().equals("TASK")&&signEs.size()>1){	
			boolean allComplet = true;//是否全部执行完
			Map<String,String> toParam = new HashMap<String,String>();//存储任务导向的执行次数
			toParam.put("$size", signEs.size()+"");
			for(WorkFlowExcute e:signEs){
				if(!e.getExcuteId().equals(excuteId)&&e.getWorkFlowTo()==null){
					allComplet = false;
					continue;
				}
				String countStr = toParam.get("$size_"+e.getWorkFlowTo().getCode());
				Integer count = 1;
				if(countStr==null) 
					count = 1;
				else{
					count = Integer.parseInt(countStr)+1;
				}
				toParam.put("$size_"+e.getWorkFlowTo().getCode(), count+"");
			}
			if(!allComplet)//没全部执行完则退出，不转向
				return null;
			else{
				String express = node.getJudgeExpress();
				if(StringUtils.isEmpty(express))
					throw new Exception("会签任务必须定义表达式！");				
				String[] expressArray = express.split("#");
				if(expressArray.length>1)
				   express = express.split("#")[1];
				Object c = this.excuteExpress(express, toParam);
				if(c instanceof Integer)
				   toCode = ((Integer)c).toString();
				else
				   toCode = (String)c;
				for(WorkFlowTo wft:node.getSrcWorkFlowTos()){
					if(wft.getCode().equals(toCode)){
						to = wft;break;
					}
				}
				if(to==null)
					throw new Exception("此功能不存在此编码导向"+toCode);
			}
		}
		//如果原结点是聚合结点
		if(node.getType().equals("JOIN")){
			Map<String,Object> term = new HashMap<String,Object>();
			term.put("flowInstanceId", excute.getBusId());
			term.put("toNodeId", node.getNodeId());
			List<WorkFlowTo> ts = (List<WorkFlowTo>)this.queryData("joinCompletCountHql", term, null).get(0);	
			if(ts.size()!=node.getToWorkFlowTo().size()){
				List rets = new ArrayList();
				for(WorkFlowTo t:ts){
					rets.add(t.getCode());
					rets.add(t.getState());
				}
				return (String[])rets.toArray(new String[]{});//聚合结点的入口未执行完成则退出
			}
		}
		//根据出口结点类型分析
		if(to.getToNode().getType().equals("TASK")){//任务结点
			if(to.getToNode().getExcuteType().intValue()==1){//手动选择
				for(WorkFlowQuarter q:to.getToNode().getWorkFlowQuarters()){
					WorkFlowExcute newExcute = new WorkFlowExcute();
					newExcute.setBusId(excute.getBusId());
					newExcute.setParentBusId(excute.getParentBusId());
					newExcute.setWorkFlowNode(to.getToNode());
					newExcute.setTaskName(to.getToNode().getName());
					newExcute.setCreateTime(DateUtils.getCurrDate());
					newExcute.setIfUndo(false);
					newExcute.setOrderNo(excute.getOrderNo()+1);
					newExcute.setParam(param);
					Map term = new HashMap();
					term.put("quarterId", q.getQuarterId());
					List es = this.queryData("employeeByQuarterHql", term, null);
					if(es.size()==0||es.size()>1){
						newExcute.setQuarterId(q.getQuarterId());
						newExcute.setQuarterName(q.getQuarterName());
					}else{
						newExcute.setEmployeeId(((Employee)es.get(0)).getEmployeeId());
						newExcute.setEmployeeName(((Employee)es.get(0)).getEmployeeName());
					}
					if(to.getToNode().getTimer()!=null){//设置计划时间
						newExcute.setPlanTime(new Date(newExcute.getCreateTime().getTime()+
								to.getToNode().getTimer()*60*1000));
					}
					this.add(newExcute);
				}
			}else if(to.getToNode().getExcuteType().intValue()==2){//前执行者上级岗位
				WorkFlowExcute newExcute = new WorkFlowExcute();
				newExcute.setBusId(excute.getBusId());
				newExcute.setParentBusId(excute.getParentBusId());
				newExcute.setWorkFlowNode(to.getToNode());
				newExcute.setTaskName(to.getToNode().getName());
				newExcute.setCreateTime(DateUtils.getCurrDate());
				newExcute.setIfUndo(false);
				newExcute.setOrderNo(excute.getOrderNo()+1);
				newExcute.setParam(param);
				Employee employee = (Employee)this.findDataByKey(employeeId, Employee.class);
				Quarter quarter = employee.getQuarter();
				if(quarter==null)
					throw new Exception("前执行者无岗位！");
				quarter = quarter.getQuarter();
				if(quarter==null)
					throw new Exception("前执行者的岗位无上级岗位！");
				newExcute.setQuarterId(quarter.getQuarterId());
				newExcute.setQuarterName(quarter.getQuarterName());
				if(to.getToNode().getTimer()!=null){
					newExcute.setPlanTime(new Date(newExcute.getCreateTime().getTime()+
							to.getToNode().getTimer()*60*1000));
				}
				this.add(newExcute);
			}else if(to.getToNode().getExcuteType().intValue()==3){//流程创建者
				WorkFlowExcute newExcute = new WorkFlowExcute();
				newExcute.setBusId(excute.getBusId());
				newExcute.setParentBusId(excute.getParentBusId());
				newExcute.setWorkFlowNode(to.getToNode());
				newExcute.setTaskName(to.getToNode().getName());
				newExcute.setCreateTime(DateUtils.getCurrDate());
				newExcute.setIfUndo(false);
				newExcute.setOrderNo(excute.getOrderNo()+1);
				newExcute.setParam(param);
				Map<String,Object> term = new HashMap<String,Object>();
				term.put("flowInstanceId", excute.getBusId());
				List cs = this.queryData("findCreateManHql", term, null,null);
				if(cs==null||cs.size()==0)
					throw new Exception("流程无创建者！");
				else{
					Object[] rs = (Object[])cs.get(0);
					newExcute.setEmployeeId((String)rs[0]);
					newExcute.setEmployeeName((String)rs[1]);
				}
				if(to.getToNode().getTimer()!=null){
					newExcute.setPlanTime(new Date(newExcute.getCreateTime().getTime()+
							to.getToNode().getTimer()*60*1000));
				}
				this.add(newExcute);
			}else{//参数指定
				String express = to.getToNode().getJudgeExpress();
				if(StringUtils.isEmpty(express))
					throw new Exception("参数指定的执行者类型必须定义表达式！");
				String[] expressArray = express.split("#");
				express = express.split("#")[0];
				String cs = (String)this.excuteExpress(express, param);
				if(StringUtils.isEmpty(cs))
					throw new Exception("未定义参数"+express+"指定的员工ID信息！");
				for(String e:cs.split(",")){
					WorkFlowExcute newExcute = new WorkFlowExcute();
					newExcute.setBusId(excute.getBusId());
					newExcute.setParentBusId(excute.getParentBusId());
					newExcute.setWorkFlowNode(to.getToNode());
					newExcute.setTaskName(to.getToNode().getName());
					newExcute.setCreateTime(DateUtils.getCurrDate());
					newExcute.setIfUndo(false);
					newExcute.setOrderNo(excute.getOrderNo()+1);
					newExcute.setParam(param);
					newExcute.setEmployeeId(e);
					Employee employee = (Employee)this.findDataByKey(employeeId, Employee.class);
					Employee em = (Employee)this.findDataByKey(e, Employee.class);
					if(employee!=null)
					    newExcute.setEmployeeName(em.getEmployeeName());
					else
						newExcute.setEmployeeName(e+"此员工不存在");
					if(to.getToNode().getTimer()!=null){
						newExcute.setPlanTime(new Date(newExcute.getCreateTime().getTime()+
								to.getToNode().getTimer()*60*1000));
					}
					this.add(newExcute);
				}
			}
		}else if(to.getToNode().getType().equals("JUDGE")){//判断结点
			WorkFlowExcute newExcute = new WorkFlowExcute();
			newExcute.setBusId(excute.getBusId());
			newExcute.setParentBusId(excute.getParentBusId());
			newExcute.setWorkFlowNode(to.getToNode());
			newExcute.setTaskName(to.getToNode().getName());
			newExcute.setCreateTime(DateUtils.getCurrDate());
			newExcute.setIfUndo(false);
			newExcute.setOrderNo(excute.getOrderNo()+1);
			newExcute.setEmployeeId(excute.getEmployeeId());
			newExcute.setParam(param);
			this.add(newExcute);
			return nexts(newExcute.getExcuteId(), 
					(String)this.excuteExpress(to.getToNode().getJudgeExpress(), param), 
					param, employeeId, null, null, true);
		}else if(to.getToNode().getType().equals("FORK")){//分技结点
			List rets = new ArrayList();
			for(WorkFlowTo wft:to.getToNode().getSrcWorkFlowTos()){
				WorkFlowExcute newExcute = new WorkFlowExcute();
				newExcute.setBusId(excute.getBusId());
				newExcute.setParentBusId(excute.getParentBusId());
				newExcute.setWorkFlowNode(to.getToNode());
				newExcute.setTaskName(to.getToNode().getName());
				newExcute.setCreateTime(DateUtils.getCurrDate());
				newExcute.setIfUndo(false);
				newExcute.setOrderNo(excute.getOrderNo()+1);
				newExcute.setEmployeeId(excute.getEmployeeId());
				newExcute.setParam(param);
				this.add(newExcute);
				String[] ss = nexts(newExcute.getExcuteId(), wft.getCode(), param, employeeId, null, null, true);
				if(ss!=null){
					for(String s:ss){
						rets.add(s);
					}
				}
			}
			return (String[])rets.toArray(new String[]{});
		}else if(to.getToNode().getType().equals("DYNAMIC_FORK")){//动态分技结点
			Integer forksize = (Integer)this.excuteExpress(to.getToNode().getJudgeExpress(), param);//计算分技数量
			List rets = new ArrayList();
			for(int i=0;i<forksize;i++){
				WorkFlowExcute newExcute = new WorkFlowExcute();
				newExcute.setBusId(excute.getBusId());
				newExcute.setParentBusId(excute.getParentBusId());
				newExcute.setWorkFlowNode(to.getToNode());
				newExcute.setTaskName(to.getToNode().getName());
				newExcute.setCreateTime(DateUtils.getCurrDate());
				newExcute.setIfUndo(false);
				newExcute.setOrderNo(excute.getOrderNo()+1);
				newExcute.setParam(param);
				newExcute.setEmployeeId(excute.getEmployeeId());
				this.add(newExcute);
				WorkFlowTo wft = to.getToNode().getSrcWorkFlowTos().iterator().next();//动态分技只有一个出口
				String[] ss = nexts(newExcute.getExcuteId(), wft.getCode(), param, employeeId, null, null, true);
				if(ss!=null){
					for(String s:ss){
						rets.add(s);
					}
				}
			}
			return (String[])rets.toArray(new String[]{});
		}else if(to.getToNode().getType().equals("JOIN")){//聚合结点
			Map term = new HashMap();
			term.put("nodeId", to.getToNode().getNodeId());
			term.put("flowInstanceId", excute.getBusId());
			List list = this.queryData("joinToHql", term, null);
			WorkFlowExcute newExcute = new WorkFlowExcute();//查询已有聚合执行
			if(list.size()>0){
				newExcute = (WorkFlowExcute)list.get(0);
				if(newExcute.getOrderNo().intValue()<=excute.getOrderNo())
					newExcute.setOrderNo(excute.getOrderNo()+1);
			}else{
				newExcute.setBusId(excute.getBusId());
				newExcute.setParentBusId(excute.getParentBusId());
				newExcute.setWorkFlowNode(to.getToNode());
				newExcute.setTaskName(to.getToNode().getName());
				newExcute.setCreateTime(DateUtils.getCurrDate());
				newExcute.setIfUndo(false);
				newExcute.setOrderNo(excute.getOrderNo()+1);
				newExcute.setParam(param);
				newExcute.setEmployeeId(excute.getEmployeeId());
				this.add(newExcute);
			}
			WorkFlowTo wft = to.getToNode().getSrcWorkFlowTos().iterator().next();//聚合结点只有一个出口
			return nexts(newExcute.getExcuteId(),wft.getCode(),param, employeeId, null, null, true);
		}else if(to.getToNode().getType().equals("AUTO")){//自动结点
			WorkFlowExcute newExcute = new WorkFlowExcute();
			newExcute.setBusId(excute.getBusId());
			newExcute.setParentBusId(excute.getParentBusId());
			newExcute.setWorkFlowNode(to.getToNode());
			newExcute.setTaskName(to.getToNode().getName());
			newExcute.setCreateTime(DateUtils.getCurrDate());
			newExcute.setIfUndo(false);
			newExcute.setOrderNo(excute.getOrderNo()+1);
			newExcute.setEmployeeId(excute.getEmployeeId());
			newExcute.setParam(param);
			this.add(newExcute);
			IClient clientServcie = (IClient)SpringContextUtil.getBean(wf.getServiceName());
			clientServcie.autoExcute(wf.getCode(), to.getToNode().getCode(), param);
			WorkFlowTo wft = to.getToNode().getSrcWorkFlowTos().iterator().next();//自动结点只有一个出口
			return nexts(newExcute.getExcuteId(),wft.getCode(),param, employeeId, null, null, true);
		}else if(to.getToNode().getType().equals("SUB_FLOW")){//子流程结点
			WorkFlowExcute newExcute = new WorkFlowExcute();
			newExcute.setBusId(excute.getBusId());
			newExcute.setParentBusId(excute.getParentBusId());
			newExcute.setWorkFlowNode(to.getToNode());
			newExcute.setTaskName(to.getToNode().getName());
			newExcute.setCreateTime(DateUtils.getCurrDate());
			newExcute.setIfUndo(false);
			newExcute.setOrderNo(excute.getOrderNo()+1);
			newExcute.setEmployeeId(excute.getEmployeeId());
			newExcute.setParam(param);
			this.add(newExcute);
			this.startWorkFlow(to.getToNode().getCode(), employeeId, param, excute.getBusId());
		}else if(to.getToNode().getType().equals("END")){//结束结点
			WorkFlowExcute newExcute = new WorkFlowExcute();
			newExcute.setBusId(excute.getBusId());
			newExcute.setParentBusId(excute.getParentBusId());
			newExcute.setWorkFlowNode(to.getToNode());
			newExcute.setTaskName(to.getToNode().getName());
			newExcute.setCreateTime(DateUtils.getCurrDate());
			newExcute.setIfUndo(false);
			newExcute.setOrderNo(excute.getOrderNo()+1);
			newExcute.setEmployeeId(excute.getEmployeeId());
			newExcute.setParam(param);
			this.add(newExcute);
			//存在父流程
			if(!StringUtils.isEmpty(excute.getParentBusId())){
				Map term = new HashMap();
				term.put("nodeCode", to.getToNode().getCode());
				term.put("flowInstanceId", excute.getParentBusId());
				List list = this.queryData("parentFlowHql", term, null);
				if(list.size()>0){
					WorkFlowExcute wfe = (WorkFlowExcute)list.get(0);
					String ptoCode = wfe.getWorkFlowNode().getSrcWorkFlowTos().iterator().next().getCode();
				    return nexts(wfe.getExcuteId(),ptoCode,param, employeeId, null, null, true);
				}
			}
		}else if(to.getToNode().getType().equals("START")){//开始结点
			WorkFlowExcute newExcute = new WorkFlowExcute();
			newExcute.setBusId(excute.getBusId());
			newExcute.setParentBusId(excute.getParentBusId());
			newExcute.setWorkFlowNode(to.getToNode());
			newExcute.setTaskName(to.getToNode().getName());
			newExcute.setCreateTime(DateUtils.getCurrDate());
			newExcute.setIfUndo(false);
			newExcute.setOrderNo(excute.getOrderNo()+1);
			newExcute.setEmployeeId(excute.getEmployeeId());
			newExcute.setParam(param);
			this.add(newExcute);
			return this.nexts(newExcute.getExcuteId(), //开始结点不会停留，直接导向
					((WorkFlowTo)to.getToNode().getSrcWorkFlowTos().iterator().next()).getCode(),
					param, employeeId, advice, ip, true);
		}
		return new String[]{to.getCode(),to.getState()};
	}
	/**
	 * 执行判断表达式
	 * @param express 表达式
	 * @param param 参数
	 * @return 执行结果
	 * @throws Exception
	 * @modified
	 */
	private Object excuteExpress(String express,Map<String,String> param) throws Exception{
		//执行判断表达式
		Interpreter interpreter = new Interpreter(); // 构造一个解释器
		for(String key:param.keySet()){
			String value = param.get(key);
			if(StringUtils.regex("^[0-9]+(\\.[0-9]{1,})?$", value))
				interpreter.set(key, Double.parseDouble(value));
			else
			    interpreter.set(key, value);
		}
		return interpreter.eval(express);
	}
	/**
	 * 打开指定工作流(取最新版本）
	 * @param workFlowCode 流程编码
	 * @param employeId 员工ID
	 * @param param 参数
	 * @param parentFLowInstanceId 父流程实例ID,用于子流程启动
	 * @throws Exception 异常
	 * @return 状态码数组,1编码2名称
	 * @modified
	 */
	public String[] startWorkFlow(String workFlowCode, String employeeId,
			Map<String, String> param,String parentFLowInstanceId) throws Exception {
		//流程实例ID
		String flowInstanceId = UUID.randomUUID().toString().replace("-", "");
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("code", workFlowCode);
		List wfs = (List)this.queryData("byCodeHeightVersionHql", term, null);
		if(wfs.size()==0)
			throw new Exception(workFlowCode+"此流程不存在！");
		if(StringUtils.isEmpty(employeeId))
			throw new Exception("流程启动必须要有操作者！");
		WorkFlow wf = (WorkFlow)wfs.get(0);
		WorkFlowNode startNode = wf.getWorkFlowNodes().iterator().next();//开始结点
		WorkFlowExcute excute = new WorkFlowExcute();
		excute.setBusId(flowInstanceId);
		excute.setParentBusId(parentFLowInstanceId);
		excute.setWorkFlowNode(startNode);
		excute.setCreateTime(DateUtils.getCurrDate());
		excute.setEmployeeId(employeeId);
		Employee employee = (Employee)this.findDataByKey(employeeId, Employee.class);
		if(employee!=null)
		   excute.setEmployeeName(employee.getEmployeeName());
		else
		   excute.setEmployeeName(employeeId+"此员工不存在");
		excute.setExcuteTiime(DateUtils.getCurrDate());
		excute.setIfUndo(false);
		excute.setOrderNo(1);
		Map copeParam = new HashMap();
		if(param!=null)
		for(String key:param.keySet()){
			copeParam.put(key, param.get(key));
		}
		excute.setParam(copeParam);
		excute.setTaskName("开始");
		this.add(excute);
		WorkFlowTo nt = startNode.getSrcWorkFlowTos().iterator().next();
		this.next(excute.getExcuteId(), nt.getCode(), copeParam, null, null, null, true);
		return new String[]{nt.getCode(),nt.getState()};
	}
	/**
	 * 撤消操作
	 * @param excuteId
	 * @param employeeId
	 * return 新编码及新状态
	 * @return
	 */
	public String[] undo(String excuteId,String employeeId)throws Exception{
		WorkFlowExcute wfe = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		if(wfe.getWorkFlowTo()==null)
			throw new Exception("此任务还未执行，不存在撤消！");
		if(!wfe.getEmployeeId().equals(employeeId))
			throw new Exception("此任务非你执行，不能撤消！");
		if(wfe.getWorkFlowTo().getToNode().getType().equals("END"))
			throw new Exception("流程已经结束，不能撤消！");
		//撤消任务的后一任务
		WorkFlowNode node = wfe.getWorkFlowTo().getToNode();
		//查询后一任务的执行实例
		List<WorkFlowExcute> afterEts = ((WorkFlowDao)getDao()).findExcuteTask(node.getNodeId(), wfe.getBusId());
		boolean ifExcute = false;
		for(WorkFlowExcute item : afterEts){
			if(item.getWorkFlowTo()!=null&&item.getOrderNo()>wfe.getOrderNo()&&!item.getIfUndo()){
				ifExcute = true;
				break;
			}
		}
		if(ifExcute)
			throw new Exception("此任务的后续任务已经部分执行，不能撤消！");
		else{
			//撤消后续任务 
			for(WorkFlowExcute item : afterEts){
				item.setIfUndo(true);
				this.delete(item);
			}
			//撤消的任务删除导向
			wfe.setWorkFlowTo(null);
			wfe.setExcuteTiime(null);
			wfe.setIp(null);
			wfe.setAdvice(null);
			wfe.setIfUndo(false);
			this.update(wfe);
			//查询撤消任务的入口导向
			Map<String,Object> term = new HashMap<String,Object>();
			term.put("nodeId", wfe.getWorkFlowNode().getNodeId());
			term.put("flowInstanceId", wfe.getBusId());
			term.put("no", wfe.getOrderNo()-1);
			List<WorkFlowExcute> lateIns = this.queryData("excuteInLateHql", term, null);
			if(lateIns.size()==0)
				throw new Exception("流程的第一个任务，不能撤消！");
			else{
				List states = new ArrayList();
				for(WorkFlowExcute excute:lateIns){
					if(wfe.getWorkFlowNode().getType().equals("JUDGE")){//撤消任务为判断时，则再次撤消
						String[] judgeState = this.undo(excute.getExcuteId(), employeeId);
						for(String js:judgeState)
							states.add(js);
					}else if(!wfe.getWorkFlowNode().getType().equals("TASK")){
						throw new Exception("只有任务或判断结点可以撤消！");
					}else{
						states.add(excute.getWorkFlowTo().getCode());
						states.add(excute.getWorkFlowTo().getState());
					}
				}
			    return (String[])states.toArray(new String[]{});
			}
		}			
	}
	/**
	 * 撤消操作
	 * @param flowCode 工作流编码
	 * @param param 业务参数
	 * @param stateCode 状态编码
	 * @param employeeId 员工ID
	 * @throws Exception
	 */
	public String[] undo(String flowCode, Map<String, String> param,
			String stateCode, String employeeId) throws Exception {
		List<WorkFlowExcute> excutes = ((WorkFlowDao)getDao()).findUndoTask(
				flowCode, param, stateCode);
		if(StringUtils.isEmpty(stateCode))
			throw new Exception("撤消的任务无状态！");
		if(stateCode.split(",").length>1)
			throw new Exception("并发任务不能撤消！");
		if(excutes.size()==0)
			throw new Exception("找不到要撤消的任务！");
		else{
			WorkFlowExcute wfe = null;
			for(WorkFlowExcute excute:excutes){
				if(excute.getEmployeeId().equals(employeeId)){
					wfe = excute;
					break;
				}
			}
			if(wfe==null)
				throw new Exception("你无权撤消他人的任务或者关联处理!");
			return this.undo(wfe.getExcuteId(), employeeId);
		}
	}
	
	/**
	 * 接收任务
	 * @param excuteId 任务执行ID
	 * @param employeeId 员工ID
	 * @modified
	 */
	public void receiveTask(String excuteId, String employeeId) throws Exception{
		WorkFlowExcute excute = (WorkFlowExcute)this.findDataByKey(excuteId, WorkFlowExcute.class);
		if(!StringUtils.isEmpty(excute.getEmployeeId()))
			throw new Exception("此任务已经执行接收操作！");
		if(StringUtils.isEmpty(employeeId))
			throw new Exception("无法确认你的身份，接收失败！");
		Employee employee = (Employee)this.findDataByKey(employeeId, Employee.class);
		if(employee==null)
			throw new Exception("无法确认你的身份，接收失败！");
		if(StringUtils.isEmpty(excute.getQuarterId()))
			throw new Exception("此任务未分配到岗位，接收失败！");
		if(employee.getQuarter()==null)
			throw new Exception("无法确认你的身份，接收失败！");
		if(!excute.getQuarterId().equals(employee.getQuarter().getQuarterId()))
			throw new Exception("此任务你无权接收！");
		excute.setEmployeeId(employeeId);
		this.update(excute);
	}
}