package cn.com.ite.eap2.module.organization.organ;

import java.io.File;
import java.util.*;
import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Organ;

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
public interface OrganService extends DefaultService{
	/**
	 * 机构部门树的数据提取
	 * @param topOrganId 项级机构ID
	 * @param deptName 部门名称(模糊匹配）
	 * @return 树结点
	 */
	List<TreeNode> findOrganDeptTree(String topOrganId,String deptName)throws Exception;
	/**
	 * 保存实体
	 * @param bo 机构实体
	 */
	void saveOrgan(Organ bo)  throws Exception;
	/**
	 * 保存机构顺序
	 * @param orders 顺序码数组
	 * @throws Exception
	 * @modified
	 */
	void saveSort(String[] orders) throws Exception;
	/**
	 * 导入机构部门岗位人员
	 * @param xls EXCEL文件
	 * @modified
	 */
	void importDate(File xls) throws Exception;
	/**
	 * 导出机构部门岗位人员
	 * @param organid 机构ID
	 * @return
	 * @modified
	 */
	File exportDate(String organId) throws Exception;
	/**
	 * 级联查询机构
	 * @param organId
	 * @return
	 * @modified
	 */
	List<Organ> findCascadeOrgan(String organId);
}
