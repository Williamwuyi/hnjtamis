package cn.com.ite.hnjtamis.excel.util;



import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.DateCell;

import org.apache.commons.lang.StringUtils;




/** 
 * <p> title:数据导入</p>
 * <p> Description:字段格式化</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2008-1-4 上午09:44:33
 */
public final class ColumnFormatUtil {
	
	
	/**
	 * 格式格字段
	 * @param cell
	 * @return
	 */
	/*public static String formatExcelColumn(Cell cell,ImportColumnInfo colvo){ 
		String value = "";
		if (cell.getType() == CellType.DATE) {
			DateCell dcell = (DateCell) cell;// 字期型格式需转换
			value = formatExcelDateColumn(dcell);
		} else
			value = cell.getContents().trim();// 取到Excel文件中相应值 除日期型外，其他都转为String型
		
		
		return value;
	}*/
	
	/**
	 * 根据规则定义的字段类型与Excel读取的值作类型解析
	 * 
	 * @param coltype
	 * @param value
	 * @return String
	 */
	public static String parseValueType(String coltype, String value) {
		String retvalue = value;
		if (StringUtils.isEmpty(coltype))
			return value;
		else if (coltype.indexOf(ColumnType.VARCHAR2) != -1 || coltype.indexOf(ColumnType.CHAR) != -1)
			retvalue = "'" + value + "'";
		else if (coltype.indexOf(ColumnType.INTEGER) != -1
				|| coltype.indexOf(ColumnType.NUMBER) != -1)
			retvalue = StringUtils.isEmpty(value)?"0":value;
		else if (coltype.indexOf(ColumnType.DATE) != -1) { 
			String plat = "yyyy-mm-dd";
			retvalue = "to_date('" + value + "','" + plat + "')";
		}
		return retvalue;
	}
	/**
	 * 格式日期数据
	 * @param dcell
	 * @return
	 */
	public static String formatExcelDateColumn(DateCell dcell){
		return formatExcelDateColumn(dcell,"yyyy-MM-dd");
	}
	/**
	 * 通过规定格式，格式化日期型数据
	 * @param dcell
	 * @param plant
	 * @return
	 */
	public static String formatExcelDateColumn(DateCell dcell,String plant){
		return formatDate(dcell.getDate(), plant);
	}
	
	/**
     * 字符转换日期(动态格式转换)
     * @param date_str 字符串时间
     * @param type     类型
     * @return 时间
     */
	public static String formatDate(java.util.Date date, String formatstr) {
		if (date == null || formatstr == null)
			return null;
		java.text.SimpleDateFormat dateFormat = new SimpleDateFormat(formatstr);
		return dateFormat.format(date);
	}
}
