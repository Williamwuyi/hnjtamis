package cn.com.ite.hnjtamis.train.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.hnjtamis.exam.base.themebank.service.ThemeBankService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;

public class TrainImplementListAction extends AbstractListAction {

	private ThemeBankService themeBankService;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4488022385623320607L;
	private List<TreeNode> themeBanks;

	/**
	 * 查询结果对象
	 */
	private List<TrainImplement> list = new ArrayList<TrainImplement>();
	private String subjectTerm;

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		try {
			list = (List<TrainImplement>) service.queryData("queryHql", this,
					this.getSortMap(), this.getStart(), this.getLimit());
			this.setTotal(service.countData("queryHql", this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @modified
	 */
	public String delete() throws Exception {
		String[] ids = this.getId().split(",");
		for (int i = 0; i < ids.length; i++) {
			TrainImplement plan = (TrainImplement) service.findDataByKey(
					ids[i], TrainImplement.class);
			plan.setIsDel(1);
			plan.setSyncStatus(3);
			service.saveOld(plan);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	/**
	 * 审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String audit() throws Exception {
		TrainImplement plan = (TrainImplement) service.findDataByKey(this
				.getId(), TrainImplement.class);
		plan.setIsAudited(1);
		plan.setAuditTime(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		plan.setAuditer(LoginAction.getUserSessionInfo().getEmployeeId());
		plan.setAuditerName(LoginAction.getUserSessionInfo().getEmployeeName());
		service.saveOld(plan);
		this.setMsg("审核成功！");
		return "save";
	}

	/**
	 * 题库树
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String findThemeBankTree() throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("nameTerm", "");
		param.put("validStr", "");
		try {
			List<ThemeBank> themeList = themeBankService.queryData("queryHql",
					param, null);
			themeBanks = ((TrainImplementService) service).constructThemeTree(
					themeList, this.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findThemeBankTree";
	}

	public List<TrainImplement> getList() {
		return list;
	}

	public void setList(List<TrainImplement> list) {
		this.list = list;
	}

	public String getSubjectTerm() {
		return subjectTerm;
	}

	public void setSubjectTerm(String subjectTerm) {
		this.subjectTerm = subjectTerm;
	}

	public void setThemeBankService(ThemeBankService themeBankService) {
		this.themeBankService = themeBankService;
	}

	public List<TreeNode> getThemeBanks() {
		return themeBanks;
	}

	public void setThemeBanks(List<TreeNode> themeBanks) {
		this.themeBanks = themeBanks;
	}

}
