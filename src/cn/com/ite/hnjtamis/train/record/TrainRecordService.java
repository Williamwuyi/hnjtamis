package cn.com.ite.hnjtamis.train.record;

import cn.com.ite.eap2.core.service.DefaultService;

public interface TrainRecordService extends DefaultService {
	/**
	 * 保存学习记录
	 * @param trainId 在线培训ID
	 * @param leafId 课件附件所在附件表中的ID
	 * @param vLength 视频长度
	 * @param type 1、保存开始时间 2、保存结束时间
	 * @return
	 */
	public String saveRecord(String trainId, String leafId, float vLength, int type) throws Exception;
}
