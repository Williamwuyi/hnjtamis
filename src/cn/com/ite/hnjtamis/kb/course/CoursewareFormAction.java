package cn.com.ite.hnjtamis.kb.course;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.kb.domain.CoursewareDistribute;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.course.CoursewareFormAction
 * </p>
 * <p>
 * Description 课件库FormAction
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
public class CoursewareFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 475413355242488960L;

	private Courseware form;

	private File posterFile;
	private String posterFileFileName;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (Courseware) service.findDataByKey(this.getId(),
				Courseware.class);
		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		try {
			form = (Courseware) this.jsonToObject(Courseware.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsDistributeVersion(0);
			form.setIsDisable(0);
			form.setIsAnnounced(0);
			form.setIsAudited(0);
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setUploaderId(session.getEmployeeId());
			form.setUploaderName(session.getEmployeeName());
			form.setUploadTime(nowTime);
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrgan((Organ) service.findDataByKey(session.getOrganId(),
					Organ.class));
		}

		if (form.getCoursewareDistributes().size() == 0) {
			throw new Exception("请分配对应的岗位及专业！");
		}

		// 验证岗位、专业是否重复分配
		for (int i = 0; i < form.getCoursewareDistributes().size(); i++) {
			CoursewareDistribute cd1 = form.getCoursewareDistributes().get(i);
			cd1.setCourseware(form);
			String qsid1 = cd1.getQuarter().getQuarterId() + ","
					+ cd1.getSpeciality().getSpecialityid();
			String qsname = cd1.getQuarter().getQuarterName() + "|"
					+ cd1.getSpeciality().getSpecialityname();
			for (int j = 0; j < form.getCoursewareDistributes().size(); j++) {
				if (i == j)
					continue;
				CoursewareDistribute cd2 = form.getCoursewareDistributes().get(
						j);
				String qsid2 = cd2.getQuarter().getQuarterId() + ","
						+ cd2.getSpeciality().getSpecialityid();
				if (qsid1.equals(qsid2)) {
					throw new Exception("【" + qsname + "】重复分配！");
				}
			}
		}
		try {
			service.save(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMsg("保存成功！");
		return "save";
	}

	/**
	 * 保存封面图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String savePoster() throws Exception {
		// 验证后缀
		String reg = "jpg|jpeg|png|gif|bmp";
		String ext = posterFileFileName.substring(posterFileFileName
				.lastIndexOf(".") + 1);
		if (reg.indexOf(ext.toLowerCase()) == -1) {
			throw new Exception("请选择图片文件");
		}
		try {
			Courseware course = (Courseware) service.findDataByKey(
					this.getId(), Courseware.class);
			course.setPoster(FileOption.file2Bytes(posterFile));
			service.update(course);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	/**
	 * 提取外部机构共享的课件或教材
	 * 
	 * @return
	 * @throws Exception
	 */
	public String takeShare() throws Exception {
		UserSession session = LoginAction.getUserSessionInfo();
		String nowTime = DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		try {
			Courseware course = (Courseware) service.findDataByKey(
					this.getId(), Courseware.class);
			if (session.getCurrentOrganId().equals(
					course.getOrgan().getOrganId())) {
				this.setMsg("该" + (course.getType() == 1 ? "课件" : "教材")
						+ "属于本机构，不需要引入");
			}
			// 先判断是否已经引入到本机构中
			else if (((CoursewareService) service).isExist(course.getId(),
					session.getCurrentOrganId())) {
				this.setMsg("当前" + (course.getType() == 1 ? "课件" : "教材")
						+ "已存在本机构中！");
			} else {
				// 复制信息到本机构数据
				Courseware newcourse = new Courseware();
				newcourse.setType(course.getType());
				newcourse.setTitle(course.getTitle());
				newcourse.setDescription(course.getDescription());
				newcourse.setSeries(course.getSeries());
				newcourse.setEditor(course.getEditor());
				newcourse.setPress(course.getPress());
				newcourse.setEdition(course.getEdition());
				newcourse.setPublishTime(course.getPublishTime());
				newcourse.setIsAnnounced(0);
				newcourse.setOriginalCourseId(course.getId());
				newcourse.setOriginalOrganId(course.getOrgan().getOrganId());
				newcourse.setIsAudited(0);
				newcourse.setIsDel(0);
				newcourse.setSyncStatus(1);
				newcourse.setUploaderId(session.getEmployeeId());
				newcourse.setUploaderName(session.getEmployeeName());
				newcourse.setUploadTime(nowTime);
				newcourse.setCreatedBy(session.getEmployeeCode());
				newcourse.setCreationDate(nowTime);
				newcourse.setLastUpdateDate(nowTime);
				newcourse.setLastUpdatedBy(session.getEmployeeCode());
				newcourse.setOrgan((Organ) service.findDataByKey(session
						.getOrganId(), Organ.class));
				// 复制附件
				Set<Accessory> set = course.getAccessories();
				Iterator<Accessory> iter = set.iterator();
				while (iter.hasNext()) {
					Accessory acce = iter.next();
					Accessory newacce = new Accessory();
					newacce.setAcceType(acce.getAcceType());
					newacce.setFilePath(acce.getFilePath());
					newacce.setFileName(acce.getFileName());
					newacce.setFileSize(acce.getFileSize());
					newacce.setUploadDate(acce.getUploadDate());
					newacce.setShowImage(acce.getShowImage());
					newacce.setOrderNo(acce.getOrderNo());
					newcourse.getAccessories().add(newacce);
				}

				service.save(newcourse);
				this.setMsg("操作成功，请到"
						+ (course.getType() == 1 ? "【课件库】" : "【教材库】")
						+ "中分配对应的岗位及专业");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	public Courseware getForm() {
		return form;
	}

	public void setForm(Courseware form) {
		this.form = form;
	}

	public File getPosterFile() {
		return posterFile;
	}

	public void setPosterFile(File posterFile) {
		this.posterFile = posterFile;
	}

	public String getPosterFileFileName() {
		return posterFileFileName;
	}

	public void setPosterFileFileName(String posterFileFileName) {
		this.posterFileFileName = posterFileFileName;
	}
}
