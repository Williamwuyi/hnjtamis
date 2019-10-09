package cn.com.ite.hnjtamis.baseinfo.quarterStandard;

import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.quarterStandard.QuarterStandardService</p>
 * <p>Description  标准岗位处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月23日 下午2:04:47
 * @version 1.0
 * 
 * @modified records:
 */
public interface QuarterStandardService extends DefaultService{

	/**
	 * 获取岗位对应的标准有多少
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,String> getQuarterStrandardNum();
}
