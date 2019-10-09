package cn.com.ite.workflow.manger;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.workflow.domain.WorkFlow;
import cn.com.ite.workflow.domain.WorkFlowExcute;

/**
 * <p>Title cn.com.ite.eap2.module.funres.module.ModuleListAction</p>
 * <p>Description 工作流的服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public interface WorkFlowService extends DefaultService{
	/**
	 * 保存流程
	 * @param flow
	 * @throws Exception
	 */
	void saveFlow(WorkFlow flow) throws Exception;
	/**
	 * 流程复制
	 * @param flowId 源流程
	 * @param update 是否修改产生的
	 * @return 新流程
	 * @throws Exception
	 */
	WorkFlow saveCopy(String flowId,boolean update) throws Exception;
	/**
	 * 删除流程
	 * @param flowId
	 * @throws Exception
	 */
	void deleteFlow(String flowId) throws Exception;
	/**
	 * 查询任务提醒
	 * @param employeeId 创建者
	 * @return
	 * @modified
	 */
	List<WorkFlowExcute> findTaskTip(String employeeId);
	/**
	 * 清除提醒
	 * @param excuteId
	 * @modified
	 */
	void canelTip(String excuteId) throws Exception;
}
