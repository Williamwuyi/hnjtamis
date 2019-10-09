package cn.com.ite.hnjtamis.exam.exampaper;


/**
 * 在线学习模拟试题
 * @author 朱健
 * @create time: 2015年11月27日 下午2:23:28
 * @version 1.0
 * 
 * @modified records:
 */
public interface OnlineMoniService extends ExampaperMoniService{

	/**
	 *
	 * @author zhujian
	 * @description 根据提供给其它接口的userTestpaperId，添加一份新考卷（试题保持不变）
	 * @param userTestpaperId  提供给其它接口的userTestpaperId
	 * @param  examPaperType 试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @modified
	 */
	public void addExamPaperByUserTestpaperId(String userTestpaperId,int examPaperType,String choutiType);
	
	/**
	 *
	 * @author zhujian
	 * @description 根据关联ID与关联类型，人员ID 添加一份新考卷（试题保持不变）
	 * @param relationId 关联ID
	 * @param relationType 关联类型
	 * @param employeeId 人员ID
	 * @param  examPaperType 试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @modified
	 */
	//public void addExamPaperInUse(String relationId,String relationType,String employeeId,int examPaperType,String choutiType);
	
	
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取模拟试卷,没有则创建
	 * @param examTitle
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param themeBankIdArr
	 * @param examStartTime
	 * @param examEndTime
	 * @param createdBy
	 * @param isCreate
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @return
	 * @throws Exception
	 * @modified
	 */
	//public String addOrGetExamPaperByUserMoni(String examTitle,String relationId,String relationType,String employeeId,
			//String[] themeBankIdArr, String examStartTime,String examEndTime,
			//String createdBy,boolean isCreate,String choutiParam,int examProperty,String choutiType)throws Exception;
	
	/**
	 *
	 * @author zhujian
	 * @description 根据关联ID与关联类型，人员ID清除该考生根据时间排序，最后一份考试的考生答案
	 * @param relationId 关联ID
	 * @param relationType 关联类型
	 * @param employeeId 人员ID
	 * @modified
	 */
	public void saveAndCleanExamPaperInUse(String relationId,String relationType,String employeeId);

	/**
	 *
	 * @author zhujian
	 * @description 产生自测试题
	 * @param examName 试卷名称
	 * @param testpaperId 试卷模版ID
	 * @param examProperty 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param relationId 培训ID
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
	public String[] addExamPaperByRelationId(String examName,String testpaperId,int examProperty,String relationId,String type,String[] employeeIds,
			String examStartTime,String examEndTime,String createdBy,String choutiType)throws Exception;
	
}
