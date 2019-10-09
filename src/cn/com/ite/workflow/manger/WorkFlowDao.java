package cn.com.ite.workflow.manger;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.workflow.domain.WorkFlowExcute;

/**
 * <p>Title cn.com.ite.workflow.domain.WorkFlowExcute</p>
 * <p>Description 流程DAO</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午01:55:37
 * @version 2.0
 * 
 * @modified records:
 */
public interface WorkFlowDao extends DefaultDAO{
	/**
	 * 查询撤消任务
	 * @param flowCode
	 * @param param
	 * @param stateCode
	 * @return
	 */
	List<WorkFlowExcute> findUndoTask(String flowCode, Map<String, String> param,
			String stateCode);
	/**
	 * 查询指定业务参数的结点的执行导向
	 * @param nodeId
	 * @param param
	 * @return
	 */
	List<WorkFlowExcute> findExcuteTask(String nodeId,String flowInstanceId);
	/**
	 * 查询执行任务
	 * @param flowCode 流程编码
	 * @param employeeId 员工ID
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param taskCode 任务编码
	 * @param param 业务参数
	 * @param complet 是否完成
	 * @param undo 是否撤消
	 * @return
	 */
	List<WorkFlowExcute> findExcuteTask(String flowCode,
			String employeeId, Date startDate, Date endDate, String taskCode,
			Map<String, String> param, Boolean complet,Boolean undo,int startNo,int rowSize);
	/**
	 * 删除流程数据
	 * @param flowCode
	 * @param param
	 * @modified
	 */
	void deleteFlowData(String flowCode,Map<String, String> param);
	/**
	 * 查询任务提醒
	 * @param employeeId 创建者
	 * @return
	 * @modified
	 */
	List<WorkFlowExcute> findTaskTip(String employeeId);
}
