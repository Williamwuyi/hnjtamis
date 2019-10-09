package cn.com.ite.hnjtamis.baseinfo.baseSignIn;

import cn.com.ite.eap2.core.service.DefaultService;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInService</p>
 * <p>Description  签到</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:15:38
 * @version 1.0
 * 
 * @modified records:
 */
public interface BaseSignInService extends DefaultService{


	/**
	 * 获取人员的签到记录量
	 * @description
	 * @param employeeId
	 * @param account
	 * @param day
	 * @return
	 * @modified
	 */
	public int getSignInDay(String employeeId,String account,String day);
	
	/**
	 * 获取某用户的签到天数
	 * @description
	 * @param employeeId
	 * @param account
	 * @return
	 * @modified
	 */
	public int getSignInCount(String employeeId,String account);
}
