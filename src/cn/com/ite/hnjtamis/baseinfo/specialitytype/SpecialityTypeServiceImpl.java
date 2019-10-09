package cn.com.ite.hnjtamis.baseinfo.specialitytype;

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
public class SpecialityTypeServiceImpl extends DefaultServiceImpl implements
		SpecialityTypeService {
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findSpecialTypeTree(String topTypeId,String typeName)throws Exception{
		Map term = new HashMap();
		term.put("nameTerm", typeName);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new LinkedHashMap<String,TreeNode>();
		
		for(TreeNode node:list){
			TreeNode f = node;
			if(!ms.containsKey(f.getId())) ms.put(f.getId(), f);
			String organId = node.getTagName();
			if(StringUtils.isEmpty(f.getParentId())) f.setParentId(organId);
			//数据
			while(!StringUtils.isEmpty(f.getParentId())){
				SpecialityType special = (SpecialityType)getDao().findEntityBykey(SpecialityType.class, f.getParentId());
				if(special!=null){
					TreeNode newNode = TreeNode.objectToTree(special, "bstid", "parentspeciltype.bstid", "typename");
					newNode.setType("specialitytype");
					newNode.setOrderNo(special.getOrderno());
					if(!ms.containsKey(newNode.getId()))
					  ms.put(newNode.getId(), newNode);
					f = newNode;
					if(StringUtils.isEmpty(newNode.getParentId())) newNode.setParentId(organId);
				}else break;
			}
			node.setType("specialitytype");
			//boolean top = false;
			
			//if(top) break;
		}
		list.clear();
		
		java.util.Collections.sort(list, new Comparator(){
			
			public int compare(Object o1, Object o2) {
				TreeNode t1 = (TreeNode)o1;
				TreeNode t2 = (TreeNode)o2;
				if(t1.getOrderNo()==null||t2.getOrderNo()==null)
					return 0;
				return t1.getOrderNo().compareTo(t2.getOrderNo());
			}
		});
		List depts = new ArrayList();
		depts.addAll(ms.values());
		java.util.Collections.sort(depts, new Comparator(){
			
			public int compare(Object o1, Object o2) {
				TreeNode t1 = (TreeNode)o1;
				TreeNode t2 = (TreeNode)o2;
				if(t1.getOrderNo()==null||t2.getOrderNo()==null)
					return 0;
				return t1.getOrderNo().compareTo(t2.getOrderNo());
			}
		});
		list.addAll(depts);		
		TreeNode.putTypeIncon("specialitytype", "resources/icons/fam/plugin_add.gif", "");
		TreeNode.putTypeIncon("specialitytype", "resources/icons/fam/plugin_add.gif", "");
		return TreeNode.toTree(list,true,null,topTypeId);
	}
}
