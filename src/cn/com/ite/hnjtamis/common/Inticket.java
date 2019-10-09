package cn.com.ite.hnjtamis.common;

import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperDao;



public class Inticket {

	public String  getPrefix(String exam_arrangeid){
		ExampaperDao exampaperDao = (ExampaperDao) SpringContextUtil.getBean("exampaperDao");
		return exampaperDao.getPrefix(exam_arrangeid);
	}
	
	public int getPostfixMaxNum(String exam_arrangeid,String prefix){
		ExampaperDao exampaperDao = (ExampaperDao) SpringContextUtil.getBean("exampaperDao");
		return exampaperDao.getPostfixMaxNum(exam_arrangeid, prefix);
	}

	
	public static String getInticket(String prefix,String examStartTime,int postfix){
		/*if(postfix<10){
			return prefix+"0000"+postfix;
		}else if(postfix<100){
			return prefix+"000"+postfix;
		}else if(postfix<1000){
			return prefix+"00"+postfix;
		}else if(postfix<10000){
			return prefix+"0"+postfix;
		}else{
			return prefix+postfix;
		}*/
		int inticketMaxLen = 13;//prefix后跟的位数
		if(examStartTime==null || "".equals(examStartTime)){
			java.util.Date d=new java.util.Date();
	        java.text.SimpleDateFormat format=new java.text.SimpleDateFormat("yyyy-MM-dd");
	        examStartTime = format.format(d);
		}
		int postfixLen = (postfix+"").length();
		String tcStr = examStartTime.replaceAll("-", "").substring(0,8);
		int maxlen = inticketMaxLen-postfixLen;
		for(int i=tcStr.length();i<maxlen;i++){
			tcStr+="0";
		}
		return prefix+tcStr+postfix;
		//String re =  RandomGUID.getGUID();
		//return re.replaceAll("-", "").substring(0, 18);
	}
	
	public static void main(String[] args){
		System.out.println(Inticket.getInticket("test", "2016-02-18 11:11:11",1));
		System.out.println(Inticket.getInticket("test", "2016-02-18 11:11:11",12));
		System.out.println(Inticket.getInticket("test", "2016-02-18 11:11:11",130));
		System.out.println(Inticket.getInticket("test", "2016-02-18 11:11:11",1450));
		System.out.println(Inticket.getInticket("test", "2016-02-18 11:11:11",10220));
		System.out.println(Inticket.getInticket("test", "2016-02-18 11:11:11",112200));
	}
	
}
