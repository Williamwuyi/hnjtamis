package cn.com.ite.hnjtamis.kb.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.kb.domain.Courseware;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.course.CoursewareServiceImpl
 * </p>
 * <p>
 * Description 课件库服务实现
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
public class CoursewareServiceImpl extends DefaultServiceImpl implements
		CoursewareService {
	/**
	 * 该ID是否已存在本机构中
	 * 
	 * @param id
	 * @param organId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isExist(String originalCourseId, String organId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", originalCourseId);
		param.put("organId", organId);
		List<Courseware> list = getDao().queryConfigQl("queryByIdAndOrganHql",
				param, null, Courseware.class);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}
}
