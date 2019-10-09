package cn.com.ite.hnjtamis.excel.util;


import java.util.ArrayList;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

/**
 * 导出复杂表头
 * <p> title:</p>
 * <p> Description:</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2007-8-19 下午06:04:27
 */
public class ExcelComplexHeaderField extends CommonExcelHeader {
	
	/**
	 * 需要实现复杂的、如合并单元格的表头逻辑处理
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	public int setHeader(WritableSheet sheet) throws Exception {
		if(headerContentList == null) headerContentList = new ArrayList();
        int nextRow = startRowCount; //表头后面记录的起始行
        int maxRow = startRowCount; //设置表头最大行(即表头将会占用的最大行数，从默认起始行开始)
       // System.out.println("headerContentList::" + headerContentList.size());
        for(int i = 0 ; i < headerContentList.size(); i++){
            ExcelHeaderField field = (ExcelHeaderField)headerContentList.get(i);//得到列内容
            //System.out.println("field::" + field.getFiledName());
            String headerName = field.getFiledName();
            fontSize = field.getFontSize();
            this.setFont();//设置表头显示的样式
            headerCellFormat.setAlignment(field.getAlign());
            headerCellFormat.setVerticalAlignment(field.getVerlign());
            boolean isBorder = field.isBorder();
            if(!isBorder){//无边框
            	headerCellFormat.setBorder(Border.ALL,BorderLineStyle.NONE);//设置边框线颜色 
            }
            boolean bold=field.isBold();
            if(bold || super.bold){//加入设置了全局加粗体或单元格加粗体均加粗体
            	this.headerFont.setBoldStyle(WritableFont.BOLD);
            }else{
            	this.headerFont.setBoldStyle(WritableFont.NO_BOLD);
            }
            
            if(field.isNotTopBorder()){
            	headerCellFormat.setBorder(Border.TOP,BorderLineStyle.NONE);//设置边框线颜色 
            }
            if(field.isNotButtomBorder()){
            	headerCellFormat.setBorder(Border.BOTTOM,BorderLineStyle.NONE);//设置边框线颜色 
            }
            
            if(field.backGroundColour!=null && !"".equals(field.backGroundColour) && !"null".equals(field.backGroundColour)){
            	headerCellFormat.setBackground(field.backGroundColour);
            }
            if(field.fontColour!=null && !"".equals(field.fontColour) && !"null".equals(field.fontColour)){
            	headerFont.setColour(field.fontColour);
            }
            
            
            int thiscol = field.getCol()<=1?0:(field.getCol()-1);
            int thisrow = (field.getRow()<=1)?0:(field.getRow()-1);
            int colspan = field.getColSpan()<=1?0:(field.getColSpan()-1);
            int rowspan = field.getRowSpan()<=1?0:(field.getRowSpan()-1);

            int one = thiscol+startColCount;
            int two = thisrow+startRowCount;
            int three = thiscol+startColCount+colspan;
            int four = thisrow+startRowCount+rowspan;
//            int one = field.getCol()<=1?0:(field.getCol()-1)+startColCount;//所在单元格起始列
//            int two = (field.getRow()<=1)?0:(field.getRow()-1)+startRowCount;//所在单元格起始行
//            int three = one+field.getColSpan()<=1?0:(field.getColSpan()-1);//所在单元格终止列
//            int four = two+field.getRowSpan()<=1?0:(field.getRowSpan()-1);//所在单元格的终止行
            if(field.isDouble(headerName)){
            	sheet.addCell(new jxl.write.Number(one,two,Double.parseDouble(headerName),headerCellFormat));
            }else{
            	sheet.addCell(new jxl.write.Label(one,two,headerName,headerCellFormat));
            }
//            System.out.println("one: " + one + " | " + two + " | " + 
//            		three + " | " + four);
            sheet.setColumnView(i,field.getFieldWidth());
            if(field.getFieldHight()!=-1){
            	 sheet.setRowView(i, field.getFieldHight());
            }
            
            if((one == three) && (two == four)){
            	//System.out.println(headerName);
            }else{
            	sheet.mergeCells(one, two, 
            			three, four);//设置跨行或跨列，起始格00,结束格，10
            }
            
            if(nextRow < field.getRowSpan())
            	nextRow = nextRow + field.getRowSpan() - 1;
            if(maxRow < four)
            	maxRow = four;
            
//            System.out.println(field.getRow()+"---nextRow---->"+nextRow+"---two---->"+two);
            nextRow = nextRow >= two ? nextRow : two + field.getRowSpan() - 1;
        }
        //sheet.mergeCells(0, 1, 1, 1);//设置跨行或跨列，起始格00,结束格，10
        sheet.setRowView(startRowCount, this.rowHeight);
        if (resetRowHeight) {
	        for(int i=startRowCount+1;i<=maxRow;i++) {
	            sheet.setRowView(i,this.rowHeight);
	        }
        }
        return nextRow+1;
	} 
}
