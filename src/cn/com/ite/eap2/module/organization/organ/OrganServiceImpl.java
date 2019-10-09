package cn.com.ite.eap2.module.organization.organ;

import java.io.File;
import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.common.utils.XlsUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.SysUser;

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
public class OrganServiceImpl extends DefaultServiceImpl implements OrganService {

	/**
	 * 机构部门树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findOrganDeptTree(String topOrganId,String deptName)throws Exception{
		Map term = new HashMap();
		term.put("nameTerm", deptName);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryDeptTreeHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new HashMap<String,TreeNode>();
		for(TreeNode node:list){
			node.setType("dept");
			TreeNode f = node;
			//加上级部门数据
			while(!StringUtils.isEmpty(f.getParentId())){
				f.setId("dept"+f.getId().split("_")[0]);
				Dept dept = (Dept)getDao().findEntityBykey(Dept.class, f.getParentId());
				if(dept!=null){
					TreeNode newNode = TreeNode.objectToTree(dept, "deptId", "dept.deptId", "deptName");
					newNode.setType("dept");
					newNode.setId("dept"+newNode.getId());
					if(!ms.containsKey(newNode.getId()))
					  ms.put(newNode.getId(), newNode);
					f = newNode;
				}else break;
			}
			//加机构数据
			
		}
		list.addAll(ms.values());
		TreeNode.putTypeIncon("dept", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("organ", "resources/icons/fam/grid.png", "");
		return TreeNode.toTree(list,true,null);
	}
	
	/**
	 * 保存实体
	 * @param entity 实体
	 */
	public void saveOrgan(Organ bo) throws Exception {
		boolean change = true;
		if(!StringUtils.isEmpty(bo.getOrganId())){
			change = false;
			Organ old = (Organ)this.findDataByKey(bo.getOrganId(), Organ.class);
			if((bo.getOrgan()==null||StringUtils.isEmpty(bo.getOrgan().getOrganId()))&&old.getOrgan()!=null)
				change = true;
			else if(bo.getOrgan()!=null&&bo.getOrgan().getOrganId()!=null&&
			(old.getOrgan()==null||!old.getOrgan().getOrganId().equals(bo.getOrgan().getOrganId())))
				change = true;
		}
		if(change)
		bo.setOrderNo(1+this.getFieldMax(Organ.class, 
				"orderNo","organ.organId",
				(bo.getOrgan()==null?null:bo.getOrgan().getOrganId())));		
		super.save(bo);
		levelCodeHandler(bo, "organId", "organ", "organs", "orderNo", "levelCode");
	}
	
