package cn.com.ite.hnjtamis.baseinfo.quarterStandard;

import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.quarterStandard.QuarterStandardServiceImpl</p>
 * <p>Description 标准岗位处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月23日 下午2:04:43
 * @version 1.0
 * 
 * @modified records:
 */
public class QuarterStandardServiceImpl extends DefaultServiceImpl implements QuarterStandardService {

	/**
	 * 获取岗位对应的标准有多少
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,String> getQuarterStrandardNum(){
		QuarterStandardDao quarterStandardDao = (QuarterStandardDao)this.getDao();
		return quarterStandardDao.getQuarterStrandardNum();
	}
}
