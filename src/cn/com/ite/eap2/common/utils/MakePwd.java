package cn.com.ite.eap2.common.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;

/**
 * 根据规则生成对应密码
 * @create time: 2015年11月11日 下午1:20:56
 * @version 1.0
 * 
 * @modified records:
 */
public final class MakePwd {

	
	
	/**
	 * 获取Unix时间戳
	 * @return
	 * @modified
	 */
	public static long getTick(){
		long epoch = System.currentTimeMillis()/1000;
		return epoch;
	}
	
	/**
	 * 获取Unix时间戳
	 * @param date
	 * @return
	 * @modified
	 */
	public static long getTick(Date date){
		long epoch = date.getTime()/1000;
		return epoch;
	}
	
	/**
	 * 获取Unix时间戳
	 * @param date
	 * @return
	 * @modified
	 */
	public static long getTick(String time){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        long epoch = -1;
		try {
			epoch = getTick(sd.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  epoch;
	}
	
	
	/**
	 * 获取加密密码
	 * @param sysKey
	 * @param password
	 * @return
	 * @modified
	 */
	public static String getPwd(String sysKey,String password){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getPwd( sysKey, password, sd.format(new Date()));
	}
	
	public static String getPwd(String sysKey,String password,String time){
		/**
		 *  s1 = MD5(key+password+tick)
			s2 = s1截取掉前六位（从第7位到最后1位） 
			新密钥 = s2
			
			key:为线下交换的另一个key，各单位不同，由系统管理员持有，需保密存放，泄密后立即修改; 
			password:经MD5加密过的密码；
			tick:为一个时间因子；将EIP系统时间转换成Unix时间戳(Unix timestamp)再进行靠近零值处理，
				具体时间因子获取方法见：http://tool.lu/timestamp，这个时间是1970年1月1日至今的总秒数，通过靠近零值，向下舍入整数。
				（例如如果容差为60s，那么tick=ROUNDDOWN(((timestamp-30)/60），0)做为salt插入MD5函数。
				URL中不包含任何tick信息。（ROUNDDOWN:靠近零值，向下（绝对值减小的方向）舍入数字；Timestamp：Unix时间戳）

		 * */
		String pwd = null;
		int tolerableNum = 30;//容差值
		int perMinute = 60;//分钟
		try{
			if(sysKey!=null && password!=null){
				//获取时间戳
				long timestamp = getTick(time);
				//根据容差进行计算
				BigDecimal tick = (new BigDecimal(((timestamp-tolerableNum)/perMinute))).setScale(0,BigDecimal.ROUND_DOWN);
				//System.out.println("tick = "+tick);
				
				//进行MD5加密
				//System.out.println("sysKey+password+tick = "+sysKey+password+tick.longValue());
				String s1 = CharsetSwitchUtil.encrypt(sysKey+password+tick.longValue(),"MD5");
				//System.out.println("s1 = "+s1);
				//舍去前6位
				pwd = s1.substring(6,s1.length());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return pwd;
	}
	
	public static void main(String[] args){
		//System.out.println(MakePwd.getPwd("000000",CharsetSwitchUtil.encrypt("888888","MD5"),"2015-12-10 17:00:00"));;
		System.out.println("s2 = "+MakePwd.getPwd("ydkh","21218cca77804d2ba1922c33e0151105","2015-12-10 17:00:00"));;
	}
}
