package cn.com.ite.hnjtamis.excel.util;


import java.io.OutputStream;
import java.util.List;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public interface ExcelBuilder {
	/**
     * 设置工作表
     * @param sheetName 表名称
     * @param index 表下标
     */
    public void createSheet(String sheetName,int index);
    /**
     * 设置工作表表头
     * @param header 工作表表头
     */
    public void setExcelHeader(ExcelHeader header,int index);//设置工作表的表头
    /**
     * 设置工作表的记录内容
     * @param recordsList 存储ExcelRecord的集合
     */
    public void setExcelRecords(List recordsList,int index);//设置工作表的内容
    /**
     * 设置工作表的标题
     * @param title 标题内容
     */
    public void setExcelTitle(ExcelTitle title,int index);//设置工作表的标题
    
    public void setExcelImage(List imageList,int index);//设置图片
    /**
     * 构建工作簿
     * @throws Exception
     */
    public void constructExcel()throws Exception;//构建工作簿
    /**
     * 得到构建好的工作簿
     * @return 工作簿
     * @throws Exception
     */
    public WritableWorkbook getExcelWorkbook()throws Exception;//得到工作簿
    /**
     * 得到工作簿
     * @param workbook
     * @param os
     */
    public void closeWorkbook(WritableWorkbook workbook,OutputStream os);
    
    public WritableWorkbook addToXls(WritableSheet sheet,int addtoCount) throws Exception;
}
