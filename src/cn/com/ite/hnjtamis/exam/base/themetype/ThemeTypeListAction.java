package cn.com.ite.hnjtamis.exam.base.themetype;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
/*
 * 提醒管理维护 - list
 */
public class ThemeTypeListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = 1622765066336948720L;
	private HttpServletRequest request;
	
	private String titleTerm;
	
	private List<ThemeType> list = new ArrayList<ThemeType>();
	private List<ThemeType> themeTypeCom = new ArrayList<ThemeType>();
	
	private String themeTypeIds;
	/*
	 * 列表查询
	 */
	public String list(){
		list = (List<ThemeType>)service.queryData("queryHql", this, null,this.getStart(),this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	/*
	 * 题型下拉框
	 */
	public String themeTypeCom(){
		themeTypeCom = service.queryData("themeTypeCom", null, null, ThemeType.class);
		return "themeTypeCom";
	}
	
	/*
	 * 列表查询
	 */
	public String queryTypeslist(){
		list = (List<ThemeType>)service.queryData("queryTypesHql", this, null);
		this.setTotal(service.countData("queryTypesHql", this));
		return "list";
	}
	/*
	 * 删除
	 */
	public String delete() throws Exception{
		try {
			//service.deleteByKeys(this.getId().split(","), ThemeType.class);
			String[] idsArray = this.getId().split(",");
			String ids = ",";
			for(String id : idsArray){
				ids+=id+",";
			}
			ids = ids.substring(0, ids.length());
			Map term = new HashMap();
			term.put("ids", ids);
			List testPaperList = service.queryData("checkTestPaperThemeSql", term, null, null);
			List examTestPaperList = service.queryData("checkExamTestPaperThemeSql", term, null, null);
			Map<String,String> cannotdelete = new HashMap<String,String>();
			if(testPaperList!=null && testPaperList.size()>0){
				for(Object obj:testPaperList){
					HashMap t = (HashMap) obj;
					Integer count = Integer.parseInt(t.get("ALLCOUNT").toString());
					String typeid = t.get("THEME_TYPE_ID").toString();
					String typename = t.get("THEME_TYPE_NAME").toString();
					cannotdelete.put(typeid, typename);
				}
			}
			if(examTestPaperList!=null && examTestPaperList.size()>0){
				for(Object obj:examTestPaperList){
					HashMap t = (HashMap) obj;
					Integer count = Integer.parseInt(t.get("ALLCOUNT").toString());
					String typeid = t.get("THEME_TYPE_ID").toString();
					String typename = t.get("THEME_TYPE_NAME").toString();
					cannotdelete.put(typeid, typename);
				}
			}
			
			String deleteMsg = "";
			for(String id : idsArray){
				if(cannotdelete.containsKey(id)){
					deleteMsg+=cannotdelete.get(id)+"已被引用，不能删除<br/>";
				}else{
					service.deleteByKey(id, ThemeType.class);
				}
			}
			if(StringUtils.isEmpty(deleteMsg)){
				deleteMsg = "题型删除成功！";
			}
			this.setMsg(deleteMsg);
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("题型删除失败！");
		}
		
		return "delete";
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public List<ThemeType> getList() {
		return list;
	}
	public void setList(List<ThemeType> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}
	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}
	public List<ThemeType> getThemeTypeCom() {
		return themeTypeCom;
	}
	public void setThemeTypeCom(List<ThemeType> themeTypeCom) {
		this.themeTypeCom = themeTypeCom;
	}
	public String getThemeTypeIds() {
		return themeTypeIds;
	}
	public void setThemeTypeIds(String themeTypeIds) {
		this.themeTypeIds = themeTypeIds;
	}
	
	
}
