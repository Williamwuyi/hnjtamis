package cn.com.ite.hnjtamis.jobstandard.scoreproportion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.BeanUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.jobstandard.domain.ScoreProportion;
/**
 * 
 * <p>Title 岗位达标培训信息系统-岗位标准设定模块</p>
 * <p>岗位标准得分比例设定 FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  6:12:48 PM
 * @version 1.0
 * 
 * @modified records:
 */
public class ScoreProportionFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private ScoreProportion form;

	public ScoreProportion getForm() {
		return form;
	}

	public void setForm(ScoreProportion form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (ScoreProportion)service.findDataByKey(this.getId(), ScoreProportion.class);
		/// 1.当前设置岗位VO
		if(!StringUtils.isEmpty(form.getJobscode())){
			Quarter thisquarter=(Quarter)service.findDataByKey(form.getJobscode(), Quarter.class);
			form.setThisquarter(thisquarter);
		}
		/// 设置的岗位集合
		List<Quarter> quarters = new ArrayList<Quarter>();
		quarters.add(form.getThisquarter());
//		List<Promotion> promotions=promotionService.findDataByJobId(form.getJobscode());
//		for(int i=0;i<promotions.size();i++){
//			String quarterid=promotions.get(i).getParentjobscode();
//			if (!StringUtils.isEmpty(quarterid)){
//				Quarter quarter = new Quarter();
//				quarter.setQuarterId(promotions.get(i).getParentjobscode());
//				quarter.setQuarterName(promotions.get(i).getParentjobsname());
//				quarters.add(quarter);
//			}
//			
//		}
		// 修改时只能修改当前单位
		form.setScoreproportionQuarters(quarters);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		/**
		 * 存储冗余的岗位名称时，添加部门名称作为前缀。为减少查询次数，添加quarterdeptMap结构
		 * 存储已有的岗位ID对应的“部门名称->岗位名称”内容
		 */
		///Map<String,String> quarterdeptMap = new HashMap<String, String>();
		ScoreProportionService pservice=(ScoreProportionService)service;
		form = (ScoreProportion)this.jsonToObject(ScoreProportion.class);
	
	//// 选择的多个岗位。同时保存多条记录
		List<Quarter> scoreproportionquarters = form.getScoreproportionQuarters();
		if (scoreproportionquarters.size()<=0) throw new Exception("请选择至少一个岗位设置");
		List<ScoreProportion> res_add = new ArrayList<ScoreProportion>();
		List<ScoreProportion> res_upp = new ArrayList<ScoreProportion>();
		for(int i=0;i<scoreproportionquarters.size();i++){
			Quarter vo=scoreproportionquarters.get(i);
			String quarterId=vo.getQuarterId();
			// 查询是否有记录
			ScoreProportion tmpVo = pservice.findDataByJobscode(quarterId);
			/// 映射岗位ID对应的名称（名称中包括了部门名称的前缀）
			/// 查询岗位
			Quarter thisquarter=(Quarter)service.findDataByKey(quarterId, Quarter.class);
			String jobsname =thisquarter.getDept().getDeptName()+"->"+thisquarter.getQuarterName();
			form.setJobsname(jobsname);
			form.setJobscode(quarterId);
			
			if(tmpVo==null){/// 新增
				tmpVo = new ScoreProportion();
				form.setScoresetid(null);
				AbstractDomain.addCommonFieldValue(form);
			}else{
				form.setScoresetid(tmpVo.getScoresetid());
				AbstractDomain.updateCommonFieldValue(form);
			}
			BeanUtils.copyProperties(tmpVo, form); // 属性对拷
			
			if(StringUtils.isEmpty(form.getScoresetid())){
				res_add.add(tmpVo);
			}else{
				res_upp.add(tmpVo);
			}
		}
		
		if (res_add.size()>0)
			service.saves(res_add);
		if (res_upp.size()>0)
			service.saves(res_upp);
		
		return "save";
	}

	public String saveSort() throws Exception{
		List<ScoreProportion> saves = new ArrayList<ScoreProportion>();
		int index = 1;
		for(String id:this.getSortIds()){
			ScoreProportion dt = (ScoreProportion)service.findDataByKey(id, ScoreProportion.class);
			dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
}
