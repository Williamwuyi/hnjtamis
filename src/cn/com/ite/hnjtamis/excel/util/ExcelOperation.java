package cn.com.ite.hnjtamis.excel.util;


import java.io.OutputStream;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class ExcelOperation {
	public static Workbook wookBook = null;
    private OutputStream outputStream = null;//输出流
    private static WritableFont font10B = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
    private static WritableFont font10 = new WritableFont(WritableFont.ARIAL, 10);

    private static WritableCellFormat wcf_maintitle = 
    	new WritableCellFormat(font10B);
    private static WritableCellFormat wcf_title = new WritableCellFormat(font10B);
    private static WritableCellFormat wcf_center = new WritableCellFormat(font10);
    private static WritableCellFormat wcf_left = new WritableCellFormat(font10);
    private static WritableCellFormat wcf_left_B = new WritableCellFormat(font10);
    private static WritableCellFormat wcf_left_A = new WritableCellFormat(font10);
    private static WritableCellFormat wcf_left_C = new WritableCellFormat(font10);

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public ExcelOperation(){

    }
    private static void setFomat()throws Exception{
        //设置单元格字体
        WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
        WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 14,
        WritableFont.BOLD);

        //设置几种格式的单元格
        //大标题
        wcf_maintitle.setBorder(Border.NONE, BorderLineStyle.THIN); //边框线条
        wcf_maintitle.setVerticalAlignment(VerticalAlignment.CENTRE); //垂直对齐
        wcf_maintitle.setAlignment(Alignment.CENTRE); //水平对齐
        wcf_maintitle.setWrap(false); //是否换行
        wcf_maintitle.setBackground(Colour.CORAL);

        //标题
        wcf_title.setBorder(Border.ALL,BorderLineStyle.THIN); //边框线条
        wcf_title.setBackground(Colour.GREY_25_PERCENT);
        wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); //垂直对齐
        wcf_title.setAlignment(Alignment.CENTRE); //水平对齐
        wcf_title.setWrap(false); //是否换行

        //中
        wcf_center.setBorder(Border.NONE, BorderLineStyle.THIN); //边框线条
        wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); //垂直对齐
        wcf_center.setAlignment(Alignment.CENTRE); //水平对齐
        wcf_center.setWrap(false); //是否换行

        //左
        wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN);
        wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
        wcf_left.setAlignment(Alignment.LEFT);
        wcf_left.setWrap(false);

        /*产生标题*/
        //WritableSheet sheet = workbook.createSheet(reportName, 0);

        /** 定义当前行指针 */
        //int currentLineNumber = 0;

        /*定义表头宽度*/
//        int[] colWidth = new int[colNameArray.length];
//        for(int k = 0; k < colNameArray.length; k++){
//        colWidth[k] = titleArray[k].length()*2 + 10;
//        }
//        /** ---------产生表格Title--------- */
//
//        sheet.mergeCells(0, 0, titleArray.length - 1, 0);
//        sheet.addCell(new Label(0, 0, reportName, wcf_maintitle));
//        currentLineNumber++;
//
//        /** ---------产生Headers--------- */
//        for (int i = 0; i < titleArray.length; i++) {
//        sheet.addCell(new Label(i, currentLineNumber, titleArray[i], wcf_title));
//        }

    }
    /*public static void writeExcel(OutputStream os,ExcelTable excelTable) throws Exception {
        jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(os);//创建工作薄
        jxl.write.WritableSheet ws = wwb.createSheet("TestSheet1", 0);//创建工作表
        List headerList = excelTable.getHeaderList();
        jxl.write.Label labelC = null;//创建表格输出内容
        WritableFont wfc = new WritableFont(WritableFont.ARIAL,excelTable.getHeaderFontSize(),WritableFont.BOLD, false);
        WritableCellFormat wcfFC = new WritableCellFormat(wfc);

        wcfFC.setBorder(Border.ALL,BorderLineStyle.THIN);//设置单元格的边框
        wcfFC.setWrap(false);//设置是否换行
        wcfFC.setAlignment(Alignment.CENTRE);//设置水平对齐方式
        wcfFC.setVerticalAlignment(VerticalAlignment.BOTTOM);//设置垂直对齐方式

        setFomat();
        ws.mergeCells(0,0,3,1);//设置工作表合并单元格,int:起始列编号,int:起始行编号,int:合并列数目,int:合并行数目
        ws.setColumnView(2,50);//设置工作表列宽度，int：列编号；int：宽度
        ws.setRowView(2,500);
        ws.addCell(new Label(0,0,"表头文字测试",wcf_title));
        
        for(int i = 0 ; i < headerList.size(); i ++){
            String headerContent = (String)headerList.get(i);
            labelC = new Label(i, 2,headerContent,wcfFC);
            ws.addCell(labelC);
        }
        List records = new ArrayList();
        for(int i = 0 ; i < records.size(); i ++){
            ExcelRecord record = (ExcelRecord)records.get(i);//得到行记录对象
            int fontSize = record.getFontSize();
            wfc = new WritableFont(WritableFont.ARIAL,fontSize);
            wcfFC = new WritableCellFormat(wfc);
            wcfFC.setBorder(Border.ALL,BorderLineStyle.THIN);
            List recordList = (List)record.get("");
            for(int j = 0 ; j < recordList.size(); j++){
                Object obj = recordList.get(j);
                //System.out.println("===classtype===="+obj.getClass().toString());
                //obj = obj.getClass().newInstance();
                int type = ExcelDataType.getDataType(obj);
                if(type == 0){
                    labelC = new Label(j, i+3,obj.toString(),wcfFC);
                    ws.addCell(labelC);
                }
                else{
                    jxl.write.Number number = new jxl.write.Number(j,i+3,Double.parseDouble(obj.toString()),wcfFC);
                    ws.addCell(number);
                }

            }
        }

        //写入Exel工作表
        wwb.write();
        //关闭Excel工作薄对象
        os.flush();
        wwb.close();
        os.close();
    }   */
}
