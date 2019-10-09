package cn.com.ite.hnjtamis.excel.util;


import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
/**
 * <p> title:数据导入</p>
 * <p> Description:EXCEL数据导入控制</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2008-1-4 下午02:19:28
 */
public class ExcelUtil {
	public static final int rows = 10000 ;	//Xls文件，每页最多行数 
	public static final int xlsPages = 2;	//每个Xls文件显示的页数
	public static final String empmapKey = "emp_";	//人员数据MAP健值
	public static final String tmpfilename = "c:\\temp";//导出的临时文件路径和名称
	public static final String tmpfileExtName = ".xls";
	
	public static Sheet getFirstSheet(InputStream is) throws DataImportException{
		Sheet sheet = null;
		try {
			Workbook book = Workbook.getWorkbook(is);
			// 获得excel工作簿
			sheet = book.getSheet(0);// 获得当前工作簿的excel表
		} catch (IOException e) { 
			e.printStackTrace();
			throw new DataImportException("IO异常：" + e.getMessage()); 
		} catch (BiffException e) {
			e.printStackTrace();
			throw new DataImportException("处理EXCEL文件错误:：" + e.getMessage()); 
		}
		return sheet;
	}
	
	
}
