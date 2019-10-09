package cn.com.ite.hnjtamis.excel.util;


import java.util.List;

import jxl.format.Colour;
import jxl.write.WritableSheet;

/**
 * 
 * <p> title:Excel表头操作</p>
 * <p> Description:</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author nick's
 * @version 1.0
 * @builder date: 2007-6-14 下午02:49:55
 */
public interface ExcelHeader {
	/**
     * 设置工作表的表头，并返回当前行编号
     * @param sheet 工作表
     * @return 行编号
     */
    public int setHeader(WritableSheet sheet)throws Exception;
    /**
     * 设置是否自动换行，true:换行；false：不换行。
     * @param wrap
     */
    public void setWrap(boolean wrap);
    /**
     * 设置起始行编号
     * @param startRowCount 起始行编号
     */
    public void setStartRowCount(int startRowCount);
    /**
     * 设置表头内容
     * @param headerContentList 存储ExcelHeaderField的集合
     */
    public void setHeaderContentList(List headerContentList);
    /**
     * 设置是否粗体显示表头
     * @param bold true:粗体；false:非粗体。
     */
    public void setBold(boolean bold);
    /**
     * 设置字体大小
     * @param fontSize 字体大小
     */
    public void setFontSize(int fontSize);
    /**
     * 设置行高
     * @param rowHeight 行高度
     */
    public void setRowHeight(int rowHeight);
    /**
     * 设置背景颜色
     * @param backGroundColour
     */
    public void setBackGroundColour(Colour backGroundColour);
}
