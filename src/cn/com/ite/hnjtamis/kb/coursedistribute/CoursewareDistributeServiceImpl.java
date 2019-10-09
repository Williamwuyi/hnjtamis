package cn.com.ite.hnjtamis.kb.coursedistribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.kb.domain.CoursewareDistribute;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.course.CoursewareService
 * </p>
 * <p>
 * Description 课件库分配服务实现
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-24
 * @version 1.0
 * 
 * @modified
 */
public class CoursewareDistributeServiceImpl extends DefaultServiceImpl
		implements CoursewareDistributeService {
	/**
	 * 根据岗位专业构建课件分配树
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findDistributeTree(String organId, String selectIds)
			throws Exception {
		List<TreeNode> list = new ArrayList<TreeNode>();
		Map<String, TreeNode> ms = new HashMap<String, TreeNode>();
		List<TreeNode> qslist = new ArrayList<TreeNode>();
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("organId", organId);
			qslist = (List<TreeNode>) getDao().queryConfigQl(
					"queryQuarterAndSpecialityHql", param, null, TreeNode.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (TreeNode node : qslist) {
			node.setType("qs");
			if (!ms.containsKey(node.getId())) {
				ms.put(node.getId(), node);
			}
			// 添加具体的课件节点
			Map<String, String> term = new HashMap<String, String>();
			String[] qsid = node.getId().split(",");
			term.put("quarterId", qsid[0]);
			term.put("specialityid", qsid[1]);
			term.put("organId", organId);
			List<CoursewareDistribute> courseList = (List<CoursewareDistribute>) getDao()
					.queryConfigQl("queryByQuarterAndSepcialityHql", term,
							null, CoursewareDistribute.class);
			for (CoursewareDistribute course : courseList) {
				TreeNode newNode = new TreeNode();
				newNode.setId(course.getId());
				newNode.setTitle(course.getCourseware().getTitle());
				newNode.setParentId(node.getId());
				newNode.setType("course");
				if (selectIds.indexOf(course.getId()) != -1) {
					newNode.setChecked(true);
				}
				if (!ms.containsKey(course.getId())) {
					ms.put(course.getId(), newNode);
				}
			}
		}
		list.clear();
		list.addAll(ms.values());

		TreeNode.putTypeIncon("qs", "resources/icons/fam/cog.gif", "");
		TreeNode.putTypeIncon("course", "resources/icons/fam/grid.png", "");
		List<String> leafTypes = new ArrayList<String>();
		leafTypes.add("qs");
		leafTypes.add("course");
		return TreeNode.toTree(list, true, leafTypes);
	}
}
