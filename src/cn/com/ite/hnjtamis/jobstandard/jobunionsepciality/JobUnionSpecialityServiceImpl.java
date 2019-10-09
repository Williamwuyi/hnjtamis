package cn.com.ite.hnjtamis.jobstandard.jobunionsepciality;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位关联专业信息接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionSpecialityServiceImpl extends DefaultServiceImpl implements
		JobUnionSpecialityService {
	// 根据岗位ID查询设定的岗位标准条款信息
	public List<JobsUnionSpeciality> findDataByJobId(String jobid){
		List<JobsUnionSpeciality> list = getDao().findEntityByField(JobsUnionSpeciality.class, "jobscode", jobid);
		return list;
	}
	
	// 根据岗位ID与专业ID查询岗位对应专业信息
	public JobsUnionSpeciality findDataByJobIdAndSpecialityId(String jobid,String sepecialId){
		JobsUnionSpeciality m = null;
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("jobscode", jobid);
		term.put("specialityid", sepecialId);
		//term.put("notstatus", DicDefine.DATA_DELETE); // 数据删除标记 查询条件: <>
		Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		List<JobsUnionSpeciality> list = getDao().queryConfigQl("queryByJobAndSepcialHql", term, sortMap, JobsUnionSpeciality.class);
		if(list!=null && list.size()>0){
			m = (JobsUnionSpeciality)list.iterator().next();
		}
		return m;
	}
}
