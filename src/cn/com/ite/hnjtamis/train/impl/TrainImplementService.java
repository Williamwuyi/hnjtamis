package cn.com.ite.hnjtamis.train.impl;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;

public interface TrainImplementService extends DefaultService {
	/**
	 * 查询员工个人应学但未开始学习的课程
	 * 
	 * @param quarterId 岗位ID
	 * @param studentId 学员ID，采用员工ID
	 * @return
	 */
	public List<TrainImplement> queryEmployeerCourse(String quarterId, String studentId);
	
	/**
	 * 查询岗位应学课程
	 * @param quarterId 岗位ID
	 * @return
	 */
	public List<TrainImplement> queryQuarterCourse(String quarterId);
	
	/**
	 * 根据题库列表构造题库树
	 * @param themeList
	 * @param implId 培训实施表ID
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> constructThemeTree(List<ThemeBank> themeList, String implId) throws Exception;
}
