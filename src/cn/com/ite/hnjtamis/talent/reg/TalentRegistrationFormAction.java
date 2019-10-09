package cn.com.ite.hnjtamis.talent.reg;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistrationBank;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistrationSpeciality;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.talent.reg.TalentRegistrationFormAction
 * </p>
 * <p>
 * Description 专家库FormAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-4-8
 * @version 1.0
 * 
 * @modified
 */
public class TalentRegistrationFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3882477324428116329L;

	private TalentRegistration form;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() throws Exception {
		form = (TalentRegistration) service.findDataByKey(this.getId(),
				TalentRegistration.class);
		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		try {
			form = (TalentRegistration) this
					.jsonToObject(TalentRegistration.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrgan((Organ) service.findDataByKey(session.getOrganId(),
					Organ.class));
		} 
//		else {
//			form.setOrgan((Organ)service.findDataByKey(form.getOrganId(), Organ.class));
//		}
		List<TalentRegistrationBank> tbankslist = form.getTalentRegistrationBanks();
		String bankNames = "";
		if(!tbankslist.isEmpty()){
			Map<String,ThemeBank> bankMap = new HashMap<String,ThemeBank>();
			List<ThemeBank> themeBanklist = this.getService().queryAllDate(ThemeBank.class);
			for(int i=0;i<themeBanklist.size();i++){
				ThemeBank themeBank = (ThemeBank)themeBanklist.get(i);
				bankMap.put(themeBank.getThemeBankId(), themeBank);
			}
			for(int i=0;i<tbankslist.size();i++){
				TalentRegistrationBank talentRegistrationBank = tbankslist.get(i);
				bankNames+=talentRegistrationBank.getBankName()+"，";
				ThemeBank themeBank = bankMap.get(talentRegistrationBank.getBankId());
				if(themeBank!=null){
					talentRegistrationBank.setBankCode(themeBank.getThemeBankCode());
				}
				talentRegistrationBank.setOrderno(i);
				talentRegistrationBank.setCreationDate(nowTime);
				talentRegistrationBank.setCreatedBy(session.getEmployeeCode());
			}
			if(bankNames.length()>0)bankNames=bankNames.substring(0,bankNames.length()-1);
		}
		if(bankNames.length()>1500){
			bankNames = bankNames.substring(0,1480)+"...";
		}
		form.setBankNames(bankNames);
		form.setIsAudited(0);
		
		List<TalentRegistrationSpeciality> specialitys = form.getSpecialitys();
		String specialityNames = "";
		if(!specialitys.isEmpty()){
			for(int i=0;i<specialitys.size();i++){
				TalentRegistrationSpeciality talentRegistrationSpeciality = (TalentRegistrationSpeciality)specialitys.get(i);
				if(talentRegistrationSpeciality.getSpeciality()!=null &&
						talentRegistrationSpeciality.getSpeciality().getSpecialityname()!=null){
					String specialityname = talentRegistrationSpeciality.getSpeciality().getSpecialityname();
					talentRegistrationSpeciality.setSpecialityname(specialityname);
					specialityNames+=specialityname+",";
					talentRegistrationSpeciality.setOrderno(i);
					talentRegistrationSpeciality.setTalentRegistration(form);
				}
			}
			if(specialityNames.length()>0)specialityNames=specialityNames.substring(0,specialityNames.length()-1);
		}
		form.setSpecialityNames(specialityNames);
		try {
			service.save(form);
			
			((TalentRegistrationService)service).updateEmployeeQuarter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	public TalentRegistration getForm() {
		return form;
	}

	public void setForm(TalentRegistration form) {
		this.form = form;
	}

}
