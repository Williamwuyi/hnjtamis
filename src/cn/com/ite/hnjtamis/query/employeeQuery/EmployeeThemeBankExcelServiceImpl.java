package cn.com.ite.hnjtamis.query.employeeQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.excel.ExportExcelServiceImpl;
import cn.com.ite.hnjtamis.excel.util.ExcelHeaderField;
import cn.com.ite.hnjtamis.excel.util.ExcelRecord;
import cn.com.ite.hnjtamis.excel.util.ExcelTitle;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;



/**
 * 全部数据的导出
 * <p>Title cn.com.ite.hnjtamis.query.employeeQuery.EmployeeThemeBankExcelServiceImpl</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2018</p>
 * @create time: 2018年8月9日 下午2:30:51
 * @version 1.0
 * 
 * @modified records:
 */
public class EmployeeThemeBankExcelServiceImpl extends ExportExcelServiceImpl {

	/**
	 * 设置表头内容
	 * @author 朱健
	 * @description
	 * @param baseinfo
	 * @return
	 * @modified
	 */
	public List setHeaderForExcel(Object form,int index) {
		Map valueMap = (Map)form;
		String op = (String)valueMap.get("op");
		Map<String,Dept> deptMap = (Map<String,Dept>)valueMap.get("deptMap");
		
		List<ExcelHeaderField> headlist = new java.util.ArrayList<ExcelHeaderField>();
		ExcelHeaderField field = null;
		//内部非合并表格部分
		int row=2;
		int col=1;
		int len=0;
		field = new ExcelHeaderField();
        field.setFiledName("序号");
        field.setFieldWidth(6);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("上级部门");
        field.setFieldWidth(30);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("部门名称");
        field.setFieldWidth(30);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("姓名");
        field.setFieldWidth(20);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        if("xxog".equals(op)||"xxdp".equals(op)){
	        field = new ExcelHeaderField();
	        field.setFiledName("题库数目");
	        field.setFieldWidth(20);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
	
	        field = new ExcelHeaderField();
	        field.setFiledName("未学习题库");
	        field.setFieldWidth(20);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName("在学习题库");
	        field.setFieldWidth(20);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName("已学完题库");
	        field.setFieldWidth(20);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
        }
        
        if("o".equals(op)||"d".equals(op)){
	        field = new ExcelHeaderField();
	        field.setFiledName("题库");
	        field.setFieldWidth(30);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
        }
        
        
        field = new ExcelHeaderField();
        field.setFiledName("总试题数");
        field.setFieldWidth(30);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        
        field = new ExcelHeaderField();
        field.setFiledName("已学习题数");
        field.setFieldWidth(30);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        if("o".equals(op)||"d".equals(op)){
	        field = new ExcelHeaderField();
	        field.setFiledName("学习比率");
	        field.setFieldWidth(30);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
        }
        row+=1;
        		
        List list =(List)valueMap.get("list");
        for(int j=0;j<list.size();j++){
        	EmployeeThemeBankForm vform = (EmployeeThemeBankForm)list.get(j);
        	col=1;
        	
        	field = new ExcelHeaderField();
	        field.setFiledName(j+1+"");
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        Dept dept = deptMap.get(vform.getDeptId());
	        if(dept!=null){
	        	int qlen = 0;
	        	while(dept.getDept()!=null && qlen<10){
	        		dept = deptMap.get(dept.getDept().getDeptId());
	        		qlen++;
	        	}
	        }
	        field = new ExcelHeaderField();
	        field.setFiledName(dept!=null?dept.getDeptName():"");
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(vform.getDeptName());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
        	field = new ExcelHeaderField();
	        field.setFiledName(vform.getEmployeeName());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        if("xxog".equals(op)||"xxdp".equals(op)){
		        field = new ExcelHeaderField();
		        field.setFiledName(vform.getThemebanknum()!=null?vform.getThemebanknum().intValue()+"":"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
	
		        field = new ExcelHeaderField();
		        field.setFiledName(vform.getWxxthemebanknum()!=null?vform.getWxxthemebanknum().intValue()+"":"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(vform.getYxxthemebanknum()!=null?vform.getYxxthemebanknum().intValue()+"":"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(vform.getYxwthemebanknum()!=null?vform.getYxwthemebanknum().intValue()+"":"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        if("o".equals(op)||"d".equals(op)){
		        field = new ExcelHeaderField();
		        field.setFiledName(vform.getThemeBankName());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(vform.getThemeNum()!=null?vform.getThemeNum().intValue()+"":"");
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(vform.getThemeFinNum()!=null?vform.getThemeFinNum().intValue()+"":"");
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        if("o".equals(op)||"d".equals(op)){
		        field = new ExcelHeaderField();
		        double v = 0;
		        if(vform.getThemeNum()!=null && vform.getThemeFinNum()!=null && vform.getThemeNum()>0){
				   v = vform.getThemeFinNum()/vform.getThemeNum()*100.0;
				}
		        if(v==0){
					field.setFiledName("0.00%");
				}else{
					field.setFiledName(NumericUtils.roundToString(v,2)+"%");
				}
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        row++;
        }
        
        //  表头
        String sheetName=(String)valueMap.get("sheetName");
        field = new ExcelHeaderField();
        field.setFiledName(sheetName);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(1);
        field.setCol(1);
        field.setColSpan(len);
        field.setFontSize(15);
        field.setBackGroundColour(Colour.WHITE);
        headlist.add(field);

        
        //表尾
        field = new ExcelHeaderField();
        field.setFiledName("导出时间："+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd"));
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(1);
        field.setColSpan(len);
        field.setFontSize(15);
        field.setBackGroundColour(Colour.WHITE);
        headlist.add(field);
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
		  /* for(int i=0;i<20;i++){
			   ExcelRecord excelRecord = new ExcelRecord();
		       for (int j = 0; j < 20; j++) {
		           excelRecord.set((i + 3) + "_", "行"+i+"列"+j);
		       }
		       recordList.add(excelRecord);
		   }*/
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
		Map value=(HashMap)form;
		String sheetName=(String)value.get("sheetName");
		return exportExcelManager(form,new String[]{sheetName}, new String[]{null}, new String[]{"P"});
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

	
	/**
	 * @author 朱健
	 * @description 是否重新设置高度
	 * @return
	 * @modified
	 */
	public boolean getResetRowHeight() {
		return false;
	}

}
