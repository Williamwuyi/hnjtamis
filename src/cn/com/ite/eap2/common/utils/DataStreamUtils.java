package cn.com.ite.eap2.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * <p>Title cn.com.ite.eap2.common.utils.DataStreamUtils</p>
 * <p>Description 数据流处理工具类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 1:26:22 PM
 * @version 2.0
 * 
 * @modified records:
 */
public class DataStreamUtils {
	/**
	 * 二进制数据转对象
	 * @param bytes 二进制数据
	 * @return 对象
	 */
	public static Object bytesToObject(byte bytes[]){
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		ObjectInputStream decoder = null;
		try{
			decoder = new ObjectInputStream(stream);
		    Object ob = decoder.readObject();
			stream.close();
			decoder.close();
			return ob;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 对象转二进制，与bytesToObject对应使用
	 * @param ob 对象
	 * @return  二进制数据
	 * byte[]
	 */
	public static byte[] objectToBytes(Object ob){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream encoder = null;
		try{
			encoder = new ObjectOutputStream(out);
			encoder.writeObject(ob);
			encoder.close();
			byte[] bytes = out.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
