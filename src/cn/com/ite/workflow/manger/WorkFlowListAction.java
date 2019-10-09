package cn.com.ite.workflow.manger;

import java.util.*;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.workflow.domain.WorkFlow;

/**
 * <p>Title cn.com.ite.workflow.manger.WorkFlowListAction</p>
 * <p>Description 工作流ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public class WorkFlowListAction extends AbstractListAction{
	private static final long serialVersionUID = -2652717947349804498L;
	/**
	 * 名称条件
	 */
	private String nameTerm;
	/**
	 * 编码条件
	 */
	private String codeTerm;
	private boolean isNew=true;
	/**
	 * 结果数据，
	 */
	private List<WorkFlow> list;
	@SuppressWarnings("unchecked")
	public String list() throws Exception{
		list = (List<WorkFlow>)service.queryData("queryHql", this,this.getSortMap(),this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		for(String id:this.getId().split(",")){
			((WorkFlowService)service).deleteFlow(id);
		}
		this.setMsg("工作流删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public String getCodeTerm() {
		return codeTerm;
	}
	public void setCodeTerm(String codeTerm) {
		this.codeTerm = codeTerm;
	}
	public List<WorkFlow> getList() {
		return list;
	}
	public void setList(List<WorkFlow> list) {
		this.list = list;
	}
	public boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}
}