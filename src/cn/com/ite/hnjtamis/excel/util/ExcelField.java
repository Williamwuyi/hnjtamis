package cn.com.ite.hnjtamis.excel.util;


public class ExcelField {
//	private String value = "";	//值
//	private String dataType = "";	//数据类型
	
	private int rowSpan = 1;
	private int colSpan = 1;
	
	public int getColSpan() {
		return colSpan<1?1:colSpan;
	}
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}
	@Deprecated
	public int getRowSpan() {
		return rowSpan<1?1:rowSpan;
	}
	
	@Deprecated
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan; 
	} 
	
}
