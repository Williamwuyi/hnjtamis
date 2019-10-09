package cn.com.ite.hnjtamis.jobstandard.jobunionsepciality;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位关联专业信息service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface JobUnionSpecialityService extends DefaultService {
	
	
	
	// 根据岗位ID查询设定的岗位对应专业信息
	public List<JobsUnionSpeciality> findDataByJobId(String jobid);
	
	// 根据岗位ID与专业ID查询岗位对应专业信息
	public JobsUnionSpeciality findDataByJobIdAndSpecialityId(String jobid,String sepecialId);
}
