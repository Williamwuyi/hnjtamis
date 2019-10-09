package cn.com.ite.hnjtamis.jobstandard.promotion;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.jobstandard.domain.Promotion;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位晋升通道service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 31, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface PromotionService extends DefaultService {
	
	
	
	// 根据岗位ID查询设定的岗位标准条款信息
	public List<Promotion> findDataByJobId(String jobid);
	
	/// 根据本岗位ID与晋升岗位ID查询唯一的晋升通道信息
	public Promotion findDataByJobIdAndPromotionJobId(String jobid,String promotionJobid);
}
