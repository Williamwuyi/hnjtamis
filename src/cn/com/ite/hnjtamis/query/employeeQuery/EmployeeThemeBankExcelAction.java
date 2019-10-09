package cn.com.ite.hnjtamis.query.employeeQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.excel.ExportExcelService;


public class EmployeeThemeBankExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = 363496752746775431L;

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String op;
	
	private String employeeTerm;
	
	private String deptTerm;
	
	private String learnTerm;
	
	private String employeeBankCreateTime;
	
	private List<EmployeeThemeBankForm> list;

	/**
	 * 导出Excel 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String toExcel()
			throws Exception {		
		    try { 
		    	 ExportExcelService excel = new EmployeeThemeBankExcelServiceImpl();
		    	 
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
		 		
		 		String sheetName = "学习情况";
		 		if("xxog".equals(op)){//电厂学习情况
		 			list = employeeThemeBankService.queryEmployeeXxQkInOrgan(usersess.getCurrentOrganId(),0,100000,paramMap);
		 			sheetName = "电厂学习情况";
		 		}else if("xxdp".equals(op)){//部门学习情况
		 			paramMap.put("organId", usersess.getCurrentOrganId());
		 			Dept dept  = (Dept)this.getService().findDataByKey(usersess.getCurrentDeptId(), Dept.class);
					if(dept!=null){
						while(dept.getDept()!=null){
							dept = dept.getDept();
						}
						list = employeeThemeBankService.queryEmployeeXxQkInDept(dept.getDeptId(),0,100000,paramMap);
					}
		 			sheetName = "部门学习情况";
		 		}else if("o".equals(op)){//电厂学习情况明细
		 			list = employeeThemeBankService.queryEmployeeThemeBankInOrgan(usersess.getCurrentOrganId(),0,100000,paramMap);
		 			sheetName = "电厂学习情况明细";
		 		}else if("d".equals(op)){//部门学习情况明细
		 			paramMap.put("organId", usersess.getCurrentOrganId());
		 			Dept dept  = (Dept)this.getService().findDataByKey(usersess.getCurrentDeptId(), Dept.class);
					if(dept!=null){
						while(dept.getDept()!=null){
							dept = dept.getDept();
						}
						list = employeeThemeBankService.queryEmployeeThemeBankInDept(dept.getDeptId(),0,100000,paramMap);
					}
		 			sheetName = "部门学习情况明细";
		 		}
		 		
		 		List<Dept> deptlist = this.getService().queryAllDate(Dept.class);
		 		Map<String,Dept> deptMap = new HashMap<>();
		 		for(Dept dept : deptlist){
		 			deptMap.put(dept.getDeptId(), dept);
		 		}
		 		
		    	 Map valueMap = new HashMap();
		    	 valueMap.put("deptMap", deptMap);
		    	 valueMap.put("op", op);
		 		 valueMap.put("list", list);
		 		 valueMap.put("sheetName", sheetName);
				 excel.download(request, response, "GBK", null, sheetName+"导出.xls", excel.exportExcel(valueMap));
		 	} catch (Exception e) {
				e.printStackTrace();
			}
		    return null;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	@Override
	public void setServletResponse(HttpServletResponse httpservletresponse) {
		this.response = httpservletresponse;
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

	public String getEmployeeBankCreateTime() {
		return employeeBankCreateTime;
	}

	public void setEmployeeBankCreateTime(String employeeBankCreateTime) {
		this.employeeBankCreateTime = employeeBankCreateTime;
	}

	public List<EmployeeThemeBankForm> getList() {
		return list;
	}

	public void setList(List<EmployeeThemeBankForm> list) {
		this.list = list;
	}
	
	
	
}
