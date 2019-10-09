package cn.com.ite.hnjtamis.exam.base.examreview.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.loader.plan.exec.process.spi.ReturnReader;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.common.utils.XlsUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.exam.base.examreview.ExamReviewListAction;
import cn.com.ite.hnjtamis.exam.base.examreview.MyXlsUtils;
import cn.com.ite.hnjtamis.exam.base.examreview.dao.ExamReviewDao;
import cn.com.ite.hnjtamis.exam.base.examreview.form.SysOutScoreExcelForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;

public class ExamReviewServiceImpl extends DefaultServiceImpl implements ExamReviewService{
	
	private int[] columnsWitdh = {20,5,50,10,20,10};
	private String[] excelColos = new String[]{
		"examName",
		"tihao",
		"timu",
		"userName",
		"zkzNum",
		"score"
	};
	
	private String[] excelTitles = new String[]{
		"考试名称",
		"题号",
		"考题名称",
		"考生姓名",
		"准考证号",
		"得分"
	};
	
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
			String state,String anotherstate){
		ExamReviewDao examReviewDao = (ExamReviewDao)this.getDao();
		return examReviewDao.queryExamTestpaperThemeList(examId, timuids, curtimuids, state, anotherstate);
	}
	
	/*
	 * 根据考试科目id 查询更新试卷 阅卷状态
	 */
	public void updateExamTestpaperState(String examId){
		try {
			if(!StringUtils.isEmpty(examId)){
				Map term = new HashMap();
				term.put("examId", examId);
				//service.excuteQl("mytest", tlist);
				List updateIds = this.queryData("queryUpdateReviewedPaper", term, null, null);
				if(updateIds!=null && updateIds.size()>0){
					ArrayList<String> idsList = new ArrayList<String>();
					Map<String,Double> updateDatas = new HashMap<String,Double>();
					for (Object object : updateIds) {
						HashMap t = (HashMap) object;
						String TESTPAPERID = t.get("TESTPAPERID").toString();
						Double sumscore = Double.parseDouble(t.get("SUMSCORE").toString());
						updateDatas.put(TESTPAPERID, sumscore);
						idsList.add(TESTPAPERID);
					}
					List tlist = new ArrayList();
					term.put("ids", idsList);
					tlist.add(term);
					this.excuteQl("updateTestPaperState", tlist);//更新试卷表的状态
					
					
					//更新 考生试卷的成绩
					List<ExamUserTestpaper> userPaperList = this.queryData("queryExamUserTestpaperByTestPaperIds", term, null, ExamUserTestpaper.class);
					if(userPaperList!=null && userPaperList.size()>0){
						//ConcurrentHashMap<String,String> userMap = ExamReviewListAction.examPaperToExaminee.get(examId);
						for (ExamUserTestpaper examUserTestpaper : userPaperList) {
							if(updateDatas.containsKey(examUserTestpaper.getExamTestpaper().getExamTestpaperId())){
								examUserTestpaper.setFristScote(updateDatas.get(examUserTestpaper.getExamTestpaper().getExamTestpaperId()));
								//userMap.remove(examUserTestpaper.getExamPublicUser().getUserId());
							}
						}
						this.saves(userPaperList);
						/*if(userMap.size()<1){
							ExamReviewListAction.examPaperToExaminee.remove(examId);
						}else{
							ExamReviewListAction.examPaperToExaminee.put(examId, userMap);
						}*/
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 导出
	 */
	public File exportDate(List<SysOutScoreExcelForm> exportDatas) throws Exception{
		File xls = File.createTempFile("系统外得分考生信息", "xls");
		MyXlsUtils utils = MyXlsUtils.createWrite(xls);
		utils.write(0, "信息", exportDatas, excelColos, excelTitles,columnsWitdh);
		utils.closeWrite();
		return xls;
	}
	/*
	 * 导入
	 */
	public List<String> importDate(File xls,HashMap<String,Object[]> updataMap,String examId) throws Exception{
		List<String> resultInfo = new ArrayList<String>();
		try {
			int successCount = 0;
			int failureCount = 0;
			PropertyUtilsBean pU = new PropertyUtilsBean();
			List<SysOutScoreExcelForm> importLists = XlsUtils.read(xls,"信息",SysOutScoreExcelForm.class,excelColos);
			HashMap<String,Double> finalUpdataMap = new HashMap<String,Double>();
			for (SysOutScoreExcelForm data : importLists) {
				//<key:考试科目名称@题号@准考证号,value:考试试题id>
				String tmpKey = data.getExamName()+"@"+(data.getTihao()-1)+"@"+data.getZkzNum();
				if(updataMap.containsKey(tmpKey)){
					Object[] tmp = updataMap.get(tmpKey);//要更新得分的题目id
					String themePaperId = tmp[0].toString();
					double defaultScore = Double.parseDouble(tmp[1].toString());
					
					String scoreRe = scoreIsRight(data.getScore(),defaultScore);
					if(!"OK".equals(scoreRe)){ //验证得分有效性
						failureCount++;
						resultInfo.add("考生:"+data.getUserName()+"("+data.getZkzNum()+"  "+data.getExamName()+") 第 "+data.getTihao()+" 题，"+scoreRe);
					}else{
						successCount++;
						finalUpdataMap.put(themePaperId, data.getScore());
					}
				}else{
					failureCount++;
					resultInfo.add("未查询到 考生:"+data.getUserName()+"("+data.getZkzNum()+"  "+data.getExamName()+") 第 "+data.getTihao()+" 题，试题信息");
				}
			}
			
			if(finalUpdataMap.size()>0){
				Map term = new HashMap();
				term.put("examId", examId);
				List<ExamTestpaperTheme> datas = this.queryData("queryExamTestpaperThemeByExamId", term, null, ExamTestpaperTheme.class);
				if(datas!=null && datas.size()>0){
					for (ExamTestpaperTheme t : datas) {
						if(finalUpdataMap.containsKey(t.getExamTestpaperThemeId())){
							t.setScore(finalUpdataMap.get(t.getExamTestpaperThemeId()));
							t.setState(15);
						}
					}
				}
			}
			resultInfo.add(0,"成功导入系统外得分信息  "+successCount+" 条;导入失败 "+failureCount+" 条");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultInfo;
	}
	
	private String scoreIsRight(double score,double defaultScore){
		String flag = "OK";
		if(score<0){
			flag = "得分不能为负数";
		}
		if(score>defaultScore){
			flag = "得分大于最高分值:"+defaultScore+"分";
		}
		return flag;
	}
}
