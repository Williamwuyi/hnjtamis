package cn.com.ite.hnjtamis.jobstandard.termstype;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityType;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业类型接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTypeServiceImpl extends DefaultServiceImpl implements
		StandardTypeService {
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findStandardTypeTree(String topTypeId,String typeName)throws Exception{
		Map term = new HashMap();
		term.put("nameTerm", typeName);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", term, null, TreeNode.class);
//		Map<String,TreeNode> ms = new LinkedHashMap<String,TreeNode>();
		
		
		TreeNode.putTypeIncon("standardtype", "resources/icons/fam/dept.gif", "");
		TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		return TreeNode.toTree(list,true,null,topTypeId);
	}
	
	/**
	 * 更新父名称
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void updateParentTypeName(){
		this.getDao().excuteQl("updateParentName", null);
	}
}
