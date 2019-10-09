package cn.com.ite.eap2.module.baseinfo.affiche;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;

public interface AfficheService extends DefaultService{
	/**
	 * 保存公告
	 * @param sa
	 * @param excuteId 任务实例ID
	 * @param toCode 导向编码
	 * @param employee 当前员工
	 * @param advice 意见
	 */
	void saveAffiche(SysAffiche sa,String excuteId,String toCode,String employeeId,String advice) throws Exception;
	/**
	 * 删除系统公告
	 * @param sa
	 * @throws Exception
	 * @modified
	 */
	void deleteAffiche(SysAffiche sa) throws Exception;
}
