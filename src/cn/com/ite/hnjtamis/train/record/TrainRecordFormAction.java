package cn.com.ite.hnjtamis.train.record;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.train.domain.TrainOnline;
import cn.com.ite.hnjtamis.train.domain.TrainOnlineRecord;
import cn.com.ite.hnjtamis.train.online.TrainOnlineService;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.train.record.TrainRecordFormAction
 * </p>
 * <p>
 * Description 在线培训记录FormAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-4-8
 * @version 1.0
 * 
 * @modified
 */
public class TrainRecordFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7524688420687784241L;

	private TrainOnlineService trainOnlineService;

	public void setTrainOnlineService(TrainOnlineService trainOnlineService) {
		this.trainOnlineService = trainOnlineService;
	}

	private TrainOnlineRecord form;

	private String leafId;// 附件ID
	private String trainId;// 在线培训ID
	private int type;// 区分是开始或者结束学习，1、开始 2、结束
	private float courseLength;// 视频长度
	private float studyMinutes;// 在线学习时间
	private String startTime;// 开始时间
	private String endTime;// 结束时间

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() throws Exception {
		form = (TrainOnlineRecord) service.findDataByKey(this.getId(),
				TrainOnlineRecord.class);
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
			form = (TrainOnlineRecord) this
					.jsonToObject(TrainOnlineRecord.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);
		if (StringUtils.isEmpty(form.getId())) {
			String nowTime = DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			form.setIsDel(0);
			form.setSyncStatus(1);
			form.setCreatedBy(session.getEmployeeCode());
			form.setCreationDate(nowTime);
			form.setLastUpdateDate(nowTime);
			form.setLastUpdatedBy(session.getEmployeeCode());
			form.setOrganId(session.getOrganId());
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
	 * 保存学习记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveRecord() throws Exception {
		// ((TrainRecordService)service).saveRecord(trainId, leafId,
		// courseLength, type);
		TrainOnline online = (TrainOnline) service.findDataByKey(trainId,
				TrainOnline.class);
		TrainOnlineRecord record = new TrainOnlineRecord();
		record.setTrainOnline(online);
		record.setLeafId(leafId);
		record.setStartTime(startTime);
		record.setEndTime(endTime);
		record.setDuration((int) studyMinutes);
		record.setNeedDuration((int) (courseLength / 60));
		record.setCreationDate(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		record.setOrganId(LoginAction.getUserSessionInfo().getCurrentOrganId());
		record
				.setSyncStatus(record.getDuration() - record.getNeedDuration() >= 0 ? 1
						: 0);// 同步字段做节点是否学习完成使用
		service.save(record);

		this.setOnlineFinishStatus(online);
		return "save";
	}

	/**
	 * 设置在线学习完成状态
	 * 
	 * @param online
	 */
	private void setOnlineFinishStatus(TrainOnline online) throws Exception {
		Set<TrainOnlineRecord> set = online.getTrainOnlineRecords();

		// 没有学习记录不做处理，默认为未开始
		if (set.size() == 0)
			return;

		// 查询应学的节点数
		try {
			int count = trainOnlineService.getCourseCount(online.getId());
			int c = 0;// 累计已完成的节点数
			Iterator<TrainOnlineRecord> iter = set.iterator();
			Map<String, TrainOnlineRecord> map = new HashMap<String, TrainOnlineRecord>();
			while (iter.hasNext()) {
				TrainOnlineRecord record = iter.next();
				if (record.getSyncStatus() == 1) {
					map.put(record.getTrainOnline().getId(), record);
				}
			}
			c = map.size();
			if (c >= count) {
				online.setStatus(2);// 已完成
			} else {
				online.setStatus(1);// 学习中
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		trainOnlineService.saveOld(online);
	}

	/**
	 * 设置对应附件应学时长
	 * 
	 * @return
	 * @throws Exception
	 */
	public String setNeedDuration() throws Exception {
		return "save";
	}

	public TrainOnlineRecord getForm() {
		return form;
	}

	public void setForm(TrainOnlineRecord form) {
		this.form = form;
	}

	public String getLeafId() {
		return leafId;
	}

	public void setLeafId(String leafId) {
		this.leafId = leafId;
	}

	public String getTrainId() {
		return trainId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getCourseLength() {
		return courseLength;
	}

	public void setCourseLength(float courseLength) {
		this.courseLength = courseLength;
	}

	public float getStudyMinutes() {
		return studyMinutes;
	}

	public void setStudyMinutes(float studyMinutes) {
		this.studyMinutes = studyMinutes;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
