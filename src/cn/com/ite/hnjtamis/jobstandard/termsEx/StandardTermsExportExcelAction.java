package cn.com.ite.hnjtamis.jobstandard.termsEx;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.excel.ExportExcelService;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;


/**
 * 标准信息Excel导出
 * @author 朱健
 * @create time: 2016年3月23日 下午1:44:53
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsExportExcelAction  extends AbstractListAction implements ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = -5270573487067596623L;

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	

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
		    	ExportExcelService excel = new StandardTermsExcelServiceImpl();
		    	StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		    	Map term = new HashMap();
		 		List<StandardTypes> standardTypeslist = (List<StandardTypes>)standardTermsExService.queryData("queryTopStandardTypesHql", term, new HashMap());
		 		String[] sheetName = new String[standardTypeslist.size()];
		 		Map<String,List<StandardTerms>> standardTermsMap =new HashMap<String,List<StandardTerms>>();
		 		if(standardTypeslist!=null && standardTypeslist.size()>0){
		 			for(int i=0;i<standardTypeslist.size();i++){
		 				StandardTypes standardTypes = (StandardTypes)standardTypeslist.get(i);
		 				sheetName[i] = standardTypes.getTypename();
		 				Map term2 = new HashMap();
		 				term2.put("standardnameTerm", null);
		 				List<StandardTerms> standlist = standardTermsExService.queryStandardTermsByParentTypeId(standardTypes.getJstypeid(),term2);
		 				standardTermsMap.put(standardTypes.getJstypeid(), standlist);
		 			}
		 		}
		 		
				List<JobsStandardQuarter> jobsStandardQuarterList = this.getService().queryData("queryAllStandardTermQuarter", new HashMap(), null);
				
				
		    	 Map valueMap = new HashMap();
		 		 valueMap.put("standardTypeslist", standardTypeslist);
		 		 valueMap.put("sheetName", sheetName);
		 		 valueMap.put("standardTermsMap", standardTermsMap);
		 		valueMap.put("jobsStandardQuarterList", jobsStandardQuarterList);
				 excel.download(request, response, "GBK", null, "岗位标准条款导出.xls", excel.exportExcel(valueMap));
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
	
}
