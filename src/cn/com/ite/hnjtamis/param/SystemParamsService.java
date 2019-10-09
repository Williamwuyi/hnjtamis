package cn.com.ite.hnjtamis.param;

import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.param.domain.SystemParams;

public interface SystemParamsService extends DefaultService {
	/**
	 * 根据参数类型查询
	 * @param sort
	 * @return
	 */
	public List<SystemParams> findBySort(String sort);
	
	/**
	 * 根据参数编码查询
	 * @param code
	 * @return
	 */
	public SystemParams findByCode(String code);
}
