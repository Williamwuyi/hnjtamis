package cn.com.ite.hnjtamis.baseinfo.downloadTemplate;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.DownloadTemplate;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.downloadTemplate.DownloadTemplateFormAction</p>
 * <p>Description 模版上传下载 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月11日 下午4:52:52
 * @version 1.0
 * 
 * @modified records:
 */
public class DownloadTemplateFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = 7081408055297084979L;

	private HttpServletRequest request;
	
	private DownloadTemplate form;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	/**
	 * 查找
	 * @author zhujian
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String find()throws Exception{
		form = (DownloadTemplate)this.getService().findDataByKey(this.getId(), DownloadTemplate.class);
		return "find";
	}
	
	
	/**
	 * 保存
	 * @author zhujian
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try{
			form = (DownloadTemplate) this.jsonToObject(DownloadTemplate.class);
			form.setAcceId(null);
			form.setAcceNum(0);
			form.setAcceSize(null);
			
			Map term = new HashMap();
			term.put("itemId", form.getTemplateId());
			List<Accessory> accList = this.getService().queryData("queryAccessory", term, new HashMap());
			if(accList!=null && accList.size()>0){
				String acceId = "";
				String acceSize = "";
				for(int i=0;i<accList.size();i++){
					acceId+=accList.get(i).getAcceId()+",";
					acceSize+=accList.get(i).getFileSize()+",";
				}
				if(acceId.length()>0)acceId=acceId.substring(0,acceId.length()-1);
				if(acceSize.length()>0)acceSize=acceSize.substring(0,acceSize.length()-1);
				form.setAcceId(acceId);
				form.setAcceNum(accList.size());
				form.setAcceSize(acceSize);
			}
			DownloadTemplate downloadTemplate = (DownloadTemplate)this.getService().findDataByKey(form.getTemplateId(), DownloadTemplate.class);
			if(downloadTemplate==null){
				downloadTemplate = new DownloadTemplate();
				BeanUtils.copyProperties(form,downloadTemplate);
				downloadTemplate.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				downloadTemplate.setCreatedBy(usersess.getEmployeeName());//创建人
				downloadTemplate.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				downloadTemplate.setOrganId(usersess.getOrganId());//机构ID
				downloadTemplate.setOrganName(usersess.getOrganAlias());//机构名称
			}else{
				String creationDate = downloadTemplate.getCreationDate();
				String createdIdBy = downloadTemplate.getCreatedIdBy();
				String createdBy = downloadTemplate.getCreatedBy();
				String organId = downloadTemplate.getOrganId();
				String organName = downloadTemplate.getOrganName();
				String syncFlag = downloadTemplate.getSyncFlag();
				BeanUtils.copyProperties(form,downloadTemplate);
				downloadTemplate.setSyncFlag(syncFlag);
				downloadTemplate.setCreationDate(creationDate);//创建时间
				downloadTemplate.setCreatedBy(createdBy);//创建人
				downloadTemplate.setCreatedIdBy(createdIdBy);//创建人ID
				downloadTemplate.setOrganId(organId);//机构ID
				downloadTemplate.setOrganName(organName);//机构名称
				downloadTemplate.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				downloadTemplate.setLastUpdatedBy(usersess.getEmployeeName());//最后修改人
				downloadTemplate.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
			}
			this.getService().saveOld(downloadTemplate);
			this.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "save";
	}


	public DownloadTemplate getForm() {
		return form;
	}


	public void setForm(DownloadTemplate form) {
		this.form = form;
	}
	

	
	
	
}