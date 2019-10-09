package cn.com.ite.hnjtamis.common;

/**
 * 试题编码 时间戳+5位序号
 * @author 朱健
 * @create time: 2016年2月24日 上午11:40:00
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeMakeCode {

	
	/**
	 * 获取试题编码
	 * @author 朱健
	 * @param themeSortIndex
	 * @return
	 * @modified
	 */
	public static String getCode(int themeSortIndex){
		int inticketMaxLen = 5;
		String tcStr = "";
		int postfixLen = (themeSortIndex+"").length();
		int maxlen = inticketMaxLen-postfixLen;
		for(int i=0;i<maxlen;i++){
			tcStr+="0";
		}
		long epoch = System.currentTimeMillis()/1000;
		return epoch+tcStr+themeSortIndex;
	}
	
	
	public static void main(String[] args){
		for(int i=0;i<100000;i++){
			System.out.println(ThemeMakeCode.getCode(i));
		}
	}
}
