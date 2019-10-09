package cn.com.ite.hnjtamis.excel.util;


import java.util.ArrayList;
import java.util.List;
 
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

/**
 * <p> title:表头部分的逻辑处理</p>
 * <p> Description:</p>
 * <p> copyright:copyright (c) 2007</p>
 * <p> Company:ITE co.,Ltd</p>
 * @author wangyong
 * @version 1.0
 * @builder date: 2007-8-19 下午06:29:07
 */
public class CommonExcelHeader implements ExcelHeader {

	protected List headerContentList = null;
	protected boolean bold = false;
	protected int fontSize = 9;
	protected int rowHeight = 400;
	protected boolean resetRowHeight = true;
	protected Colour fontColour = null;// 字体颜色
	protected Colour backGroundColour = null;// 背景颜色
	protected WritableFont headerFont = null;//表头固定格式
	protected WritableCellFormat headerCellFormat = null;//表头单元格格式
	protected int startRowCount = 1;//设置起始行下标
	protected static int startColCount = 0; //设置起始列
	protected boolean wrap = true;//设置是否自动换行
    protected void setFont()throws Exception{
        if(bold){
            headerFont = new WritableFont(WritableFont.ARIAL,fontSize,WritableFont.BOLD);
        }
        else{
            headerFont = new WritableFont(WritableFont.ARIAL,fontSize);
        }
        if(fontColour!=null){
        	headerFont.setColour(fontColour);
        }
        headerCellFormat = new WritableCellFormat(headerFont);

        if(backGroundColour != null){
            headerCellFormat.setBackground(backGroundColour);//设置背景颜色
        }
        headerCellFormat.setBorder(Border.ALL,BorderLineStyle.THIN);
        headerCellFormat.setWrap(this.wrap);

    }
    /**
     * 设置表头内容
     * @param sheet 表格对象
     * @return 设置行号
     * @throws Exception
     */
    public int setHeader(WritableSheet sheet)throws Exception {

    	if(headerContentList == null) headerContentList = new ArrayList();
    	int rowSpan = 1;
        for(int i = 0 ; i < headerContentList.size(); i ++){
            ExcelHeaderField field = (ExcelHeaderField)headerContentList.get(i);//得到列内容 
            
            String headerName = field.getFiledName();
            if (field.getRowSpan() > rowSpan)
            	rowSpan = field.getRowSpan();
            
            this.setFont();//设置表头显示的样式
            if(!field.isBorder()){
            	headerCellFormat.setBorder(Border.ALL,BorderLineStyle.NONE);//设置边框线颜色 
            }
            if(field.isNotTopBorder()){
            	headerCellFormat.setBorder(Border.TOP,BorderLineStyle.NONE);//设置边框线颜色 
            }
            if(field.isNotButtomBorder()){
            	headerCellFormat.setBorder(Border.BOTTOM,BorderLineStyle.NONE);//设置边框线颜色 
            }
            headerCellFormat.setAlignment(field.getAlign()); 
            headerCellFormat.setVerticalAlignment(field.getVerlign()); 
            
            if(field.isDouble(headerName)){
            	sheet.addCell(new jxl.write.Number(i,this.startRowCount,Double.parseDouble(headerName),headerCellFormat));
            }else{
            	 sheet.addCell(new jxl.write.Label(i,this.startRowCount,headerName,headerCellFormat));
            }
           
            sheet.setColumnView(i,field.getFieldWidth());
            if(field.getFieldHight()!=-1){
           	  sheet.setRowView(i, field.getFieldHight());
           }
            
        }
        sheet.setRowView(this.startRowCount,this.rowHeight);
        //System.out.println(">>>"+startRowCount+rowSpan);
        return (this.startRowCount + rowSpan);
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public void setStartRowCount(int startRowCount) {
        this.startRowCount = startRowCount;
    }

    public void setHeaderContentList(List headerContentList) {
        this.headerContentList = headerContentList;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public void setBackGroundColour(Colour backGroundColour) {
        this.backGroundColour = backGroundColour;
    }
	public void setFontColour(Colour fontColour) {
		this.fontColour = fontColour;
	}
	public void setResetRowHeight(boolean resetRowHeight) {
		this.resetRowHeight = resetRowHeight;
	}

}