	/**
	 * 保存机构顺序
	 * @param orders 顺序码数组
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void saveSort(String[] orders) throws Exception{
		int index = 0;
		List saves = new ArrayList();
		for(String id:orders){
			Organ organ = (Organ)this.findDataByKey(id, Organ.class);
			organ.setOrderNo(index++);
			levelCodeHandler(organ, "organId", "organ", "organs", "orderNo", "levelCode");
			saves.add(organ);
		}
		super.saves(saves);
	}
	
	/**
	 * 级联查询机构
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<Organ> findCascadeOrgan(String organId){
		Organ organ = (Organ)getDao().findEntityBykey(organId);
		List<Organ> rets = new ArrayList<Organ>();
		rets.add(organ);
		if(organ.getOrgans()!=null)
		for(Organ child:organ.getOrgans()){
			rets.addAll(this.findCascadeOrgan(child.getOrganId()));
		}
		return rets;
	}

	//导出对象的属性配置
	private String[] organCols = new String[]{"organId","organ.organId",
			"area","organCode","organName","organAlias","organType",
			"orderNo","postcode","address","telephone","fax",
			"validation","remark","levelCode"};
	private String[] organTitles = new String[]{"机构ID","父机构ID",
			"地区","机构编码","机构名称","机构别名","机构类型",
			"机构排序","邮编","地址","电话","传真",
			"是否有效","备注","级别编码"};
	private String[] deptCols = new String[]{"deptId","dept.deptId","organ.organId",
			"deptCode","deptName","deptAlias","deptType","deptCharacter",
			"orderNo","validation","levelCode","remark"};
	private String[] deptTitles = new String[]{"部门ID","父部门ID","机构ID",
			"部门编码","部门名称","部门别名","部门类型","部门性质",
			"部门排序","是否有效","部门备注"};
	private String[] quarterCols = new String[]{"quarterId","dept.deptId","quarter.quarterId",
			"quarterCode","quarterName","quarterType","validation","remark",
			"responsibility","orderNo"};
	private String[] quarterTitles = new String[]{"岗位ID","部门ID","父岗位ID",
			"岗位编码","岗位名称","岗位类型","是否有效","描述","职责","岗位排序"};
	private String[] employeeCols = new String[]{"employeeId","quarter.quarterId",
			"dept.deptId","employeeCode","employeeName","simpleName","alias",
			"sex","nationality","birthday","nativeplace","identityCard","officePhone",
			"addressPhone","movePhone","fax","email","address","postalCode","validation","remark","orderNo"};
	private String[] employeeTitles = new String[]{"员工ID","岗位ID",
			"部门ID","员工编码","员工名称","简拼","曾用名",
			"性别","民族","生日","籍贯","身份证","办公电话",
			"住宅电话","移动电话","传真","邮箱","住址","邮政编码","是否有效","描述","排序"};
	private String[] userCols = new String[]{"userId","employee.employeeId",
			"app.appCode","account","password","orderNo","allowRepeatLogin",
			"tig","validation","organ.organId"};
	private String[] userTitles = new String[]{"用户ID","员工ID",
			"系统编码","用户账号","用户密码","用户排序","是否允许重复登录",
			"是否首登修密提示","是否有效","机构ID"};
	
	/**
	 * 导出机构部门岗位人员
	 * @param organid 机构ID
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public File exportDate(String organId) throws Exception {
		List<Organ> organs = this.findCascadeOrgan(organId);
		File xls = File.createTempFile("机构部门岗位员工用户", "xls");
		XlsUtils utils = XlsUtils.createWrite(xls);
		utils.write(0, "机构", organs, organCols, organTitles);
		List<Dept> depts = new ArrayList<Dept>();
		for(Organ organ:organs){
			depts.addAll(organ.getDepts());
		}
		utils.write(1, "部门", depts, deptCols, deptTitles);
		List<Quarter> quarters = new ArrayList<Quarter>();
		for(Dept dept:depts){
			quarters.addAll(dept.getQuarters());
		}
		utils.write(2, "岗位", quarters, quarterCols, quarterTitles);
		Map term = new HashMap();
		Organ organ = (Organ)this.findDataByKey(organId, Organ.class);
		term.put("level", organ.getLevelCode());
		List<Employee> employees = this.queryData("queryEmployeeHql", term, null);
		utils.write(3, "员工", employees, employeeCols, employeeTitles);
		List<SysUser> users = this.queryData("queryUserHql", term, null);
		utils.write(4, "用户", users, userCols, userTitles);
		utils.closeWrite();
		return xls;
	}

	/**
	 * 导入机构部门岗位人员
	 * @param xls EXCEL文件
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void importDate(File xls) throws Exception {
		List organs = XlsUtils.read(xls, "机构", Organ.class, organCols);
		Map<String,Object> oldIdToObject = new HashMap();//原文件中的主健对对象的映射
		Map<String,Object> parentToChild = new HashMap<String,Object>();
		for(Organ organ:(List<Organ>)organs){
			String key = "";
			Organ po = organ.getOrgan();
			if(po!=null) key = po.getOrganId();
			List child = (List)parentToChild.get("organ_"+key);
			if(child==null){
				child = new ArrayList();
				parentToChild.put("organ_"+key, child);
			}
			child.add(organ);
		}
		//保存机构
		this.iterateImportOrgan((List)parentToChild.get("organ_"), oldIdToObject, parentToChild, null);
		List depts = XlsUtils.read(xls, "部门", Dept.class, deptCols);
		for(Dept dept:(List<Dept>)depts){//处理部门
			String key = "";
			Dept pd = dept.getDept();
			if(pd!=null) key = pd.getDeptId();
			List child = (List)parentToChild.get("dept_"+key);
			if(child==null){
				child = new ArrayList();
				parentToChild.put("dept_"+key, child);
			}
			child.add(dept);
		}
		//保存部门
		this.iterateImportDept((List)parentToChild.get("dept_"), oldIdToObject, parentToChild, null);
		List quarters = XlsUtils.read(xls, "岗位", Quarter.class, quarterCols);
		for(Quarter quarter:(List<Quarter>)quarters){//处理岗位
			String key = "";
			Quarter pq = quarter.getQuarter();
			if(pq!=null) key = pq.getQuarterId();
			List child = (List)parentToChild.get("quarter_"+key);
			if(child==null){
				child = new ArrayList();
				parentToChild.put("quarter_"+key, child);
			}
			child.add(quarter);
			Dept dept = quarter.getDept();
			if(dept!=null){
				dept = (Dept)oldIdToObject.get("dept_"+dept.getDeptId());
				if(dept==null)
					throw new Exception("\\\""+quarter.getQuarterName()+
							"\\\"此岗位关联的部门\\\""+quarter.getDept().getDeptId()+"\\\"的不在此文件中");
				quarter.setDept(dept);
			}
		}
		//保存岗位
		this.iterateImportQuarter((List)parentToChild.get("quarter_"), oldIdToObject, parentToChild, null);
		List employees = XlsUtils.read(xls, "员工", Employee.class, employeeCols);
		for(Employee employee:(List<Employee>)employees){
			Quarter quarter = employee.getQuarter();
			oldIdToObject.put("employee_"+employee.getEmployeeId(), employee);
			if(quarter!=null){
				quarter = (Quarter)oldIdToObject.get("quarter_"+quarter.getQuarterId());
				if(quarter==null)
					throw new Exception("\\\""+employee.getEmployeeName()+
							"\\\"此员工关联的岗位\\\""+employee.getQuarter().getQuarterId()+"\\\"的不在此文件中");
				employee.setQuarter(quarter);
			}
			Dept dept = employee.getDept();
			if(dept!=null){
				dept = (Dept)oldIdToObject.get("dept_"+dept.getDeptId());
				if(dept==null)
					throw new Exception("\\\""+employee.getEmployeeName()+
							"\\\"此员工关联的部门\\\""+employee.getDept().getDeptId()+"\\\"的不在此文件中");
				employee.setDept(dept);
			}
			getDao().addEntity(employee);//保存员工
		}
		List users = XlsUtils.read(xls, "用户", SysUser.class, userCols);
		for(SysUser user:(List<SysUser>)users){
			Employee employee = user.getEmployee();
			if(employee!=null){
				employee = (Employee)oldIdToObject.get("employee_"+employee.getEmployeeId());
				if(employee==null)
					throw new Exception("\\\""+user.getAccount()+
							"\\\"此用户关联的员工\\\""+user.getEmployee().getEmployeeId()+"\\\"的不在此文件中");
				user.setEmployee(employee);
			}
			Organ organ = user.getOrgan();
			if(organ!=null){
				organ = (Organ)oldIdToObject.get("organ_"+user.getOrgan().getOrganId());
				if(organ==null)
					throw new Exception("\\\""+user.getAccount()+
							"\\\"此用户关联的机构\\\""+user.getOrgan().getOrganId()+"\\\"的不在此文件中");
				user.setOrgan(organ);
			}
			AppSystem app = user.getApp();
			if(app!=null&&!StringUtils.isEmpty(app.getAppCode())){
				List apps = getDao().findEntityByField(AppSystem.class,"appCode",app.getAppCode());
				if(apps.size()>0){
					app = (AppSystem)apps.get(0);
					if(app!=null)
						user.setApp(app);
					else
						user.setApp(null);
				}else
					user.setApp(null);
			}
			getDao().addEntity(user);//保存用户
		}
	}
	/**
	 * 迭代处理机构
	 * @param organs
	 * @param oldIdToObject
	 * @param parentToChild
	 * @param parentOrgan
	 * @modified
	 */
	@SuppressWarnings({ "unchecked" })
	private void iterateImportOrgan(List organs,Map<String,Object> oldIdToObject,
			Map<String,Object> parentToChild,Organ parentOrgan) throws Exception{
		if(organs!=null)
		for(Organ organ:(List<Organ>)organs){
			String oldId = organ.getOrganId();
			organ.setOrgan(parentOrgan);//设置父模块
			getDao().addEntity(organ);//增加模块
			oldIdToObject.put("organ_"+oldId, organ);
			this.iterateImportOrgan((List)parentToChild.get("organ_"+oldId), oldIdToObject,parentToChild, organ);
	    }
	}
	/**
	 * 迭代处理部门
	 * @param organs
	 * @param oldIdToObject
	 * @param parentToChild
	 * @param parentOrgan
	 * @modified
	 */
	@SuppressWarnings({ "unchecked" })
	private void iterateImportDept(List depts,Map<String,Object> oldIdToObject,
			Map<String,Object> parentToChild,Dept parentDept) throws Exception{
		if(depts!=null)
		for(Dept dept:(List<Dept>)depts){
			String oldId = dept.getDeptId();
			dept.setDept(parentDept);//设置父模块
			Organ organ = dept.getOrgan();
			if(organ!=null){
				organ = (Organ)oldIdToObject.get("organ_"+organ.getOrganId());
				if(organ==null)
					throw new Exception("\\\""+dept.getDeptName()+"\\\"此部门关联的机构\\\""
							+dept.getOrgan().getOrganId()+"\\\"不在此文件中!");
				dept.setOrgan(organ);
			}
			getDao().addEntity(dept);//增加模块
			oldIdToObject.put("dept_"+oldId, dept);
			this.iterateImportDept((List)parentToChild.get("dept_"+oldId), oldIdToObject,parentToChild, dept);
	    }
	}
	/**
	 * 迭代处理岗位
	 * @param organs
	 * @param oldIdToObject
	 * @param parentToChild
	 * @param parentOrgan
	 * @modified
	 */
	@SuppressWarnings({ "unchecked" })
	private void iterateImportQuarter(List quarters,Map<String,Object> oldIdToObject,
			Map<String,Object> parentToChild,Quarter parentQuarter) throws Exception{
		if(quarters!=null)
		for(Quarter quarter:(List<Quarter>)quarters){
			String oldId = quarter.getQuarterId();
			quarter.setQuarter(parentQuarter);//设置父模块
			getDao().addEntity(quarter);//增加模块
			oldIdToObject.put("quarter_"+oldId, quarter);
			this.iterateImportQuarter((List)parentToChild.get("quarter_"+oldId), oldIdToObject,
					parentToChild, quarter);
	    }
	}
}