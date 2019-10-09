package cn.com.ite.eap2.domain.power;

import cn.com.ite.eap2.domain.funres.AppSystem;

/**
 * <p>Title cn.com.ite.eap2.domain.organization.UserMapp</p>
 * <p>Description 用户映射</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-2 下午04:11:58
 * @version 2.0
 * 
 * @modified records:
 */
public class UserMapp implements java.io.Serializable {
	private static final long serialVersionUID = -3921725108680941228L;
	private String umId;
	private SysUser sysUser;
	private AppSystem app;
	private String mppAccount;
	private String mppPassword;

	/** default constructor */
	public UserMapp() {
	}

	public String getUmId() {
		return umId;
	}

	public void setUmId(String umId) {
		this.umId = umId;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public AppSystem getApp() {
		return app;
	}

	public void setApp(AppSystem app) {
		this.app = app;
	}

	public String getMppAccount() {
		return mppAccount;
	}

	public void setMppAccount(String mppAccount) {
		this.mppAccount = mppAccount;
	}

	public String getMppPassword() {
		return mppPassword;
	}

	public void setMppPassword(String mppPassword) {
		this.mppPassword = mppPassword;
	}
}