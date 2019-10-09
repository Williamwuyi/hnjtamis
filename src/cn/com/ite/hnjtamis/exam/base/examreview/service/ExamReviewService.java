package cn.com.ite.hnjtamis.exam.base.examreview.service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.hnjtamis.exam.base.examreview.form.SysOutScoreExcelForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;

public interface ExamReviewService extends DefaultService{
	/*
	 * 根据考试科目id 查询更新试卷 阅卷状态
	 */
	public void updateExamTestpaperState(String examId);
	/*
	 * 导入
	 */
	public List<String> importDate(File xls,HashMap<String,Object[]> updataMap,String examId) throws Exception;
	/*
	 * 导出
	 */
	public File exportDate(List<SysOutScoreExcelForm> exportDatas) throws Exception;
	
	/**
	 * 查询试题
	 * @description
	 * @param examId
	 * @param timuids
	 * @param curtimuids
	 * @param state
	 * @param anotherstate
	 * @return
	 * @modified
	 */
	public List<ExamTestpaperTheme> queryExamTestpaperThemeList(String examId,
			LinkedList<String> timuids,LinkedList<String> curtimuids,
			String state,String anotherstate);
}
