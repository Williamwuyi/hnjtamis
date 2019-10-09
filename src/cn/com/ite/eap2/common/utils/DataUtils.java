package cn.com.ite.eap2.common.utils;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *<p>Title:com.ite.oxhide.common.util.DataUtils</p>
 *<p>Description:数据操作工具</p>
 *<p>Copyright: Copyright (c) 2007</p>
 *<p>Company: ITE</p>
 * @author 宋文科
 * @version 1.0
 * @date 2007-6-4
 *
 * @modify 
 * @date
 */
public class DataUtils {
	/**
	 * POJO对象复制
	 * @param object 对象
	 * @param classType 对象类型
	 * @return POJO对象
	 * @throws Exception
	 */
	public static Object copyPoJo(Object object,Class classType) throws Exception {
		if (object == null)
			return null;
		Object objectCopy = classType.getConstructor(new Class[] {})
				.newInstance(new Object[] {});
		Field fields[] = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getMethodName = ((!field.getType().getName().equals(
					"java.lang.Boolean") && !field.getType().getName().equals(
					"boolean")) ? "get" : "is")
					+ firstLetter + fieldName.substring(1);
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			try{
			Method getMethod = classType.getMethod(getMethodName,
					new Class[] {});
			Method setMethod = classType.getMethod(setMethodName,
					new Class[] { field.getType() });
			if (field.getType().getName().equals("java.lang.String")
					|| field.getType().getName().equals("java.lang.Boolean")
					|| field.getType().getName().equals("java.lang.Long")
					|| field.getType().getName().equals("java.lang.Integer")
					|| field.getType().getName().equals("java.lang.Double")
					|| field.getType().getName().equals("java.lang.Float")
					|| field.getType().getName().equals("java.math.BigInteger")
					|| field.getType().getName().equals("java.util.Date")
					|| field.getType().getName().equals("java.sql.Date")
					|| field.getType().getName().equals("int")
					|| field.getType().getName().equals("long")
					|| field.getType().getName().equals("float")
					|| field.getType().getName().equals("double")
					|| field.getType().getName().equals("boolean")) {
				Object value = getMethod.invoke(object, new Object[] {});
				setMethod.invoke(objectCopy, new Object[] { value });
			} else if (field.getType().getName().equals("java.util.Set")) {
				setMethod.invoke(objectCopy, new Object[] { null });
			} else {
				Object value = getMethod.invoke(object, new Object[] {});
				setMethod.invoke(objectCopy, copyPoJo2(value,field.getType()));
			}
			}catch(Exception e){}
		}
		return objectCopy;
	}
	/**
	 * 判断字符串src是否在数组list中
	 * @param list 数据
	 * @param src 字符串
	 * @return
	 */
	public static boolean contains(String[] list, String src){
		if(list==null)
			return false;
		for(int i=0;i<list.length;i++){
			if(list[i].equals(src))
				return true;
		}
		return false;
	}
	
	/**
	 * POJO对象复制
	 * @param object 对象
	 * @param classType 对象类型
	 * @return POJO对象
	 * @throws Exception
	 */
	private static Object copyPoJo2(Object object,Class classType) throws Exception {
		if (object == null)
			return null;
		Object objectCopy = classType.getConstructor(new Class[] {})
				.newInstance(new Object[] {});
		Field fields[] = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getMethodName = ((!field.getType().getName().equals(
					"java.lang.Boolean") && !field.getType().getName().equals(
					"boolean")) ? "get" : "is")
					+ firstLetter + fieldName.substring(1);
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			try{
			Method getMethod = classType.getMethod(getMethodName,
					new Class[] {});
			Method setMethod = classType.getMethod(setMethodName,
					new Class[] { field.getType() });
			if (field.getType().getName().equals("java.lang.String")
					|| field.getType().getName().equals("java.lang.Boolean")
					|| field.getType().getName().equals("java.lang.Long")
					|| field.getType().getName().equals("java.lang.Integer")
					|| field.getType().getName().equals("java.lang.Double")
					|| field.getType().getName().equals("java.lang.Float")
					|| field.getType().getName().equals("java.math.BigInteger")
					|| field.getType().getName().equals("java.util.Date")
					|| field.getType().getName().equals("java.sql.Date")
					|| field.getType().getName().equals("int")
					|| field.getType().getName().equals("long")
					|| field.getType().getName().equals("float")
					|| field.getType().getName().equals("double")
					|| field.getType().getName().equals("boolean")) {
				Object value = getMethod.invoke(object, new Object[] {});
				setMethod.invoke(objectCopy, new Object[] { value });
			} else if (field.getType().getName().equals("java.util.Set")) {
				setMethod.invoke(objectCopy, new Object[] { null });
			} else {
				Object value = getMethod.invoke(object, new Object[] {});
				setMethod.invoke(objectCopy, new Object[] { null });
			}
			}catch(Exception e){}
		}
		return objectCopy;
	}

	/**
	 * POJO对象复制
	 * @param list 原POJO对象组
	 * @param classType 对象类型
	 * @return 复制POJO对象组
	 */
	public static List copyPoJos(List list,Class classType) {
		if(list==null)
			return null;
		Iterator iterator = list.iterator();
		List returnColl = new ArrayList();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			try {
				returnColl.add(copyPoJo(object,classType));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnColl;
	}
	/**
	 * 获得集合中指定对象属性的数组
	 * @param coll 集合
	 * @param field 对象属性
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static String[] getElementsOfCollection(java.util.Collection coll,String field){
		Object[] obs=null;
		List list=new ArrayList();
		Iterator iterator=coll.iterator();
		while(iterator.hasNext()){
			Object ob=iterator.next();
			Method method;
			try {
				method = ob.getClass().getMethod("get"+field.substring(0,1).toUpperCase()+field.substring(1), null);
				list.add((String)method.invoke(ob, null));
			} catch (Exception e) {} 
		}
		return (String[])list.toArray(new String[0]);
	}
	/**
	 * 数组变成以分割符相连的字符串
	 * @param obs 数组
	 * @param stuff 分割符
	 * @return
	 */
	public static String collectionToString(Object[] obs,String stuff){
		String str=stuff;
		for(int i=0;i<obs.length;i++){
			str+=obs[i]+stuff;
		}
		return str;
	}
	/**
	 * 数组变成以分割符相连的字符串 支持数组和集合
	 * @param obs 数组
	 * @param stuff 分割符
	 * @return
	 */
	public static String collectionToStringEx(Object ob,String stuff){
		String str="";
		if(ob instanceof Collection){
			Collection list=(Collection)ob;
			Iterator iterator=list.iterator();
			while(iterator.hasNext()){
			   if(str.equals(""))
				   str=(String)iterator.next();
			   else
				   str+=stuff+(String)iterator.next();
			}
		}else
		if(ob.getClass().isArray()){
			Object[] array=(Object[])ob;
			for(int i=0;i<array.length;i++){
				if(str.equals(""))
					   str=(String)array[i];
				   else
					   str+=stuff+(String)array[i];
			}
		}
		return str;
	}
	
	/**
	 * 字节数组传成对象
	 * @param bytes
	 * @return 对象
	 */
	public static Object bytesToObject(byte[] bytes){
		XMLDecoder decoder = new XMLDecoder(new java.io.ByteArrayInputStream(bytes));
		decoder.setExceptionListener(exceptionListener);
        return decoder.readObject();
	}
	/**
	 * 对象传成字节数组
	 * @param ob 对象
	 * @return 字节数组
	 */
	public static byte[] objectToBytes(Object ob){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(out);
		encoder.setExceptionListener(exceptionListener);
		encoder.writeObject(ob);
		encoder.close();
		return out.toByteArray();
	}
	/**
	 * 对象序列化异常监听器
	 */
	static ExceptionListener exceptionListener = new ExceptionListener() {
        public void exceptionThrown(Exception e) {
        	//对象序号化中错误不输出
        }
    };
	/**
	 * 从map中取数据
	 * @param map
	 * @param key
	 * @return
	 */
	public static String mapget(Map map, String key){
		if(map==null)
			return "";
		return (String)map.get(key);
	}
	/**
	 * 判断为空
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value){
		if(value==null||value.equals(""))
			return true;
		else
			return false;
	}
	
	/**
	 * excel文件导入通用方法
	 * @param sheetIndex 第几个表
	 * @param sheetName 表名称
	 * @param xs 行序号集
	 * @param ys 列序号集
	 * @param obs 单元格数据
	 * @param fileOs 输出流文件
	 */
	public static void jxlWrite(int sheetIndex,String sheetName,int[] xs,int[] ys,
			Object[] obs,OutputStream fileOs){
		try {
            if (sheetIndex < 0)
				sheetIndex = 0;
			WritableWorkbook bookWrite = Workbook.createWorkbook(fileOs);
			WritableSheet sheet = bookWrite.createSheet(sheetName, sheetIndex);
			for (int i = 0; i < xs.length; i++) {
				if (obs[i] instanceof String) {
					Label label = new Label(xs[i], ys[i], (String)obs[i]);
					sheet.addCell(label);
				} else if (obs[i] instanceof Integer
						|| obs[i] instanceof Double || obs[i] instanceof Float
						|| obs[i] instanceof Long) {
					jxl.write.Number number = new jxl.write.Number(xs[i],
							ys[i], (Double) obs[i]);
					sheet.addCell(number);
				}
			}
			bookWrite.write();
			bookWrite.close(); 
        } catch (Exception e) {   
            e.printStackTrace();   
        }
	}
	
	/**
	 * excel文件导出通用方法
	 * @param sheetIndex 第几个表
	 * @param xs 行序号集
	 * @param ys 列序号集
	 * @param startRowIndex 开始读的行号
	 * @param startColumnIndex 开始读的列号
	 * @param obs 单元格数据
	 * @param fileIS 模板文件
	 */
	public static void jxlRead(int sheetIndex,int startRowIndex,int startColumnIndex,
			List xs,List ys,List obs,InputStream fileIS){
		try {
            Workbook bookRed= Workbook.getWorkbook(fileIS);
                Sheet sheet = bookRed.getSheet(sheetIndex);
                for(int i=startRowIndex;i<sheet.getRows();i++){
                	for(int j=startColumnIndex;j<sheet.getColumns();j++){
                		Cell cell = sheet.getCell(i, j);   
                        String result = cell.getContents();
                        xs.add(i);
                        ys.add(j);
                        obs.add(result);
                	}
                }
                bookRed.close(); 
        } catch (Exception e) {   
            e.printStackTrace();   
        }
	}
    //	测试
	public static void main(String[] args) throws Exception{
		File fos=new File("c:/dbf.dbf");
		/*List<Object[]> datas=DataUtils.dbfRead(new java.io.FileInputStream(fos));
		String value=(String)datas.get(0)[0];
		System.out.println(value);*/
		FileOutputStream fo=new FileOutputStream(fos);
		//DataUtils.dbfWrite(new String[]{"名称","姓别2"}, new Object[][]{{"朋友啊朋士大夫是 友","的的的但是是否是"},{"在要对人对的","的是上的是说"}},fo);
		fo.close();
	}
}
