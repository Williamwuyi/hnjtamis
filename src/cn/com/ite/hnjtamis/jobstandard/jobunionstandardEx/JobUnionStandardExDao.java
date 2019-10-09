package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;

/**
 * 岗位对应的标准管理
 * @author 朱健
 * @create time: 2016年3月7日 下午1:30:53
 * @version 1.0
 * 
 * @modified records:
 */
public interface JobUnionStandardExDao extends DefaultDAO{
	
	/**
	 * 复制并插入新的标准
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void insertAndCopyJobsStandardQuarter(String sourceQid,String targetQid);
	
	/**
	 * 删除标准源于父岗位不存在的
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void deleteJobsStandardQuarterNotInParent(String sourceQid,String targetQid);
	
	
	/**
	 * 删除标准源于父岗位,并排除父岗位存在notDelParentQuarterIds里面的数据
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void deleteJobsStandardQuarterHaveParentQuarter(String quarterId,String[] notDelParentQuarterIds);
	
	/**
	 * 更新个人学习题库信息
	 * @description
	 * @param quarterId
	 * @param notDelParentQuarterIds
	 * @modified
	 */
	public void updatePersonalBankLearning();
}
