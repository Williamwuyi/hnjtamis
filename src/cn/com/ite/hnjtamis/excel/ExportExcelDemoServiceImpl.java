package cn.com.ite.hnjtamis.excel;


import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.excel.util.ExcelRecord;
import cn.com.ite.hnjtamis.excel.util.ExcelHeaderField;
import cn.com.ite.hnjtamis.excel.util.ExcelTitle;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableImage;



/**
 * <p>Title cn.com.ite.ydtcms.excel.ExportExcelDemoServiceImpl</p>
 * <p>Description 导出Excel例子</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2011</p>
 * @author 朱健
 * @create time: 2011-10-10 下午07:03:13
 * @version 1.0
 * 
 * @modified records:
 */
public class ExportExcelDemoServiceImpl extends ExportExcelServiceImpl {

	/**
	 * 设置表头内容
	 * @author 朱健
	 * @description
	 * @param baseinfo
	 * @return
	 * @modified
	 */
	public List setHeaderForExcel(Object form,int index) {

		
		List<ExcelHeaderField> headlist = new java.util.ArrayList<ExcelHeaderField>();
		ExcelHeaderField field = null;
		//内部非合并表格部分
		int row=1;

		field = new ExcelHeaderField();
        field.setFiledName("序号");
        field.setFieldWidth(6);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(1);
        field.setRowSpan(2);
        field.setBackGroundColour(Colour.RED);
        headlist.add(field);
        
        field = new ExcelHeaderField();
        field.setFiledName("名称");
        field.setFieldWidth(15);
        field.setAlign(Alignment.LEFT);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(2);
        field.setRowSpan(2);
        field.setBackGroundColour(Colour.RED);
        headlist.add(field);
        
        field = new ExcelHeaderField();
        field.setFiledName("月份");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(3);
        field.setColSpan(12);
        field.setBackGroundColour(Colour.RED);
        headlist.add(field);
		
        row++;
        for(int j=1;j<13;j++){
            field = new ExcelHeaderField();
            field.setFiledName(j+"月");
            field.setFieldWidth(10);
            field.setAlign(Alignment.LEFT);
    		field.setVerlign(VerticalAlignment.CENTRE);
            field.setRow(row);
            field.setCol(j+2);
            field.setBackGroundColour(Colour.RED);
            headlist.add(field);
        }
		return headlist;
		
	}
	
	
	/**
	 * 设置表格高度
	 * 
	 * @param reslist
	 * @return
	 */
	public int getRowHeight(){
		return 420;
	}
	
	
	/**
	 * 设置Excel表的数据
	 * @author 朱健
	 * @description
	 * @param baseinfo
	 * @param rowList
	 * @param columnList
	 * @return
	 * @modified
	 */
	public List setExcelRecord(Object form,int index) {
		   List<ExcelRecord> recordList = new ArrayList<ExcelRecord>();
		   for(int i=0;i<20;i++){
			   ExcelRecord excelRecord = new ExcelRecord();
		       for (int j = 0; j < 20; j++) {
		           excelRecord.set((i + 3) + "_", "行"+i+"列"+j);
		       }
		       recordList.add(excelRecord);
		   }
	       return recordList;
	}
	
	
	/**
	 *
	 * @author 朱健
	 * @description
	 * @param baseinfo
	 * @param rowList
	 * @param columnList
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String exportExcel(Object form) throws Exception {
		return exportExcelManager(form,new String[]{"测试"}, new String[]{null}, new String[]{"P"});
	}


	@Override
	public List setExcelImage(Object form, int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ExcelTitle setTitle(Object form) {
		// TODO Auto-generated method stub
		return null;
	}

	


}
