package cn.com.ite.hnjtamis.exam.examControl;

import cn.com.ite.eap2.common.utils.NumericUtils;

/**
 * 考试信息获取操作
 * <p>Title cn.com.ite.hnjtamis.exam.examControl.ExamControlMain</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年10月27日 上午9:02:13
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamControlMain {

	
	private static int maxRemIsBetter = 20;//内存大于此数被认为良好，可以使用内存，否则需要进行清除
	
	public static void setMaxRemIsBetter(int _maxRemIsBetter) {
		 maxRemIsBetter = _maxRemIsBetter;
	}
	
	/**
	 * 判断内存是否良好
	 * @description
	 * @return
	 * @modified
	 */
	protected static int remIsBetter(){
		double maxMemory = Runtime.getRuntime().maxMemory()/1024.0/1024.0;//最大内存
		double freeMemory = Runtime.getRuntime().freeMemory()/1024.0/1024.0;//可用内存 当前JVM空闲内存
		double totalMemory = Runtime.getRuntime().totalMemory()/1024.0/1024.0;//当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		System.out.println(
				("最大内存" + NumericUtils.round(maxMemory,2)+"M  ")
				+("可用内存："+NumericUtils.round(freeMemory,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(totalMemory,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		if(freeMemory>maxRemIsBetter*3){//可用内存大于20M
			return 3;
		}else if(freeMemory>maxRemIsBetter*2){//可用内存大于20M
			return 2;
		}else if(freeMemory>maxRemIsBetter){//可用内存大于20M
			return 1;
		}else{
			return -1;
		}
	}
}
