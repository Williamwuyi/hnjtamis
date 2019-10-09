package cn.com.ite.hnjtamis.exam.exampaper;

/**
 * 培训在线学习模拟试题
 * @author 朱健
 * @create time: 2015年11月27日 下午2:23:28
 * @version 1.0
 * 
 * @modified records:
 */
public interface TrainImplementMoniService extends ExampaperMoniService{

	
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
			String examStartTime,String examEndTime,String createdBy,String choutiType)throws Exception;
	
	
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
			String examStartTime,String examEndTime,String createdBy,boolean isCreate,String choutiType)throws Exception;
}
