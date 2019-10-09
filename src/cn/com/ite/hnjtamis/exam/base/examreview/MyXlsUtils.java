package cn.com.ite.hnjtamis.exam.base.examreview;

import java.io.File;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;

import cn.com.ite.eap2.common.utils.StringUtils;
import jxl.*;
import jxl.write.*;

/**
 * 
 * <p>Title cn.com.ite.eap2.common.utils.XlsUtils</p>
 * <p>Description Excel工具类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-23 下午01:47:42
 * @version 2.0
 * 
 * @modified records:
 */
public class MyXlsUtils {
	private MyXlsUtils(){}
	/**
	 * 读EXCEL文件到指定类型的对象中
	 * @param xls EXCEL文件
	 * @param tagName 标签名
	 * @param clazz 对象类型
	 * @param colMap EXCEL的列对对象属性的映射
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public static List read(File xls,String tagName,Class clazz,String[] colMap) throws Exception{
		 Workbook wb = null;
		 try{
			 wb = Workbook.getWorkbook(xls);
		 }catch(Exception e){
			 throw new Exception("只能选择EXCEL文件！");
		 }
		 //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
         Sheet sheet = wb.getSheet(tagName);
         if(sheet==null)
        	 throw new Exception("找不到名为\\\""+tagName+"\\\"的EXCEL页签内容！");
         //得到当前工作表的行数
         int rowNum = sheet.getRows();
         List rets = new ArrayList();
         for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
             //得到当前行的所有单元格
             Cell[] cells = sheet.getRow(j);
             Map linkMap = new HashMap();
             Object newObject = clazz.newInstance();
             for(int x=0;x<cells.length;x++){
            	 Object object = newObject;
            	 String columnName = colMap[x];
            	 String content = cells[x].getContents();
            	 if(StringUtils.isEmpty(content)) continue;
            	 if(columnName.indexOf(".")>0){//级联属性的处理
            		 String[] columns = columnName.split("\\.");
            		 if(columns.length>2) 
            			 throw new Exception("\\\""+columnName
            					 +"\\\"属性中有两级以上关联，目前只支持两级关联对象引用！");
            		 Object link = linkMap.get(columns[0]);
            		 if(link==null){
            			 link = PropertyUtils.getPropertyType(object, columns[0]).newInstance();
            			 linkMap.put(columns[0], link);
            		 }
            		 PropertyUtils.setProperty(object, columns[0], link);
            		 object = link;
            		 columnName = columns[1];
            	 }
            	 Class fieldClass = PropertyUtils.getPropertyType(object, columnName);
            	 if(fieldClass==null)
            		 throw new Exception(object.getClass()+"中无"+columnName+"属性！");
            	 //Object fieldValue = PropertyUtils.getProperty(object, columnName);
            	 CellType cellType = cells[x].getType();
        		 if(fieldClass.isAssignableFrom(String.class))
        			 PropertyUtils.setProperty(object, columnName, content);
        		 else if(fieldClass.isAssignableFrom(Date.class)){
        			 if(cellType.equals(CellType.DATE)){//日期类型
            			 DateCell dateCell = (DateCell)cells[x];
            			 PropertyUtils.setProperty(object, columnName, dateCell.getDate());
            		 }else if(cellType.equals(CellType.DATE_FORMULA)){
            			 DateFormulaCell dateFormulaCell = (DateFormulaCell)cells[x];
            			 PropertyUtils.setProperty(object, columnName, dateFormulaCell.getDate());
            		 }
        		 }else if(fieldClass.getName().equals("java.lang.Boolean")||fieldClass.getName().equals("boolean")){//布尔类型
        			 if(cellType.equals(CellType.BOOLEAN)){
            			 BooleanCell booleanCell = (BooleanCell)cells[x];
            			 PropertyUtils.setProperty(object, columnName, booleanCell.getValue());
            		 }else if(cellType.equals(CellType.BOOLEAN_FORMULA)){
            			 BooleanFormulaCell booleanFormulaCell = (BooleanFormulaCell)cells[x];
            			 PropertyUtils.setProperty(object, columnName, booleanFormulaCell.getValue());
            		 }
        		 }else{//数字类型
        			 double d = 0;
        			 if(cellType.equals(CellType.NUMBER)){
            			 NumberCell numberCell = (NumberCell)cells[x];
            			 d = numberCell.getValue();
            		 }else if(cellType.equals(CellType.NUMBER_FORMULA)){
            			 NumberFormulaCell numberFormulaCell = (NumberFormulaCell)cells[x];
            			 d = numberFormulaCell.getValue();            			 
            		 }
        			 if(fieldClass.getName().equals("java.lang.Integer")||
        				fieldClass.getName().equals("int")){
        				 PropertyUtils.setProperty(object, columnName, new Double(d).intValue());
        			 }else if(fieldClass.getName().equals("java.lang.Long")||
        					  fieldClass.getName().equals("long")){
        				 PropertyUtils.setProperty(object, columnName, new Double(d).longValue());
        			 }else if(fieldClass.getName().equals("java.lang.Float")||
        					  fieldClass.getName().equals("float")){
        				 PropertyUtils.setProperty(object, columnName, new Double(d).floatValue());
        			 }else
        				 PropertyUtils.setProperty(object, columnName, d);
        		 }
             }
             rets.add(newObject);
         }
         wb.close();
         return rets;
	}
	private WritableWorkbook wwb;
	/**
	 * 创建写的EXCEL工具类
	 * @param xls
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public static MyXlsUtils createWrite(File xls) throws Exception{
		MyXlsUtils utils = new MyXlsUtils();
		utils.wwb = Workbook.createWorkbook(xls);
		return utils;
	}
	/**
	 * 关闭EXCEL写操作
	 * @throws Exception
	 * @modified
	 */
	public void closeWrite() throws Exception{
		try{
			wwb.write();
		}catch(Exception e){throw e;}
		finally{
		  wwb.close();
		}
	}
	/**
	 * 把数据写EXCEL文件
	 * @param xls EXCEL文件
	 * @param tagName 标签名
	 * @param list 数据
	 * @param colMap EXCEL的列对对象属性的映射
	 * @param titles 标题行
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void write(int tagIndex,String tagName,Collection coll,String[] colMap,String[] titles,int[] columnsWitdh) throws Exception{
	    WritableSheet sheet = wwb.getSheet(tagName);	    
		if(sheet==null) {
			sheet = wwb.createSheet(tagName, tagIndex);
		}
		for(int i=0;i<titles.length;i++){//加标题
			jxl.write.Label label = new jxl.write.Label(i, 0, titles[i]);
			sheet.addCell(label);
			sheet.setColumnView(i, columnsWitdh[i]);
		}
		WritableFont fontTitle = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);  
		//fontTitle.setColour(jxl.format.Colour.RED);  
		WritableCellFormat formatTitle = new WritableCellFormat(fontTitle);  
		formatTitle.setWrap(true);  
		//数据
		Iterator iterator = coll.iterator();
		int i = 0;
		while(iterator.hasNext()){
			Object item = iterator.next();
			for(int j=0;j<colMap.length;j++){
				String col = colMap[j];
				Class fieldClass = null;
				Object fieldValue = null;
				try{
					fieldClass = PropertyUtils.getPropertyType(item, colMap[j]);
					fieldValue = PropertyUtils.getProperty(item, col);
				}catch(Exception e){}
				if(fieldClass==null||fieldValue==null){
					jxl.write.Label data = new jxl.write.Label(j, i+1, "");
					sheet.addCell(data);
					continue;
				}
				if(fieldClass.isAssignableFrom(String.class)){//字符串
					jxl.write.Label data = new jxl.write.Label(j, i+1, (String)fieldValue,formatTitle);
					sheet.addCell(data);
				}else if(fieldClass.isAssignableFrom(java.lang.Boolean.class)
						||fieldValue instanceof java.lang.Boolean){//布尔
					jxl.write.Boolean labelB = new jxl.write.Boolean(j, i+1, (java.lang.Boolean)fieldValue,formatTitle);
					sheet.addCell(labelB);
				}else if(fieldClass.isAssignableFrom(Date.class)){//日期
					jxl.write.DateTime labelDT = new jxl.write.DateTime(j, i+1, (Date)fieldValue,formatTitle);
					sheet.addCell(labelDT);
				}else{//数字
					jxl.write.Number labelnumber = new jxl.write.Number(j, i+1, 
							((java.lang.Number)fieldValue).doubleValue(),formatTitle);
					sheet.addCell(labelnumber);
				}
			}
			i++;
		}
	}
}