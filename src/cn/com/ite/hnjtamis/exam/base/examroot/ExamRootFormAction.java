package cn.com.ite.hnjtamis.exam.base.examroot;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.examroot.form.ArrangeRootForm;
import cn.com.ite.hnjtamis.exam.base.examroot.service.ExamRootService;
import cn.com.ite.hnjtamis.exam.base.examroot.util.PinYin2Abbreviation;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamExamroot;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamRoot;
/*
 * 考点管理 维护 --form
 */
public class ExamRootFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = 1694057330022467860L;
	private HttpServletRequest request;
	
	private ExamRoot form;
	private ArrangeRootForm arrangeForm;
	private List<Exam> queryExamCom = new ArrayList<Exam>();
	
	public String queryExamCom(){
		String id = request.getParameter("id");
		ExamArrange examArrange = id!=null && !"".equals(id) 
				&& !"null".equals(id) ? (ExamArrange) service.findDataByKey(id, ExamArrange.class) : null;
		if(examArrange==null && (id!=null && !"".equals(id) 
				&& !"null".equals(id))){
			Map paramMap = new HashMap();
			paramMap.put("examId", id);
			List list = service.queryData("examArrangeInExamIdHql", paramMap, new HashMap());
			if(list!=null && list.size()>0){
				examArrange = (ExamArrange)list.get(0);
				queryExamCom.addAll(examArrange.getExams());
			}
		}
		return "queryExamCom";
	}
	/*
	 * 查询 考试安排中 设置考点
	 */
	public String findArrange(){
		arrangeForm = new ArrangeRootForm();
		String id = request.getParameter("id");
		ExamArrange examArrange = id!=null && !"".equals(id) 
				&& !"null".equals(id) ? (ExamArrange) service.findDataByKey(id, ExamArrange.class) : null;
		if(examArrange==null && (id!=null && !"".equals(id) 
				&& !"null".equals(id))){
			Map paramMap = new HashMap();
			paramMap.put("examId", id);
			List list = service.queryData("examArrangeInExamIdHql", paramMap, new HashMap());
			if(list!=null && list.size()>0){
				examArrange = (ExamArrange)list.get(0);
			}
		}
		if(examArrange!=null){
			try {
				arrangeForm.setExamArrangeId(examArrange.getExamArrangeId());
				arrangeForm.setExamArrangeName(examArrange.getExamName()); 
				arrangeForm.setScore(examArrange.getScore());//总分
				arrangeForm.setExamCode(examArrange.getExamCode());//考试编码 考试类型编码 + 日期 + 序号（2位）
				arrangeForm.setExamTypeId(examArrange.getExamTypeId());//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
				arrangeForm.setExamTypeName(examArrange.getExamTypeName());//考试类型(性质)，与考试类型ID一组
				arrangeForm.setExamProperty(examArrange.getExamProperty());//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
				arrangeForm.setIsPublic(examArrange.getIsPublic());//是否发布成绩 5：否，10：是
				arrangeForm.setPublicUser(examArrange.getPublicUser());//成绩发布人
				arrangeForm.setPublicUserId(examArrange.getPublicUserId());//成绩发布人编号
				arrangeForm.setPublicTime(examArrange.getPublicTime());//成绩发布时间
				arrangeForm.setIsUse(examArrange.getIsUse());///是否使用 5：否,10：是
				arrangeForm.setCheckUser(examArrange.getCheckUser());//审核人
				arrangeForm.setCheckUserId(examArrange.getCheckUserId());///审核人ID
				arrangeForm.setCheckTime(examArrange.getCheckTime());//审核时间
				arrangeForm.setState(examArrange.getState());//
				arrangeForm.setExamPaperType(examArrange.getExamPaperType());//
				arrangeForm.setIsIdNumberLogin(examArrange.getIsIdNumberLogin());//
				arrangeForm.setRelationId(examArrange.getRelationId());//关联ID（如：练习安排等）
				arrangeForm.setRelationType(examArrange.getRelationType());//关联类型
				arrangeForm.setRemark(examArrange.getRemark());//备注
				arrangeForm.setPublicId(examArrange.getExamPublic()!=null?examArrange.getExamPublic().getPublicId() : null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Exam> examList = examArrange.getExams();
			if(examList!=null && examList.size()>0){
				List<ExamExamroot> allRootList = new ArrayList<ExamExamroot>();
				for(int i = 0 ;i<examList.size();i++){
					Exam exam  = (Exam)examList.get(i);
					List<ExamExamroot> rootList = exam.getExamExamroots();
					allRootList.addAll(rootList);
				}
				arrangeForm.setExamRootList(allRootList);
			}
		}
		System.out.println(arrangeForm.getExamArrangeName()+"  "+arrangeForm.getExamCode());
		return "findArrange";
	}
	/*
	 * 保存 考试安排中 设置考点
	 */
	public String saveArrange(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			arrangeForm = new ArrangeRootForm();
			arrangeForm = (ArrangeRootForm) this.jsonToObject(ArrangeRootForm.class);
			List<ExamExamroot> rootList = arrangeForm.getExamRootList();
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			if(rootList!=null && rootList.size()>0){
				for(ExamExamroot t : rootList){
					if(StringUtils.isEmpty(t.getExamExamrootId())){
						t.setCreatedBy(usersess.getEmployeeName());
						t.setCreatedIdBy(usersess.getEmployeeId());
						t.setCreationDate(nowTime);
					}else{
						t.setLastUpdatedBy(usersess.getEmployeeName());
						t.setLastUpdatedIdBy(usersess.getEmployeeId());
						t.setLastUpdateDate(nowTime);
					}
					t.setOrganId(usersess.getOrganId());
					t.setOrganName(usersess.getOrganName());
					t.setExamRootName(t.getExamRoot().getExamRootPlace());
				}
			}
			service.saves(rootList);
			//删除
			ExamArrange examArrange = (ExamArrange) service.findDataByKey(arrangeForm.getExamArrangeId(), ExamArrange.class);
			if(examArrange!=null){
				List<Exam> examList = examArrange.getExams();
				Map<String,String> allRootMap = new HashMap<String,String>();
//				List<ExamExamroot> deleteList = new ArrayList<ExamExamroot>();
				if(examList!=null && examList.size()>0){
					for(int i = 0 ;i<examList.size();i++){
						Exam exam  = (Exam)examList.get(i);
						List<ExamExamroot> tmpList = exam.getExamExamroots();
						for(ExamExamroot t : tmpList){
							allRootMap.put(t.getExamExamrootId(), t.getExamExamrootId());
						}
					}
					
					if(allRootMap.size()>0){
						for(ExamExamroot t : rootList){
							if(allRootMap.containsKey(t.getExamExamrootId())){
								allRootMap.remove(t.getExamExamrootId());
							}
						}
					}
					if(allRootMap.size()>0){
						Iterator<String> ids = allRootMap.keySet().iterator();
						List<String> deleteIds = new ArrayList<String>();
						if(ids.hasNext()){
							deleteIds.add(ids.next());
							((ExamRootService)service).deleteExamRoots(deleteIds); 
						}
					}
				}
			}
			this.setMsg("保存考点成功");
		} catch (Exception e) {
			this.setMsg("保存考点失败");
			e.printStackTrace();
		}
		return "save";
	}
	
	/*
	 * 修改
	 */
	public String find(){
		try {
			form = (ExamRoot) service.findDataByKey(this.getId(), ExamRoot.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "find";
	}
	/*
	 * 保存
	 */
	public String save(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		try {
			form = (ExamRoot) this.jsonToObject(ExamRoot.class);
			if(StringUtils.isEmpty(form.getExamRootId())){
				form.setCreatedBy(usersess.getEmployeeName());
				form.setCreatedIdBy(usersess.getEmployeeId());
				form.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				form.setSortNum(1+service.getFieldMax(ExamRoot.class, "sortNum"));
			}else{
				form.setLastUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				form.setLastUpdatedBy(usersess.getEmployeeName());
				form.setLastUpdatedIdBy(usersess.getEmployeeId());
			}
			String code = PinYin2Abbreviation.cn2py(form.getExamRootName()+"@"+form.getExamRootPlace());
			form.setExamRootCode(code);
			form.setOrganId(usersess.getOrganId());
			form.setOrganName(usersess.getOrganName());
			service.save(form);
			this.setMsg("考点保存成功");
		} catch (Exception e) {
			this.setMsg("考点保存失败");
			e.printStackTrace();
		}
		return "save";
	}
	/*
	 * 排序
	 */
	public String saveSort() throws Exception{
		List saves = new ArrayList();
		int index = 1;
		try {
			for(String id:this.getSortIds()){
				ExamRoot tt = (ExamRoot) service.findDataByKey(id, ExamRoot.class);
				tt.setSortNum(index++);
				saves.add(tt);
			}
			service.saves(saves);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	public ExamRoot getForm() {
		return form;
	}
	public void setForm(ExamRoot form) {
		this.form = form;
	}
	public ArrangeRootForm getArrangeForm() {
		return arrangeForm;
	}
	public void setArrangeForm(ArrangeRootForm arrangeForm) {
		this.arrangeForm = arrangeForm;
	}
	public List<Exam> getQueryExamCom() {
		return queryExamCom;
	}
	public void setQueryExamCom(List<Exam> queryExamCom) {
		this.queryExamCom = queryExamCom;
	}
	
	
}
