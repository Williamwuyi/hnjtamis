package cn.com.ite.hnjtamis.kb.coursedistribute;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.course.CoursewareService
 * </p>
 * <p>
 * Description 课件库分配服务接口
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
public interface CoursewareDistributeService extends DefaultService {
	/**
	 * 根据岗位专业构建课件分配树
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> findDistributeTree(String organId, String selectIds) throws Exception;
}
