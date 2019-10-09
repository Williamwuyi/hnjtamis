package cn.com.ite.hnjtamis.jobstandard.jobunionstandard;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface JobUnionStandardService extends DefaultService {
	
	
	// 根据岗位ID查询设定的岗位标准条款信息
	public List<JobsUnionStandard> findDataByJobId(String jobid);
	
	// 根据条款ID与岗位ID查询数据
	public JobsUnionStandard findDataByJobIdAndTermId(String jobid,String standardtermid);
}
