package cn.com.ite.hnjtamis.jobstandard.termstype;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业类型service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface StandardTypeService extends DefaultService {
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findStandardTypeTree(String topTypeId,String typeName)throws Exception;
	
	/**
	 * 更新父名称
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void updateParentTypeName();
}
