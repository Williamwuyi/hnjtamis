package cn.com.ite.hnjtamis.excel;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.CellType;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import cn.com.ite.hnjtamis.common.RandomGUID;
import cn.com.ite.hnjtamis.excel.util.CommonExcelBuilder;
import cn.com.ite.hnjtamis.excel.util.CommonExcelHeader;
import cn.com.ite.hnjtamis.excel.util.ExcelBuilder;
import cn.com.ite.hnjtamis.excel.util.ExcelComplexHeaderField;
import cn.com.ite.hnjtamis.excel.util.ExcelTitle;


/**
 * <p>Title cn.com.ite.excel.ExportExcelServiceImpl</p>
 * <p>Description Excel处理导出类(其余导出Excel均继承该类进行实现)</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2009</p>
 * @author 朱健
 * @create time: 2009-3-11 上午09:19:18
 * @version 1.0
 * 
 * @modified records:
 */
public abstract class ExportExcelServiceImpl  implements ExportExcelService {
	

	/**
	 * @author 朱健
	 * @description
	 * @param form 存放数据对象 用于表头内容设置方法setHeaderForExcel和设置Excel表的数据方法setExcelRecord中调用
	 * @param sheetName sheet名称
	 * @param pagesize 如:A4
	 * @param ortaion L：横向  P:纵向
	 * @return
	 * @modified
	 */
	public String exportExcelManager(Object form,String[] sheetNames,String[] pagesizes,String[] ortaions){
		
		//BaseReportForm baseinfo=((ExcelForm)form).getBaseinfo();
		/*String fileName = System.getProperty("file.separator")+"temp"+System.getProperty("file.separator") ;
		if(!new File(fileName).exists()){
	         new File(fileName).mkdir();
	     }*/
		String fileName=RandomGUID.getGUID()+".xls";
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
			ExcelBuilder excelBuilder = new CommonExcelBuilder(workbook); // 生成excel表格设置器
			if(pagesizes==null){
				pagesizes=new String[sheetNames.length];
			}
			if(ortaions==null){
				ortaions=new String[sheetNames.length];
			}
			for(int i=0;i<sheetNames.length;i++){
				String sheetName=sheetNames[i];
				/** ******************设置该工作簿的内容************************ */
				excelBuilder.createSheet(sheetName, i);// 创建工作表
				/** *************设置标题************** */
				/*ExcelTitle title = new ExcelTitle();
				title.setBold(true);
				title.setFrontColor(Colour.GRAY_50);
				HashMap value=(HashMap)form;
				title.setTitle((String)value.get("TITLE_"+i));
				if(StringUtils.isNotEmpty(title.getTitle())){
					excelBuilder.setExcelTitle(title, i);
				}*/
				
				excelBuilder.setExcelImage(setExcelImage(form, i),i);
				CommonExcelHeader header = new ExcelComplexHeaderField();
				header.setBold(false);
				header.setWrap(true);
				if(getRowHeight()!=-1){
					header.setRowHeight(getRowHeight());
				}
				header.setResetRowHeight(getResetRowHeight());
				header.setHeaderContentList(setHeaderForExcel(form,i));
				excelBuilder.setExcelTitle(setTitle(form),i);
				excelBuilder.setExcelHeader(header, i);// 设置表格头
				excelBuilder.setExcelRecords(setExcelRecord(form,i),i);
				
				if(pagesizes[i]==null)pagesizes[i]="A4";
				if(ortaions[i]==null)ortaions[i]="L";
				
			}
			workbook = excelBuilder.getExcelWorkbook();
			workbook.write();
			workbook.close();
			setPage(sheetNames,fileName,pagesizes,ortaions);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileName ;
	}
	
	/**
	 * 设置表格高度
	 * 
	 * @param reslist
	 * @return
	 */
	public int getRowHeight(){
		return -1;
	}
	
	public boolean getResetRowHeight() {
		return true;
	}
	
	public abstract ExcelTitle setTitle(Object form);
	/**
	 * @author 朱健
	 * @description 设置表头内容
	 * @param form
	 * @param index sheet页号
	 * @return
	 * @modified
	 */
	public abstract List setHeaderForExcel(Object form,int index);
	
	/**
	 * @author 朱健
	 * @description  设置Excel表的数据
	 * @param form
	 * @param index sheet页号
	 * @return
	 * @modified
	 */
	public abstract List setExcelRecord(Object form,int index);
	
	/**
	 * 设置图片
	 * @param form
	 * @param index
	 * @return
	 */
	public abstract List setExcelImage(Object form,int index);
	
	

