package cn.com.ite.hnjtamis.baseinfo.quarterStandard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx.JobUnionStandardExService;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.quarterStandard.QuarterStandardFormAction</p>
 * <p>Description  标准岗位处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月23日 下午2:05:22
 * @version 1.0
 * 
 * @modified records:
 */
public class QuarterStandardFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = -3452366517345370449L;
	private HttpServletRequest request;
	private JobUnionStandardExService jobUnionStandardExService;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
 
	private QuarterStandard form;
	
	
	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() throws Exception {
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		form = (QuarterStandard) service.findDataByKey(this.getId(), QuarterStandard.class);
		if(form.getParentQuarterId()!=null && form.getParentQuarterId().length()>0){
			String[] pids = form.getParentQuarterId().split(",");
			String[] pnames = form.getParentQuarterName().split(",");
			form.setParentQuarterStandard(new ArrayList());
			for(int i=0;i<pids.length;i++){
				QuarterStandard parentQuarterStandard = new QuarterStandard();
				parentQuarterStandard.setQuarterId(pids[i]);
				parentQuarterStandard.setQuarterName(pnames[i]);
				form.getParentQuarterStandard().add(parentQuarterStandard);
			}
			
		}
		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		try {
			form = (QuarterStandard) this.jsonToObject(QuarterStandard.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		if ("".equals(form.getQuarterId()))
			form.setQuarterId(null);
		if (StringUtils.isEmpty(form.getQuarterId())) {	
			form.setCreatedBy(usersess.getEmployeeCode());
			form.setCreationDate(nowTime);
		}else{
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(usersess.getEmployeeCode());
		}
		if("10".equals(form.getBelongType())){
			form.setOrganName(null);
			form.setOrganId(null);
		}else if(usersess.getOrganName()!=null && !"".equals(usersess.getOrganName())){
			form.setOrganName(usersess.getOrganName());
			form.setOrganId(usersess.getOrganId());
		}else if(usersess.getCurrentOrganName()!=null && !"".equals(usersess.getCurrentOrganName())){
			form.setOrganName(usersess.getCurrentOrganName());
			form.setOrganId(usersess.getCurrentOrganId());
		}else{
			this.setMsg("企业自定义岗位必须用户存在机构信息！");
			return "save";
		}
		if (form.getSpecialityName() == null){
			form.setSpecialityName("");
		}
		String pids = "";
		String pnames = "";
		String pcodes = "";
		String[] notDelParentQuarterIds = null;
		if(form.getParentQuarterStandard()!=null && form.getParentQuarterStandard().size()>0){
			notDelParentQuarterIds = new String[form.getParentQuarterStandard().size()];
			for(int i=0;i<form.getParentQuarterStandard().size();i++){
				QuarterStandard quarterStandard = (QuarterStandard)this.getService().findDataByKey((form.getParentQuarterStandard().get(i)).getQuarterId(), QuarterStandard.class);
				if(quarterStandard!=null){
					if(form.getQuarterId()!=null && quarterStandard.getQuarterId().equals(form.getQuarterId())){
						notDelParentQuarterIds[i]="";
						continue;
					}
					pids+=quarterStandard.getQuarterId()+",";
					pnames+=quarterStandard.getDeptName()+"("+quarterStandard.getQuarterName()+"),";
					pcodes+=quarterStandard.getQuarterCode()+",";
					notDelParentQuarterIds[i]=quarterStandard.getQuarterId();
				}else{
					notDelParentQuarterIds[i]="";
				}
			}
			if(pids.length()>0)pids=pids.substring(0,pids.length()-1);
			if(pnames.length()>0)pnames=pnames.substring(0,pnames.length()-1);
			if(pcodes.length()>0)pcodes=pcodes.substring(0,pcodes.length()-1);
		}
		form.setParentQuarterId(pids);
		form.setParentQuarterName(pnames);
		form.setParentQuarterCode(pcodes);
		try {
			service.save(form);
			jobUnionStandardExService.deleteJobsStandardQuarterHaveParentQuarter(form.getQuarterId(), notDelParentQuarterIds);
			if(notDelParentQuarterIds!=null && notDelParentQuarterIds.length>0){
				for(int i=0;i<notDelParentQuarterIds.length;i++){
					String sourceQid = notDelParentQuarterIds[i];
					if(sourceQid!=null && !"".equals(sourceQid)){
						jobUnionStandardExService.saveAndCopySourceStandard(sourceQid, form.getQuarterId());
					}
				}
			}
			jobUnionStandardExService.saveChildeJobUnionStandard(form.getQuarterId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}
	

	public QuarterStandard getForm() {
		return form;
	}

	public void setForm(QuarterStandard form) {
		this.form = form;
	}

	public JobUnionStandardExService getJobUnionStandardExService() {
		return jobUnionStandardExService;
	}

	public void setJobUnionStandardExService(
			JobUnionStandardExService jobUnionStandardExService) {
		this.jobUnionStandardExService = jobUnionStandardExService;
	}
	
	
}