package cn.com.ite.hnjtamis.baseinfo.quarterStandard;

import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.quarterStandard.QuarterStandardDao</p>
 * <p>Description  标准岗位处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月23日 下午2:05:15
 * @version 1.0
 * 
 * @modified records:
 */
public interface QuarterStandardDao extends DefaultDAO{

	/**
	 * 获取岗位对应的标准有多少
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,String> getQuarterStrandardNum();
}
