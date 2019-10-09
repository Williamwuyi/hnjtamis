package cn.com.ite.hnjtamis.exam.base.exampublicuser.service;

import java.io.File;
import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;

public interface ExamPublicUserService extends DefaultService{
	public File exportDate(String examPublicId) throws Exception;
	public List<String> importDate(File xls)throws Exception;
	public void updateExamUser();
	
	/**
	 * 保存状态
	 * @author 朱健
	 * @param updateIds
	 * @return
	 * @modified
	 */
	public int savePublicUserState(String updateIds);
}
