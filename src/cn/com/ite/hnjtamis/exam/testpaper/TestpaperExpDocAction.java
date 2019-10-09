package cn.com.ite.hnjtamis.exam.testpaper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;









import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.doc.ExportDocService;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperExpDocAction</p>
 * <p>Description 导出试卷 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月5日 上午10:41:36
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperExpDocAction extends AbstractListAction implements ServletRequestAware,ServletResponseAware{
	
	
	
	private HttpServletRequest request;

	private HttpServletResponse response;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	@Override
	public void setServletResponse(HttpServletResponse httpservletresponse) {
		this.response = httpservletresponse;
	}
	
	/**
	 * 导出Word
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws BaseException
	 */
	public String exportToDoc() throws Exception {
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			String id = request.getParameter("id");
			String showRight = request.getParameter("showRight");
			Testpaper testpaper = (Testpaper)this.getService().findDataByKey(id, Testpaper.class);
			
			
			Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
			List<ThemeType> themeTypeList = this.getService().queryAllDate(ThemeType.class);
			for(int i=0;i<themeTypeList.size();i++){
				ThemeType themeType = themeTypeList.get(i);
				themeTypeMap.put(themeType.getThemeTypeId(), themeType);
			}
			
			
			Map<String,String> param = new HashMap<String,String>();
			param.put("testpaperId", testpaper.getTestpaperId());
			Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
			sortMap.put("sortNum", true);
			List<TestpaperTheme> testpaperthemeList = null;
			try{
				testpaperthemeList = this.getService().queryData("themeInTemplateHql", param , sortMap);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//Map term = new HashMap();
			//term.put("testpaperId", id);
			//List<ThemeType> themeTypeList = this.getService().queryData("themeTypeInTestpaperThemeHql", term, new HashMap());
			ExportDocService doc = new TestpaperExpServiceImpl();
			Map valueMap=new HashMap();
			String basepath = FileOption.getFileBasePath(request);
			valueMap.put("testpaper", testpaper);
			valueMap.put("testpaperthemeList", testpaperthemeList);
			valueMap.put("themeTypeMap", themeTypeMap);
			valueMap.put("basepath", basepath);
			valueMap.put("showRight", showRight);
			doc.exportDoc(request, response,  testpaper.getTestpaperName()+".doc",valueMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
