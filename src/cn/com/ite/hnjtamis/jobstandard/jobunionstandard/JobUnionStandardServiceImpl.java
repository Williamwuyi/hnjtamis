package cn.com.ite.hnjtamis.jobstandard.jobunionstandard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardServiceImpl extends DefaultServiceImpl implements
		JobUnionStandardService {
	
	
	// 根据岗位ID查询设定的岗位标准条款信息
	public List<JobsUnionStandard> findDataByJobId(String jobid){
		List<JobsUnionStandard> list = getDao().findEntityByField(JobsUnionStandard.class, "jobscode", jobid);
		return list;
	}
	
	// 根据条款ID与岗位ID查询数据
	public JobsUnionStandard findDataByJobIdAndTermId(String jobid,String standardtermid){
		JobsUnionStandard u = null;
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("jobscode", jobid);
		term.put("standardid", standardtermid);
		//term.put("notstatus", DicDefine.DATA_DELETE); // 数据删除标记 查询条件: <>
		Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		List<JobsUnionStandard> list = getDao().queryConfigQl("queryJobIdAndTermId", term, sortMap, JobsUnionStandard.class);
		if(list!=null && list.size()>0){
			u = (JobsUnionStandard)list.iterator().next();
		}
		return u;
	}
}
