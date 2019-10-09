package cn.com.ite.hnjtamis.train.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;

public class TrainOnlineServiceImpl extends DefaultServiceImpl implements
		TrainOnlineService {
	/**
	 * 查询课程信息并构造成树：培训实施-->课件(教程)-->附件
	 * 
	 * @param trainOnlineId
	 *            在线学习表数据ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findCourseTree(String trainOnlineId) throws Exception {
		List<TreeNode> list = new ArrayList<TreeNode>();
		Map<String, TreeNode> ms = new LinkedHashMap<String, TreeNode>();
		Map<String, String> param = new HashMap<String, String>();

		// 查询数据
		param.put("trainOnlineId", trainOnlineId);
		List<Map> qlist = new ArrayList<Map>();
		try {
			qlist = getDao()
					.queryConfigQl("queryCourseTree", param, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 构造树形结构
		try {
			for (Map map : qlist) {
				// 课程
				TreeNode implement;
				if (!ms.containsKey(map.get("IMPL_ID").toString())) {
					implement = new TreeNode();
					implement.setId(map.get("IMPL_ID").toString());
					implement.setTitle(map.get("SUBJECT").toString());
					implement.setType("impl");
					ms.put(implement.getId(), implement);
				} else {
					implement = ms.get(map.get("IMPL_ID").toString());
				}

				// 课件、教材
				TreeNode course;
				if (!ms.containsKey(map.get("COURSE_ID").toString())) {
					course = new TreeNode();
					course.setId(map.get("COURSE_ID").toString());
					course.setTitle(map.get("COURSE_TITLE").toString());
					course.setParentId(implement.getId());
					course.setType("course");
					ms.put(course.getId(), course);
				} else {
					course = ms.get(map.get("COURSE_ID").toString());
				}

				// 附件
				TreeNode acc = new TreeNode();
				acc.setId(map.get("ACCE_ID").toString());
				String fileName = map.get("FILE_NAME").toString();
				int pos = fileName.lastIndexOf(".");
				acc.setTagName(fileName.substring(pos + 1).toLowerCase());
				fileName = fileName.substring(0, pos);
				acc.setTitle(fileName);
				acc.setQtip(fileName);
				acc.setParentId(course.getId());
				acc.setType("acc");
				acc.setUrl(map.get("FILE_PATH").toString().replaceAll("\\\\",
						"/"));
				pos = acc.getUrl().lastIndexOf("/");
				acc.setTemp(acc.getUrl().substring(0, pos));
				if (!ms.containsKey(acc.getId())) {
					ms.put(acc.getId(), acc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> leafTypes = new ArrayList<String>();
		list.addAll(ms.values());
		TreeNode.putTypeIncon("acc", "resources/icons/fam/book.png", "");
		TreeNode.putTypeIncon("course", "resources/icons/fam/grid.png", "");
		TreeNode.putTypeIncon("impl", "resources/icons/fam/theme.png", "");
		leafTypes.add("impl");
		leafTypes.add("course");
		leafTypes.add("acc");
		return TreeNode.toTree(list, true, leafTypes);
	}

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
	@SuppressWarnings("unchecked")
	public List<StudyLeaf> findPreCourseLeaf(String trainOnlineId,
			String currentLeafId) throws Exception {
		List<StudyLeaf> list = new ArrayList<StudyLeaf>();
		Map<String, String> param = new HashMap<String, String>();
		// 查询数据
		param.put("trainOnlineId", trainOnlineId);
		List<Map> qlist = new ArrayList<Map>();
		try {
			qlist = getDao().queryConfigQl("queryPreLeaf", param, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (Map map : qlist) {
				String leafId = map.get("ACCE_ID").toString();
				// 遍历到当前节点为止，因为只需判断前提节点是否已经完成
				if (leafId.equals(currentLeafId))
					break;

				String duration = map.get("DURATION") == null ? "" : map.get(
						"DURATION").toString();
				String needDuration = map.get("NEED_DURATION") == null ? ""
						: map.get("NEED_DURATION").toString();
				// 如果没有对应的学习记录或者学习时长未达到应学时长则添加到返回列表中
				if (StringUtils.isEmpty(duration)
						|| Integer.parseInt(duration) < Integer
								.parseInt(needDuration)) {
					StudyLeaf leaf = new StudyLeaf();
					String fileName = map.get("FILE_NAME").toString();
					int pos = fileName.lastIndexOf(".");
					fileName = fileName.substring(0, pos);
					leaf.setFileName(fileName);
					list.add(leaf);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 查询当前学习的课程下所有节点数
	 * 
	 * @param trainOnlineId
	 * @return
	 */
	public int getCourseCount(String trainOnlineId) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("trainOnlineId", trainOnlineId);

		return getDao().countConfigQl("queryCourseTree", param);
	}

	/**
	 * 查询课程学习记录
	 * 
	 * @param trainOnlineId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<StudyLeaf> findStudyRecords(String trainOnlineId)
			throws Exception {
		String extension = "doc|pdf|ppt|excel|swf";// 文档类型扩展名
		List<StudyLeaf> list = new ArrayList<StudyLeaf>();
		Map<String, StudyLeaf> ms = new LinkedHashMap<String, StudyLeaf>();
		Map<String, String> param = new HashMap<String, String>();
		// 查询数据
		param.put("trainOnlineId", trainOnlineId);
		List<Map> qlist = new ArrayList<Map>();
		try {
			qlist = getDao().queryConfigQl("queryStudyRecord", param, null,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (Map map : qlist) {
				// 课程
				StudyLeaf implement;
				if (!ms.containsKey(map.get("IMPL_ID").toString())) {
					implement = new StudyLeaf();
					implement.setId(map.get("IMPL_ID").toString());
					implement.setFileName(map.get("SUBJECT").toString());
					implement.setLevel(0);
					ms.put("impl_" + implement.getId(), implement);
				} else {
					implement = ms.get("impl_" + map.get("IMPL_ID").toString());
				}

				// 课件、教材
				StudyLeaf course;
				if (!ms.containsKey(map.get("COURSE_ID").toString())) {
					course = new StudyLeaf();
					course.setId(map.get("COURSE_ID").toString());
					course.setFileName(map.get("COURSE_TITLE").toString());
					course.setIsRequired(Integer.valueOf(map.get("IS_REQUIRED")
							.toString()));
					course.setLevel(1);
					ms.put("course_" + course.getId(), course);
				} else {
					course = ms
							.get("course_" + map.get("COURSE_ID").toString());
				}

				// 附件
				StudyLeaf acc = new StudyLeaf();
				acc.setId(map.get("ACCE_ID").toString());
				String fileName = map.get("FILE_NAME").toString();
				int pos = fileName.lastIndexOf(".");
				if (extension.indexOf(fileName.substring(pos + 1)) != -1) {
					acc.setUnit("页");
				} else {
					acc.setUnit("分钟");
				}
				fileName = fileName.substring(0, pos);
				acc.setFileName(fileName);
				acc.setLevel(2);
				acc.setStartTime(map.get("START_TIME") == null ? "" : map.get(
						"START_TIME").toString());
				acc.setEndTime(map.get("END_TIME") == null ? "" : map.get(
						"END_TIME").toString());
				acc.setDuration(map.get("DURATION") == null ? 0 : Integer
						.parseInt(map.get("DURATION").toString()));
				acc
						.setNeedDuration(map.get("NEED_DURATION") == null ? 0
								: Integer.parseInt(map.get("NEED_DURATION")
										.toString()));
				acc.setStudyCount(map.get("STUDY_COUNT") == null ? 0 : Integer
						.parseInt(map.get("STUDY_COUNT").toString()));
				acc.setFinishStatus(map.get("FINISH_STATUS") == null ? null
						: Integer.valueOf(map.get("FINISH_STATUS").toString()));
				if (!ms.containsKey("acc_" + acc.getId())) {
					ms.put("acc_" + acc.getId(), acc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		list.addAll(ms.values());
		return list;
	}
}
