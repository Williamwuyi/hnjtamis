package cn.com.ite.hnjtamis.common;

import java.io.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * 消息png图片文件添加数字
 * @author 朱健
 * @create time: 2016年3月18日 下午1:33:35
 * @version 1.0
 * 
 * @modified records:
 */
public class MsgPNGAddNum {
	
	    /**
	     * 消息png文件添加数字
	     * @author 朱健
	     * @param sourceFile 源文件
	     * @param targetFile 生成后的文件
	     * @param fontValue  文字
	     * @param x 文字在图片位置 -X 
	     * @param y 文字在图片位置 -Y
	     * @return 是否生成成功
	     * @modified
	     */
	    public static boolean addPNGFont(File sourceFile,File targetFile,String fontValue,int x,int y){
	    	BufferedImage buffImg = null;
	    	Graphics2D g = null;
	    	boolean createFlag = false;
	    	try{
		    	buffImg = ImageIO.read(sourceFile);
		        g = buffImg.createGraphics();
		        g.setColor(Color.WHITE);
		        g.setFont(new Font("Arial",Font.PLAIN,10));//风格：三个常量 lFont.PLAIN, Font.BOLD, Font.ITALIC
		        g.drawString(fontValue,x, y); //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
	            ImageIO.write(buffImg, "png", targetFile);
	            //System.out.println(targetFile.getPath()+" ===== 生成完毕 =====");
	            createFlag = true;
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		 buffImg = null;
		         g = null;
	    	}
	    	return createFlag;
	    }  

	    
	    public static void main(String[]args) throws Exception{
	    	File sourceFile = new File("D:\\CVS\\hnjtamis\\Code\\hnjtamis\\WebRoot\\resources\\images\\top_menu021.png");
	    	File targetFile = new File("D:\\CVS\\hnjtamis\\Code\\hnjtamis\\WebRoot\\upload\\msgpng\\top_menu021_9cb4eb936a4f46df8be1b57e18421947.png");
	    	MsgPNGAddNum.addPNGFont(sourceFile, targetFile,"M",47,15);//49 45
	   }
}
