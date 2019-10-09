package cn.com.ite.hnjtamis.exam.exampaper;


/**
 * 考生模拟试题
 * @author 朱健
 * @create time: 2015年11月27日 下午2:23:16
 * @version 1.0
 * 
 * @modified records:
 */
public interface EmployeeMoniService extends ExampaperMoniService{

	
	/**
	 * 首页生成模拟试题
	 * @author 朱健
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @param cleanUserAns
	 * @param examTitle
	 * @param choutiParam
	 * @param addExamType
	 * @param themeBankId
	 * @return 首页生成模拟试题ID
	 * @throws Exception
	 * @modified
	 */
	public String addOrGetMoniIndexExam(String relationId,String relationType,String employeeId,String employeeName,
			String cleanUserAns,String examTitle,String choutiParam,String addExamType,String themeBankId)throws Exception;
}
