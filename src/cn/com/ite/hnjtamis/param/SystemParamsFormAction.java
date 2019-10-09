package cn.com.ite.hnjtamis.param;

import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.hnjtamis.param.domain.SystemParams;

public class SystemParamsFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 150036989976271980L;

	private SystemParams form;
	
	private String code;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (SystemParams) service.findDataByKey(this.getId(),
				SystemParams.class);
		return "find";
	}
	
	/**
	 * 根据编码查参数
	 * @return
	 * @throws Exception
	 */
	public String findByCode() throws Exception {
		form = ((SystemParamsService)service).findByCode(this.getCode());
		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		try {
			form = (SystemParams) this.jsonToObject(SystemParams.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		
//		//验证编码是否重复
//		SystemParams param = ((SystemParamsService)service).findByCode(form.getCode());
//		if (param != null) {
//			throw new Exception("该参数编码已经被使用，请重新输入编码！");
//		}
		try {
			service.save(form);
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("保存出错，可能由于编码重复！");
		}
		this.setMsg("保存成功！");
		return "save";
	}

	public SystemParams getForm() {
		return form;
	}

	public void setForm(SystemParams form) {
		this.form = form;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
