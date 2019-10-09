package cn.com.ite.hnjtamis.exam.base.exampublic;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
/*
 * 考试信息发布 -list
 */
public class ExamPublicListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = -6866356743924518844L;
	private HttpServletRequest request;
	
	private String titleTerm;//考试信息名称
	private String startTerm;//开始时间
	private String endTerm;//结束时间
	private String stateTerm;//状态
	private String examTypeIdTerm;//考试类型(性质)
	private String passDeadLineTerm;
	private List<ExamPublic> list = new ArrayList<ExamPublic>();
	private List<ExamPublic> comboExamPublish = new ArrayList<ExamPublic>();
	private String isExist;
	private String updateResult;
	private String employeeId;
	
	private String scexers;
	
	private String organId;

	
	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	/*
	 * 审核
	 */
	@SuppressWarnings("unchecked")
	public String auditList() throws Exception{
		String ids = request.getParameter("ids");
		try {
			if(!StringUtils.isEmpty(ids)){
				String[] saveids = ids.split(",");
				List updateIds = new ArrayList();
				for(int i=0;i<saveids.length;i++){
					if(!StringUtils.isEmpty(saveids[i])){
						updateIds.add(saveids[i]);
					}
				}
				Map term = new HashMap();
				UserSession usersess = LoginAction.getUserSessionInfo();
				term.put("auditPeopleId", usersess.getEmployeeId());
				term.put("auditPeople", usersess.getEmployeeName());
				term.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				term.put("ids", updateIds);
				
				List tlist = new ArrayList();
				tlist.add(term);
				
				service.excuteQl("auditList", tlist);
				updateResult = "success";
			}
		} catch (Exception e) {
			updateResult="failure";
			e.printStackTrace();
		}
		return "auditList";
	}
	
	/**
	 * 查询初始化是否完成
	 * @description
	 * @return
	 * @modified
	 */
	public String getExamStCreateNow(){
		scexers = "";
		if(this.getId()!=null && !"".equals(this.getId()) 
				&& !"null".equals(this.getId())){
			String[] ids = this.getId().split(",");
			for(int i=0;i<ids.length;i++){
				String publicId = ids[i];
				if(StaticVariable.publicIdMap.get(publicId)!=null){
					scexers+=publicId+",";
				}
			}
			if(scexers.length()>0)scexers = scexers.substring(0,scexers.length()-1);
		}
		return "scexers";
	} 
	
	/*
	 * 列表查询
	 */
	public String list() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		if(!StringUtils.isEmpty(stateTerm) && stateTerm.equals("30")){
			stateTerm =null;
		}
		this.setOrganId(usersess.getCurrentOrganId());
		list = (List<ExamPublic>)service.queryData("queryHql", this, null, ExamPublic.class, this.getStart(), this.getLimit());
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				ExamPublic examPublic = (ExamPublic)list.get(i);
				if(StaticVariable.publicIdMap.get(examPublic.getPublicId())!=null){
					examPublic.setStCreateNow("T");
				}
			}
		}
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	/*
	 * 考试信息 名称  是否已存在  0:不存在   1:存在
	 */
	public String queryExactExamPublish(){
		List<ExamPublic> tmp = (List<ExamPublic>)service.queryData("queryExactHql", this, null); 
		if(!StringUtils.isEmpty(this.getId())){//修改记录
			if(tmp!=null && tmp.size()>0){
				ExamPublic t = tmp.get(0);
				if(t.getPublicId().equals(this.getId().trim())){
					isExist = "0";
				}else{
					isExist = "1";
				}
			}else{
				isExist = "0";
			}
		}else{//新增记录
			if(tmp!=null && tmp.size()>0){
				isExist = "1";
			}else{
				isExist = "0";
			}
		}
		
		
		
		return "queryExactExamPublish";
	}
	/*
	 * 考试信息 下拉框 查询
	 */
	public String comboExamPublish() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			comboExamPublish = new ArrayList();
			return "comboExamPublish";
	 	}
		Map term = new HashMap();
		term.put("stateTerm", "20");
		term.put("organId",usersess.getCurrentOrganId());
		comboExamPublish = (List<ExamPublic>)service.queryData("queryHql", term, null); 
		return "comboExamPublish";
	}
	/*
	 * 考生自主报名 考试信息下拉框
	 */
	public String queryComboBoxSelf() throws Exception{
		comboExamPublish = (List<ExamPublic>)service.queryData("queryComboBoxSelf", this, null); 
		return "queryComboBoxSelf";
	}
	/*
	 * 删除
	 */
	public String delete(){
		try {
			String[] ids = this.getId().split(",");
			int pb = 0;
			for(int i=0;i<ids.length;i++){
				ExamPublic examPublic= (ExamPublic)service.findDataByKey(ids[i], ExamPublic.class);
				if("10".equals(examPublic.getState())){
					examPublic.getExamPublicSearchkeies().clear();
					service.delete(examPublic);
				}else{
					pb++;
				}
			}
			if(pb == 0){
				this.setMsg("记录删除成功！");
			}else{
				this.setMsg("保存的发布记录删除成功，但有"+pb+"条非保存状态，不能删除！");
			}
			//service.deleteByKeys(,ExamPublic.class);
			
		} catch (Exception e) {
			this.setMsg("记录删除失败！");
			e.printStackTrace();
		}
		return "delete";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<ExamPublic> getList() {
		return list;
	}

	public void setList(List<ExamPublic> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}

	public String getStartTerm() {
		return startTerm;
	}

	public void setStartTerm(String startTerm) {
		this.startTerm = startTerm;
	}

	public String getEndTerm() {
		return endTerm;
	}

	public void setEndTerm(String endTerm) {
		this.endTerm = endTerm;
	}

	public String getStateTerm() {
		return stateTerm;
	}

	public void setStateTerm(String stateTerm) {
		this.stateTerm = stateTerm;
	}
	

	public String getPassDeadLineTerm() {
		return passDeadLineTerm;
	}

	public void setPassDeadLineTerm(String passDeadLineTerm) {
		this.passDeadLineTerm = passDeadLineTerm;
	}

	public List<ExamPublic> getComboExamPublish() {
		return comboExamPublish;
	}

	public void setComboExamPublish(List<ExamPublic> comboExamPublish) {
		this.comboExamPublish = comboExamPublish;
	}

	public String getIsExist() {
		return isExist;
	}

	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}
	public String getExamTypeIdTerm() {
		return examTypeIdTerm;
	}
	public void setExamTypeIdTerm(String examTypeIdTerm) {
		this.examTypeIdTerm = examTypeIdTerm;
	}

	public String getUpdateResult() {
		return updateResult;
	}

	public void setUpdateResult(String updateResult) {
		this.updateResult = updateResult;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getScexers() {
		return scexers;
	}

	public void setScexers(String scexers) {
		this.scexers = scexers;
	}
	
	
	
}
