package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

/**
 * 岗位对应的标准管理
 * @author 朱健
 * @create time: 2016年3月7日 下午1:31:21
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardExServiceImpl extends DefaultServiceImpl implements
JobUnionStandardExService {
	
	
	/**
	 * 复制新体系
	 * @description
	 * @param sourceQids
	 * @param targetQids
	 * @modified
	 */
	public void saveAndCopyStandard(String[] sourceQids,String[] targetQids){
		JobUnionStandardExDao jobUnionStandardExDao = (JobUnionStandardExDao)this.getDao();
		for(int i=0;i<sourceQids.length;i++){
			String sourceQid = sourceQids[i];
			for(int j=0;j<targetQids.length;j++){
				String targetQid = targetQids[j];
				jobUnionStandardExDao.insertAndCopyJobsStandardQuarter(sourceQid, targetQid);
				jobUnionStandardExDao.deleteJobsStandardQuarterNotInParent(sourceQid, targetQid);
			}
		}
		
		
	}
	
	
	/**
	 * 复制新体系
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void saveAndCopySourceStandard(String sourceQid,String targetQid){
		JobUnionStandardExDao jobUnionStandardExDao = (JobUnionStandardExDao)this.getDao();
		jobUnionStandardExDao.insertAndCopyJobsStandardQuarter(sourceQid, targetQid);
		jobUnionStandardExDao.deleteJobsStandardQuarterNotInParent(sourceQid, targetQid);
	}
	
	/**
	 * 删除标准源于父岗位,并排除父岗位存在notDelParentQuarterIds里面的数据
	 * @description
	 * @param sourceQid
	 * @param targetQid
	 * @modified
	 */
	public void deleteJobsStandardQuarterHaveParentQuarter(String quarterId,String[] notDelParentQuarterIds){
		JobUnionStandardExDao jobUnionStandardExDao = (JobUnionStandardExDao)this.getDao();
		jobUnionStandardExDao.deleteJobsStandardQuarterHaveParentQuarter(quarterId, notDelParentQuarterIds);
	}
	
	
	/**
	 * 处理下级的
	 * @description
	 * @param sourceQid
	 * @modified
	 */
	public void saveChildeJobUnionStandard(String sourceQid){
		Map term = new HashMap();
		term.put("quarterId", sourceQid);
		List<QuarterStandard> childeList = this.queryData("queryQuarterStandardFromParent", term, null);
		if(childeList!=null && childeList.size()>0){
			for(int i=0;i<childeList.size();i++){
				QuarterStandard quarterStandard = childeList.get(i);
				saveAndCopySourceStandard(sourceQid, quarterStandard.getQuarterId());
				saveChildeJobUnionStandard(quarterStandard.getQuarterId());
			}
		}
	}
	
	/**
	 * 更新个人学习题库信息
	 * @description
	 * @param quarterId
	 * @param notDelParentQuarterIds
	 * @modified
	 */
	public void updatePersonalBankLearning(){
		JobUnionStandardExDao jobUnionStandardExDao = (JobUnionStandardExDao)this.getDao();
		jobUnionStandardExDao.updatePersonalBankLearning();
	}
}
