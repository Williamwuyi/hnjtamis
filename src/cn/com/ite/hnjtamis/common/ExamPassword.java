package cn.com.ite.hnjtamis.common;

import java.util.Random;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.common.ExamPassword</p>
 * <p>Description 产生密码</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月17日 下午2:26:54
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamPassword {

	
	/**
	 *
	 * @author zhujian
	 * @description 产生密码
	 * @param len
	 * @return
	 * @modified
	 */
	public static String getExamPassword(int len){
		//String re =  RandomGUID.getGUID();
		//re = re.replaceAll("-", "").replaceAll("F", "A").replaceAll("E", "A").replaceAll("e", "a").substring(0, len);
		int max=1;
        int min=1;
        for(int i=0;i<len-1;i++){
        	min = min * 10;
        }
        max = min * 10 -1;
        Random random = new Random();
        String re = (random.nextInt(max)%(max-min+1) + min)+"";
		return re;
	}
}
