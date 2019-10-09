package cn.com.ite.hnjtamis.jobstandard.scoreproportion;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.jobstandard.domain.ScoreProportion;
/**
 * 
 * <p>Title 岗位达标培训信息系统-岗位标准设定模块</p>
 * <p>岗位标准得分比例设定 Service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  6:12:48 PM
 * @version 1.0
 * 
 * @modified records:
 */
public interface ScoreProportionService extends DefaultService {
	/**
	 * 根据岗位ID查询已配置的有效信息
	 * @param jobscode
	 * @return
	 */
	public ScoreProportion findDataByJobscode(String jobscode);
}
