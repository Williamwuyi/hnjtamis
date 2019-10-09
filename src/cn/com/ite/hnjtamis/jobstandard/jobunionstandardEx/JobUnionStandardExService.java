package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;


import cn.com.ite.eap2.core.service.DefaultService;

/**
 * 岗位对应的标准管理
 * @author 朱健
 * @create time: 2016年3月7日 下午1:31:17
 * @version 1.0
 * 
 * @modified records:
 */
public interface JobUnionStandardExService extends DefaultService {
	
	/**
	 * 复制新体系
	 * @description
	 * @param sourceQids
	 * @param targetQids
	 * @modified
	 */
	public void saveAndCopyStandard(String[] sourceQids,String[] targetQids);
	

	/**
	 * 复制新体系
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void saveAndCopySourceStandard(String sourceQid,String targetQid);
	
	/**
	 * 删除标准源于父岗位,并排除父岗位存在notDelParentQuarterIds里面的数据
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void deleteJobsStandardQuarterHaveParentQuarter(String quarterId,String[] notDelParentQuarterIds);
	
	/**
	 * 处理下级的
	 * @description
	 * @param sourceQid
	 * @modified
	 */
	public void saveChildeJobUnionStandard(String sourceQid);
	
	/**
	 * 更新个人学习题库信息
	 * @description
	 * @param quarterId
	 * @param notDelParentQuarterIds
	 * @modified
	 */
	public void updatePersonalBankLearning();
}
