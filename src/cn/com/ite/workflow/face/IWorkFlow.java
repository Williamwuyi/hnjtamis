package cn.com.ite.workflow.face;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.ite.workflow.domain.WorkFlowExcute;
import cn.com.ite.workflow.manger.FlowInfo;

/**
 * <p>Title cn.com.ite.workflow.face.IWorkFlow</p>
 * <p>Description 工作流接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午02:46:19
 * @version 2.0
 * 
 * @modified records:
 */
public interface IWorkFlow {
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
	String[] startWorkFlow(String workFlowCode,String employeId,Map<String,String> param,String parentFLowInstanceId) throws Exception;
	/**
	 * 获取工作流映射
	 * @return
	 */
	Map<String,String> getFlowMap();
	/**
	 * 获得工作流的功能映射（要判断权限），包括历史版本的
	 * @param workFlowCode 工作流编码
	 * @param employeeId 员工ID，只取此人有权限的
	 * @return 结点编码与结点名称的映射
	 * @modified
	 */
	Map<String,String> getTaskMap(String workFlowCode,String employeeId);
	/**
	 * 获得操作（状态）映射，包括历史版本的
	 * @param workFlowCode 工作流编码
	 * @return 状态映射
	 * @modified
	 */
	Map<String,String> getStateMapByFlowCode(String workFlowCode);
	/**
	 * 根据执行ID查询状态（操作或导向)映射,用于业务操作中的按钮生成
	 * @param excuteId
	 * @return
	 */
	Map<String,String> getStateMapByExcuteId(String excuteId);
	/**
	 * 判断开始状态
	 * @param workFlowCode 工作流编码
	 * @param state 状态
	 * @param param 业务参数
	 * @return
	 */
	boolean judgeStartState(String flowCode,String state,Map<String,String> param)throws Exception;
	/**
	 * 判断结束状态
	 * @param workFlowCode 工作流编码
	 * @param state 状态
	 * @param param 业务参数
	 * @return
	 */
	boolean judgeEndState(String flowCode,String state,Map<String,String> param)throws Exception;
	/**
	 * 获取任务的打开地址，里面包括结点执行ID（excuteId）等等其它业务参数，用于业务功能中的流程打开操作
	 * @param nodeCode
	 * @param param
	 * @return
	 * @throws Exception
	 */
	String getTaskUrl(String nodeCode,Map<String,String> param,String employeeId) throws Exception;
	/**
	 * 获取执行任务的打开地址
	 * @param excuteId
	 * @return
	 */
	String getTaskUrl(String excuteId)throws Exception;
	/**
	 * 查询执行任务
	 * @param flowCode 流程编码
	 * @param employeeId 员工ID
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param taskName 任务名称
	 * @param param 业务参数
	 * @param complet 是否完成
	 * @param undo 是否需要打开路径
	 * @return
	 */
	List<WorkFlowExcute> findExcuteTask(String flowCode,String employeeId,Date startDate,
			Date endDate,String taskCode,Map<String,String> param,Boolean complet,Boolean undo,int startNo,int rowSize);
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
	String[] next(String excuteId,String toCode,Map<String,String> param,String employeeId,String advice,String ip,boolean ifCopyBeforeParam) throws Exception;
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
		   String employeeId,String advice,String ip,boolean ifCopyBeforeParam) throws Exception;
	/**
	 * 结点流转,带执行参数
	 * @param flowCode 流程编码
	 * @param toName 导向（操作）名称
	 * @param param 业务参数或执行参数
	 * @param term 任务查询的业务条件
	 * @param employeeId 员工ID
	 * @param advice 意见
	 * @param ip 操作IP地址
	 * @param ifCopyBeforeParam 是否复制前一结点的业务参数，如果与param参数冲突，则保存param参数
	 * @return 状态编码和名称，奇数位存储状态编码，偶数位存储状态名称
	 * @throws Exception
	 */
	public String[] nextEx(String flowCode,String toName,Map<String,String> param,Map<String,String> term,
		    String employeeId,String advice,String ip,boolean ifCopyBeforeParam) throws Exception;
	/**
	 * 根据业务参数查询审核信息
	 * @param flowCode 流程编码
	 * @param param 业务参数
	 * @return
	 */
	List<AuditInfo> getAuditInfo(String flowCode,Map<String,String> param);
	/**
	 * 根据流程实例查询审核信息
	 * @param flowInstanceId
	 * @return
	 * @modified
	 */
	List<AuditInfo> getAuditInfo(String flowInstanceId);
	/**
	 * 撤消操作
	 * @param flowCode 工作流编码
	 * @param param 业务参数
	 * @param stateCode 状态编码
	 * @param employeeId 员工ID
	 * return 新编码及新状态
	 * @throws Exception
	 */
	String[] undo(String flowCode,Map<String,String> param,String stateCode,String employeeId) throws Exception;
	/**
	 * 撤消操作
	 * @param excuteId
	 * @param employeeId
	 * return 新编码及新状态
	 * @return
	 */
	String[] undo(String excuteId,String employeeId)throws Exception;
	/**
	 * 接收任务
	 * @param excuteId 任务执行ID
	 * @param employeeId 员工ID
	 * @modified
	 */
	void receiveTask(String excuteId,String employeeId)throws Exception;
	/**
	 * 删除流程数据
	 * @param flowCode 流程编码
	 * @param param 业务参数
	 * @modified
	 */
	void deleteFLowData(String flowCode,Map<String,String> param) throws Exception;
	/**
	 * 查询流程监控信息
	 * @param manId
	 * @return
	 * @modified
	 */
	List<FlowInfo> findFlowInfo(Map term,int startNo,int rowSize);
}
