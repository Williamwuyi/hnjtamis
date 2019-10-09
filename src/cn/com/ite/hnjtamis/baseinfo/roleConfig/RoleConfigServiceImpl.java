package cn.com.ite.hnjtamis.baseinfo.roleConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.domain.power.SysUser;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.roleConfig.RoleConfigServiceImpl</p>
 * <p>Description 角色配置管理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:22:55
 * @version 1.0
 * 
 * @modified records:
 */
public class RoleConfigServiceImpl extends DefaultServiceImpl implements RoleConfigService {

	/**
	 * 获取角色配置树
	 * @description
	 * @param configId
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findRoleConfigTree(String configId)throws Exception{
		RoleConfigDao roleConfigDao = (RoleConfigDao)this.getDao();
		return roleConfigDao.findRoleConfigTree(configId);
	}
}
