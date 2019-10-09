package cn.com.ite.hnjtamis.common;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.common.ExamCode</p>
 * <p>Description 考试编码生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月6日 下午3:20:55
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamCode {

	
	
	/**
	 * 根据时间生成序号
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public static String getSequence(){
		java.util.Date d=new java.util.Date();
        java.text.SimpleDateFormat format=new java.text.SimpleDateFormat("yyyyMMddHHmmssSS");
        return format.format(d);
		
	}
}
