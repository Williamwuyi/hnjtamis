package cn.com.ite.hnjtamis.jobstandard.promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;
import cn.com.ite.hnjtamis.jobstandard.domain.Promotion;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位晋升通道信息接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 31, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PromotionServiceImpl extends DefaultServiceImpl implements
		PromotionService {
	// 根据岗位ID查询设定的岗位标准条款信息
	public List<Promotion> findDataByJobId(String jobid){
		List<Promotion> list = getDao().findEntityByField(Promotion.class, "jobscode", jobid);
		return list;
	}
	
	/// 根据本岗位ID与晋升岗位ID查询唯一的晋升通道信息
	public Promotion findDataByJobIdAndPromotionJobId(String jobid,String promotionJobid){
		Promotion m = null;
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("jobscode", jobid);
		term.put("parentjobscode", promotionJobid);
		//term.put("notstatus", DicDefine.DATA_DELETE); // 数据删除标记 查询条件: <>
		Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		List<Promotion> list = getDao().queryConfigQl("queryJobIdAndPromotionJobId", term, sortMap, Promotion.class);
		if(list!=null && list.size()>0){
			m = (Promotion)list.iterator().next();
		}
		return m;
	}
}
