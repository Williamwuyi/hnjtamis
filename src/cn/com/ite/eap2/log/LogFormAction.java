package cn.com.ite.eap2.log;

import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.baseinfo.LogMain;

/**
 * <p>Title cn.com.ite.eap2.log.LogFormAction</p>
 * <p>Description 日志FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-23 上午08:56:33
 * @version 2.0
 * 
 * @modified records:
 */
public class LogFormAction  extends AbstractFormAction{
	private static final long serialVersionUID = 6866380450438689949L;
	private LogMain form;
	public LogMain getForm() {
		return form;
	}
	public void setForm(LogMain form) {
		this.form = form;
	}
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String find(){
		form = (LogMain)service.findDataByKey(this.getId(),LogMain.class);
		return "find";
	}
}
