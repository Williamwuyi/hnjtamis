package cn.com.ite.hnjtamis.train.online;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

public interface TrainOnlineService extends DefaultService {
	/**
	 * 查询课程信息并构造成树：培训实施-->课件(教程)-->附件
	 * 
	 * @param trainOnlineId
	 *            在线学习表数据ID
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> findCourseTree(String trainOnlineId) throws Exception;

	/**
	 * 查询在学习当前节点应先学习的节点
	 * 
	 * @param trainOnlineId
	 *            在线学习表数据ID
	 * @param currentLeafId
	 *            当前准备学习的节点ID
	 * @return
	 * @throws Exception
	 */
	public List<StudyLeaf> findPreCourseLeaf(String trainOnlineId,
			String currentLeafId) throws Exception;
	
	/**
	 * 查询当前学习的课程下所有节点数
	 * @param trainOnlineId
	 * @return
	 */
	public int getCourseCount(String trainOnlineId) throws Exception;
	
	/**
	 * 查询课程学习记录
	 * @param trainOnlineId
	 * @return
	 * @throws Exception
	 */
	public List<StudyLeaf> findStudyRecords(String trainOnlineId) throws Exception;
}
