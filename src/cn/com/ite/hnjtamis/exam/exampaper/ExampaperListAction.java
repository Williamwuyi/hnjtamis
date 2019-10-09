package cn.com.ite.hnjtamis.exam.exampaper;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.CharsetSwitch;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.examreview.ExamReviewListAction;
import cn.com.ite.hnjtamis.exam.base.examroot.util.PinYin2Abbreviation;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamReviewForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ReviewForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamUserJsonForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamMarkpeople;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperListAction</p>
 * <p>Description 考试安排与试卷生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午3:00:56
 * @version 1.0
 * 
 * @modified records:
 */
public class ExampaperListAction extends AbstractListAction implements ServletRequestAware{
 
	private static final long serialVersionUID = 2951108490451624080L;

	private HttpServletRequest request;
	
	private List<ExamForm> examList;
	
	private List<ExamReviewForm> examReviewList = new ArrayList<ExamReviewForm>();//阅卷列表
	
	private int examListTotal;
	
	private List<Dictionary> examKslxList;
	
	private List examPublicList;
	
	private List<TreeNode> examineeTreeList;
	
	private List<TreeNode> children;
	
	private List<ExamUserForm> examineeInfoList;
	
	private int examineeInfoListTotal;
	
	private String examCode;
	
	private String isExist;
	
	private String examNameTerm;
	
	private String nameTerm;
	
	private String testpaperNameTerm;
	
	private String examStartTimeMaxTerm;
	
	private String examStartTimeMinTerm;
	
	private String creationDateMaxTerm;
	
	private String creationDateMinTerm;
	
	private String examTypeIdTerm;
	
	private String examType;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	
	private String examArrangeName;
	
	private String examArrangeCode;
	
	private String requestHttpUrl;
	
	private String examPropertyTerm;
	
	private List examUserList;
	private int examUserTotal;
	
	private String nodeId;
	private String nodeType;
	
	private String op;
	
	//用于查询---考核情况
	private String qid;
	private String qtype;
	private String qexamId;
	private String qstartDay;
	private String qendDay;
	
