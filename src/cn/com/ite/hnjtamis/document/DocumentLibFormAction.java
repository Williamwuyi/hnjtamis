package cn.com.ite.hnjtamis.document;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;
import cn.com.ite.hnjtamis.document.domain.DocumentSearchkey;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;


/**
 * 文档库管理
 * @author 朱健
 * @create time: 2016年3月16日 上午9:10:45
 * @version 1.0
 * 
 * @modified records:
 */
public class DocumentLibFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = 7081408055297084979L;

	private HttpServletRequest request;
	
	private DocumentLib form;

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
		form = (DocumentLib)this.getService().findDataByKey(this.getId(), DocumentLib.class);
		form.setSpecialityList(new ArrayList());
		List newdocumentSearchkeiesList = new ArrayList();
		for(int i=0;i<form.getDocumentSearchkeies().size();i++){
			DocumentSearchkey formKey = form.getDocumentSearchkeies().get(i);
			if(formKey.getSpecialityCode()!=null && formKey.getQuarterTrainCode() == null
					&& formKey.getDeptId() == null){
				Speciality speciality = new Speciality();
				speciality.setSpecialityid(formKey.getSpecialityCode());
				speciality.setSpecialityname(formKey.getSpecialityName());
				form.getSpecialityList().add(speciality);
			}else if(formKey.getFavoriteEmployee()!=null){
				continue;
			}else{
				newdocumentSearchkeiesList.add(formKey);
			}
		}
		form.setDocumentSearchkeies(newdocumentSearchkeiesList);
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
			form = (DocumentLib) this.jsonToObject(DocumentLib.class);
			DocumentLib documentLib = null ;
			if(form.getDocumentId()!=null && !"".equals(form.getDocumentId()) && !"null".equals(form.getDocumentId())){
				documentLib = (DocumentLib)this.getService().findDataByKey(this.getId(), DocumentLib.class);
			}
			if(documentLib == null){
				documentLib = new DocumentLib();
				documentLib.setDocumentId(this.getId());
				documentLib.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				documentLib.setSyncStatus(0);
				documentLib.setCreatedBy(usersess.getEmployeeName());//创建人
				documentLib.setCreatedById(usersess.getEmployeeId());//创建人
				documentLib.setOriginalOrganId(usersess.getOrganId());//机构名称
				documentLib.setOriginalOrganName(usersess.getOrganName());
				documentLib.setOrganId(usersess.getOrganId());//机构ID
				documentLib.setOrganName(usersess.getOrganName());
			}else{
				documentLib.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				
				documentLib.setSyncStatus(1);
				if("admin".equals(usersess.getAccount())){
					documentLib.setLastUpdatedBy("admin");//最后修改人
					documentLib.setLastUpdatedById("admin");//最后修改人
				}else{
					documentLib.setOrganId(usersess.getOrganId());//机构ID
					documentLib.setOrganName(usersess.getOrganName());
					documentLib.setLastUpdatedBy(usersess.getEmployeeName());//最后修改人
					documentLib.setLastUpdatedById(usersess.getEmployeeId());//最后修改人
				}
			}
			
			documentLib.setIsAnnounced(form.getIsAnnounced());
			documentLib.setIsDel(0);
			documentLib.setDocumentName(form.getDocumentName());
			documentLib.setDocumentTypeId(form.getDocumentTypeId());
			documentLib.setDocumentType(form.getDocumentType());
			documentLib.setDescription(form.getDescription());//描述
			documentLib.setWriterUser(form.getWriterUser());//编著人
			documentLib.setPublishers(form.getPublishers());//出版社
			documentLib.setFxDay(form.getFxDay());//发行日期
			documentLib.setIsAnnounced(form.getIsAnnounced());
			documentLib.setIsDistributeVersion(1);
			documentLib.setIsAudited(20);
			documentLib.setRemark(form.getRemark());
			
			
			List<QuarterStandard> quarterStandardList = this.getService().queryAllDate(QuarterStandard.class);
			Map<String,QuarterStandard> quarterStandardMap = new HashMap<String,QuarterStandard>();
			for(int i=0;i<quarterStandardList.size();i++){
				QuarterStandard quarterStandard = quarterStandardList.get(i);
				quarterStandardMap.put(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode(),quarterStandard);
			}
			
			//documentLib.getDocumentSearchkeies().clear();
			//注意收藏的不移除
			if(!documentLib.getDocumentSearchkeies().isEmpty()){
				Iterator its = documentLib.getDocumentSearchkeies().iterator();
				while(its.hasNext()){
					DocumentSearchkey documentSearchkey = (DocumentSearchkey)its.next();
					if(documentSearchkey.getFavoriteEmployee()==null){
						its.remove();
					}
				}
			}
			
			int index = 0;
			for(int i=0;i<form.getDocumentSearchkeies().size();i++){
				DocumentSearchkey formKey = form.getDocumentSearchkeies().get(i);
				QuarterStandard quarterStandard = quarterStandardMap.get(formKey.getDeptName()+"@"+formKey.getQuarterTrainCode());
				if(quarterStandard!=null){
					DocumentSearchkey documentSearchkey = new DocumentSearchkey();
					documentSearchkey.setQuarterTrainCode(formKey.getQuarterTrainCode());
					documentSearchkey.setQuarterTrainName(quarterStandard.getQuarterName());
					documentSearchkey.setQuarterTrainId(quarterStandard.getQuarterId());
					documentSearchkey.setDocumentLib(documentLib);
					documentSearchkey.setCreatedBy(StringUtils.isEmpty(usersess.getEmployeeName())?usersess.getAccount():usersess.getEmployeeName());
					documentSearchkey.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					documentSearchkey.setDeptName(quarterStandard.getDeptName());
					documentSearchkey.setDeptId(quarterStandard.getDeptId());
					documentSearchkey.setSpecialityName(quarterStandard.getSpecialityName());
					documentSearchkey.setSpecialityCode(quarterStandard.getSpecialityCode());
					documentSearchkey.setSortNum(index);
					documentLib.getDocumentSearchkeies().add(documentSearchkey);
					index++;
				}
			}
			
			String specialityNames = "";
			if(form.getSpecialityList()!=null && form.getSpecialityList().size()>0){
				for(int i=0;i<form.getSpecialityList().size();i++){
					Speciality speciality = form.getSpecialityList().get(i);
					DocumentSearchkey documentSearchkey = new DocumentSearchkey();
					documentSearchkey.setSpecialityName(speciality.getSpecialityname());
					documentSearchkey.setSpecialityCode(speciality.getSpecialityid());
					documentSearchkey.setCreatedBy(StringUtils.isEmpty(usersess.getEmployeeName())?usersess.getAccount():usersess.getEmployeeName());
					documentSearchkey.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					documentSearchkey.setDocumentLib(documentLib);
					documentSearchkey.setSortNum(index);
					documentLib.getDocumentSearchkeies().add(documentSearchkey);
					specialityNames+=speciality.getSpecialityname()+",";
					index++;
				}
				if(specialityNames.length()>0)
					specialityNames = specialityNames.substring(0,specialityNames.length()-1);
			}
			documentLib.setSpecialityNames(specialityNames);
			this.getService().saveOld(documentLib);
			this.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "save";
	}


	public DocumentLib getForm() {
		return form;
	}


	public void setForm(DocumentLib form) {
		this.form = form;
	}
	

	
	
	
}