package cn.com.ite.hnjtamis.exam.study;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyUserAnswerkey;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.study.StudyThemeServiceImpl</p>
 * <p>Description 在线学习试题</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年9月1日 下午2:21:57
 * @version 1.0
 * 
 * @modified records:
 */
public class StudyThemeServiceImpl extends DefaultServiceImpl implements StudyThemeService {
	
	/**
	 * 查询试卷的试题数、总分、考生正确题数、正确得分
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public Object[] getStudyTestpaperThemeCount(String studyTestpaperId,String employeeId){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		return studyThemeDao.getStudyTestpaperThemeCount(studyTestpaperId,employeeId);
	}

	
	/**
	 * 查询试卷的试题
	 * @description
	 * @param studyTestpaperId
	 * @return
	 * @modified
	 */
	public List<StudyTestpaperTheme> getStudyTestpaperThemeList(String studyTestpaperId,Integer startIndex,Integer endIndex){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		return studyThemeDao.getStudyTestpaperThemeList(studyTestpaperId, startIndex, endIndex);
	}
	
	/**
	 * 添加数据库中没有题库的试卷
	 * @description
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @modified
	 */
	public void addStudyTestpaper(String relationType,
			String employeeId,String employeeName){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		studyThemeDao.addStudyTestpaper(relationType, employeeId, employeeName);
	}
	
	
	/**
	 * 获取或添加学习试卷
	 * @description
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @param examTitle
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String addOrGetExamStudyTestpaper(String relationId,String relationType,String employeeId,
			String employeeName,String examTitle)throws Exception{
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		if(examTitle == null || "".equals(examTitle) || "null".equals(examTitle)){
			examTitle = "岗位培训随堂练习("+(DateUtils.convertDateToStr(new Date(),"yyyyMMdd"))+")";
		}
		String nowTime = DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
		//---判断试卷是否存在
		Map term = new HashMap();
		term.put("relationId", relationId);
		term.put("relationType", relationType);
		List<StudyTestpaper> studyTestpaperlist = this.queryData("queryStudyTestpaperInRelationHql", term, new HashMap());
		StudyTestpaper studyTestpaper = null;
		if(studyTestpaperlist==null || studyTestpaperlist.size()==0){
			studyTestpaper = this.addStudyTestpaper(relationId, relationType, employeeId, employeeName, examTitle);
		}else{
			studyTestpaper = studyTestpaperlist.get(0);
		}
		return studyTestpaper.getStudyTestpaperId();
	}
	
	
	private StudyTestpaper addStudyTestpaper(String relationId,String relationType,String employeeId,
			String employeeName,String examTitle){
		StudyTestpaper studyTestpaper = new StudyTestpaper();
		studyTestpaper.setStudyTestpaperName(examTitle);//
		studyTestpaper.setTotalTheme(0);//总题数
		studyTestpaper.setTotalScore(0.0d);//总分数
		studyTestpaper.setIsUse(10);//是否使用(5：否,10：是)
		//studyTestpaper.setRemark;//备注
		studyTestpaper.setState("5");//状态 5:生成
		//studyTestpaper.setOrganName();//机构名
		//studyTestpaper.setOrganId;//机构ID
		//studyTestpaper.setSyncFlag;//同步标志
		//studyTestpaper.setLastUpdateDate;//最后修改时间
		//studyTestpaper.setLastUpdatedBy;//最后修改人
		//studyTestpaper.setLastUpdatedIdBy;//最后修改人ID
		studyTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));//创建时间
		studyTestpaper.setCreatedBy(employeeName);//创建人
		studyTestpaper.setCreatedIdBy(employeeId);//创建人ID
		studyTestpaper.setRelationId(relationId);//关联ID（如：练习安排等）
		studyTestpaper.setRelationType(relationType);//关联类型
		this.getDao().addEntity(studyTestpaper);
		return studyTestpaper;
	}
	
	/**
	 * 试卷添加试题
	 * @description
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @throws Exception
	 * @modified
	 */
	public void addThemeInStudyTestpaper(String study_testpaper_id,String relationId,String relationType,
			String employeeId,String employeeName)throws Exception{
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		studyThemeDao.addThemeInStudyTestpaper(study_testpaper_id, relationId, relationType,
				 employeeId, employeeName);
	}
	