	/**
	 * 处理空字符
	 * @param arg
	 * @return
	 */
	protected String isEmpty(Object arg){
		if(arg==null){
			return "" ;
		}else{
			return arg.toString() ;
		}
	}
	
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @param encoding
	 *            编码方式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public boolean download(HttpServletRequest request,
			HttpServletResponse response, String encoding, String contentType,
			String name,String fileName) throws Exception {
		boolean flag = false;

		request.setCharacterEncoding(encoding);
		File file = new File(fileName);// +filename
		if(!file.exists()){
			file.createNewFile() ;
		}
		InputStream ins = null;
		OutputStream outStream = null;
		try {
			ins = new FileInputStream(file);
			
			if (contentType == null) {
				contentType = "application/vnd.ms-excel";
			}

			response.setContentType(contentType);
			// filename=MimeUtility.decodeText(filename);
			String tmpname = name;
			// 保存文件名转码
			tmpname = new String(tmpname.getBytes("GB2312"), "ISO_8859_1");

			response.addHeader("Content-Disposition", "attachment; filename="
					+ tmpname + "");

			outStream = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = ins.read(bytes)) != -1) {
				outStream.write(bytes, 0, len);
			}
			flag = true;

		} catch (Exception e) {
			System.out.print("e:" + e.getMessage());
		} finally {
			closeStream(ins, outStream);
			boolean delfile = file.delete();// 删除临时文件
			// response.sendRedirect("/pages/universalquery/exportflag.jsp");
		}
		return flag;
	}
	
	/**
	 * 关闭相关流
	 * 
	 * @param ins
	 * @param outStream
	 * @throws IOException
	 */
	private void closeStream(InputStream ins, OutputStream outStream)
			throws IOException {
		if (ins != null)
			ins.close();
		if (outStream != null) {
			outStream.close();
		}
	}
	
	/**
	 * 设置页面打印方向
	 * @param fileName
	 * @param pagesize  如:A4
	 * @param ortaion   L：横向  P:纵向
	 */
	public void setPage(String sheetNames[],String fileName,String pagesizes[],String ortaions[]){
		 jxl.write.WritableWorkbook  book = null;
		 jxl.write.WritableSheet sheet=null;
		 jxl.Workbook wb=null;
		 File file=null;
		 File file1=null;
		try {
			file = new java.io.File(fileName);
			//file1 = new java.io.File("c:\\test1.xls"); 
			wb = jxl.Workbook.getWorkbook(file);
			book=jxl.Workbook.createWorkbook(file,wb);   
			for(int i=0;i<sheetNames.length;i++){
				String sheetName=sheetNames[i];
				String pagesize=pagesizes[i];
				String ortaion=ortaions[i];
				sheet = book.getSheet(i); 
				if(pagesize.equals("A4"))
				{
					if(ortaion.equals("L"))
					    sheet.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A4,0.5d,0.5d);
					else
						sheet.setPageSetup(PageOrientation.LANDSCAPE.PORTRAIT,PaperSize.A4,0.5d,0.5d);
				}
				else
				{
					if(ortaion.equals("L"))
					    sheet.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A3,0.5d,0.5d);
					else
						sheet.setPageSetup(PageOrientation.LANDSCAPE.PORTRAIT,PaperSize.A3,0.5d,0.5d);
				}
			}
			book.write();     	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sheet=null;
			try {
				if(book!=null)
				book.close();
				book=null;
			} catch (Exception e) {
				e.printStackTrace();
			} 
			try{
				if(wb!=null)
				wb.close();
				wb=null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 设置行高，及自动换行
	 * @param fileName
	 * @param pagesize  如:A4
	 * @param ortaion   L：横向  P:纵向
	 */
	public void setRowProP(String fileName,String pagesize,String ortaion){
		 jxl.write.WritableWorkbook  book = null;
		 jxl.write.WritableSheet sheet=null;
		 jxl.Workbook wb=null;
		 File file=null;
		 File file1=null;
		try {
			file = new java.io.File(fileName);
			//file1 = new java.io.File("c:\\test1.xls"); 
			wb = jxl.Workbook.getWorkbook(file);
			book=jxl.Workbook.createWorkbook(file,wb);   
			sheet = book.getSheet("sheet1"); 
			if(pagesize.equals("A4"))
			{
				if(ortaion.equals("L"))
				    sheet.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A4,0.5d,0.5d);
				else
					sheet.setPageSetup(PageOrientation.LANDSCAPE.PORTRAIT,PaperSize.A4,0.5d,0.5d);
			}
			else
			{
				if(ortaion.equals("L"))
				    sheet.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A3,0.5d,0.5d);
				else
					sheet.setPageSetup(PageOrientation.LANDSCAPE.PORTRAIT,PaperSize.A3,0.5d,0.5d);
			}
			book.write();     	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sheet=null;
			try {
				if(book!=null)
				book.close();
				book=null;
			} catch (Exception e) {
				e.printStackTrace();
			} 
			try{
				if(wb!=null)
				wb.close();
				wb=null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
     * 修改字符单元格的值
     * @param dataSheet WritableSheet : 工作表
     * @param col int : 列
     * @param row int : 行
     * @param str String : 字符
     * @param format CellFormat : 单元格的样式
     * @throws RowsExceededException
     * @throws WriteException
     */

	private void modiStrCell(WritableSheet dataSheet, int col, int row, String str, CellFormat format) throws RowsExceededException, WriteException {
        // 获得单元格对象
        WritableCell cell = dataSheet.getWritableCell(col, row);
        // 判断单元格的类型, 做出相应的转化
        if (cell.getType() == CellType.EMPTY)
        {
            Label lbl = new Label(col, row, str);
            if(null != format)
            {
                lbl.setCellFormat(format);
            } else
            {   
            	if(cell.getCellFormat()==null){
	                format   =   new   WritableCellFormat(); 
	                lbl.setCellFormat(format);
            	}
            	else{
            		lbl.setCellFormat(cell.getCellFormat());
            	}
            }
            dataSheet.addCell(lbl);
        } else if (cell.getType() == CellType.LABEL)
        {
            Label lbl = (Label)cell;
            lbl.setString(str);
        } else if (cell.getType() == CellType.NUMBER)
        {
            // 数字单元格修改
            Number n1 = (Number)cell;
            n1.setValue(Double.valueOf(str));
        }
    }
}
