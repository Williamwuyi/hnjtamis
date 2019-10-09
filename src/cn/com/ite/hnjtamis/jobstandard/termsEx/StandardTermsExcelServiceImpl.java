package cn.com.ite.hnjtamis.jobstandard.termsEx;

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
public class StandardTermsExcelServiceImpl extends ExportExcelServiceImpl {

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
		List<StandardTypes> standardTypeslist = (List<StandardTypes> )valueMap.get("standardTypeslist");//大分类
		StandardTypes standardTypes = standardTypeslist.get(index);
		String[] sheetName = (String[])valueMap.get("sheetName");
		Map<String,List<StandardTerms>> standardTermsMap = (Map<String,List<StandardTerms>>)valueMap.get("standardTermsMap");//大分类对应条款
		List<StandardTerms> standardTermslist= standardTermsMap.get(standardTypes.getJstypeid());
		List<JobsStandardQuarter> jobsStandardQuarterList = (List<JobsStandardQuarter> )valueMap.get("jobsStandardQuarterList");//岗位对应条款信息
		
		
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
        
       /* field = new ExcelHeaderField();
        field.setFiledName("类别");
        field.setFieldWidth(10);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;*/
        
        
        field = new ExcelHeaderField();
        field.setFiledName("模块");
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
        field.setFiledName("子模块");
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
        field.setFiledName("详细内容");
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
        field.setFiledName("岗位编码");
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
        field.setFiledName("有效期");
        field.setFieldWidth(8);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        
        field = new ExcelHeaderField();
        field.setFiledName("参考学分");
        field.setFieldWidth(8);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        field = new ExcelHeaderField();
        field.setFiledName("达标标准(分)");
        field.setFieldWidth(8);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        
        field = new ExcelHeaderField();
        field.setFiledName("考核方式");
        field.setFieldWidth(8);
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
        for(int j=0;j<standardTermslist.size();j++){
        	StandardTerms standardTermsForm = (StandardTerms)standardTermslist.get(j);
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
	        
        	/*field = new ExcelHeaderField();
	        field.setFiledName(standardTermsForm.getParentTypeName());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;*/
	        
	        if(!oldTypename.equals(standardTermsForm.getTypename())){
	        	int rowspan = 1;
	        	for(int i=j+1;i<standardTermslist.size();i++){
	        		StandardTerms tt = (StandardTerms)standardTermslist.get(i);
	        		if(tt.getTypename().equals(standardTermsForm.getTypename())){
	        			rowspan++;
	        		}else{
	        			break;
	        		}
	        	}
	        	field = new ExcelHeaderField();
	 	        field.setFiledName(standardTermsForm.getTypename());
	 	        field.setFieldWidth(15);
	 	        field.setAlign(Alignment.CENTRE);
	 			field.setVerlign(VerticalAlignment.CENTRE);
	 			field.setRow(row);
		        field.setCol(col);
		        field.setRowSpan(rowspan);
	 	        field.setBackGroundColour(Colour.WHITE);
	 	        headlist.add(field);
	        }
	        oldTypename = standardTermsForm.getTypename();
	        col++;
	        
	        if(!oldstandardname.equals(standardTermsForm.getStandardname())){
	        	int rowspan = 1;
	        	for(int i=j+1;i<standardTermslist.size();i++){
	        		StandardTerms tt = (StandardTerms)standardTermslist.get(i);
	        		if(tt.getStandardname().equals(standardTermsForm.getStandardname())){
	        			rowspan++;
	        		}else{
	        			break;
	        		}
	        	}
		        field = new ExcelHeaderField();
		        field.setFiledName(standardTermsForm.getStandardname());
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setRowSpan(rowspan);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
	        }
	        oldstandardname = standardTermsForm.getStandardname();
	        col++;
	        
	        field = new ExcelHeaderField();
	        String contents = standardTermsForm.getContents().replaceAll("<br>", "\\n");
	        field.setFiledName(contents);
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.LEFT);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        String qvalue = "";
	        for(int k=0;k<jobsStandardQuarterList.size();k++){
	        	JobsStandardQuarter jobsStandardQuarter = jobsStandardQuarterList.get(k);
	        	if(jobsStandardQuarter.getStandardTerms().getStandardid().equals(standardTermsForm.getStandardid())){
	        		qvalue+=jobsStandardQuarter.getQuarterTrainCode()+" ";
	        	}
	        }
	        field = new ExcelHeaderField();
	        field.setFiledName(qvalue);
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.LEFT);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(standardTermsForm.getEfficient());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(standardTermsForm.getRefScore());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(standardTermsForm.getUpStandardScore());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        field = new ExcelHeaderField();
	        field.setFiledName(standardTermsForm.getExamTypeName());
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
