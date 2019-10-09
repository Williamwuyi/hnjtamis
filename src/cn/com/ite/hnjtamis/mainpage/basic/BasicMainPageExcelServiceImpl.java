package cn.com.ite.hnjtamis.mainpage.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamDeptPassForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserPassForm;
import cn.com.ite.hnjtamis.excel.ExportExcelServiceImpl;
import cn.com.ite.hnjtamis.excel.util.ExcelHeaderField;
import cn.com.ite.hnjtamis.excel.util.ExcelRecord;
import cn.com.ite.hnjtamis.excel.util.ExcelTitle;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;



/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperUserExcelServiceImpl</p>
 * <p>Description 人员导入</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年6月10日 下午4:27:27
 * @version 1.0
 * 
 * @modified records:
 */
public class BasicMainPageExcelServiceImpl extends ExportExcelServiceImpl {

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
		List examDeptList = (List)valueMap.get("examDeptList");
		List examUserList = (List)valueMap.get("examUserList");
		String[] sheetName = (String[])valueMap.get("sheetName");
		
		List<ExcelHeaderField> headlist = new java.util.ArrayList<ExcelHeaderField>();
		ExcelHeaderField field = null;
		
		if(index == 0){
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
	        field.setFiledName("部门");
	        field.setFieldWidth(50);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName("总人数");
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
	        field.setFiledName("已完成学习人数");
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
	        field.setFiledName("在学习人数");
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
	        field.setFiledName("达标人数");
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
	        field.setFiledName("未达标人数");
	        field.setFieldWidth(20);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
	       
	        
	        row+=1;
	        String oldTypename = "";
	        String oldstandardname ="";
	        for(int j=0;j<examDeptList.size();j++){
	        	ExamDeptPassForm examDeptPassForm = (ExamDeptPassForm)examDeptList.get(j);
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
		         
		        field = new ExcelHeaderField();
		        field.setFiledName(examDeptPassForm.getShowDeptName().replaceAll("&nbsp;", "  "));
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.LEFT);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examDeptPassForm.getPassStateCount());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examDeptPassForm.getXxrsCount());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examDeptPassForm.getZzxxrsCount());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examDeptPassForm.getPassCount());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examDeptPassForm.getUnPassCount());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        row++;
	        }
	        
	        //  表头
	        field = new ExcelHeaderField();
	        field.setFiledName(sheetName[index]);
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
		}else{
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
	        field.setFiledName("人员");
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
	        field.setFiledName("部门");
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
	        field.setFiledName("岗位");
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
	        field.setFiledName("是否合格");
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
	        field.setFiledName("有效期(开始)");
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
	        field.setFiledName("有效期(结束)");
	        field.setFieldWidth(20);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.GRAY_25);
	        headlist.add(field);
	        col++;
	        len++;
	        
	        row+=1;
	        String oldTypename = "";
	        String oldstandardname ="";
	        for(int j=0;j<examUserList.size();j++){
	        	ExamUserPassForm examUserPassForm = (ExamUserPassForm)examUserList.get(j);
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
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserPassForm.getEmployeeName());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserPassForm.getUserDeptName());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserPassForm.getQuarterName());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        if("T".equals(examUserPassForm.getPassState())){
		        	field.setFiledName("合格");
		        }else{
		        	field.setFiledName("不合格");
		        }
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserPassForm.getScoreStartTime());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserPassForm.getScoreEndTime());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
		        
		        row++;
	        }
	        
	        //  表头
	        field = new ExcelHeaderField();
	        field.setFiledName(sheetName[index]);
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
		String[] sheetName=(String[])value.get("sheetName");
		String[] pagesizes = new String[sheetName.length];
		String[] ortaions = new String[sheetName.length];
		for(int i=0;i<pagesizes.length;i++){
			pagesizes[i] = null;
			ortaions[i] = "P";
		}
		return exportExcelManager(form,sheetName, pagesizes, ortaions);
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
