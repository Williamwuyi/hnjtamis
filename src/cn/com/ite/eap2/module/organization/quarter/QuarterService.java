package cn.com.ite.eap2.module.organization.quarter;

import java.util.*;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganService</p>
 * <p>Description 机构部门服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public interface QuarterService extends DefaultService{
	/**
	 * 查询机构部门岗位树
	 * @param organId 机构ID
	 * @param quarterName 岗位名称
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findOrganDeptQuarterTree(String organId,String quarterName)throws Exception;
}
