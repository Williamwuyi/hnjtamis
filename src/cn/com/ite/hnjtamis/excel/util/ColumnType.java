package cn.com.ite.hnjtamis.excel.util;

/** 
 * <p> title:数据导入</p>
 * <p> Description:数据导入字段类型</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2008-1-3 下午06:46:44
 */
public final class ColumnType {
	public static final String VARCHAR2 = "VARCHAR2"; 	// 不定长字符 
	public static final String INTEGER = "INT"; 	// 数据型 
	public static final String NUMBER = "NUMBER"; 		// 数据型 
	public static final String DATE = "DATE"; 			// 日期型 
	public static final String CHAR = "CHAR"; 			// 定长字符型 
	
	//执行状态
	public static final int EXEC_READY = 0; // 准备
	public static final int EXEC_START = 1; // 开始
	public static final int EXEC_ING = 2; // 执行中
	public static final int EXEC_END = 3; // 执行结束
}
