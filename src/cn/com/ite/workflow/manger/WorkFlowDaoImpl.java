package cn.com.ite.workflow.manger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.workflow.domain.WorkFlowExcute;

public class WorkFlowDaoImpl extends HibernateDefaultDAOImpl implements WorkFlowDao{
	/**
	 * 查询任务提醒,针对创建者的最后一个任务
	 * @param employeeId 创建者
	 * @return
	 * @modified
	 */
	public List<WorkFlowExcute> findTaskTip(String employeeId){
		String sql = "select distinct a.* from work_flow_excute a,work_flow_node b"+
          " where a.node_id=b.node_id and a.to_id is not null and a.employee_id<>'"+
              employeeId+"' and b.type='TASK' and b.url is not null";
		sql += " and not exists (select b.excute_id from work_flow_excute_param b " +
				"where b.excute_id=a.excute_id and b.param_key='notice')";
		sql += " and exists (select b.excute_id from work_flow_excute b " +
		"where b.bus_id=a.bus_id and b.order_No=1 and b.employee_Id='"+employeeId+"')";
		sql += " and a.EXCUTE_TIIME=(select max(x.EXCUTE_TIIME) from work_flow_excute x " +//最新任务
		"where x.bus_id=a.bus_id)";
		sql+=" order by a.EXCUTE_TIIME desc";
		return this.querySql(sql, null, null, WorkFlowExcute.class);
	}
	/**
	 * 查询要撤消任务
	 * @param flowCode 流程编码
	 * @param param 参数
	 * @param stateCode 状态
	 * @return
	 */
	public List<WorkFlowExcute> findUndoTask(String flowCode, Map<String, String> param,
			String stateCode){
		String sql = "select distinct a.* from work_flow_excute a,"+
                     "work_flow c,work_flow_node d,work_flow_to e "+
                       " where a.node_id=d.node_id "+
                          "and d.work_id=c.flow_id and c.code=:flowCode "+
                          "and a.to_id=e.to_id and e.code=:toCode and a.if_Undo=0";
		for(String key:param.keySet()){
			sql += " and exists (select b.excute_id from work_flow_excute_param b " +
					"where b.excute_id=a.excute_id and b.param_key='"+key+"' and b.param_value='"+param.get(key)+"')";
		}
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("flowCode", flowCode);
		term.put("toCode", stateCode);
		return this.querySql(sql, term, null, WorkFlowExcute.class);
	}
	
	/**
	 * 查询指定业务参数的结点的执行导向
	 * @param nodeId
	 * @param param
	 * @return
	 */
	public List<WorkFlowExcute> findExcuteTask(String nodeId,String flowInstanceId){
		String sql = "select distinct a.* from work_flow_excute a"+
		             " where a.node_id='"+nodeId+"' and a.bus_id='"+flowInstanceId+"'";
		return this.querySql(sql, null, null, WorkFlowExcute.class);
	}
	
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
	public List<WorkFlowExcute> findExcuteTask(String flowCode,
			String employeeId, Date startDate, Date endDate, String taskCode,
			Map<String, String> param, Boolean complet,Boolean undo,int startNo,int rowSize){
		String sql = "select k.* from (select distinct a.* from work_flow_excute a,"+
        "work_flow c,work_flow_node d where a.node_id=d.node_id "+
             "and d.work_id=c.flow_id and (c.code=:flowCode or :flowCode is null) "+
             "and (a.employee_id=:employeeId or a.employee_id is null or :employeeId is null) "+
             "and (a.quarter_Id=:quarterId or a.quarter_Id is null or :quarterId is null)"+
             "and (a.create_Time>=:startDate or :startDate is null) "+
             "and (a.create_Time<=:endDate or :endDate is null) "+
             "and (d.code=:taskCode or :taskCode is null) and d.type='TASK' ";
		if(complet!=null)
			if(complet.booleanValue())
				sql += " and a.to_id is not null ";
			else
				sql += " and a.to_id is null ";
		if(undo!=null&&undo.booleanValue())
			sql += " and d.url is not null";
		if(param!=null)
		for(String key:param.keySet()){
			sql += " and exists (select b.excute_id from work_flow_excute_param b " +
			"where b.excute_id=a.excute_id and b.param_key='"+key+"' and b.param_value='"+param.get(key)+"')";
		}
		sql += ") k order by k.create_Time desc";
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("flowCode", flowCode);
		term.put("startDate", startDate);
		term.put("endDate", endDate);
		String quarterId = null;
		if(!StringUtils.isEmpty(employeeId)){
			Employee employee = (Employee)this.findEntityBykey(Employee.class,employeeId);
			if(employee!=null&&employee.getQuarter()!=null)
				quarterId = employee.getQuarter().getQuarterId();
		}
		term.put("quarterId", quarterId);
		term.put("employeeId", employeeId);
		term.put("taskCode", taskCode);
		return this.querySql(sql, term, null, WorkFlowExcute.class,startNo,rowSize);
	}
	/**
	 * 删除流程数据
	 * @param flowCode
	 * @param param
	 * @modified
	 */
	public void deleteFlowData(String flowCode,Map<String, String> param){
		String sql = "select distinct a.* from work_flow_excute a,"+
        "work_flow c,work_flow_node d where a.node_id=d.node_id "+
             "and d.work_id=c.flow_id and c.code=:flowCode ";
		if(param!=null)
		for(String key:param.keySet()){
			sql += " and exists (select b.excute_id from work_flow_excute_param b " +
			"where b.excute_id=a.excute_id and b.param_key='"+key+"' and b.param_value='"+param.get(key)+"')";
		}
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("flowCode", flowCode);
		this.deleteBatchEntity(this.querySql(sql, term, null, WorkFlowExcute.class));
	}
}
