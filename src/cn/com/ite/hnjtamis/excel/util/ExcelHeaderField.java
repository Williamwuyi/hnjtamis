package cn.com.ite.hnjtamis.excel.util;


import jxl.format.Alignment;
import jxl.format.VerticalAlignment;

/**
 * 表头字段信息VO
 * <p> title:</p>
 * <p> Description:</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2007-8-16 下午05:51:37
 */
public class ExcelHeaderField extends CommonExcelHeader {
	private String filedName = "";
    private int fieldWidth = 10;
    private int fieldHight = -1;
    private Alignment align = Alignment.LEFT;//水平对齐方式
    private VerticalAlignment verlign = VerticalAlignment.BOTTOM;//垂直对齐方式
    private int colSpan = 0; //字段的colSpan属性
    private int rowSpan = 0; //字段的rowspan属性
    private int row = 0 ; //单元格所在行
    private int col = 0 ; //单元格所在列
    private boolean border = true;	//默认带浅色边框
    private int fontSize = 9;
    private boolean bold=false;//是否设置粗体
    private boolean notTopBorder=false;//设置无上部边框
    private boolean notButtomBorder=false;//设置无下部边框
     

    
    public int getFieldHight() {
		return fieldHight;
	}

	public void setFieldHight(int fieldHight) {
		this.fieldHight = fieldHight;
	}

	public static boolean isDouble(String value){
		try{
			Double.parseDouble(value);
			if(value!=null && !value.equals(value.trim())){
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
    
	public boolean isNotButtomBorder() {
		return notButtomBorder;
	}

	public void setNotButtomBorder(boolean notButtomBorder) {
		this.notButtomBorder = notButtomBorder;
	}

	public boolean isNotTopBorder() {
		return notTopBorder;
	}

	public void setNotTopBorder(boolean notTopBorder) {
		this.notTopBorder = notTopBorder;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public VerticalAlignment getVerlign() {
        return verlign;
    }

    public void setVerlign(VerticalAlignment verlign) {
        this.verlign = verlign;
    }

    public Alignment getAlign() {
        return align;
    }

    public void setAlign(Alignment align) {
        this.align = align;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public boolean isBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
