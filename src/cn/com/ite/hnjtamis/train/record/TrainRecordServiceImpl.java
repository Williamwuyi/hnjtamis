package cn.com.ite.hnjtamis.train.record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.hnjtamis.train.domain.TrainOnline;
import cn.com.ite.hnjtamis.train.domain.TrainOnlineRecord;

public class TrainRecordServiceImpl extends DefaultServiceImpl implements
		TrainRecordService {
	/**
	 * 保存学习记录
	 * @param trainId 在线培训ID
	 * @param leafId 课件附件所在附件表中的ID
	 * @param type 1、开始时间 2、结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String saveRecord(String trainId, String leafId, float vLength, int type) throws Exception{
		//查询在线培训数据
		TrainOnline online = (TrainOnline)getDao().findEntityBykey(TrainOnline.class, trainId);
		//保存开始时间
		TrainOnlineRecord record;
		if (type == 1) {
			record = new TrainOnlineRecord();
			record.setStartTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			record.setTrainOnline(online);
			record.setLeafId(leafId);
			record.setNeedDuration((int) vLength /  60);
			record.setCreatedBy(LoginAction.getUserSessionInfo().getEmployeeCode());
			record.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			record.setOrganId(LoginAction.getUserSessionInfo().getCurrentOrganId());
			getDao().saveEntity(record);
		} else {
			Map<String, String> param = new HashMap<String, String>();
			param.put("trainIdTerm", trainId);
			param.put("leafIdTerm", leafId);
			List<TrainOnlineRecord> list = getDao().queryConfigQl("queryRecord", param, null, TrainOnlineRecord.class);
			if (list != null && list.size() >0 ) {
				record = list.get(0);
				record.setEndTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				record.setDuration(getMinutesBetween(record.getStartTime(), record.getEndTime()));
				getDao().saveEntityOld(record);
			}
		}
		return null;
	}
	
	/**
	 * 计算两个时间之间相差的分钟
	 * @param time1
	 * @param time2
	 * @return
	 */
	private int getMinutesBetween(String time1, String time2) {
		Date date1 = DateUtils.convertStrToDate(time1, "yyyy-MM-dd HH:mm:ss");
		Date date2 = DateUtils.convertStrToDate(time2, "yyyy-MM-dd HH:mm:ss");
		long diff = Math.abs(date1.getTime() - date2.getTime());
		return (int) (diff / (1000 * 60));
	}
}
