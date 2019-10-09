package cn.com.ite.hnjtamis.jobstandard.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
import cn.com.ite.hnjtamis.jobstandard.jobunionstandard.JobUnionStandardService;
import cn.com.ite.hnjtamis.personal.form.IndexStandardForm;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsServiceImpl extends DefaultServiceImpl implements
		StandardTermsService {
	private JobUnionStandardService jobunionstandardServer;
	public JobUnionStandardService getJobunionstandardServer() {
		return jobunionstandardServer;
	}
	public void setJobunionstandardServer(
			JobUnionStandardService jobunionstandardServer) {
		this.jobunionstandardServer = jobunionstandardServer;
	}
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findStandardTermsTree(String topTypeId,String typeName)throws Exception{
		Map term = new HashMap();
		///term.put("nameTerm", typeName);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new HashMap<String,TreeNode>();
		
		//// 默认勾选复选框
		List<JobsUnionStandard> ulist = getJobunionstandardServer().findDataByJobId(typeName);
		List<JobsUnionStandard> newlist = new ArrayList<JobsUnionStandard>();
		if(ulist!=null && ulist.size()>0){
			for(int i=0;i<ulist.size();i++){
				JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)ulist.get(i);
				if(jobsUnionStandard.getIsavailable()!=null && jobsUnionStandard.getIsavailable().intValue()!=0){
					newlist.add(jobsUnionStandard);
				}
			}
			ulist = newlist;
		}
		
		for(TreeNode node:list){
			TreeNode f = node;
			f.setType("standardterm");
			if(!StringUtils.isEmpty(f.getParentId())){
				/// 添加父类型
				StandardTypes parent = (StandardTypes)getDao().findEntityBykey(StandardTypes.class,f.getParentId());
				TreeNode newNode = new TreeNode();
				newNode.setId(parent.getJstypeid());
				newNode.setParentId(null);
				newNode.setTitle(parent.getTypename());
				
				//TreeNode newNode = TreeNode.objectToTree(parent, "jstypeid", "", "typename");
				if(!ms.containsKey(newNode.getId())){
					newNode.setParentId(null);
				    ms.put(newNode.getId(), newNode);
				}
				newNode.setType("standardtype");
			}
			Iterator<JobsUnionStandard> it = ulist.iterator();
			while (it.hasNext()){
				JobsUnionStandard us = it.next();
				if (f.getId().equals(us.getStandardTerms().getStandardid())){
					f.setChecked(true); //// 添加默认复选框
					it.remove();
					break;
				}
			}
		}
		list.addAll(ms.values());
		TreeNode.putTypeIncon("standardtype", "resources/icons/fam/plugin.gif", "");
		TreeNode.putTypeIncon("standardterm", "resources/icons/fam/theme.gif", "");
		return TreeNode.toTree(list,true,null,"");
	}
	/*
	 * 查询首页 题库
	 * @param quarterId 岗位id
	 */
	public List<ThemeBank> queryIndexTk(String quarter_train_code,String organId){
		List<ThemeBank> IndexTk = new ArrayList<ThemeBank>();
		Map term = new HashMap();
		term.put("quarter_train_code", quarter_train_code);
		term.put("organId", organId);
		IndexTk = this.queryData("indexTkSql", term, null, ThemeBank.class);
		if(IndexTk!=null && IndexTk.size()>0){
			List<ThemeBank> returnList = new ArrayList<ThemeBank>();
			Map repeatMap = new HashMap();
			for (ThemeBank themeBank : IndexTk) {
				if(repeatMap.containsKey(themeBank.getThemeBankId())){
					continue;
				}else{
					repeatMap.put(themeBank.getThemeBankId(), themeBank.getThemeBankId());
					returnList.add(themeBank);
				}
			}
			return returnList;
		}
		return IndexTk;
	}
	
	/**
	 *
	 * @author 朱健
	 * @param employeeId
	 * @param relationType
	 * @return 查询题库的题目数量与当前模拟考试（或对应relationType类型）的题目完成数量
	 * @modified
	 */
	public Map<String,String> getThemeNumInBank(String employeeId,String relationType){
		StandardTermsDao standardTermsDao = (StandardTermsDao)this.getDao();
		return standardTermsDao.getThemeNumInBank(employeeId, relationType);
	}
	
	/**
	 * 获取模拟试题
	 * @author 朱健
	 * @param employee_id
	 * @return
	 * @modified
	 */
	public Map<String,String> getMoniExamScore(String employee_id,String relationType){
		StandardTermsDao standardTermsDao = (StandardTermsDao)this.getDao();
		return standardTermsDao.getMoniExamScore(employee_id,relationType);
	}
	
	/**
	 * 获取岗位培训得分情况 
	 * @author 朱健
	 * @param employee_id
	 * @param exam_type_id
	 * @param startTime
	 * @param endTime
	 * @return
	 * @modified
	 */
	public Map<String,String> getGwpxExamScore(String employee_id,String exam_property,String startTime,String endTime){
		StandardTermsDao standardTermsDao = (StandardTermsDao)this.getDao();
		return standardTermsDao.getGwpxExamScore(employee_id, exam_property, startTime, endTime);
	}
	
	/*
	 * 查询首页 培训内容及标准  和  参考教材
	 * @param quarterId 岗位id
	 */
	public HashMap<String,ArrayList> queryIndexStandAndRefer(String quarterId){
		HashMap<String,ArrayList> result = new HashMap<String,ArrayList>();
		Map term = new HashMap();
		term.put("quarterId", quarterId);
		try {
			List<StandardTerms> list = this.queryData("queryIndexStandAndReferSql", term, null, StandardTerms.class);
			Map isRepeat = new HashMap();
			Map topSort = new HashMap();
			if(list!=null && list.size()>0){
				for (StandardTerms standardTerms : list) {
					String standarId = standardTerms.getStandardid();
					if(isRepeat.containsKey(standarId)){
						continue;
					}else{
						isRepeat.put(standarId, standarId);
					}
//					String standardname = standardTerms.getStandardTypes().getTypename();  --取分类的
					String standardname = standardTerms.getStandardname();
					/*  //循环取顶级分类
					StandardTypes sort = standardTerms.getStandardTypes();
					while(sort.getParentSpeciltype()!=null){
						sort = sort.getParentSpeciltype();
					}
					String standardname = "";
					if(topSort.containsKey(sort.getJstypeid())){
						standardname = null;
					}else{
						standardname = sort.getTypename();
						topSort.put(sort.getJstypeid(), sort.getJstypeid());
					}
					*/
					
					String contents = standardTerms.getContents()!=null?standardTerms.getContents():"";
					String referenceBook = standardTerms.getReferenceBook()!=null?standardTerms.getReferenceBook():"";
					
					String[] contentArray = contents.split("\n");
					String[] referenceBookArray = referenceBook.split("\n");
					
					ArrayList<IndexStandardForm> standardnameList = new ArrayList<IndexStandardForm>();
					ArrayList<String> contentList = new ArrayList<String>();
					ArrayList<String> referenceBookList = new ArrayList<String>();
					if(result.containsKey("contents")){
						contentList = result.get("contents");
					}
					if(result.containsKey("referenceBook")){
						referenceBookList = result.get("referenceBook");
					}
					if(result.containsKey("standardname")){
						standardnameList = result.get("standardname");
					}
					contentList.add("《"+standardname+"》");	
					String standardTermsRemark1 = "";
					String standardTermsRemark2 = "";
					String standardTermsRemark3 = "";
					String standardTermsRemark4 = "";
					standardTermsRemark1+= "【有效期】：";
					standardTermsRemark1+= standardTerms.getEfficient()!=null 
							&& !"".equals(standardTerms.getEfficient())
							&& !"null".equals(standardTerms.getEfficient()) ? standardTerms.getEfficient() : "--" ;
					standardTermsRemark2+= "【参考学分】：";
					standardTermsRemark2+= standardTerms.getRefScore()!=null 
							&& !"".equals(standardTerms.getRefScore())
							&& !"null".equals(standardTerms.getRefScore()) ? standardTerms.getRefScore() : "--" ;
					standardTermsRemark3+= "【达标标准】：";
					standardTermsRemark3+= standardTerms.getUpStandardScore()!=null 
							&& !"".equals(standardTerms.getUpStandardScore())
							&& !"null".equals(standardTerms.getUpStandardScore()) ? standardTerms.getUpStandardScore() : "--" ;
					standardTermsRemark4+= "【考核方式】：";
					standardTermsRemark4+= standardTerms.getExamTypeName()!=null 
							&& !"".equals(standardTerms.getExamTypeName())
							&& !"null".equals(standardTerms.getExamTypeName()) ? standardTerms.getExamTypeName() : "--" ;
					contentList.add(standardTermsRemark1);
					contentList.add(standardTermsRemark2);
					contentList.add(standardTermsRemark3);
					contentList.add(standardTermsRemark4);
					contentList.add("【详细内容】：");		
					setArrayInList(contentArray,contentList);
					setArrayInList(referenceBookArray, referenceBookList);
					if(!StringUtils.isEmpty(standardname)){
						IndexStandardForm tmp = new IndexStandardForm();
						tmp.setStandardname(standardname);
						tmp.setThemeBank(standardTerms.getThemeBank());
						standardnameList.add(tmp);
						result.put("standardname",standardnameList);
					}
					
					result.put("contents", contentList);
					result.put("referenceBook", referenceBookList);
				}
			}
			
			/*List list = this.queryData("queryIndexStandAndReferSql", term, null, null);
			if(list!=null && list.size()>0){
				for (Object object : list) {
					Map o = (Map) object;
					String standardname = o.get("STANDARDNAME")!=null?o.get("STANDARDNAME").toString():"";
					String contents = o.get("CONTENTS")!=null?o.get("CONTENTS").toString():"";
					String referenceBook = o.get("REFERENCE_BOOK")!=null?o.get("REFERENCE_BOOK").toString():"";
					
					String[] contentArray = contents.split("\n");
					String[] referenceBookArray = referenceBook.split("\n");
					
					ArrayList<String> standardnameList = new ArrayList<String>();
					ArrayList<String> contentList = new ArrayList<String>();
					ArrayList<String> referenceBookList = new ArrayList<String>();
					if(result.containsKey("contents")){
						contentList = result.get("contents");
					}
					if(result.containsKey("referenceBook")){
						referenceBookList = result.get("referenceBook");
					}
					if(result.containsKey("standardname")){
						standardnameList = result.get("standardname");
					}
					
					setArrayInList(contentArray,contentList);
					setArrayInList(referenceBookArray, referenceBookList);
					standardnameList.add(standardname);
					result.put("standardname",standardnameList);
					result.put("contents", contentList);
					result.put("referenceBook", referenceBookList);
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	private void setArrayInList(String[] param1,ArrayList<String> param2){
		for (String string : param1) {
			if(string!=null && string!=""){
				param2.add(string);
			}
		}
	}
	
	
	/**
	 * 在jobs_standard_quarter表没有数据的时候进行初始化
	 * @author 朱健
	 * @modified
	 */
	public void saveInitAllJobStandardQuarter(){
		StandardTermsDao standardTermsDao = (StandardTermsDao)this.getDao();
		standardTermsDao.saveInitAllJobStandardQuarter();
	}
	
	/**
	 * 根据标准条款中对应的系统岗位信息，处理不存在的并保存到标准条款中的标准岗位信息里面
	 * @author 朱健
	 * @modified
	 */
	public void updateStandardQuarterNotInSysQuarter(){
		StandardTermsDao standardTermsDao = (StandardTermsDao)this.getDao();
		standardTermsDao.updateStandardQuarterNotInSysQuarter();
	}
	
	
	/**
	 * 根据标准条款中对应的标准岗位信息更新标准条款对应的系统岗位信息
	 * @author 朱健
	 * @modified
	 */
	public void updateUnionStandardByStandard(){
		StandardTermsDao standardTermsDao = (StandardTermsDao)this.getDao();
		standardTermsDao.updateUnionStandardByStandard();
	}
}
