package cn.com.ite.eap2.module.power.user;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>Title cn.com.ite.eap2.module.power.user.UserService</p>
 * <p>Description 用户服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-9 上午09:30:04
 * @version 2.0
 * 
 * @modified records:
 */
public interface UserService extends DefaultService{
	/**
     * 查询用户资源
     * @param userId 用户ID
     * @return 树结构
     * @modified
     */
    List<TreeNode> findUserResource(String userId);
    /**
     * 查询机构部门用户树
     * @param topOrganId
     * @param nameTerm
     * @return
     * @modified
     */
    List<TreeNode> findUserTree(String topOrganId,String nameTerm) throws Exception;
}
