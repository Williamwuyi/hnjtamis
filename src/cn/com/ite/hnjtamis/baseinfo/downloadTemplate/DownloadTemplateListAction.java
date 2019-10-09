package cn.com.ite.hnjtamis.baseinfo.downloadTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.hnjtamis.baseinfo.domain.DownloadTemplate;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.downloadTemplate.DownloadTemplateListAction</p>
 * <p>Description 模版上传下载</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月11日 下午4:53:12
 * @version 1.0
 * 
 * @modified records:
 */
public class DownloadTemplateListAction extends AbstractListAction implements ServletRequestAware{
 
	private static final long serialVersionUID = 2951108490451624080L;

	private HttpServletRequest request;
	
	private List<DownloadTemplate> templateList;
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	/**
	 * 查询列表
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String list(){
		templateList = new ArrayList();
		Map term = new HashMap();
		List<DownloadTemplate> list = this.getService().queryData("getTemplateList", term, new HashMap());
		for(int i=this.getStart();i<this.getStart()+this.getLimit() && i<list.size();i++){
			templateList.add(list.get(i));
		}
		this.setTotal(list.size());
		return "list";
	}
	

	/**
	 * 删除
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String delete() throws EapException{
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		for(int i = 0;i<ids.length ;i++){
			sum++;
			DownloadTemplate downloadTemplate = (DownloadTemplate)service.findDataByKey(ids[i], DownloadTemplate.class);
			if(downloadTemplate!=null){
				Map term = new HashMap();
				term.put("itemId", ids[i]);
				List<Accessory> accList = this.getService().queryData("queryAccessory", term, new HashMap());
				service.deletes(accList);
				
				service.delete(downloadTemplate);
				succ++;
			}
		}
		this.setMsg("选中模版"+sum+"个,成功删除"+succ+"个！");
		return "delete";
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public List<DownloadTemplate> getTemplateList() {
		return templateList;
	}


	public void setTemplateList(List<DownloadTemplate> templateList) {
		this.templateList = templateList;
	}
	
	
	
}
