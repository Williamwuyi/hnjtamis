package cn.com.ite.hnjtamis.baseinfo.baseSignIn;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.BaseSignIn;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInFormAction</p>
 * <p>Description  签到</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:15:20
 * @version 1.0
 * 
 * @modified records:
 */
public class BaseSignInFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = -1295916553729990996L;
	
	private HttpServletRequest request;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	public String save(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		String nowDay = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		String employeeId = usersess.getEmployeeId() == null ? usersess.getAccount() : usersess.getEmployeeId();
		String account = usersess.getAccount();
		BaseSignInService baseSignInService =(BaseSignInService)this.getService();
		int signNum = baseSignInService.getSignInDay(employeeId, account, nowDay);
		if(signNum == 0){
			BaseSignIn baseSignIn = new BaseSignIn();
			baseSignIn.setOrganId(usersess.getOrganId() == null ? null : usersess.getOrganId());
			baseSignIn.setOrganName(usersess.getOrganName() == null ? null : usersess.getOrganName());;
			baseSignIn.setDeptId(usersess.getDeptId() == null ? null : usersess.getDeptId());
			baseSignIn.setDeptName(usersess.getDeptName() == null ? null : usersess.getDeptName());
			baseSignIn.setQuarterId(usersess.getQuarterId() == null ? null : usersess.getQuarterId());
			baseSignIn.setQuarterName(usersess.getQuarterName() == null ? null : usersess.getQuarterName());
			baseSignIn.setEmployeeId(employeeId);
			baseSignIn.setEmployeeName(usersess.getEmployeeName() == null ? null : usersess.getEmployeeName());
			baseSignIn.setUserName(usersess.getAccount());;
			baseSignIn.setUserId(usersess.getUserId());;
			baseSignIn.setSignInDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			try {
				baseSignInService.add(baseSignIn);
				this.setMsg("已签到！");
			} catch (EapException e) {
				this.setMsg("签到失败！");
				e.printStackTrace();
			}
		}else{
			this.setMsg("您今天已经签到！");
		}
		return "save";
	}
	
}