package cn.com.ite.hnjtamis.kb.course;

import cn.com.ite.eap2.core.service.DefaultService;

/**
 * <p>Title cn.com.ite.hnjtamis.course.CoursewareService</p>
 * <p>Description 课件库服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 李奉学
 * @create time 2015-3-24
 * @version 1.0
 * 
 * @modified 
 */
public interface CoursewareService extends DefaultService {
	/**
	 * 该ID是否已存在本机构中
	 * @param originalCourseId
	 * @param organId
	 * @return
	 */
	public boolean isExist(String originalCourseId, String organId);
}
