package cn.com.ite.hnjtamis.kb.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.param.SystemParamsService;
import cn.com.ite.hnjtamis.param.domain.SystemParams;
import cn.com.ite.hnjtamis.train.domain.AccessoryFileTransform;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.course.CoursewareListAction
 * </p>
 * <p>
 * Description 课件库ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time: 2015-3-24
 * @version 1.0
 * 
 * @modified records:
 */
public class CoursewareListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5908849402889186792L;
	private SystemParamsService systemParamsService;

	private String organId;// 机构ID
	private String titleTerm;// 标题查询条件
	private String uploaderTerm;// 上传人查询条件
	private Integer typeTerm;// 类型：课件或教材
	/**
	 * 查询结果对象
	 */
	private List<Courseware> list = new ArrayList<Courseware>();

	/**
	 * 查询方法
	 * 
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<Courseware>) service.queryData("queryHql", this, this
				.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	/**
	 * 机构共享课件及教材列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String shareList() throws Exception {
		try {
			list = (List<Courseware>) service.queryData("querySharedHql", this,
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
			Courseware course = (Courseware) service.findDataByKey(ids[i],
					Courseware.class);
			course.setIsDel(1);
			course.setSyncStatus(3);
			service.saveOld(course);
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
		Courseware course = (Courseware) service.findDataByKey(this.getId(),
				Courseware.class);
		course.setIsAudited(1);
		course.setAuditTime(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		course.setAuditer(LoginAction.getUserSessionInfo().getEmployeeName());

		// 将附件数据添加到附件转换表中
		try {
			Set<Accessory> set = course.getAccessories();
			Iterator<Accessory> iter = set.iterator();
			List<AccessoryFileTransform> list = new ArrayList<AccessoryFileTransform>();
			while (iter.hasNext()) {
				Accessory acce = iter.next();
				//已存在则跳过，避免插入重复数据
				if (service.findDataByKey(acce.getAcceId(), AccessoryFileTransform.class) != null ) 
					continue;
				AccessoryFileTransform transform = new AccessoryFileTransform();
				transform.setId(acce.getAcceId());
				transform.setFilePath(acce.getFilePath());
				transform.setFileName(acce.getFileName());
				transform.setIsFinished(0);
				list.add(transform);
			}
			service.saves(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		service.saveOld(course);
		
		//查询附件转换地址
		SystemParams param = systemParamsService.findByCode("IOCPURL");
		this.setMsg(param.getValue());
		return "save";
	}

	/**
	 * 共享
	 * 
	 * @return
	 * @throws Exception
	 */
	public String share() throws Exception {
		Courseware course = (Courseware) service.findDataByKey(this.getId(),
				Courseware.class);
		course.setIsAnnounced(1);
		course.setLastUpdateDate(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		course.setLastUpdatedBy(LoginAction.getUserSessionInfo()
				.getEmployeeName());
		service.saveOld(course);
		this.setMsg("操作成功！");
		return "save";
	}

	public List<Courseware> getList() {
		return list;
	}

	public void setList(List<Courseware> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}

	public String getUploaderTerm() {
		return uploaderTerm;
	}

	public void setUploaderTerm(String uploaderTerm) {
		this.uploaderTerm = uploaderTerm;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public Integer getTypeTerm() {
		return typeTerm;
	}

	public void setTypeTerm(Integer typeTerm) {
		this.typeTerm = typeTerm;
	}

	public void setSystemParamsService(SystemParamsService systemParamsService) {
		this.systemParamsService = systemParamsService;
	}

}
