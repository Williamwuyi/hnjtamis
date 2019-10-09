package cn.com.ite.hnjtamis.exam.study.jsonFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamJsonForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperThemeSign;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

public class StudyTestpaperJsonForm extends ExamJsonForm{

	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInStudyTestpaper(StudyTestpaper examTestpaper,List<ThemeType> themeTypeList,
			List<StudyTestpaperTheme> themelist,Map<String,List<StudyTestpaperAnswerkey>> ansListMap,Map<String,StudyTestpaperThemeSign> signThemeMap){
		String examTestpaperStr = "";
		int themeIndex = 1;
		if(themeTypeList!=null && themeTypeList.size()>0){
			examTestpaperStr += "{";
			examTestpaperStr += "testPaperId:'"+examTestpaper.getStudyTestpaperId()+"'";
			if(themeTypeList.size()>0){
				examTestpaperStr+=",themeType:[";
				for(int k=0;k<themeTypeList.size();k++){
					ThemeType themeType = (ThemeType)themeTypeList.get(k);
					examTestpaperStr+="{themeTypeId:'"+themeType.getThemeTypeId()+"',";
					examTestpaperStr+="themeTypeName:'"+stringToJson(themeType.getThemeTypeName())+"',";
					examTestpaperStr+="type:'"+themeType.getThemeType()+"'";
					if(themelist.size()>0){
						examTestpaperStr+=",theme:[";
						//boolean isAddTheme = false;
						List newthemelist = new ArrayList();
						for(int i=0;i<themelist.size();i++){
							StudyTestpaperTheme examTestpaperTheme = (StudyTestpaperTheme)themelist.get(i);
							if(themeType.getThemeTypeId().equals(examTestpaperTheme.getThemeTypeId())
									&& ("0".equals(examTestpaperTheme.getScoreType()) || "1".equals(examTestpaperTheme.getScoreType()))){
								examTestpaperTheme.setThemeIndex(themeIndex);
								newthemelist.add(examTestpaperTheme);
								themeIndex++;
								///isAddTheme = true;
							}
						}
						if(newthemelist.size()>0){
							for(int i=0;i<newthemelist.size();i++){
								StudyTestpaperTheme examTestpaperTheme = (StudyTestpaperTheme)newthemelist.get(i);
								examTestpaperStr+=getExamTestpaperThemeStr(examTestpaperTheme,ansListMap,signThemeMap);
							}
							//if(isAddTheme){
								examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
							//}
						}
						examTestpaperStr+="]";
					}
					examTestpaperStr+="},";
				}
				examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
				examTestpaperStr+="]";
			}
			examTestpaperStr+="}";
		}
		return examTestpaperStr;
	}
	
	
	private static String getExamTestpaperThemeStr(StudyTestpaperTheme examTestpaperTheme,
			Map<String,List<StudyTestpaperAnswerkey>> ansListMap,Map<String,StudyTestpaperThemeSign> signThemeMap){
		String examTestpaperStr="{themeId:'"+examTestpaperTheme.getStudyTestpaperThemeId()+"',";
		if(signThemeMap.get(examTestpaperTheme.getStudyTestpaperThemeId())!=null){
	    	examTestpaperStr+="sg:'t',";
	    }
		examTestpaperStr+="sort:"+examTestpaperTheme.getThemeIndex()+",";
		examTestpaperStr+="fraction:'"+examTestpaperTheme.getDefaultScore()+"',";
		examTestpaperStr+="eachline:'"+examTestpaperTheme.getEachline()+"',";
		examTestpaperStr+="explain:'"+examTestpaperTheme.getExplain()+"',";
		examTestpaperStr+="value:'"+CharsetSwitchUtil.encode(stringToJson(examTestpaperTheme.getThemeName()))+"'";
		
		
		List<StudyTestpaperAnswerkey> ansList = ansListMap.get(examTestpaperTheme.getStudyTestpaperThemeId());
		if(ansList==null) ansList = examTestpaperTheme.getStudyTestpaperAnswerkeies();
		if(ansList!=null && ansList.size()>0){
			examTestpaperStr+=",answer:[";
			String rightAnsStr = "";
			//boolean isHaveRight = false;
			for(int tt=0;tt<ansList.size();tt++){
				StudyTestpaperAnswerkey answerkey = (StudyTestpaperAnswerkey)ansList.get(tt);
				examTestpaperStr+="{ansSort:'"+(tt+1)+"',ansValue:'"+CharsetSwitchUtil.encode(stringToJson(answerkey.getAnswerkeyValue()))+"'},";
				if(answerkey.getIsRight()!=null && answerkey.getIsRight().intValue() == 10){//是否正确 5：否,10：是
					rightAnsStr+="{ansSort:'"+(tt+1)+"',value:'"+stringToJson(answerkey.getAnswerkeyValue())+"'},";
					//isHaveRight = true;
				}
				//System.out.println(tt);
			}
			examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
			examTestpaperStr+="]";
			
			if(rightAnsStr.length()>0){
				examTestpaperStr+=",rightAns:["+rightAnsStr.substring(0,rightAnsStr.length()-1);;
			}else{
				examTestpaperStr+=",rightAns:[";
			}
			
			
			/*for(int tt=0;tt<ansList.size();tt++){
				StudyTestpaperAnswerkey answerkey = (StudyTestpaperAnswerkey)ansList.get(tt);
				if(answerkey.getIsRight()!=null && answerkey.getIsRight().intValue() == 10){//是否正确 5：否,10：是
					examTestpaperStr+="{ansSort:'"+(tt+1)+"',value:'"+stringToJson(answerkey.getAnswerkeyValue())+"'},";
					isHaveRight = true;
				}
			}
			if(isHaveRight){
				examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
			}*/
			examTestpaperStr+="]";
			ansListMap.remove(examTestpaperTheme.getStudyTestpaperThemeId());
		}
		examTestpaperStr+="},";
		return examTestpaperStr;
	}
	
	
/*	
	public static void getAllExamTestpaperThemeStr(List<StudyTestpaperTheme> list) throws Exception {

		 // 开始时间
        long start = System.currentTimeMillis();
     // 模拟数据List
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 3000; i++) {
            list.add(i + "");
        }


        // 每500条数据开启一条线程
        int threadSize = 500;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<StudyTestpaperTheme> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List<StudyTestpaperTheme> listStr = cutList;
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "线程：" + listStr);
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<Integer>> results = exec.invokeAll(tasks);
        for (Future<Integer> future : results) {
            System.out.println(future.get());
        }
        // 关闭线程池
        exec.shutdown();
        System.out.println("线程任务执行结束");
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }
*/
}
