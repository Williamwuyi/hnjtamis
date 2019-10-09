package cn.com.ite.hnjtamis.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.param.domain.SystemParams;

public class SystemParamsServiceImpl extends DefaultServiceImpl implements
		SystemParamsService {

	/**
	 * 根据参数类型查询
	 * 
	 * @param sort
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SystemParams> findBySort(String sort) {
		List<SystemParams> list = new ArrayList<SystemParams>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("sortTerm", sort);
		list = getDao().queryConfigQl("queryHql", param, null,
				SystemParams.class);
		return list;
	}

	/**
	 * 根据参数编码查询
	 * 
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SystemParams findByCode(String code) {
		List<SystemParams> list = new ArrayList<SystemParams>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		list = getDao().queryConfigQl("queryByCode", param, null,
				SystemParams.class);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

}
