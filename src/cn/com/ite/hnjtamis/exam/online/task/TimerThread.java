package cn.com.ite.hnjtamis.exam.online.task;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.online.task.TimerThread</p>
 * <p>Description 定时处理任务</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月30日 下午1:51:42
 * @version 1.0
 * 
 * @modified records:
 */
public class TimerThread implements ServletContextListener{

	private static boolean isrun=true;//判断是否任务是否在运行，主要控制一个线程的处理	
	
	private static boolean isrun2=true;//判断是否任务是否在运行，主要控制一个线程的处理	
	
	private static boolean isrun3=true;//判断是否任务是否在运行，主要控制一个线程的处理	
	
	private static String realPath = null;
	
    public static String getRealPath() {
		return realPath;
	}

	public static void setRealPath(String realPath) {
		TimerThread.realPath = realPath;
	}

	/***************************   以下是定时任务的处理   ***********************************************/
    
    private static Timer cycleTasktimer = null;
    
    private static Timer cycleTasktimerTwo = null;
    
    private static Timer cycleTasktimerThree = null;
    
    private static int resetCycleTasktimerMax = 0;//重启的最大次数

    private static int resetCycleTasktimerTwoMax  = 0;//重启的最大次数
    
    private static int resetCycleTasktimerThreeMax  = 0;//重启的最大次数

	public void contextDestroyed(ServletContextEvent arg0) {
		cycleTasktimer.cancel();
		cycleTasktimerTwo.cancel();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		TimerThread.setRealPath(arg0.getServletContext().getRealPath(""));
		exeTask();
	}

	/**
	 * 定期任务自动读取
	 *
	 */
	public void exeTask(){
		try{
			if(cycleTasktimer==null){
				cycleTasktimer = new Timer();
				cycleTasktimer.schedule(new java.util.TimerTask() { 
					public void run() {
						if(isrun){
					    	isrun=false;
					    	try{
					    		TaskForm.exeAutoStateTask();
					    		resetCycleTasktimerMax = 0;
					    	}catch(Exception e){
					    		e.printStackTrace();
					    	}
					    	isrun=true;
					   }else{
						   System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器1]还有数据正在运行,未调用定时任务。 检测当前内存情况："
									+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
									+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
									+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
					   }
					}
					
				}, 5*60*1000L,30*60*1000L);
			}
		}catch(Exception e){
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器1]定时器发生错误，销毁并重启定时器，当前已重启次数"+resetCycleTasktimerMax+" 检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			try{
				cycleTasktimer.cancel();
			}catch(Exception ee){}
			cycleTasktimer = null;
			if(resetCycleTasktimerMax<5){
				resetCycleTasktimerMax++;
				exeTask();
			}
			e.printStackTrace();
		}
		
		try{
			if(cycleTasktimerTwo == null){
				cycleTasktimerTwo = new Timer();
				cycleTasktimerTwo.schedule(new java.util.TimerTask() { 
					public void run() {
						 if(isrun2){
							 isrun2=false;
						     try{
						    	 TaskForm.exeAutoStateTaskTwo();
						    	 resetCycleTasktimerTwoMax = 0;
						     }catch(Exception e){
						    	 e.printStackTrace();
						     }
						     isrun2=true;
						   }else{
							   System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器2]还有数据正在运行,未调用定时任务。 检测当前内存情况："
										+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
										+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
										+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
						   }
					}
					
				}, 1*60*1000L,10*60*1000L);
			}
		}catch(Exception e){
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器2]定时器发生错误，销毁并重启定时器，当前已重启次数"+resetCycleTasktimerTwoMax+" 检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			try{
				cycleTasktimerTwo.cancel();
			}catch(Exception ee){}
			cycleTasktimerTwo = null;
			if(resetCycleTasktimerTwoMax<5){
				resetCycleTasktimerTwoMax++;
				exeTask();
			}
			
			e.printStackTrace();
		}
		
		
		try{
			if(cycleTasktimerThree == null){
				cycleTasktimerThree = new Timer();
				cycleTasktimerThree.schedule(new java.util.TimerTask() { 
					public void run() {
						 if(isrun3){
							 isrun3=false;
						     try{
						    	 TaskForm.exeAutoStateTaskThree();
						    	 resetCycleTasktimerThreeMax = 0;
						     }catch(Exception e){
						    	 e.printStackTrace();
						     }
						     isrun3=true;
						   }else{
							   System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器3]还有数据正在运行,未调用定时任务。 检测当前内存情况："
										+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
										+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
										+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
						   }
					}
					
				}, 1*60*1000L,60*60*1000L);
			}
		}catch(Exception e){
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器3]定时器发生错误，销毁并重启定时器，当前已重启次数"+resetCycleTasktimerThreeMax+" 检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			try{
				cycleTasktimerThree.cancel();
			}catch(Exception ee){}
			cycleTasktimerThree = null;
			if(resetCycleTasktimerThreeMax<5){
				resetCycleTasktimerThreeMax++;
				exeTask();
			}
			e.printStackTrace();
		}
		
	}
}

