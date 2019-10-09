package cn.com.ite.eap2.module.power.login;

import java.util.*;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.domain.power.SysUser;

/**
 * <p>Title cn.com.ite.eap2.module.power.login.LoginService</p>
 * <p>Description 登录服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-14 上午09:39:14
 * @version 2.0
 * 
 * @modified records:
 */
public interface LoginService extends DefaultService{
	/**
	 * 判断账号和密码
	 * @param account 账号
	 * @param password 密码
	 * @return 正确返回用户对象,否则返回空
	 * @modified
	 */
	SysUser judgeUserPass(String account,String password);
	SysUser judgeUserPassForEncrypt(String account,String password);
	/**
	 * 获得被代理的用户
	 * @param userId 用户ID
	 * @return
	 * @modified
	 */
	List<SysUser> getByProxyUser(String userId);
	/**
	 * 获取SESSION信息
	 * @param user 用户
	 * @param proxy 是否代理模式
	 * @param appId 系统ID
	 * @return
	 * @modified
	 */
    UserSession getSession(SysUser user,boolean proxy,String appId);
    /**
     * 查询主菜单
     * @param user 用户,代理模式下对应代理用户
     * @param proxy 是否代理模式
     * @param appId 系统ID
     * @return 树结构
     * @modified
     */
    List<TreeNode> findMainMenu(SysUser user,boolean proxy,String appId);
    /**
     * 获取系统切换下拉数据
     * @param userId
     * @return
     * @modified
     */
    List<AppSystem> getAppList(String userId);
}
