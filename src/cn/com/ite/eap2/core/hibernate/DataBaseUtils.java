package cn.com.ite.eap2.core.hibernate;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * <p>Title cn.com.ite.eap2.core.hibernate.DataBaseUtils</p>
 * <p>Description JDBC工具类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 5:38:50 PM
 * @version 2.0
 * 
 * @modified records:
 */
public class DataBaseUtils {
   /**
    * 查询数据
    * @param rs 结果
    * @param fn 属性
    * @return
    * byte[]
    */
   public static byte[] getByte(ResultSet rs,String fn){
	   try{
		   InputStream in = rs.getBinaryStream(fn);
		   if(in==null) return null;
		   java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
	       byte[] b = new byte[1024];
	       int len = 0;
	       while ( (len = in.read(b)) != -1) {
	          out.write(b, 0, len);
	          out.flush();
	       }
	       byte[] retByte = out.toByteArray();
	       out.close();
	       in.close();
	       return retByte;
	   }catch(Exception e){
		   e.printStackTrace();
		   return null;
	   }
   }
   /**
    * 设置二进制数据
    * @param sm 批处理
    * @param index 序号
    * @param bytes 二进制数据
    * void
    */
   public static void setByte(PreparedStatement sm,int index,byte[] bytes){
	   java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(bytes);
	   try {
		   sm.setBinaryStream(index,in,in.available());
		}catch (SQLException e) {
			e.printStackTrace();
		}
   }
   /**
    * 获得大字符串
    * @param rs 结果集
    * @param fn 字段
    * @return ���ַ�
    */
   public static String getBigString(ResultSet rs,String fn){
	   try{
		   Reader reader =rs.getCharacterStream(fn);
	       char buffer[] =new char [1024]; 
	       int len=0;  
	       StringWriter fw =new StringWriter();
	       while ((len=reader.read(buffer))>0){ 
	             fw.write(buffer,0,len);  
	       }
	       String ret = new String(fw.getBuffer());
	       fw.close();  
	       reader.close();  
	       return ret;
	   }catch(Exception e){
		   return "";
	   }
   }
}
