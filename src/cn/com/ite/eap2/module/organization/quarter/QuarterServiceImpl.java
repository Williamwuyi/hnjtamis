package cn.com.ite.eap2.module.organization.quarter;

import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.organization.dept.DeptService;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganServiceImpl</p>
 * <p>Description 机构部门服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public class QuarterServiceImpl extends DefaultServiceImpl implements QuarterService {
	private DeptService ds;
	
	public void setDs(DeptService ds) {
		this.ds = ds;
	}

	/**
	 * 查询机构部门岗位树
	 * @param organId 机构ID
	 * @param quarterName 岗位名称
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> findOrganDeptQuarterTree(String organId,String quarterName)throws Exception{
		Map term = new HashMap();
		term.put("nameTerm", quarterName);
		List<Quarter> list = (List<Quarter>)getDao().queryConfigQl("queryHql", term, null, TreeNode.class);
		Map<String,TreeNode> ms = new HashMap<String,TreeNode>();
		for(Quarter quarter:list){
			TreeNode f = new TreeNode();
			f.setType("quarter");
			f.setId(quarter.getQuarterId());
			f.setTitle(quarter.getQuarterName());
			Quarter parent = quarter.getQuarter();
			if(parent==null){
				Dept dept = quarter.getDept();
				f.setParentId(dept.getDeptId());
			}
			if(!ms.containsKey(f.getId()))
			   ms.put(f.getId(), f);
			boolean loop = false;
			//加上级部门数据
			while(parent!=null){
				if(!loop)
				   f.setParentId(parent.getQuarterId());
				TreeNode newNode = TreeNode.objectToTree(parent, "quarterId", "quarter.quarterId", "quarterName");
				newNode.setType("quarter");
				if(!ms.containsKey(newNode.getId())){
					if(StringUtils.isEmpty(newNode.getParentId())){
						Dept dept = parent.getDept();
						newNode.setParentId(dept.getDeptId());
					}
				    ms.put(newNode.getId(), newNode);
				}
				loop=true;
				parent = parent.getQuarter();
			}
		}
		List<TreeNode> trees = new ArrayList<TreeNode>();
		trees.addAll(ms.values());
		trees.addAll(ds.findOrganDeptTree(organId, null));
		TreeNode.putTypeIncon("quarter", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("dept", "resources/icons/fam/grid.png", "");
		List leafs = new ArrayList();
		leafs.add("quarter");
		List res = TreeNode.toTree(trees,true,leafs);
		return res;
	}
}