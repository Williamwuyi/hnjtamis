package cn.com.ite.hnjtamis.kb.customfolder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.kb.domain.CustomFolder;
/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.customfolde.CustomFolderFormAction
 * </p>
 * <p>
 * Description 自定义文件夹ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time: 2015-3-30
 * @version 1.0
 * 
 * @modified records:
 */
public class CustomFolderFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7076226675197406422L;

	private CustomFolder form;
	
	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (CustomFolder) service.findDataByKey(this.getId(),
				CustomFolder.class);
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
			form = (CustomFolder) this.jsonToObject(CustomFolder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrganId(session.getOrganId());
		}
		
		//验证是否已经存在相同的名称
		Map<String, String> term = new HashMap<String, String>();
		term.put("createUserId", session.getEmployeeCode());
		term.put("nameTerm", form.getName());
		if (form.getParentFolder() != null) {
			term.put("parentId", form.getParentFolder().getId());
		}
		int c = service.queryData("folderUnique", term, null).size();
		if (c > 0) {
			throw new Exception("文件夹名称【" + form.getName() + "】已经存在！");
		}
		try {
			service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	public CustomFolder getForm() {
		return form;
	}

	public void setForm(CustomFolder form) {
		this.form = form;
	}
}
