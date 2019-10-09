package cn.com.ite.workflow.manger;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.workflow.domain.WorkFlow;
import cn.com.ite.workflow.domain.WorkFlowNode;
import cn.com.ite.workflow.domain.WorkFlowTo;

/**
 * <p>Title cn.com.ite.workflow.manger.WorkFlowFormAction</p>
 * <p>Description 工作流FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:14:34
 * @version 2.0
 * 
 * @modified records:
 */
public class WorkFlowFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 5927931667526549415L;
	/**
	 * 表单数据
	 */
	private WorkFlow form;
	
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String find() throws Exception{
		form = (WorkFlow)service.findDataByKey(this.getId(), WorkFlow.class);
		Map<String, Object> term = new HashMap<String, Object>();
		term.put("flowId", form.getFlowId());
		form.setTos((List<WorkFlowTo>)service.queryData("toHql", term, null));
		for(WorkFlowTo to:form.getTos()){
			to.setSrcNodeId(to.getSrcNode().getCode());
			to.setToNodeId(to.getToNode().getCode());
		}
		return "find";
	}
	/**
	 * 复制操作
	 * @return
	 * @throws Exception
	 */
	public String copy() throws Exception{
		((WorkFlowService)service).saveCopy(this.getId(),false);
		return "save";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save() throws Exception{
		try{
		form = (WorkFlow)this.jsonToObject(WorkFlow.class);
		//判断是否被引用
		Map<String, Object> term = new HashMap<String, Object>();
		term.put("flowId", form.getFlowId());
		long c = (Long)service.queryData("useHql", term, null).get(0);
		if(c>0){
			WorkFlow old = (WorkFlow)service.findDataByKey(form.getFlowId(), WorkFlow.class);
			if(!old.getCode().equals(form.getCode()))
				throw new Exception("此流程已经使用，不能修改编码!");
			form.setFlowId(null);
			for(WorkFlowNode node:form.getWorkFlowNodes())
				node.setNodeId(null);			
			for(WorkFlowTo to:form.getTos())
				to.setToId(null);
			form.setVersion(service.getFieldMax(WorkFlow.class, "version", "code", form.getCode())+1);
		}	
		((WorkFlowService)service).saveFlow(form);
		if(c>0)
			this.setMsg("流程已经被使用，修改已产生新版本流程！");
		else
			this.setMsg("流程保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "save";
	}
	public WorkFlow getForm() {
		return form;
	}
	public void setForm(WorkFlow form) {
		this.form = form;
	}
}