package cn.com.ite.hnjtamis.baseinfo.speciality;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.speciality.SpecialityDao</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年12月7日 下午3:56:15
 * @version 1.0
 * 
 * @modified records:
 */
public interface SpecialityDao extends DefaultDAO{

	/**
	 * 同步岗位对应专业
	 * @description
	 * @modified
	 */
	public void saveSyncJobsUnionSpeciality()throws Exception;
	
	
}