	/**
	 * 根据试题Id获取答案
	 * @description
	 * @param themeIds
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getAnswerkeyByStThemeIds(String studyTestpaperThemeIds){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		return studyThemeDao.getAnswerkeyByStThemeIds(studyTestpaperThemeIds);
	}

	/**
	 * 查询考生答案
	 * @description
	 * @param studyTestpaperId
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public List<StudyUserAnswerkey> getUserAnswerkeyList(String studyTestpaperId,String employeeId){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		return studyThemeDao.getUserAnswerkeyList(studyTestpaperId,employeeId);
	}
	
	/**
	 * 获取考生的试题显示答案
	 * @author 朱健
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getStudyTestpaperAnswerkeyByTestpaperId(String studyTestpaperId){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		Map term = new HashMap();
		term.put("studyTestpaperId", studyTestpaperId);
		List<StudyTestpaperAnswerkey> themeAnsList = studyThemeDao.getStudyTestpaperAnswerkeyList(studyTestpaperId);
				//this.queryData("queryStudyTestpaperAnswerkeyByStudyTestpaperIdHql", 
				//term, new HashMap());
		
		Map<String,List<StudyTestpaperAnswerkey>> themeAnsMap = new HashMap<String,List<StudyTestpaperAnswerkey>>();
		for(int i=0;i<themeAnsList.size();i++){
			StudyTestpaperAnswerkey studyTestpaperAnswerkey = (StudyTestpaperAnswerkey)themeAnsList.get(i);
			String studyTestpaperThemeId = studyTestpaperAnswerkey.getStudyTestpaperTheme().getStudyTestpaperThemeId();
			List tmpList = (List)themeAnsMap.get(studyTestpaperThemeId);
			if(tmpList == null)tmpList = new ArrayList();
			tmpList.add(studyTestpaperAnswerkey);
			themeAnsMap.put(studyTestpaperThemeId, tmpList);
		}
		return themeAnsMap;
	}
	
	/**
	 * 获取考生的试题显示答案
	 * @description
	 * @param studyTestpaperThemelist
	 * @return
	 * @modified
	 */
	public Map<String,List<StudyTestpaperAnswerkey>> getStudyTestpaperAnswerkeyByThemelist(List<StudyTestpaperTheme> studyTestpaperThemelist){
		Map<String,List<StudyTestpaperAnswerkey>> ansMap = new HashMap<String,List<StudyTestpaperAnswerkey>>();
		String studyTestpaperThemeIds ="";
		for(int j = 0;j<studyTestpaperThemelist.size();j++){
			StudyTestpaperTheme theme = (StudyTestpaperTheme)studyTestpaperThemelist.get(j);
			studyTestpaperThemeIds += "'"+theme.getStudyTestpaperThemeId()+"',";
			
			if(j>0 && (j%100==0 || j==studyTestpaperThemelist.size()-1)){
				studyTestpaperThemeIds = studyTestpaperThemeIds.substring(0,studyTestpaperThemeIds.length()-1);
				Map<String,List<StudyTestpaperAnswerkey>> _ansMap = this.getAnswerkeyByStThemeIds(studyTestpaperThemeIds);
				ansMap.putAll(_ansMap);
				
				studyTestpaperThemeIds = "";
			}
		}
		return ansMap;
	}
	
	/**
	 * 更新到答案历史表，清理答案表的内容
	 * @description
	 * @param studyTestpaperId
	 * @param employeeId
	 * @modified
	 */
	public void updateAnsHis(String studyTestpaperId,String employeeId){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		studyThemeDao.updateAnsHis(studyTestpaperId, employeeId);
	}
	
	/**
	 * 获取原试题的人员信息
	 * @description
	 * @return
	 * @modified
	 */
	public List<String> getEmployeeIdInExamUserTestpaper(String relation_type){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		return studyThemeDao.getEmployeeIdInExamUserTestpaper(relation_type);
	}
	
	
	/**
	 * 根据旧的数据
	 * @description
	 * @modified
	 */
	public void updateUserAnsInOld(String employeeId,String relation_type){
		StudyThemeDao studyThemeDao = (StudyThemeDao)this.getDao();
		studyThemeDao.updateUserAnsInOld(employeeId, relation_type);
	}
}
