package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.speciality.SpecialityService;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

/**
 * 岗位对应的标准管理
 * @author 朱健
 * @create time: 2016年3月7日 下午1:31:11
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardExListAction extends AbstractListAction {

	private static final long serialVersionUID = 8467133642253697201L;

	private String quarterId;
	
	private List<JobsUnionSpeciality> specialitys;
	
	private int specialitysTotal;
	
	private SpecialityService specialityServer;
	
	
	public String specialitylist(){
		List<Speciality> specialitylist = this.getService().queryAllDate(Speciality.class); 
		Map<String,Speciality> specialityMap = new HashMap<String,Speciality>();
		for(int i=0;i<specialitylist.size();i++){
			Speciality speciality = specialitylist.get(i);
			specialityMap.put(speciality.getSpecialityid(), speciality);
		}
		
		Map term = new HashMap();
		term.put("quarterId", quarterId);
		//查询标准岗位对应的专业信息
		specialitys = this.getService().queryData("querySpecialityByStandardQuarterId", term, null);
		Map specialityIdsMap = new HashMap();
		List tmplist = new ArrayList();
		for(int i=0;i<specialitys.size();i++){
			JobsUnionSpeciality jobsUnionSpeciality = specialitys.get(i);
			Speciality speciality = specialityMap.get(jobsUnionSpeciality.getSpeciality().getSpecialityid());
			if(specialityIdsMap.get(speciality.getSpecialityid()) == null){
				if(speciality!=null){
					specialitys.get(i).setSpeciality(speciality);
				}
				jobsUnionSpeciality.setSpecialityid(speciality.getSpecialityid());/// 专业ID
				jobsUnionSpeciality.setSpecialityname(speciality.getSpecialityname());/// 专业名称
				jobsUnionSpeciality.setBstid(speciality.getSpecialityType().getBstid());
				jobsUnionSpeciality.setTypename(speciality.getSpecialityType().getTypename());
				specialityIdsMap.put(speciality.getSpecialityid(),"a"); 
				tmplist.add(jobsUnionSpeciality);
			}
		}
		specialitys = tmplist;
		specialitysTotal = specialitys.size();
		return "specialitylist";
	}
	
	
	public String delete()throws EapException{
		Map term = new HashMap();
		term.put("quarterId", quarterId);
		term.put("standardIds", this.getId());
		//查询标准岗位对应的专业信息
		JobUnionStandardExService jobUnionStandardExService = (JobUnionStandardExService)this.getService();
		List<JobsStandardQuarter> delList1 = this.getService().queryData("queryJobsStandardQuarterByQid", term, null);
		List<JobsUnionStandard> delList2 = this.getService().queryData("queryJobsUnionStandardByQid", term, null);
		if(delList1.size()>0){
			this.getService().deletes(delList1);
		}
		if(delList2.size()>0){
			this.getService().deletes(delList2);
		}
		
		Map term2 = new HashMap();
		term2.put("quarterCode", quarterId);
		List<QuarterStandard> quarterStandardlist = this.getService().queryData("quarterStandardQuarterByCodeHql", term2, null);
		//处理子的添加问题
		for(int j=0;j<quarterStandardlist.size();j++){
			QuarterStandard quarterStandard = quarterStandardlist.get(j);
			jobUnionStandardExService.saveChildeJobUnionStandard(quarterStandard.getQuarterId());
		}
		//处理岗位<->条款<->专业的联系
		try {
			this.getSpecialityServer().saveSyncJobsUnionSpeciality();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("删除成功！");
		return "delete";
	}


	public String getQuarterId() {
		return quarterId;
	}


	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}


	public List<JobsUnionSpeciality> getSpecialitys() {
		return specialitys;
	}


	public void setSpecialitys(List<JobsUnionSpeciality> specialitys) {
		this.specialitys = specialitys;
	}


	public int getSpecialitysTotal() {
		return specialitysTotal;
	}


	public void setSpecialitysTotal(int specialitysTotal) {
		this.specialitysTotal = specialitysTotal;
	}


	public SpecialityService getSpecialityServer() {
		return specialityServer;
	}


	public void setSpecialityServer(SpecialityService specialityServer) {
		this.specialityServer = specialityServer;
	}
	
	
}
