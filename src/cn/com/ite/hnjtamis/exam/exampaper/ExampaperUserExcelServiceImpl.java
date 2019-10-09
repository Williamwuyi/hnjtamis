package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.excel.ExportExcelServiceImpl;
import cn.com.ite.hnjtamis.excel.util.ExcelHeaderField;
import cn.com.ite.hnjtamis.excel.util.ExcelRecord;
import cn.com.ite.hnjtamis.excel.util.ExcelTitle;
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
public class ExampaperUserExcelServiceImpl extends ExportExcelServiceImpl {

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
        field.setFiledName("机构");
        field.setFieldWidth(40);
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
        field.setFieldWidth(10);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        
        field = new ExcelHeaderField();
        field.setFiledName("准考证");
        field.setFieldWidth(35);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("密码");
        field.setFieldWidth(10);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        
        field = new ExcelHeaderField();
        field.setFiledName("用户名");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("密码");
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
        field.setFiledName("身份证");
        field.setFieldWidth(25);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("密码");
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
        		
        List examineeInfoList =(List)valueMap.get("examineeInfoList");
        for(int j=0;j<examineeInfoList.size();j++){
        	ExamUserForm examUserForm = (ExamUserForm)examineeInfoList.get(j);
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
	        field.setFiledName(examUserForm.getUserOrganName());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
        	field = new ExcelHeaderField();
	        field.setFiledName(examUserForm.getUserName());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        if(examUserForm.getInticket().matches("^[0-9]*$")){
	        	field.setFiledName(examUserForm.getInticket()+" ");
	        }else{
	        	field.setFiledName(examUserForm.getInticket());
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
	        if(examUserForm.getExamPassword()!=null && 
	        		examUserForm.getExamPassword().matches("^[0-9]*$") 
	        		&& examUserForm.getExamPassword().length()>4){
	        	field.setFiledName(examUserForm.getExamPassword()+" ");
	        }else if("F".equals(examUserForm.getExamPassword().substring(examUserForm.getExamPassword().length()-1))){
	        	field.setFiledName(examUserForm.getExamPassword()+" ");
	        }else{
	        	field.setFiledName(examUserForm.getExamPassword());
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
	        if(examUserForm.getLoginId()!=null && !"".equals(examUserForm.getLoginId())
	        	 && !"null".equals(examUserForm.getLoginId())){
	        	 field.setFiledName(examUserForm.getLoginId()+" ");	
	        }else{
	        	 field.setFiledName("");
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
	        field.setFiledName("同账号登陆密码");
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        if(examUserForm.getIdNumber()!=null && !"".equals(examUserForm.getIdNumber())
		        	 && !"null".equals(examUserForm.getIdNumber())){
	        		field.setFiledName(examUserForm.getIdNumber()+" ");
		        }else{
		        	 field.setFiledName("");
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
	        field.setFiledName("同账号登陆密码");
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
