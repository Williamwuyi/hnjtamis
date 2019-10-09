package cn.com.ite.hnjtamis.excel.util;


import jxl.format.Colour;

/**
 * 表头对象VO
 * <p> title:</p>
 * <p> Description:</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2007-7-1 下午01:09:16
 */
public class ExcelTitle {
	private String title = null;
    private boolean bold = false;
    private int fontSize = 20;
    private Colour frontColor = null;
    private boolean border = false;
    private int rowHeight = 500;
    private int colNum = 1;

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Colour getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(Colour frontColor) {
        this.frontColor = frontColor;
    }

    public boolean isBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
}
