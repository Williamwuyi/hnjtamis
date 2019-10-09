package cn.com.ite.eap2.module.organization.employeeBatchModify;

import java.util.*;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.organization.EmployeeModifyBatch;

/**
 *
 * <p>Title cn.com.ite.eap2.module.organization.employeeBatchModify.EmplyeeBatchModifyListAction</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2018</p>
 * @create time: 2018年12月25日 上午9:34:49
 * @version 1.0
 * 
 * @modified records:
 */
public class EmplyeeBatchModifyListAction extends AbstractListAction{

	private static final long serialVersionUID = 6651475487054750273L;
	
	/**
	 * 查询结果
	 */
	private List<EmployeeModifyBatch> list;
	
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		
		return "list";
	}
	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), EmployeeModifyBatch.class);
		this.setMsg("删除成功！");
		return "delete";
	}

	public List<EmployeeModifyBatch> getList() {
		return list;
	}

	public void setList(List<EmployeeModifyBatch> list) {
		this.list = list;
	}
	
	
	
	
}
