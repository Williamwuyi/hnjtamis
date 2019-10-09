package cn.com.ite.hnjtamis.query.employeeQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.query.employeeQuery.EmployeeThemeBankListAction</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年5月16日 上午10:31:02
 * @version 1.0
 * 
 * @modified records:
 */
public class EmployeeThemeBankListAction extends AbstractListAction {

	private static final long serialVersionUID = -4068494551896015923L;

	private String op;
	private String employeeTerm;
	
	private String deptTerm;
	
	private String learnTerm;
	
	private String employeeBankCreateTime;
	
	private List<EmployeeThemeBankForm> list;

	public String list()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		EmployeeThemeBankService employeeThemeBankService = (EmployeeThemeBankService)this.getService();
		list = new ArrayList<EmployeeThemeBankForm>();
		Map paramMap = new HashMap();
		if(employeeTerm!=null && !"undefined".equals(employeeTerm) && !"".equals(employeeTerm)&& !"null".equals(employeeTerm)){
			paramMap.put("employeeTerm", employeeTerm);
		}
		if(deptTerm!=null && !"undefined".equals(deptTerm) && !"".equals(deptTerm)&& !"null".equals(deptTerm)){
			paramMap.put("deptTerm", deptTerm);
		}
		paramMap.put("learnTerm", learnTerm);
		
		if("xxog".equals(op)){
			list = employeeThemeBankService.queryEmployeeXxQkInOrgan(usersess.getCurrentOrganId(),this.getStart(),this.getLimit(),paramMap);
			this.setTotal(employeeThemeBankService.countEmployeeXxQkInOrgan(usersess.getCurrentOrganId(),paramMap));
		}else if("xxdp".equals(op)){
			paramMap.put("organId", usersess.getCurrentOrganId());
			
			Dept dept  = (Dept)this.getService().findDataByKey(usersess.getCurrentDeptId(), Dept.class);
			if(dept!=null){
				while(dept.getDept()!=null){
					dept = dept.getDept();
				}
				list = employeeThemeBankService.queryEmployeeXxQkInDept(dept.getDeptId(),this.getStart(),this.getLimit(),paramMap);
				this.setTotal(employeeThemeBankService.countEmployeeXxQkInDept(dept.getDeptId(),paramMap));
			}else{
				list = new ArrayList();
				this.setTotal(0);
			}
			
		}else if("o".equals(op)){
			list = employeeThemeBankService.queryEmployeeThemeBankInOrgan(usersess.getCurrentOrganId(),this.getStart(),this.getLimit(),paramMap);
			this.setTotal(employeeThemeBankService.countEmployeeThemeBankInOrgan(usersess.getCurrentOrganId(),paramMap));
		}else if("d".equals(op)){
			paramMap.put("organId", usersess.getCurrentOrganId());
			Dept dept  = (Dept)this.getService().findDataByKey(usersess.getCurrentDeptId(), Dept.class);
			if(dept!=null){
				while(dept.getDept()!=null){
					dept = dept.getDept();
				}
				list = employeeThemeBankService.queryEmployeeThemeBankInDept(dept.getDeptId(),this.getStart(),this.getLimit(),paramMap);
				this.setTotal(employeeThemeBankService.countEmployeeThemeBankInDept(dept.getDeptId(),paramMap));
			}else{
				list = new ArrayList();
				this.setTotal(0);
			}
		}
		//this.setTotal(tmplist.size());
	/*	for(int i=this.getStart();i<this.getStart()+this.getLimit() && i<tmplist.size();i++){
			list.add(tmplist.get(i));
		}*/
		return "list";
	}
	
	
	public String queryTjEndTime(){
		EmployeeThemeBankService employeeThemeBankService = (EmployeeThemeBankService)this.getService();
		employeeBankCreateTime = employeeThemeBankService.getEmployeeBankCreateTime();
		return "queryTjEndTime";
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getEmployeeTerm() {
		return employeeTerm;
	}

	public void setEmployeeTerm(String employeeTerm) {
		this.employeeTerm = employeeTerm;
	}

	public List<EmployeeThemeBankForm> getList() {
		return list;
	}

	public void setList(List<EmployeeThemeBankForm> list) {
		this.list = list;
	}


	public String getEmployeeBankCreateTime() {
		return employeeBankCreateTime;
	}


	public void setEmployeeBankCreateTime(String employeeBankCreateTime) {
		this.employeeBankCreateTime = employeeBankCreateTime;
	}


	public String getDeptTerm() {
		return deptTerm;
	}


	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}


	public String getLearnTerm() {
		return learnTerm;
	}


	public void setLearnTerm(String learnTerm) {
		this.learnTerm = learnTerm;
	}
	
	
	 
}
