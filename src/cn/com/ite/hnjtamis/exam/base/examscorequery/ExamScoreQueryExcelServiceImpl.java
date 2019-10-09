package cn.com.ite.hnjtamis.exam.base.examscorequery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.hnjtamis.exam.base.examscorequery.form.ScoreForm;
import cn.com.ite.hnjtamis.excel.ExportExcelServiceImpl;
import cn.com.ite.hnjtamis.excel.util.ExcelHeaderField;
import cn.com.ite.hnjtamis.excel.util.ExcelRecord;
import cn.com.ite.hnjtamis.excel.util.ExcelTitle;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;


/**
 * 考试成绩导出
 * @author 朱健
 * @create time: 2016年2月17日 上午9:12:27
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamScoreQueryExcelServiceImpl extends ExportExcelServiceImpl {

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
		Map typeNameMap = (Map)valueMap.get("typeNameMap");
		Map typeNameColMap = new HashMap();
		
		List<ExcelHeaderField> headlist = new java.util.ArrayList<ExcelHeaderField>();
		ExcelHeaderField field = null;
		//内部非合并表格部分
		int row=2;
		int col=1;
		int len=0;
		int colIndex = 0;
		
		colIndex++;
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
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("考试名称");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        colIndex++; 
        field = new ExcelHeaderField();
        field.setFiledName("考生姓名");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        colIndex++; 
        field = new ExcelHeaderField();
        field.setFiledName("总得分");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        headlist.add(field);
        col++;
        len++;
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("单选题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("多选题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("填空题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("判断题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("问答题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("视听题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("论述题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("计算题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("画图题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("其它");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("客观题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("主观题");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        colIndex++;
        field = new ExcelHeaderField();
        field.setFiledName("系统外");
        field.setFieldWidth(15);
        field.setAlign(Alignment.CENTRE);
		field.setVerlign(VerticalAlignment.CENTRE);
        field.setRow(row);
        field.setCol(col);
        field.setBackGroundColour(Colour.GRAY_25);
        if(typeNameMap==null || typeNameMap.get(field.getFiledName())!=null){
	        headlist.add(field);
	        col++;
	        len++;
	        typeNameColMap.put(colIndex+"", field.getFiledName());
        }
        
        row+=1;
        		
        List<ScoreForm> list =(List<ScoreForm>)valueMap.get("list");
        for(int j=0;j<list.size();j++){
        	ScoreForm examUserForm = (ScoreForm)list.get(j);
        	col=1;
        	int colIndex_ = 1;
        	
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
	        
	        colIndex_++;
	        field = new ExcelHeaderField();
	        field.setFiledName(examUserForm.getExamName());
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        colIndex_++;
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
	        
	        colIndex_++;
	        field = new ExcelHeaderField();
	        field.setFiledName(examUserForm.getFirstScore()==-1?"":examUserForm.getFirstScore()+"");
	        field.setFieldWidth(15);
	        field.setAlign(Alignment.CENTRE);
			field.setVerlign(VerticalAlignment.CENTRE);
	        field.setRow(row);
	        field.setCol(col);
	        field.setBackGroundColour(Colour.WHITE);
	        headlist.add(field);
	        col++;
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getDanxuan()==-1?"":examUserForm.getDanxuan()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getDuoxuan()==-1?"":examUserForm.getDuoxuan()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getTiankong()==-1?"":examUserForm.getTiankong()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getPanduan()==-1?"":examUserForm.getPanduan()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getWenda()==-1?"":examUserForm.getWenda()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getShiting()==-1?"":examUserForm.getShiting()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getLst()==-1?"":examUserForm.getLst()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getJst()==-1?"":examUserForm.getJst()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getHtt()==-1?"":examUserForm.getHtt()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getQita()==-1?"":examUserForm.getQita()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getKeguan()==-1?"":examUserForm.getKeguan()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getZhuguan()==-1?"":examUserForm.getZhuguan()+"");
		        field.setFieldWidth(15);
		        field.setAlign(Alignment.CENTRE);
				field.setVerlign(VerticalAlignment.CENTRE);
		        field.setRow(row);
		        field.setCol(col);
		        field.setBackGroundColour(Colour.WHITE);
		        headlist.add(field);
		        col++;
	        }
	        
	        colIndex_++;
	        if(typeNameColMap.get(colIndex_+"")!=null){
		        field = new ExcelHeaderField();
		        field.setFiledName(examUserForm.getXtw()==-1?"":examUserForm.getXtw()+"");
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