	private String scexers;

	

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	public String getExamCodePy(){
		try {
			String tmpCode = PinYin2Abbreviation.cn2py(examArrangeName.replaceAll(" ", ""));
			if(tmpCode==null || "".equals(tmpCode) || "null".equals(tmpCode)){
				examArrangeCode = "";
			}else{
				//int tolerableNum = 1800;//容差值 30*60=半小时
				//int perMinute = 3600;//分钟 60*60=1小时
				//long timestamp = System.currentTimeMillis()/1000;//时间戳
				//BigDecimal tick = (new BigDecimal(((timestamp-tolerableNum)/perMinute))).setScale(0,BigDecimal.ROUND_DOWN);
				
				examArrangeCode = CharsetSwitch.CharacterUpperCase(tmpCode);//+tick.longValue();
						//DateUtils.convertDateToStr(new Date(), "yyyyMMdd");
				//System.out.println(examArrangeCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			examArrangeCode = "";
		}
		return "examCodePy";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 查询是否编码有重复
	 * @return
	 * @modified
	 */
	public String querylistInExamCode(){
		Map param = new HashMap();
		Map sortMap = new HashMap();
		param.put("id", this.getId());
		param.put("examCode", examCode);
		List<ExamArrange> exampojolist = this.getService().queryData("listInExamCodeHql", param , sortMap);
		if(exampojolist!=null && exampojolist.size()>0){
			isExist = "1";
		}else{
			isExist = "0";
		}
		return "isExistCode";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 查询是否编码有重复
	 * @return
	 * @modified
	 */
	public String querylistInExamArrangeName(){
		Map param = new HashMap();
		Map sortMap = new HashMap();
		param.put("id", this.getId());
		param.put("examArrangeName", examArrangeName.trim());
		List<ExamArrange> exampojolist = this.getService().queryData("listInExamArrangeNameHql", param , sortMap);
		if(exampojolist!=null && exampojolist.size()>0){
			isExist = "1";
		}else{
			isExist = "0";
		}
		return "isExistCode";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 查询列表
	 * @return
	 * @modified
	 */
	public String list(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		ExamVariable.setHttpRequest(request);
		
		ExampaperService exampaperService = (ExampaperService)this.getService();
		//exampaperService.saveAndsubInExamUserTestpaper();
		
		examList = new ArrayList<ExamForm>();
		Map param = new HashMap();
		param.put("examNameTerm", examNameTerm);
		param.put("nameTerm", nameTerm);
		param.put("testpaperNameTerm", testpaperNameTerm);
		param.put("examStartTimeMaxTerm", examStartTimeMaxTerm);
		param.put("examStartTimeMinTerm", examStartTimeMinTerm);
		param.put("creationDateMaxTerm", creationDateMaxTerm);
		param.put("creationDateMinTerm", creationDateMinTerm);
		param.put("examTypeIdTerm", examTypeIdTerm);
		param.put("organId",usersess.getCurrentOrganId());
		
		if("30".equals(examType)){
			param.put("examProperty",examType);
		}else if("40".equals(examType)){
			param.put("examProperty",examType);
		}else{
			param.put("examProperty", "10,20");
		}
		
		Map sortMap = new HashMap();
		sortMap.put("createdBy", false);
		//List<ExamArrange> exampojolist = this.getService().queryData("examArrangeForParamHql", param , sortMap);
		List<Exam> exams= this.getService().queryData("examQueryForParamHql", param , sortMap,this.getStart(),this.getLimit());
		if(exams!=null && exams.size()>0){
			Map haveUserReviewExamMap = new HashMap();
			Map haveMarkpeopleMap = new HashMap();
			if(!"base".equals(this.getOp())){
				haveUserReviewExamMap = exampaperService.getHaveUserReviewExam();
				haveMarkpeopleMap = exampaperService.getHaveMarkpeople();
			}
			//for(int i=0;i<exampojolist.size();i++){
				//ExamArrange examArrange = exampojolist.get(i);
				//ExamForm parent = new ExamForm();
				//parent.setExamId(examArrange.getExamArrangeId());
				//parent.setExamName(examArrange.getExamName());
				//parent.setCreationDate(examArrange.getCreationDate());
				//parent.setState(examArrange.getState());
				//parent.setIsExam("20");
				//examList.add(parent);
				
				//List exams = examArrange.getExams();
				for(int k = 0 ;k<exams.size();k++){
					Exam exam = (Exam)exams.get(k);
					ExamForm examForm = new ExamForm();
					examForm.setPid(exam.getExamArrange().getExamArrangeId());
					examForm.setExamId(exam.getExamId());
					//examForm.setExamName(exam.getExamName());
					if(exam.getExamArrange().getExamName()!=null 
							&& exam.getExamArrange().getExamName().equals(exam.getExamName())){
						examForm.setExamName(exam.getExamName());
					}else{
						examForm.setExamName(exam.getExamArrange().getExamName() +" [ "+exam.getExamName()+" ]");
					}
					examForm.setExamStartTime(exam.getExamStartTime());
					examForm.setExamEndTime(exam.getExamEndTime());
					examForm.setState(exam.getExamArrange().getState());
					examForm.setCreationDate(exam.getCreationDate());
					examForm.setIsExam("10");
					if(StaticVariable.examCrIdMap.get(examForm.getExamId())!=null){
						examForm.setStCreateNow("T");
					}
					if(haveUserReviewExamMap.get(exam.getExamId())!=null){
						examForm.setHaveUserReviewExam("是");
						if(haveMarkpeopleMap.get(exam.getExamId())!=null){
							examForm.setHaveYjr("已配置");
						}else{
							examForm.setHaveYjr("未配置");
						}
					}else{
						examForm.setHaveUserReviewExam("否");
						examForm.setHaveYjr("-");
					}
					
					//parent.getExamList().add(examForm);
					examList.add(examForm);
				}
			//}
		}
		//examListTotal = examList.size();
		setTotal(this.getService().countData("examQueryForParamHql", param));
		return "list";
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
				String mid = ids[i];
				if(StaticVariable.examCrIdMap.get(mid)!=null){
					scexers+=mid+",";
				}
			}
			if(scexers.length()>0)scexers = scexers.substring(0,scexers.length()-1);
		}
		return "scexers";
	} 
	
	
	/**
	 * 删除
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String delete() throws Exception{
		String[] ids = this.getId().split(",");
		boolean isFlag = false;
		for(int i = 0;i<ids.length ;i++){
			ExamArrange  examArrange = (ExamArrange)service.findDataByKey(ids[i], ExamArrange.class);
			try{
				if(examArrange==null){
					Exam exam = (Exam)service.findDataByKey(ids[i], Exam.class);
					if(exam!=null){
						if("5".equals(exam.getState())){//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回 30-发布
							try{
								service.delete(exam);
								isFlag = true;
							}catch(Exception e){
								e.printStackTrace();
								isFlag = false;
								this.setMsg("删除记录失败！");
							}
						}else{
							isFlag = false;
							this.setMsg("未上报状态才能删除！");
						}
					}
				}else{
					if("5".equals(examArrange.getState())){//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回 30-发布
						try{
							service.delete(examArrange);
							isFlag = true;
						}catch(Exception e){
							e.printStackTrace();
							isFlag = false;
							this.setMsg("删除记录失败！");
							
						}
					}else{
						isFlag = false;
						this.setMsg("未上报状态才能删除！");
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				isFlag = false;
				throw e;
			}
		}
		return "delete";
	}
	
	
	/**
	 * 考试作废
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String cancelled() throws Exception{
		String[] ids = this.getId().split(",");
		for(int i = 0;i<ids.length ;i++){
			ExamArrange  examArrange = (ExamArrange)service.findDataByKey(ids[i], ExamArrange.class);
			try{
				if(examArrange==null){
					Exam exam = (Exam)service.findDataByKey(ids[i], Exam.class);
					if(exam!=null){
						examArrange = exam.getExamArrange();
					}
					if(examArrange!=null){
						examArrange.setState("-1");
						Iterator<Exam> its = examArrange.getExams().iterator();
						while(its.hasNext()){
							Exam exam2 = its.next();
							exam2.setState("-1");
							this.getService().saveOld(exam2);
						}
						this.getService().saveOld(examArrange);
					}
				}else{
					examArrange.setState("-1");
					Iterator<Exam> its = examArrange.getExams().iterator();
					while(its.hasNext()){
						Exam exam = its.next();
						exam.setState("-1");
						this.getService().saveOld(exam);
					}
					this.getService().saveOld(examArrange);
				}
				this.setMsg("考试作废操作成功！");
			}catch(Exception e){
				e.printStackTrace();
				this.setMsg("考试作废操作失败！");
				throw e;
			}
		}
		return "delete";
	}
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 成绩发布
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String publicExam()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		//String publicTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		 ExampaperService exampaperService = (ExampaperService)this.getService();
		String[] ids = this.getId().split(",");
		for(int i = 0;i<ids.length ;i++){	
			exampaperService.saveAndPublicInExamArrange(ids[i], 
					usersess.getEmployeeId()==null?usersess.getAccount():usersess.getEmployeeId(), 
					usersess.getEmployeeName()==null?usersess.getAccount():usersess.getEmployeeName());
			
			ExamArrange examArrange = (ExamArrange)exampaperService.findDataByKey(ids[i], ExamArrange.class);
			if(examArrange!=null){
				if("30".equals(examArrange.getState())){
					this.setMsg("成绩发布成功！");
				}else{
					this.setMsg("成绩发布失败，请查看是否已完成全部阅卷工作！");
				}
			}else{
				this.setMsg("成绩发布失败！");
			}
			//examThreadService.addPublicExam(ids[i], usersess);
			//Exam exam = (Exam)exampaperService.findDataByKey(ids[i], Exam.class);
			//try{
				//ExamArrange examArrange = null;
				//if(exam==null){
					//examArrange = (ExamArrange)exampaperService.findDataByKey(ids[i], ExamArrange.class);
					//examThreadService.addPublicExam(ids[i], usersess);
				//}else{
					//examThreadService.addPublicExam(exam.getExamArrange().getExamArrangeId(), usersess);
					//examArrange  = (ExamArrange)exampaperService.findDataByKey(exam.getExamArrange().getExamArrangeId(), ExamArrange.class);//= exam.getExamArrange();
				//}	
				//if(examArrange!=null){
					//examThreadService.addPublicExam(ids[i], usersess);
					//exampaperService.saveAndPublicExam(examArrange,usersess)
					//this.setMsg("成绩发布中，请稍后！");
				//}else{
					//this.setMsg("没有找到对应的记录！");
				//}
				
			//}catch(Exception e){
				//this.setMsg("发布失败！");
				//e.printStackTrace();
				//throw e;
			//}
			
		}
		return "delete";
	}
	
/*	public static void main(String[] args){
		String publicTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		System.out.println("publicTime : "+publicTime);
		String stateTime = DateUtils.getDateAddDay(publicTime.substring(0,10),"yyyy-MM-dd",1);
		System.out.println("stateTime : "+stateTime);
		String year = stateTime.substring(0,4);
		String endTime = (Integer.parseInt(year)+1)+(stateTime.substring(4,10));
		endTime = DateUtils.getDateAddDay(endTime,"yyyy-MM-dd",-1);
		System.out.println("endTime : "+endTime);
		
	}*/
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取考试类型
	 * @return
	 * @modified
	 */
	public String getDicKslx(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		examKslxList = new ArrayList();
		List<Dictionary> ls = exampaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
		if(ls!=null && ls.size()>0){
			if("10".equals(examPropertyTerm)){
				for(int i=0;i<ls.size();i++){
					Dictionary tmp = ls.get(i);
					if(tmp.getDataName().indexOf("竞赛")==-1){
						examKslxList.add(tmp);
					}
				}
			}else if("20".equals(examPropertyTerm)){
				for(int i=0;i<ls.size();i++){
					Dictionary tmp = ls.get(i);
					if(tmp.getDataName().indexOf("竞赛")!=-1){
						examKslxList.add(tmp);
					}
				}
			}else{
				examKslxList.addAll(ls);
			}
			
		}
		return "dickslx";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取考试类型
	 * @return
	 * @modified
	 */
	public String getDicKslxAndNull(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		examKslxList = new ArrayList();
		Dictionary dictionary = new Dictionary();
		dictionary.setDataName("全部");
		dictionary.setDataKey(null);
		examKslxList.add(dictionary);
		List<Dictionary> ls = exampaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
		if(ls!=null && ls.size()>0){
			if("10".equals(examPropertyTerm)){
				for(int i=0;i<ls.size();i++){
					Dictionary tmp = ls.get(i);
					if(tmp.getDataName().indexOf("竞赛")==-1){
						examKslxList.add(tmp);
					}
				}
			}else if("20".equals(examPropertyTerm)){
				for(int i=0;i<ls.size();i++){
					Dictionary tmp = ls.get(i);
					if(tmp.getDataName().indexOf("竞赛")!=-1){
						examKslxList.add(tmp);
					}
				}
			}else{
				examKslxList.addAll(ls);
			}
			
		}
		return "dickslx";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 获取可选的发布内容
	 * @return
	 * @modified
	 */
	public String queryExamPublicList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			examPublicList = new ArrayList();
			return "examPublicList";
	 	}
		ExampaperService exampaperService = (ExampaperService)this.getService();
		if("all".equals(this.getOp())){
			examPublicList = exampaperService.queryData("examPublicListHql", null, null);//   queryAllDate(ExamPublic.class);
		}else if("ctandjoinAndEndTime".equals(this.getOp())){
			Map termMap = new HashMap();
			termMap.put("organId" ,usersess.getCurrentOrganId());
			termMap.put("examEndTime" ,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			examPublicList = exampaperService.queryData("examPublicListInCreateAndJoinOrganAndEndTimeHql", termMap, null);
		}else if("ctandjoin".equals(this.getOp())){
			Map termMap = new HashMap();
			termMap.put("organId" ,usersess.getCurrentOrganId());
			examPublicList = exampaperService.queryData("examPublicListInCreateAndJoinOrganHql", termMap, null);
		}else if("create".equals(this.getOp())){
			Map termMap = new HashMap();
			termMap.put("organId" ,usersess.getCurrentOrganId());
			examPublicList = exampaperService.queryData("examPublicListInCreateHql", termMap, null);
		}else{
			Map termMap = new HashMap();
			termMap.put("organId" ,usersess.getCurrentOrganId());
			examPublicList = exampaperService.queryData("examPublicListInOrganHql", termMap, null);
		}
		
		return "examPublicList";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取可选的发布内容
	 * @return
	 * @modified
	 */
	public String queryExamPublicTree(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		children = new ArrayList();
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "examPublicList";
	 	}
		ExampaperService exampaperService = (ExampaperService)this.getService();
		
		String[][] treenames = {{"unUsed","未安排考试的考试安排"},{"used","已安排考试的考试安排"}};
		for(int ii=0;ii<treenames.length;ii++){
			String tcode = treenames[ii][0];
			String tname = treenames[ii][1];
			List<ExamPublic> list = null;
			if("all".equals(this.getOp())){
				list = exampaperService.queryData(tcode+"ExamPublicListHql", null, null);//   queryAllDate(ExamPublic.class);
			}else if("ctandjoin".equals(this.getOp())){
				Map termMap = new HashMap();
				termMap.put("organId" ,usersess.getCurrentOrganId());
				list = exampaperService.queryData(tcode+"ExamPublicListInCreateAndJoinOrganHql", termMap, null);
			}else if("create".equals(this.getOp())){
				Map termMap = new HashMap();
				termMap.put("organId" ,usersess.getCurrentOrganId());
				list = exampaperService.queryData(tcode+"ExamPublicListInCreateHql", termMap, null);
			}else{
				Map termMap = new HashMap();
				termMap.put("organId" ,usersess.getCurrentOrganId());
				list = exampaperService.queryData(tcode+"ExamPublicListInOrganHql", termMap, null);
			}
			if(list!=null && list.size()>0){
				TreeNode parent = null;
				for(int k=0;k<list.size();k++){
					if(parent == null){
						parent = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
						parent.setId(tcode);
						parent.setTitle(tname);
						parent.setType("type");
						parent.setChildren(new ArrayList());
						children.add(parent);
					}
					ExamPublic examPublic = list.get(k);
					TreeNode child = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
					child.setId(examPublic.getPublicId());
					child.setTitle(examPublic.getExamTitle());
					child.setType("examPublic");
					child.setParentId(parent.getId());
					child.setLeaf(true);
					parent.getChildren().add(child);
				}
			}
		}
		
		
		
		return "children";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 考生树
	 * @return
	 * @modified
	 */
	public String queryExamineeTree(){
		examineeTreeList = new ArrayList();
		try{
			Exam exam = (Exam)this.getService().findDataByKey(this.getId(), Exam.class);
			ExampaperService exampaperService = (ExampaperService)this.getService();
			if(exam!=null){
				String publicId = exam.getExamArrange().getExamPublic().getPublicId();
				examineeTreeList = exampaperService.getExamineeTree(publicId, exam.getExamId());;
				/*
				Map term = new HashMap();
				term.put("publicId", publicId);
				term.put("publicIdInUser",publicId);
				term.put("examId",exam.getExamId());
				List<ExamPublicUser> unExamineeList = this.getService().queryData("unExamExamineeHql", term, new HashMap());
				if(unExamineeList!=null && unExamineeList.size()>0){
					TreeNode parentNode = new TreeNode();
					parentNode.setId("unExaminee");
					parentNode.setTitle("未选择的考生");
					parentNode.setType("parentN");
					//parentNode.setParentId(dept.getDeptId());
					parentNode.setChildren(new ArrayList());
					
					for(int i=0;i<unExamineeList.size();i++){
						ExamPublicUser examPublicUser = (ExamPublicUser)unExamineeList.get(i);
						TreeNode childeNode = new TreeNode();
						childeNode.setId(examPublicUser.getUserId());
						childeNode.setTitle(examPublicUser.getUserName());
						childeNode.setType("exam");
						childeNode.setLeaf(true);
						childeNode.setParentId(parentNode.getId());
						parentNode.getChildren().add(childeNode);
					}
					examineeTreeList.add(parentNode);
				}
				
				List<ExamPublicUser> examineeList = this.getService().queryData("examExamineeHql", term, new HashMap());
				if(examineeList!=null && examineeList.size()>0){
					TreeNode parentNode = new TreeNode();
					parentNode.setId("examinee");
					parentNode.setTitle("已选择的考生");
					parentNode.setType("parentN");
					//parentNode.setParentId(dept.getDeptId());
					parentNode.setChildren(new ArrayList());
					
					for(int i=0;i<examineeList.size();i++){
						ExamPublicUser examPublicUser = (ExamPublicUser)examineeList.get(i);
						TreeNode childeNode = new TreeNode();
						childeNode.setId(examPublicUser.getUserId());
						childeNode.setTitle(examPublicUser.getUserName());
						childeNode.setType("exam");
						childeNode.setLeaf(true);
						childeNode.setParentId(parentNode.getId());
						parentNode.getChildren().add(childeNode);
					}
					examineeTreeList.add(parentNode);
				}*/
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "examineeList";
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 考生树
	 * @return
	 * @modified
	 */
	public String queryExamineeOrganTree(){
		children = new ArrayList();
		try{
			Exam exam = (Exam)this.getService().findDataByKey(this.getId(), Exam.class);
			ExampaperService exampaperService = (ExampaperService)this.getService();
			if(exam!=null){
				String publicId = exam.getExamArrange().getExamPublic().getPublicId();
				children = exampaperService.getExamineeOrganTree(publicId, exam.getExamId());;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "children";
	}
	
	/**
	 * 查询考生的考试情况
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public String queryExamTjxx(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		String examTypeId = StaticVariable.EXAM_EXAM_PROPERTY;
		examUserList = exampaperService.getExamTjxxByUser( qid, qexamId,qstartDay,qendDay,examTypeId);
		examUserTotal = examUserList.size();
		return "examUserList";
	}
	
	public String queryExamInTopDeptTjxx(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		String examTypeId = StaticVariable.EXAM_EXAM_PROPERTY;
		examUserList = exampaperService.getExamDeptTjxxByUser( qid, qexamId,qstartDay,qendDay,examTypeId);
		examUserTotal = examUserList.size();
		return "examUserList";
	}
	
	public String queryExamInDeptTjxxByQuarter(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		Quarter quarter= (Quarter)exampaperService.findDataByKey(qid, Quarter.class);
		if(quarter!=null){
			Dept dept = quarter.getDept();
			String examTypeId = StaticVariable.EXAM_EXAM_PROPERTY;
			examUserList = exampaperService.getExamDeptTjxxByUser( dept.getDeptId(), qexamId,qstartDay,qendDay,examTypeId);	
		}else{
			examUserList = new ArrayList();
		}
		
		examUserTotal = examUserList.size();
		return "examUserList";
	}

	/**
	 * 查询考生的考试情况
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public String queryExamUser(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		String examTypeId = StaticVariable.EXAM_EXAM_PROPERTY;
		examUserList = exampaperService.getExamUserInfos( qid, qtype, qexamId,qstartDay,qendDay,examTypeId);
		examUserTotal = examUserList.size();
		return "examUserList";
	}
	
	/**
	 * 查询考生信息
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String queryExamineeInfoList(){
		ExampaperService exampaperService = (ExampaperService)this.getService();
		examineeInfoList = new ArrayList();
		
		Exam exam = (Exam)exampaperService.findDataByKey(this.getId(), Exam.class);
		Map<String,File> titleMap = new HashMap<String,File>();
		Map<String,int[][]> themeStateMap = new HashMap<String,int[][]> ();
		if(!"base".equals(this.getOp())){
			
			
			File file = new File(ExamVariable.getExamFilePath(request));  
			File[] files = file.listFiles(); 
			
			if(files!=null && files.length>0)
			for(int i=0;i<files.length;i++){
				String fileName = files[i].getName();
				titleMap.put(fileName, files[i]);
			}
			
			file = new File(ExamVariable.getExamUserFilePath(request)+exam.getExamId()+System.getProperty("file.separator"));  
			files = file.listFiles(); 
			if(files!=null && files.length>0)
			for(int i=0;i<files.length;i++){
				String fileName = files[i].getName();
				titleMap.put(fileName, files[i]);
			}
			
			//System.out.println(ExamVariable.getSaveAnsPath(request)+exam.getExamId()+System.getProperty("file.separator"));
			file = new File(ExamVariable.getSaveAnsPath(request)+exam.getExamId()+System.getProperty("file.separator"));  
			files = file.listFiles(); 
			if(files!=null && files.length>0)
			for(int i=0;i<files.length;i++){
				String fileName = files[i].getName();
				//System.out.println("user:"+fileName);
				titleMap.put("userExam_"+fileName, files[i]);
			}
		
		
			//查询阅卷状态 
			Map paramMap = new HashMap();
			paramMap.put("examId", this.getId());
			List themeStateList = service.queryData("queryThemeState", paramMap, null, null);
			
			if(themeStateList!=null && themeStateList.size()>0){
				for(int i=0;i<themeStateList.size();i++){
					Map t = (Map) themeStateList.get(i);
					String examTestpaperId = (String)t.get("EXAMTESTPAPERID");
					BigDecimal succ = (BigDecimal)t.get("SUCC");
					BigDecimal unsucc = (BigDecimal)t.get("UNSUCC");
					BigDecimal notsub = (BigDecimal)t.get("NOTSUB");
					String scoreType = (String)t.get("SCORETYPE");
					int[][] intstate = (int[][])themeStateMap.get(examTestpaperId);
					if(intstate == null){
						intstate = new int[][]{{0,0,0},{0,0,0},{0,0,0}};
					}
					intstate[Integer.parseInt(scoreType)] = new int[]{succ.intValue(),unsucc.intValue(),notsub.intValue()};
					themeStateMap.put(examTestpaperId,intstate);
				}
			}
		}
		String iocpUrl =  ExamVariable.getRequestUrl(request);
		
		String organId = "";
		String quarter_train_id = "";
		if("allOrgan".equals(this.getNodeId())){
			organId = null;
			quarter_train_id = null;
		}else if("organ".equals(this.getNodeType())){
			organId = this.getNodeId();
			quarter_train_id = null;
		}else if("quarter".equals(this.getNodeType())){
			String[] tmpArr = this.getNodeId().split("@");
			organId = tmpArr[0];
			quarter_train_id = tmpArr[1];
		}
		List<ExamUserTestpaper> examUserTestpaperList = exampaperService.getExamUserTestpaperList(this.getId(), organId,quarter_train_id);
		for(int i=0;i<examUserTestpaperList.size();i++){
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)examUserTestpaperList.get(i);
			ExamUserForm examUserForm = new ExamUserForm();
			try{
			
				BeanUtils.copyProperties(examUserTestpaper,examUserForm);
				examUserForm.setIocpUrl(iocpUrl);
				
				if(!"base".equals(this.getOp())){
					String yjstateRemark = "";
					ExamTestpaper examTestpaper = examUserTestpaper.getExamTestpaper();
					if(examTestpaper!=null){
						int[][] intstate = themeStateMap.get(examUserTestpaper.getExamTestpaper().getExamTestpaperId());
						if(intstate!=null){
							boolean isFlag = true;
							for(int k=0;k<intstate.length;k++){
								if(k==0){
									yjstateRemark+="自动阅卷={";
								}else if(k==1){
									yjstateRemark+="人工阅卷={";
								}else if(k==2){
									yjstateRemark+="系统外导入={";
								}
								yjstateRemark+="已阅 "+intstate[k][0]+" 题|未阅 "+intstate[k][1]+" 题|未提交阅卷 "+intstate[k][2]+" 题|共 "+(intstate[k][0]+intstate[k][1]+intstate[k][2])+" 题},<br>";
								if(intstate[k][1]>0 || intstate[k][2]>0){
									isFlag = false;
								}
							}
							if(isFlag){
								examUserForm.setYjstate("20");
							}else{
								examUserForm.setYjstate("10");
							}
							yjstateRemark=yjstateRemark.substring(0,yjstateRemark.length()-5);
						}
					}
					examUserForm.setYjstateRemark(yjstateRemark);
					
					if(examUserTestpaper.getExamPublicUser()!=null){
						examUserForm.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
					}
					examUserForm.setInitType("待考中");
					if(examUserTestpaper.getInTime()!=null && !"".equals(examUserTestpaper.getInTime())){
						examUserForm.setInitType("考试中");
					}
					if(examUserTestpaper.getSubTime()!=null && !"".equals(examUserTestpaper.getSubTime())){
						examUserForm.setInitType("已交卷");
					}
					//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
					if(examUserTestpaper.getState()!=null && "20".equals(examUserTestpaper.getState())){
						examUserForm.setInitType("已阅卷");
					}
					if(examUserTestpaper.getState()!=null && "30".equals(examUserTestpaper.getState())){
						examUserForm.setInitType("已发布");
					}
					String flname = "title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
					File titleFile = (File)titleMap.get(flname);
					if(titleFile!=null){
						String fileStr = FileOption.readFile(titleFile);
						examUserForm.setTitleFile(1);
						examUserForm.setTitleFileString(fileStr);
						
					}
					
					flname = "ans_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
					File ansFile = (File)titleMap.get(flname);
					if(exam.getExamArrange()!=null 
							&& exam.getExamArrange().getIsPublic()!=null 
							&& exam.getExamArrange().getIsPublic().intValue() != 10){
						examUserForm.setAnsFile(-1);
					}else if(titleFile!=null){
						String fileStr = FileOption.readFile(ansFile);
						examUserForm.setAnsFile(1);
						examUserForm.setAnsFileString(fileStr);
					}else{
						examUserForm.setAnsFile(0);
					}
					
					flname = "userExam_"+examUserTestpaper.getInticket()+".txt";
					String userAns = examUserTestpaper.getExamTestpaper()==null? null: examUserTestpaper.getExamTestpaper().getExamFileUserAnswerkey();
					titleFile = (File)titleMap.get(flname);
					if(userAns!=null && !"".equals(userAns)){
						examUserForm.setUserAnsFile(1);
					}else if(titleFile!=null){
						examUserForm.setUserAnsFile(1);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			examineeInfoList.add(examUserForm);
		}
		examineeInfoListTotal = examineeInfoList.size();
		return "examineeInfoList";
	}
	
	
	public String initExamFile(){
		Exam exam =  (Exam) service.findDataByKey(this.getId(), Exam.class);
		if(exam!=null){
			ExamUserJsonForm.getAndSaveExamUser(request, exam);
		}
		return "save";
	}


	public String scoreQueryList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
		}
		ExamVariable.setHttpRequest(request);
		String onlyexam = request.getParameter("onlyexam");
		
		ExampaperService exampaperService = (ExampaperService)this.getService();
		exampaperService.saveAndsubInExamUserTestpaper();
		
		examList = new ArrayList<ExamForm>();
		Map param = new HashMap();
		
		Map sortMap = new HashMap();
		sortMap.put("createdBy", false);
		List<ExamArrange> exampojolist = null;
		if("true".equals(onlyexam)){
			param.put("organId", usersess.getCurrentOrganId());
			exampojolist = this.getService().queryData("examArrangeOnlyExamForScoreQueryHql", param , sortMap);
		}else{
			exampojolist = this.getService().queryData("examArrangeForScoreQueryHql", param , sortMap);
		}
		if(exampojolist!=null){
			for(int i=0;i<exampojolist.size();i++){
				ExamArrange examArrange = exampojolist.get(i);
				//ExamForm parent = new ExamForm();
				//parent.setExamId(examArrange.getExamArrangeId());
				//parent.setExamName(examArrange.getExamName());
				//parent.setCreationDate(examArrange.getCreationDate());
				//parent.setState(examArrange.getState());
				//parent.setIsExam("20");
				
				//boolean isSetParent = true;
				List exams = examArrange.getExams();
				for(int k = 0 ;k<exams.size();k++){
					Exam exam = (Exam)exams.get(k);
					ExamForm examForm = new ExamForm();
					examForm.setPid(null);//examArrange.getExamArrangeId()
					examForm.setExamId(exam.getExamId());
					examForm.setExamName(exam.getExamName());
					examForm.setExamStartTime(exam.getExamStartTime());
					examForm.setExamEndTime(exam.getExamEndTime());
					examForm.setState(examArrange.getState());
					examForm.setCreationDate(exam.getCreationDate());
					examForm.setIsExam("10");
					
					if((this.getNameTerm()!=null && !"".equals(this.getNameTerm()) 
							&& exam.getExamName().indexOf(this.getNameTerm())!=-1)
							||  this.getNameTerm()==null || "".equals(this.getNameTerm())){
						examList.add(examForm);
					}
					
				}
			}
		}
		examListTotal = examList.size();
		return "list";
	}
	
	/*
	 * 根据 登陆人id 返回考试安排及考试科目树
	 */
	public String empIdlist(){
		examList = new ArrayList<ExamForm>();
		UserSession usersess = LoginAction.getUserSessionInfo();
		Map param = new HashMap();
		param.put("empId", usersess.getEmployeeId());
		Map sortMap = new HashMap();
		sortMap.put("createdBy", false);
		List<ExamArrange> exampojolist = this.getService().queryData("examArrangeExamineeForParamHql", param , sortMap);
		if(exampojolist!=null){
			for(int i=0;i<exampojolist.size();i++){
				ExamArrange examArrange = exampojolist.get(i);
				//ExamForm parent = new ExamForm();
				//parent.setExamId(examArrange.getExamArrangeId());
				//parent.setExamName(examArrange.getExamName());
				//parent.setCreationDate(examArrange.getCreationDate());
				//parent.setState(examArrange.getState());
				
				
				//boolean isSetParent = true;
				
				List exams = examArrange.getExams();
				for(int k = 0 ;k<exams.size();k++){
					Exam exam = (Exam)exams.get(k);
					ExamForm examForm = new ExamForm();
					examForm.setPid(null);//examArrange.getExamArrangeId()
					examForm.setExamId(exam.getExamId());
					examForm.setExamName(exam.getExamName());
					examForm.setExamStartTime(exam.getExamStartTime());
					examForm.setExamEndTime(exam.getExamEndTime());
					examForm.setState(examArrange.getState());
					examForm.setCreationDate(exam.getCreationDate());
					
					if((this.getNameTerm()!=null && !"".equals(this.getNameTerm()) 
							&& exam.getExamName().indexOf(this.getNameTerm())!=-1)
							||  this.getNameTerm()==null || "".equals(this.getNameTerm())){
						//if(isSetParent){
							//examList.add(parent);
							//isSetParent = false;
						//}
						examList.add(examForm);
					}
					
				}
			}
		}
		examListTotal = examList.size();
		return "list";
	}
	/*
	 * 阅卷列表
	 */
	public String reviewList(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		boolean isAdmin = false;
		String adminflag = request.getParameter("isAdmin");
		if(!StringUtils.isEmpty(adminflag) && "true".equals(adminflag)){
			isAdmin = true;
		}
		
		Map param = new HashMap();
		param.put("examNameTerm", examNameTerm);
		param.put("testpaperNameTerm", testpaperNameTerm);
		param.put("scoreType", "1");//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
		if(!isAdmin){
			param.put("empId", usersess.getEmployeeId());//员工id过滤
		}
		Map sortMap = new HashMap();
		sortMap.put("createdBy", false);
		//List<ExamArrange> exampojolist = this.getService().queryData("examArrangeReviewedForParamHql", param , sortMap);
		
		List reviewCountList = service.queryData("queryReviewCount", null, null, null);
		Map<String,ReviewForm> reviewMap = new HashMap<String,ReviewForm>();
		if(reviewCountList!=null && reviewCountList.size()>0){
			Date nowDate = new Date();
			for (Object obj : reviewCountList) {
				Map t = (Map) obj;
				ReviewForm rt = new ReviewForm();
				rt.setExamId(t.get("EXAMID").toString());
				int FIVECOUNT = Integer.parseInt(t.get("FIVECOUNT").toString());//初始化题目数量
				int TENCOUNT = Integer.parseInt(t.get("TENCOUNT").toString());//待考
				int REVIEWEDCOUNT = Integer.parseInt(t.get("REVIEWEDCOUNT").toString());//已阅卷
				int NEEDREVIEWCOUNT = Integer.parseInt(t.get("NEEDREVIEWCOUNT").toString());//需要阅卷
				int BACKCOUNT = Integer.parseInt(t.get("BACKCOUNT").toString());//打回
				int FINCOUNT = Integer.parseInt(t.get("FINCOUNT").toString());//完成
				NEEDREVIEWCOUNT+=BACKCOUNT;
				String examstate = t.get("EXAMSTATE").toString();
				int flag = 1;
				
				rt.setNeedReviewCount(NEEDREVIEWCOUNT+"");
				rt.setUnReviewCount(FIVECOUNT+TENCOUNT+"");
				rt.setSuccReviewCount(REVIEWEDCOUNT+"");
				if(NEEDREVIEWCOUNT>0){
					
				}else if("0".equals(NEEDREVIEWCOUNT) && (FIVECOUNT!=0 || TENCOUNT!=0)){
					flag = 0;
				}else{
					flag = 2;
					rt.setNeedReviewCount("0");
				}
				String strSt = t.get("ST").toString();
				String strEd = t.get("ET").toString();
				
				Date stDate = getDateByCal(strSt);
				Date etDate = getDateByCal(strEd);
				
				// 0:试卷未提交  1: 可阅卷   2:未到阅卷时间  3:已过阅卷时间
				if("30".equals(examstate)){//已发布
					rt.setYjState("4");
				}else if(flag==0){
					rt.setYjState("0");
				}else if(nowDate.getTime()>=stDate.getTime() && nowDate.getTime()<=etDate.getTime()){
					rt.setYjState("1");
				}else if(nowDate.getTime()<stDate.getTime()){
					rt.setYjState("2");
				}else if(nowDate.getTime()>etDate.getTime()){
					//System.out.println(nowDate+" "+etDate);
					rt.setYjState("3");
				}
				
				rt.setReviewStartTime(strSt);
				rt.setReviewEndTime(strEd);
				reviewMap.put(rt.getExamId(), rt);
			}
		}
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<Exam> exams = null;//this.getService().queryData("examInReviewedForParamHql",
				//param , sortMap,this.getStart(),this.getLimit());
		if(param.get("empId")!=null){
			exams = this.getService().queryData("examInReviewedNotEmpIdForParamHql",
					param , sortMap,this.getStart(),this.getLimit());
		}else{
			exams = this.getService().queryData("examInReviewedAllEmpForParamHql",
					param , sortMap,this.getStart(),this.getLimit());
		}
		if(exams!=null){
			//for(int i=0;i<exampojolist.size();i++){
				//ExamArrange examArrange = exampojolist.get(i);
				//ExamReviewForm parent = new ExamReviewForm();
				//parent.setExamId(examArrange.getExamArrangeId());
				//parent.setExamName(examArrange.getExamName());
				//parent.setCreationDate(examArrange.getCreationDate());
				//parent.setState(examArrange.getState());
				//examReviewList.add(parent);
				
				//List exams = examArrange.getExams();
				for(int k = 0 ;k<exams.size();k++){
					Exam exam = (Exam)exams.get(k);
					ExamReviewForm examForm = new ExamReviewForm();
					examForm.setPid(exam.getExamArrange().getExamArrangeId());
					examForm.setExamId(exam.getExamId());
					//examForm.setExamArrangeName(exam.getExamArrange().getExamName());
					if(exam.getExamArrange().getExamName()!=null 
							&& exam.getExamArrange().getExamName().equals(exam.getExamName())){
						examForm.setExamName(exam.getExamName());
					}else{
						examForm.setExamName(exam.getExamArrange().getExamName() +" [ "+exam.getExamName()+" ]");
					}
					
					examForm.setExamStartTime(exam.getExamStartTime());
					examForm.setExamEndTime(exam.getExamEndTime());
					//examForm.setState(examArrange.getState());
					examForm.setCreationDate(exam.getCreationDate());
					List<ExamMarkpeople> examMarkpeoples = exam.getExamMarkpeoples();
					for (ExamMarkpeople examMarkpeople : examMarkpeoples) {
						if(examMarkpeople.getEmpId().equals(usersess.getEmployeeId())){
							examForm.setAllowReview(true);
							if(examMarkpeople.getIsMain()==5){
								examForm.setIsMain(true);
							}
							break;
						}
					}
					if(reviewMap.containsKey(exam.getExamId())){
						examForm.setNeedReviewCount(reviewMap.get(exam.getExamId()).getNeedReviewCount());
						examForm.setReviewStartTime(reviewMap.get(exam.getExamId()).getReviewStartTime());
						examForm.setReviewEndTime(reviewMap.get(exam.getExamId()).getReviewEndTime());
						examForm.setUnReviewCount(reviewMap.get(exam.getExamId()).getUnReviewCount());
						examForm.setSuccReviewCount(reviewMap.get(exam.getExamId()).getSuccReviewCount());
						examForm.setCurrentTime(nowTime);
						examForm.setState(reviewMap.get(exam.getExamId()).getYjState());
					}
					String tempKey = usersess.getEmployeeId()+"@"+exam.getExamId();
					if(ExamReviewListAction.peopleToExam.containsKey(tempKey)){
						examForm.setShowRelease(tempKey);
					}
					examReviewList.add(examForm);
					//parent.getExamReviewList().add(examForm);
				}
			//}
		}
		setTotal(this.getService().countData("examInReviewedForParamHql", param));
		return "reviewList";
	}
	/*
	 * 系统复核分数导入列表
	 */
	public String checkImportSysScoreList(){
		UserSession usersess = LoginAction.getUserSessionInfo();
		boolean isAdmin = false;
		String adminflag = request.getParameter("isAdmin");
		if(!StringUtils.isEmpty(adminflag) && "true".equals(adminflag)){
			isAdmin = true;
		}
		
		Map param = new HashMap();
		param.put("examNameTerm", examNameTerm);
		param.put("testpaperNameTerm", testpaperNameTerm);
		param.put("scoreType", "2");//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
		if(!isAdmin){
			param.put("empId", usersess.getEmployeeId());//员工id过滤
		}
		Map sortMap = new HashMap();
		sortMap.put("createdBy", false);
		//List<ExamArrange> exampojolist = this.getService().queryData("examArrangeReviewedForParamHql", param , sortMap);
		
		List checkCountList = service.queryData("queryCheckImportCount", null, null, null);
		Map<String,ReviewForm> reviewMap = new HashMap<String,ReviewForm>();
		if(checkCountList!=null && checkCountList.size()>0){
			for (Object object : checkCountList) {
				HashMap obj = (HashMap) object;
				int allCount = Integer.parseInt(obj.get("ALLCOUNT").toString());
				int passCount = Integer.parseInt(obj.get("PASSCOUNT").toString());
				String examId = obj.get("EXAM_ID").toString();
				
				ReviewForm t = new ReviewForm();
				t.setExamId(examId);
				t.setNeedReviewCount((allCount-passCount)+"");
				reviewMap.put(examId, t);
			}
		}
		
		
		List<Exam> exams = this.getService().queryData("examInReviewedForParamHql", param , sortMap);
		if(exams!=null && exams.size()>0){
			//for(int i=0;i<exampojolist.size();i++){
				//ExamArrange examArrange = exampojolist.get(i);
				//ExamReviewForm parent = new ExamReviewForm();
				//parent.setExamId(examArrange.getExamArrangeId());
				//parent.setExamName(examArrange.getExamName());
				//parent.setCreationDate(examArrange.getCreationDate());
				//parent.setState(examArrange.getState());
				//examReviewList.add(parent);
				
				//List exams = examArrange.getExams();
				for(int k = 0 ;k<exams.size();k++){
					Exam exam = (Exam)exams.get(k);
					ExamReviewForm examForm = new ExamReviewForm();
					examForm.setPid(exam.getExamArrange().getExamArrangeId());
					examForm.setExamId(exam.getExamId());
					//examForm.setExamName(exam.getExamName());
					if(exam.getExamArrange().getExamName()!=null 
							&& exam.getExamArrange().getExamName().equals(exam.getExamName())){
						examForm.setExamName(exam.getExamName());
					}else{
						examForm.setExamName(exam.getExamArrange().getExamName() +" [ "+exam.getExamName()+" ]");
					}
					examForm.setExamStartTime(exam.getExamStartTime());
					examForm.setExamEndTime(exam.getExamEndTime());
					//examForm.setState(examArrange.getState());
					examForm.setCreationDate(exam.getCreationDate());
					List<ExamMarkpeople> examMarkpeoples = exam.getExamMarkpeoples();
					for (ExamMarkpeople examMarkpeople : examMarkpeoples) {
						if(examMarkpeople.getEmpId().equals(usersess.getEmployeeId())){
							examForm.setAllowReview(true);
							break;
						}
					}
					if(reviewMap.containsKey(exam.getExamId())){
						examForm.setNeedReviewCount(reviewMap.get(exam.getExamId()).getNeedReviewCount());
					}
					//parent.getExamReviewList().add(examForm);
					examReviewList.add(examForm);
				}
			//}
		}
		//examListTotal = examReviewList.size();
		setTotal(this.getService().countData("examInReviewedForParamHql", param));
		return "reviewList";
	}
	private Date getDateByCal(String strDate){
		//System.out.println(strDate);
		Calendar cal = Calendar.getInstance();
		int[] tmp = getTimeArray(strDate);
		cal.set(cal.YEAR, tmp[0]);
		cal.set(cal.MONTH, tmp[1]);
		cal.set(cal.DAY_OF_MONTH, tmp[2]);
		cal.set(cal.HOUR_OF_DAY, tmp[3]);
		cal.set(cal.MINUTE, tmp[4]);
		Date ss = cal.getTime();
		//System.out.println(ss);
		return ss;
	}
	
	private int[] getTimeArray(String strDate){
		int[] resultInt = new int[6]; 
		String[] part = strDate.split(" ");
		String[] part1 = part[0].split("-");
		String[] part2 = part[1].split(":");
		
		resultInt[0]=Integer.parseInt(part1[0]);
		resultInt[1]=Integer.parseInt(part1[1])-1;
		resultInt[2]=Integer.parseInt(part1[2]);
		
		resultInt[3]=Integer.parseInt(part2[0]);
		resultInt[4]=Integer.parseInt(part2[1]);
		
		return resultInt;
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 查询考试开始时间前3天与考后时间3天内的考生信息
	 * @return
	 * @modified
	 */
	public String queryExeExamUserList(){
		examUserList = new ArrayList();
		ExampaperService exampaperService = (ExampaperService)this.getService();
		List<ExamUserTestpaper> list = exampaperService.getExeExamUserList();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)list.get(i);
				if(examUserTestpaper.getExam()!=null){
					ExamUserForm examUserForm = new ExamUserForm();
					examUserForm.setExamId(examUserTestpaper.getExam().getExamId());
					examUserForm.setInticket(examUserTestpaper.getInticket());
					examUserList.add(examUserForm);
				}
			}
		}
		return "examUserList";
	}
	
	
	public String getRequestHttp(){
		requestHttpUrl = ExamVariable.getRequestUrl(request);
		return "requestHttpUrl";
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public int getExamListTotal() {
		return examListTotal;
	}

	public void setExamListTotal(int examListTotal) {
		this.examListTotal = examListTotal;
	}

	public List<Dictionary> getExamKslxList() {
		return examKslxList;
	}

	public void setExamKslxList(List<Dictionary> examKslxList) {
		this.examKslxList = examKslxList;
	}

	public void setExamList(List<ExamForm> examList) {
		this.examList = examList;
	}

	public List<ExamForm> getExamList() {
		return examList;
	}

	public List getExamPublicList() {
		return examPublicList;
	}

	public void setExamPublicList(List examPublicList) {
		this.examPublicList = examPublicList;
	}

	public List<TreeNode> getExamineeTreeList() {
		return examineeTreeList;
	}

	public void setExamineeTreeList(List<TreeNode> examineeTreeList) {
		this.examineeTreeList = examineeTreeList;
	}

	public List<ExamUserForm> getExamineeInfoList() {
		return examineeInfoList;
	}

	public void setExamineeInfoList(List<ExamUserForm> examineeInfoList) {
		this.examineeInfoList = examineeInfoList;
	}

	public int getExamineeInfoListTotal() {
		return examineeInfoListTotal;
	}

	public void setExamineeInfoListTotal(int examineeInfoListTotal) {
		this.examineeInfoListTotal = examineeInfoListTotal;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getIsExist() {
		return isExist;
	}

	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}

	public String getExamNameTerm() {
		return examNameTerm;
	}

	public void setExamNameTerm(String examNameTerm) {
		this.examNameTerm = examNameTerm;
	}

	public String getTestpaperNameTerm() {
		return testpaperNameTerm;
	}

	public void setTestpaperNameTerm(String testpaperNameTerm) {
		this.testpaperNameTerm = testpaperNameTerm;
	}

	public List<ExamReviewForm> getExamReviewList() {
		return examReviewList;
	}

	public void setExamReviewList(List<ExamReviewForm> examReviewList) {
		this.examReviewList = examReviewList;
	}

	public String getExamStartTimeMaxTerm() {
		return examStartTimeMaxTerm;
	}

	public void setExamStartTimeMaxTerm(String examStartTimeMaxTerm) {
		this.examStartTimeMaxTerm = examStartTimeMaxTerm;
	}

	public String getExamStartTimeMinTerm() {
		return examStartTimeMinTerm;
	}

	public void setExamStartTimeMinTerm(String examStartTimeMinTerm) {
		this.examStartTimeMinTerm = examStartTimeMinTerm;
	}

	public String getCreationDateMaxTerm() {
		return creationDateMaxTerm;
	}

	public void setCreationDateMaxTerm(String creationDateMaxTerm) {
		this.creationDateMaxTerm = creationDateMaxTerm;
	}

	public String getCreationDateMinTerm() {
		return creationDateMinTerm;
	}

	public void setCreationDateMinTerm(String creationDateMinTerm) {
		this.creationDateMinTerm = creationDateMinTerm;
	}

	public String getExamTypeIdTerm() {
		return examTypeIdTerm;
	}

	public void setExamTypeIdTerm(String examTypeIdTerm) {
		this.examTypeIdTerm = examTypeIdTerm;
	}

	public String getNameTerm() {
		return nameTerm;
	}

	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}

	public String getExamArrangeName() {
		return examArrangeName;
	}


	public void setExamArrangeName(String examArrangeName) {
		this.examArrangeName = examArrangeName;
	}


	public String getExamArrangeCode() {
		return examArrangeCode;
	}


	public void setExamArrangeCode(String examArrangeCode) {
		this.examArrangeCode = examArrangeCode;
	}


	public String getExamType() {
		return examType;
	}


	public void setExamType(String examType) {
		this.examType = examType;
	}


	public String getRequestHttpUrl() {
		return requestHttpUrl;
	}


	public void setRequestHttpUrl(String requestHttpUrl) {
		this.requestHttpUrl = requestHttpUrl;
	}


	public List getExamUserList() {
		return examUserList;
	}


	public void setExamUserList(List examUserList) {
		this.examUserList = examUserList;
	}


	public String getQid() {
		return qid;
	}


	public void setQid(String qid) {
		this.qid = qid;
	}


	public String getQtype() {
		return qtype;
	}


	public void setQtype(String qtype) {
		this.qtype = qtype;
	}


	public String getQexamId() {
		return qexamId;
	}


	public void setQexamId(String qexamId) {
		this.qexamId = qexamId;
	}


	public int getExamUserTotal() {
		return examUserTotal;
	}


	public void setExamUserTotal(int examUserTotal) {
		this.examUserTotal = examUserTotal;
	}


	public String getQstartDay() {
		return qstartDay;
	}


	public void setQstartDay(String qstartDay) {
		this.qstartDay = qstartDay;
	}


	public String getQendDay() {
		return qendDay;
	}


	public void setQendDay(String qendDay) {
		this.qendDay = qendDay;
	}


	public String getExamPropertyTerm() {
		return examPropertyTerm;
	}


	public void setExamPropertyTerm(String examPropertyTerm) {
		this.examPropertyTerm = examPropertyTerm;
	}


	public List<TreeNode> getChildren() {
		return children;
	}


	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}


	public String getNodeId() {
		return nodeId;
	}


	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}


	public String getNodeType() {
		return nodeType;
	}


	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}


	public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


	public String getScexers() {
		return scexers;
	}


	public void setScexers(String scexers) {
		this.scexers = scexers;
	}

	
}
