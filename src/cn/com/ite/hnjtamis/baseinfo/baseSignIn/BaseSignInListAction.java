package cn.com.ite.hnjtamis.baseinfo.baseSignIn;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.BaseSignIn;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInListAction</p>
 * <p>Description  签到</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:15:30
 * @version 1.0
 * 
 * @modified records:
 */
public class BaseSignInListAction extends AbstractListAction implements ServletRequestAware{

	private static final long serialVersionUID = -4136995508104343932L;
	
	private HttpServletRequest request;
	
	private int signInFlag;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	public String querySignIn(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		String nowDay = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		String employeeId = usersess.getEmployeeId() == null ? usersess.getAccount() : usersess.getEmployeeId();
		String account = usersess.getAccount();
		BaseSignInService baseSignInService =(BaseSignInService)this.getService();
		signInFlag = baseSignInService.getSignInDay(employeeId, account, nowDay);
		return "querySignIn";
	}


	public int getSignInFlag() {
		return signInFlag;
	}


	public void setSignInFlag(int signInFlag) {
		this.signInFlag = signInFlag;
	}
	
	

}
