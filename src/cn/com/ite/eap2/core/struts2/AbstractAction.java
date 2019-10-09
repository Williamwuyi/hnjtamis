package cn.com.ite.eap2.core.struts2;

import java.io.Serializable;

import cn.com.ite.eap2.core.service.DefaultRedisService;
import cn.com.ite.eap2.core.service.DefaultService;

/**
 * <p>Title cn.com.ite.eap2.core.struts2.AbstractAction</p>
 * <p>Description 抽象action</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-14 下午03:06:46
 * @version 2.0
 * 
 * @modified records:
 */
public abstract class AbstractAction implements Serializable{
	private static final long serialVersionUID = 2431432981774161717L;
	/**
	 * 服务
	 */
	protected DefaultService service;
	
	
	protected DefaultRedisService redisService;
	/**
	 * 操作信息
	 */
	private String msg;
	/**
	 * 表示提交是否成功
	 */
	private boolean success = true;
	/**
	 * 主健值
	 */
	private String id;
	/**
	 * 工作流中用到的任务实例ID
	 */
	private String excuteId;
	/**
	 * 工作流中用到的导向编码
	 */
	private String toCode;
	/**
	 * 状态
	 */
	private String state;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DefaultService getService() {
		return service;
	}
	public void setService(DefaultService service) {
		this.service = service;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getExcuteId() {
		return excuteId;
	}
	public void setExcuteId(String excuteId) {
		this.excuteId = excuteId;
	}
	public String getToCode() {
		return toCode;
	}
	public void setToCode(String toCode) {
		this.toCode = toCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public DefaultRedisService getRedisService() {
		return redisService;
	}
	public void setRedisService(DefaultRedisService redisService) {
		this.redisService = redisService;
	}
	
}
