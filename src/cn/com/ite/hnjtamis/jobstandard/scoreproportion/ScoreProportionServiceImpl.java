package cn.com.ite.hnjtamis.jobstandard.scoreproportion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.ScoreProportion;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
import cn.com.ite.hnjtamis.jobstandard.jobunionstandard.JobUnionStandardService;
/**
 * 
 * <p>Title 岗位达标培训信息系统-岗位标准设定模块</p>
 * <p>岗位标准得分比例设定 Service接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  6:12:48 PM
 * @version 1.0
 * 
 * @modified records:
 */
public class ScoreProportionServiceImpl extends DefaultServiceImpl implements
		ScoreProportionService {
	/**
	 * 根据岗位ID查询已配置的有效信息
	 * @param jobscode
	 * @return
	 */
	public ScoreProportion findDataByJobscode(String jobscode){
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("jobscode", jobscode);
		List list = queryData("queryDataByJobscode", term, null);
		ScoreProportion vo = null;
		if (list!=null && list.size()>0){
			vo=(ScoreProportion)list.iterator().next();
		}
		return vo;
	}
}
