package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;

/**
 * 培训在线学习模拟试题
 * @author 朱健
 * @create time: 2015年11月27日 下午2:23:28
 * @version 1.0
 * 
 * @modified records:
 */
public class TrainImplementMoniServiceImpl extends ExampaperMoniServiceImpl implements TrainImplementMoniService{

	
	/**
	 *
	 * @author zhujian
	 * @description 获取培训使用的模拟试卷,没有则创建
	 @param impl_id 培训ID
	 * @param type  培训类型
	 * @param employeeIds 参考考生
	 * @param examStartTime 考试开始时间，如果为空则可随时开始
	 * @param examEndTime 考试结束时间，如果为空则可随时开始
	 * @param createdBy 创建人（为空则显示系统自动创建）
	 * @param isCreate true-为强制创建 false-为没有才创建
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String addOrGetExamPaperByImplId(String impl_id,String type,String employeeId,
			String examStartTime,String examEndTime,String createdBy,boolean isCreate,String choutiType)throws Exception{
		String  examUserTestpaperId = null;
		TrainImplement trainImplement =  (TrainImplement)this.findDataByKey(impl_id, TrainImplement.class);
		if(trainImplement!=null){
			if(isCreate){
				String[] ids = this.addOrgetTrainImplementExamPaperByImplId(impl_id, type, new String[]{employeeId}, examStartTime, examEndTime, createdBy,choutiType);
				if(ids!=null){
					examUserTestpaperId = ids[0];
				}
			}else{
				ExamUserTestpaper examUserTestpaper = null;
				Map term = new HashMap();
				term.put("relationId", impl_id);
				term.put("relationType", type);
				term.put("employeeId", employeeId);
				List<ExamUserTestpaper> list = this.queryData("queryExamUserTestpaperInRelationHql", term, null);
				if(list!=null && list.size()>0){
					examUserTestpaper = list.get(0);
					examUserTestpaperId = examUserTestpaper.getUserTestpaperId();
				}else{
					String[] ids = this.addOrgetTrainImplementExamPaperByImplId(impl_id, type, new String[]{employeeId}, examStartTime, examEndTime, createdBy,choutiType);
					if(ids!=null){
						examUserTestpaperId = ids[0];
					}
				}
			}
		}
		return examUserTestpaperId;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 培训自动产生并保存模拟试卷
	 * @param impl_id 培训ID
	 * @param type  培训类型
	 * @param employeeIds 参考考生
	 * @param examStartTime 考试开始时间，如果为空则可随时开始
	 * @param examEndTime 考试结束时间，如果为空则可随时开始
	 * @param createdBy 创建人（为空则显示系统自动创建）
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String[] addOrgetTrainImplementExamPaperByImplId(String impl_id,String type,String[] employeeIds,
			String examStartTime,String examEndTime,String createdBy,String choutiType)throws Exception{
		String[] userTestpaperId = null;	
		TrainImplement trainImplement =  (TrainImplement)this.findDataByKey(impl_id, TrainImplement.class);
		if(trainImplement!=null){
			if(createdBy==null || "".equals(createdBy))createdBy = "系统自动创建";//创建人
			userTestpaperId = new String[employeeIds.length];
			Employee employee = (Employee)this.findDataByKey(employeeIds[0], Employee.class);
			String organId = employee.getDept().getOrgan().getOrganId();
		    String organName = employee.getDept().getOrgan().getOrganAlias();
		    String testpaperType = "20";////试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
		    String examTypeId = null;//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
		    int examProperty = 30;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
		    //产生试题方式 //{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
		    String initParam = ExamVariable.getTrainImplementExamPaperInitThemeType();//Config.getPropertyValue("trainImplement_ExamPaper_Init_ThemeType");//"{0001,null,10,5,0},{0002,null,10,5,0},";
		    Iterator<ThemeBank> its = trainImplement.getThemeBanks().iterator();
		    String themeBankIds = null;
		    while(its.hasNext()){
		    	if(themeBankIds==null){ themeBankIds = ""; }
		    	ThemeBank themeBank = (ThemeBank)its.next();
		    	themeBankIds+=themeBank.getThemeBankId()+",";
		    }
		    if(themeBankIds!=null){
		    	themeBankIds = themeBankIds.substring(0,themeBankIds.length()-1);
		    }
		    Testpaper testpaper = saveOrGetMoniTestpaper(trainImplement.getSubject()+"模拟试题",impl_id, type,themeBankIds, 
		    		organName, organId, initParam,createdBy,testpaperType, examProperty, examTypeId,choutiType,employee.getEmployeeId());
		    
		    ExamArrange examArrange =  saveOrGetMoniExam(testpaper, trainImplement.getSubject()+"模拟试题", impl_id, type, organName, 
		    		organId,examStartTime,examEndTime,createdBy,testpaperType, examProperty, examTypeId);
		    Exam exam = examArrange.getExams().get(0);
		    exam.setTestpaper(testpaper);
		    ExampaperService exampaperService = (ExampaperService)SpringContextUtil.getBean("exampaperService");
		    Map<String,SysUser> sysUserMap = exampaperService.getSysUserMap();
		    for(int i=0;i<employeeIds.length;i++){
		    	employee = (Employee)this.findDataByKey(employeeIds[i], Employee.class);
			    ExamUserTestpaper examUserTestpaper = saveOrGetMoniUserExamTestpaper(exam, testpaper, 
			    		trainImplement.getSubject()+"模拟试题", impl_id, type, organName, 
			    		organId, employee,createdBy,testpaperType, examProperty, examTypeId,StaticVariable.defined_password,choutiType,sysUserMap);
			    userTestpaperId[i] = examUserTestpaper.getUserTestpaperId();
		    }
		}
		return userTestpaperId;
	}
}
