package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;


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
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.excel.ExportExcelService;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.termsEx.StandardTermsExService;


/**
 * 岗位标准Excel导出
 * @author 朱健
 * @create time: 2016年3月23日 下午1:44:53
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardExportExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = -5270573487067596623L;

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private StandardTermsExService standardtermsExServer;

	/**
	 * 导出Excel 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String expExcel()
			throws Exception {		
		    try { 
		    	ExportExcelService excel = new JobUnionStandardExcelServiceImpl();
		    	JobUnionStandardExService jobUnionStandardExService = (JobUnionStandardExService)this.getService();
		    	
		    	String typeId = request.getParameter("typeId");
		    	String nodeType = request.getParameter("nodeType");
		    	
		    	if("userquarter".equals(nodeType)){
					UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
					if(usersess == null){
						this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
						return "save";
				 	}
					Quarter quarter = (Quarter)this.getService().findDataByKey(usersess.getQuarterId(), Quarter.class);
					typeId = quarter.getQuarterTrainCode();
		    	}
		    	
		    	Map term = new HashMap();
		    	term.put("quarterCode",typeId);
		    	List quarterStandardlist = this.getService().queryData("quarterStandardQuarterByCodeHql", term, null);
		    	if(quarterStandardlist == null){
		    		quarterStandardlist = new ArrayList();
		    	}
		    	String name = "";
		    	if(quarterStandardlist!=null && quarterStandardlist.size()>1){
		    		QuarterStandard quarter  = (QuarterStandard)quarterStandardlist.get(0);
		    		name+=quarter.getQuarterName()+"(";
			    	for(int i=0;i<quarterStandardlist.size();i++){
			    		quarter  = (QuarterStandard)quarterStandardlist.get(i);
			    		name+=quarter.getDeptName()+",";
			    	}
			    	name+=")";
		    	}else if(quarterStandardlist!=null && quarterStandardlist.size()>0){
		    		QuarterStandard quarter  = (QuarterStandard)quarterStandardlist.get(0);
		    		name+=quarter.getDeptName()+"("+quarter.getQuarterName()+")";
		    	}else{
		    		name = "岗位达标标准";
		    	}
		    	//quarterStandard quarter  = (QuarterStandard)this.getService().findDataByKey(typeId,QuarterStandard.class);
		    	
		    	Map term2 = new HashMap();
 				term2.put("standardnameTerm", null);
 				List<StandardTerms> standardTermslist  = standardtermsExServer.queryStandardTermsByQuarterId(typeId,term2);
		 		
 				String[] sheetName = new String[]{name};
 				Map valueMap = new HashMap();
		 		valueMap.put("sheetName", sheetName);
		 		valueMap.put("standardTermslist", standardTermslist);
				excel.download(request, response, "GBK", null, name+"岗位标准条款导出.xls", excel.exportExcel(valueMap));
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

	public StandardTermsExService getStandardtermsExServer() {
		return standardtermsExServer;
	}

	public void setStandardtermsExServer(
			StandardTermsExService standardtermsExServer) {
		this.standardtermsExServer = standardtermsExServer;
	}

	
	
}
