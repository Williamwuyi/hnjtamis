package cn.com.ite.hnjtamis.train.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;

public class TrainImplementServiceImpl extends DefaultServiceImpl implements
		TrainImplementService {
	/**
	 * 查询员工个人应学但未开始学习的课程
	 * 
	 * @param quarterId 岗位ID
	 * @param studentId 学员ID，采用员工ID
	 * @return
	 */
	public List<TrainImplement> queryEmployeerCourse(String quarterId,
			String studentId) {
		Map<String, String> term = new HashMap<String, String>();
		term.put("quarterTerm", quarterId);
		term.put("studentTerm", studentId);

		@SuppressWarnings("unchecked")
		List<TrainImplement> list = getDao().queryConfigQl(
				"queryEmployeeCourseHql", term, null, TrainImplement.class);
		return list;
	}
	
	/**
	 * 查询岗位应学课程
	 * @param quarterId 岗位ID
	 * @return
	 */
	public List<TrainImplement> queryQuarterCourse(String quarterId) {
		Map<String, String> term = new HashMap<String, String>();
		term.put("quarterTerm", quarterId);

		@SuppressWarnings("unchecked")
		List<TrainImplement> list = getDao().queryConfigQl(
				"queryQuarterCourseHql", term, null, TrainImplement.class);
		return list;
	}
	
	/**
	 * 根据题库列表构造题库树
	 * @param themeList
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> constructThemeTree(List<ThemeBank> themeList, String implId) throws Exception {
		List<TreeNode> list = new ArrayList<TreeNode>();
		//查询培训实施情况
		TrainImplement impl = (TrainImplement) getDao().findEntityBykey(TrainImplement.class, implId);
		for (ThemeBank theme : themeList) {
			TreeNode node = new TreeNode();
			node.setId(theme.getThemeBankId());
			node.setTitle(theme.getThemeBankName());
			node.setType("theme");
			if (theme.getThemeBank() != null) 
				node.setParentId(theme.getThemeBank().getThemeBankId());
			
			//验证是否已经指定该题库，已指定则设置选择
			for (ThemeBank t : impl.getThemeBanks()) {
				if (t.getThemeBankId().equals(node.getId())) {
					node.setChecked(true);
				}
			}
			list.add(node);
		}
		TreeNode.putTypeIncon("theme", "resources/icons/fam/book.png", "");
		List<String> leafTypes = new ArrayList<String>();
		leafTypes.add("theme");
		return TreeNode.toTree(list, true, leafTypes);
	}
}
