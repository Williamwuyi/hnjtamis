package cn.com.ite.eap2.module.organization.employee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.organization.dept.DeptService;
import cn.com.ite.eap2.module.organization.organ.OrganService;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganServiceImpl</p>
 * <p>Description 机构部门服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public class EmployeeServiceImpl extends DefaultServiceImpl implements EmployeeService {
	private DeptService deptService;
	private OrganService organService;
	
	/**
	 * 根据deptId包括的子部门
	 * @author 朱健
	 * @param deptId
	 * @return
	 * @modified
	 */
	public String queryOwnerDeptIds(String deptId){
		EmployeeDao employeeDao = (EmployeeDao)this.getDao();
		return employeeDao.queryOwnerDeptIds(deptId);
	}
	/**
	 * 员工保存，同时增加保存用户 
	 * @param bo
	 * @throws EapException
	 * @modified
	 */
	public void save(Serializable bo) throws EapException{
		super.save(bo);
		/*Employee employee = (Employee)bo;
		List users = getDao().findEntityByField(SysUser.class, "account", employee.getEmployeeCode());
		if(users.size()==0){
			SysUser user = new SysUser();
			user.setAccount(employee.getEmployeeCode());
			user.setOrgan(employee.getDept().getOrgan());
			user.setEmployee(employee);
			user.setPassword(CharsetSwitchUtil.encryptPassword("888888"));
			user.setValidation(true);
			this.saveOld(user);
		}*/
	}
	
	/**
	 * 查询机构部门员工树
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @return
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findOrganDeptEmployeeTree(String organId,String employeeName)throws Exception{
		EmployeeDao employeeDao = (EmployeeDao)this.getDao();
		return employeeDao.findOrganDeptEmployeeTree(organId, employeeName);
		
		/*Map allDeptMap = new HashMap();
		List<Dept> allDeptList = this.queryAllDate(Dept.class);
		if(allDeptList!=null){
			for(Dept dept : allDeptList){
				allDeptMap.put(dept.getDeptId(), dept);
			}
		}
 
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("nameTerm", employeeName);
		List<Employee> list = (List<Employee>)getDao().queryConfigQl("queryHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new HashMap<String,TreeNode>();
		for(Employee employee:list){
			TreeNode f = new TreeNode();
			f.setType("employee");
			f.setId(employee.getEmployeeId());
			f.setTitle(employee.getEmployeeName());
			Dept parent = (Dept)allDeptMap.get(employee.getDept().getDeptId());//employee.getDept();
			f.setParentId(parent.getDeptId());
			ms.put(f.getId(), f);
		}
		List<TreeNode> trees = new ArrayList<TreeNode>();
		trees.addAll(ms.values());
		trees.addAll(deptService.findOrganDeptTree(organId, null));
		TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("dept", "resources/icons/fam/grid.png", "");
		List leafs = new ArrayList();
		leafs.add("employee");
		List res = TreeNode.toTree(trees,true,leafs);
		return res;*/
	}
	/**
	 * 查询机构部门员工树，用于分级下载
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @param parentId 父部门ID
	 * @param parentType 父结点类型
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findPxOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception{
		 EmployeeDao employeeDao = (EmployeeDao)this.getDao();
		 return employeeDao.findPxOrganDeptEmployeeTree(organId, employeeName, parentId, parentType);
	}
	/**
	 * 查询机构部门员工树，用于分级下载
	 * @param organId 机构ID
	 * @param employeeName 员工名称
	 * @param parentId 父部门ID
	 * @param parentType 父结点类型
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findOrganDeptEmployeeTree(String organId,String employeeName,String parentId,String parentType)throws Exception{
		 EmployeeDao employeeDao = (EmployeeDao)this.getDao();
		 return employeeDao.findOrganDeptEmployeeTree(organId, employeeName, parentId, parentType);
		
		/*TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		TreeNode.putTypeIncon("employee", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("dept", "resources/icons/fam/grid.png", "");
		if(parentId==null){
			List<Organ> organs = null;
			if(StringUtils.isEmpty(organId)){
				organs = organService.queryAllDate(Organ.class);
			}else
				organs = organService.findCascadeOrgan(organId);
			List<TreeNode> trees = new ArrayList<TreeNode>();
			for(Organ organ:organs){
				TreeNode f = new TreeNode();
				f.setType("organ");
				f.setId(organ.getOrganId());
				f.setTitle(organ.getOrganName());
				Organ parentOrgan = organ.getOrgan();
				if(parentOrgan!=null&&organId!=null&&!organId.equals(organ.getOrganId()))
					f.setParentId(parentOrgan.getOrganId());
				trees.add(f);
			}			
			trees = TreeNode.toTree(trees,false,null);
			this.setLeafNullParent(trees);
			return trees;
		}else{
			if(parentType.equals("organ")){
				List<Dept> depts = deptService.findEntityByField(Dept.class, "organ.organId", parentId);
				List<TreeNode> trees = new ArrayList<TreeNode>();
				for(Dept dept:depts){
					if(dept.getDept()!=null) continue;
					TreeNode f = new TreeNode();
					f.setType("dept");
					f.setId(dept.getDeptId());
					f.setTitle(dept.getDeptName());
					f.setParentId(parentId);
					f.setLeaf(false);
					trees.add(f);
				}			
				return trees;
			}else{
				List<TreeNode> trees = new ArrayList<TreeNode>();
				List<Dept> depts = deptService.findEntityByField(Dept.class, "dept.deptId", parentId);
				for(Dept dept:depts){
					TreeNode f = new TreeNode();
					f.setType("dept");
					f.setId(dept.getDeptId());
					f.setTitle(dept.getDeptName());
					f.setParentId(parentId);
					f.setLeaf(false);
					trees.add(f);
				}			
				Map<String,Object> term = new HashMap<String,Object>();
				term.put("nameTerm", employeeName);
				term.put("deptTerm", parentId);
				List<Employee> list = (List<Employee>)getDao().queryConfigQl("queryHql", term, null, TreeNode.class);
				Map<String,TreeNode> ms = new HashMap<String,TreeNode>();
				for(Employee employee:list){
					TreeNode f = new TreeNode();
					f.setType("employee");
					f.setId(employee.getEmployeeId());
					f.setTitle(employee.getEmployeeName());
					f.setLeaf(true);
					Dept parent = employee.getDept();
					f.setParentId(parent.getDeptId());
					ms.put(f.getId(), f);
				}
				trees.addAll(ms.values());
				return trees;
			}
		}*/
	}
	
	/*private void setLeafNullParent(List<TreeNode> trees){
		if(trees!=null)
		for(TreeNode node:trees){
			if(node.getChildren()==null||node.getChildren().size()==0){
				node.setLeaf(false);
				node.setChildren(null);
			}else
				this.setLeafNullParent(node.getChildren());
		}
	}*/

	public DeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}

	public void setOrganService(OrganService organService) {
		this.organService = organService;
	}
}
