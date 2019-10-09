package cn.com.ite.hnjtamis.excel.util;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;

public class CommonExcelBuilder implements ExcelBuilder {
	private WritableWorkbook workbook = null;// 工作簿对象
	private WritableSheet sheet = null;
	private ExcelTitle excelTitle = null;// 表格标题
	private List recordsList = null;// 表格行记录集合
	private ExcelHeader excelHeader = null;// 表格表头对象
	private WritableFont wf = null;// 格式对象
	private int colNum = 0;// 列数量
	private WritableCellFormat wcfFC = null;// 单元格格式化对象
	private Label cellLabel = null;// 单元格内容标签
	private Number cellNumber = null;// 单元格内容数字
	private int headerRowCount = 0;// 表头之后的起始行号，用来确定表格内容的起始位置
	private List sheetList = null;// 存储工作表的集合
	private List headerList = null;// 存储表头的集合
	private List recordContentList = null;// 存储记录集内容的集合
	private List titleList = null;// 存储工作表标题的集合
	private static int addtoCount = 0;

	private WritableImage image = null;
	private List imageList = null;
	private List imageContentList = null;

	private int colC = 0;

	/**
	 * 初始化通用表格建造器对象
	 * 
	 * @param os
	 *            文件输出流
	 * @throws IOException
	 */
	public CommonExcelBuilder(OutputStream os) throws IOException {
		workbook = Workbook.createWorkbook(os);
		sheetList = new ArrayList();
		headerList = new ArrayList();
		recordContentList = new ArrayList();
		titleList = new ArrayList();
		imageContentList = new ArrayList();
	}

	public CommonExcelBuilder(WritableWorkbook workbook) throws IOException {
		this.workbook = workbook;
		sheetList = new ArrayList();
		headerList = new ArrayList();
		recordContentList = new ArrayList();
		titleList = new ArrayList();
		imageContentList = new ArrayList();
	}

	public void setExcelTitle(ExcelTitle excelTitle, int sheetIndex) {
		titleList.add(sheetIndex, excelTitle);
	}

	public void createSheet(String sheetName, int index) {
		WritableSheet writableSheet = workbook.createSheet(sheetName, index);
		sheetList.add(index, writableSheet);
	}

	public void setExcelHeader(ExcelHeader excelHeader, int sheetIndex) {
		headerList.add(sheetIndex, excelHeader);
	}

	// 设置工作表的内容
	public void setExcelRecords(List recordsList, int sheetIndex) {
		recordContentList.add(sheetIndex, recordsList);
	}

	public void setExcelImage(List imageList, int sheetIndex) {
		imageContentList.add(sheetIndex, imageList);
	}

	/**
	 * 设置工作表标题
	 * 
	 * @throws Exception
	 */
	public void setTitle() throws Exception {
		if (this.excelTitle == null)
			excelTitle = new ExcelTitle();
		String title = excelTitle.getTitle();// 得到标题内容
		if (StringUtils.isNotEmpty(title)) {
			int fontSize = excelTitle.getFontSize();// 得到标题字体大小
			boolean bold = excelTitle.isBold();// 得到是否粗体
			if (bold) {
				wf = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.BOLD, false);
			} else {
				wf = new WritableFont(WritableFont.ARIAL, fontSize);
			}
			boolean border = excelTitle.isBorder();// 得到是否带有边框
			if (excelTitle.getFrontColor() != null)// 设置颜色
				wf.setColour(excelTitle.getFrontColor());
			wcfFC = new WritableCellFormat(wf);
			if (border) {
				wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框线颜色
			}
			wcfFC.setAlignment(Alignment.CENTRE);// 设置对齐方式为居中
			wcfFC.setVerticalAlignment(VerticalAlignment.BOTTOM);// 设置上下对齐方式为底端
			wcfFC.setWrap(false);// 设置不能换行
			cellLabel = new Label(0, 0, title, wcfFC);// 设置标题到单元格
			sheet.addCell(cellLabel);
			sheet.mergeCells(0, 0, excelTitle.getColNum() - 1, 0);// 合并列
		} else
			this.excelTitle = null;
	}

	/**
	 * 设置excel表格的表头
	 * 
	 * @throws Exception
	 */
	private void setHeader() throws Exception {
		// System.out.println(headerRowCount+"===============setHeader=====");
		if (this.excelTitle == null)
			this.excelHeader.setStartRowCount(0);
		headerRowCount = this.excelHeader.setHeader(sheet);// 设置表头
	}

	private void setFont(ExcelRecord record) throws Exception {
		// 设置基本行格式
		int fontSize = record.getFontSize();// 得到行的字体大小

		Colour backgroudColour = record.getBackGroundColor();// 得到背景色
		Colour frontColour = record.getFrontColor();// 得到前景色

		boolean bold = record.isFontBold();// 得到是否粗体
		if (bold) {
			wf = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.BOLD, false);
		} else {
			wf = new WritableFont(WritableFont.ARIAL, fontSize);
		}
		if (frontColour != null) {
			wf.setColour(frontColour);
		}

		wcfFC = new WritableCellFormat(wf);// 得到格式化对象

		if (backgroudColour != null) {
			wcfFC.setBackground(backgroudColour);
		}
		if (record.isBorder())
			wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
		else
			wcfFC.setBorder(Border.ALL, BorderLineStyle.NONE);// 设置边框线颜色
	}

	/**
	 * 设置excel要显示的主体内容
	 * 
	 * @throws Exception
	 */
	private void setContent() throws Exception {
		if (recordsList == null) {
			recordsList = new ArrayList();
		}
		int count = 0;
		if (recordsList.size() == 0)
			return;
		ExcelRecord record_ = (ExcelRecord) (recordsList.iterator().next());

		setFont(record_);
		boolean haveSeriesNum = record_.isHaveSeriesNum();// 得到是否有行号
		boolean seriesNum = false;
		wcfFC.setAlignment(Alignment.CENTRE);

		int rowHeight = record_.getRowHeight();// 得到行高

		// 循环提取行记录
		for (int i = 0; i < recordsList.size(); i++) {
			ExcelRecord record = (ExcelRecord) recordsList.get(i);

			int rowCount = i + addtoCount + headerRowCount;
			int columnCount = 0;
			// List recordFields = record.getRecordFieldList();
			//
			if (haveSeriesNum) {
				++count;
				cellNumber = new Number(columnCount++, rowCount, count, wcfFC);
				sheet.addCell(cellNumber);
				seriesNum = true;
			}

			List recordFields = record.getRecordFieldList();
			List<ExcelField> fields = record.getFields();
			// 循环设置表格记录行中的记录到表格内容中
			if (fields == null || fields.size() == 0)
				setFieldsData(recordFields, record, columnCount, rowCount);
			else
				setFieldsData(fields, seriesNum, recordFields, record, columnCount, rowCount);

			sheet.setRowView(rowCount, rowHeight);// 设置行高
			this.colNum = recordFields.size();
		}
		if (seriesNum) {
			sheet.setColumnView(0, 6);
		}

	}

	private void setImage() throws Exception {
		if (imageList == null) {
			imageList = new ArrayList();
		}
		if (imageList.size() == 0)
			return;

		// 循环提取行记录
		for (int i = 0; i < imageList.size(); i++) {
			WritableImage exImage = (WritableImage) imageList.get(i);
			sheet.addImage(exImage);
		}
	}

	/**
	 * 设定记录值
	 * 
	 * @param recordFields
	 * @param record
	 * @param columnCount
	 * @param rowCount
	 * @throws Exception
	 */
	private void setFieldsData(List recordFields, ExcelRecord record, int columnCount, int rowCount)
			throws Exception {
		for (int i = 0; i < recordFields.size(); i++) {
			Object obj = recordFields.get(i);

			if (obj == null || "—".equals(obj)) {
				setFont(record);
				wcfFC.setAlignment(Alignment.CENTRE);
				wcfFC.setWrap(true);
				cellLabel = new Label(columnCount, rowCount, "", wcfFC);
				sheet.addCell(cellLabel);
			} else if(isDouble(obj.toString())){
				setFont(record);
				wcfFC.setAlignment(Alignment.RIGHT);
				wcfFC.setWrap(true);
				cellNumber = new Number(columnCount, rowCount, Double.parseDouble(obj
						.toString()), wcfFC);
				sheet.addCell(cellNumber);
			}else {
				if (ExcelDataType.getDataType(obj) == 0) {
					setFont(record);
					wcfFC.setAlignment(Alignment.CENTRE);
					wcfFC.setWrap(true);
					cellLabel = new Label(columnCount, rowCount, obj.toString(), wcfFC);
					sheet.addCell(cellLabel);
				} else {
					setFont(record);
					wcfFC.setAlignment(Alignment.RIGHT);
					wcfFC.setWrap(true);
					cellNumber = new Number(columnCount, rowCount, Double.parseDouble(obj
							.toString()), wcfFC);
					sheet.addCell(cellNumber);
				}
			}

			columnCount++;
		}
	}

	/**
	 * 设定记录值
	 * 
	 * @param recordFields
	 * @param record
	 * @param columnCount
	 * @param rowCount
	 * @throws Exception
	 */

	private void setFieldsData(List<ExcelField> fields, boolean seriesNum, List recordFields,
			ExcelRecord record, int columnCount, int rowCount) throws Exception {
		int count = recordFields.size() - fields.size();
		int j = 0;
		colC = columnCount;
		for (int i = 0; i < recordFields.size(); i++) {
			Object obj = recordFields.get(i);

			if (obj == null || "—".equals(obj)) {
				setFont(record);
				wcfFC.setAlignment(Alignment.CENTRE);
				wcfFC.setWrap(true);
				cellLabel = new Label(colC, rowCount, "", wcfFC);
				sheet.addCell(cellLabel);
			} else if(isDouble(obj.toString())){
				setFont(record);
				wcfFC.setAlignment(Alignment.RIGHT);
				wcfFC.setWrap(true);
				cellNumber = new Number(colC, rowCount, Double.parseDouble(obj.toString()),
						wcfFC);
				sheet.addCell(cellNumber);
			}else {
				if (ExcelDataType.getDataType(obj) == 0) {
					setFont(record);
					wcfFC.setAlignment(Alignment.CENTRE);
					wcfFC.setWrap(true);
					cellLabel = new Label(colC, rowCount, obj.toString(), wcfFC);
					sheet.addCell(cellLabel);
				} else {
					setFont(record);
					wcfFC.setAlignment(Alignment.RIGHT);
					wcfFC.setWrap(true);
					cellNumber = new Number(colC, rowCount, Double.parseDouble(obj.toString()),
							wcfFC);
					sheet.addCell(cellNumber);
				}
			}
			if (i >= count) {
				ExcelField field = fields.get(j);
				j++;
				// rowC += field.getRowSpan();ROWSPAN太复杂
				int colcount = colC + field.getColSpan() - 1;
				sheet.mergeCells(colC, rowCount, colcount, rowCount);
				colC = colcount;
			}
			colC++;
			columnCount++;
		}
	}
	
	private boolean isDouble(String value){
		try{
			Double.parseDouble(value);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	/**
	 * 构造excel各部分内容
	 * 
	 * @throws Exception
	 */
	public void constructExcel() throws Exception {
		addtoCount = 0;
		for (int i = 0; i < this.sheetList.size(); i++) {
			this.sheet = (WritableSheet) this.sheetList.get(i);
			this.excelHeader = (ExcelHeader) this.headerList.get(i);
			this.recordsList = (List) this.recordContentList.get(i);
			this.imageList = (List) this.imageContentList.get(i);
			if (!this.titleList.isEmpty())
				this.excelTitle = (ExcelTitle) this.titleList.get(i);

			this.setHeader();
			this.setContent();
			this.setImage();
			this.setTitle();
		}
	}

	public WritableWorkbook addToXls(WritableSheet sheet, int addtoCount) throws Exception {
		this.sheet = sheet;
		this.addtoCount = addtoCount;
		headerRowCount = 3;
		recordsList = (List) recordContentList.get(0);
		imageList = (List)imageContentList.get(0);
		setContent();
		return workbook;
	}

	/**
	 * 返回设置好的excel表格
	 * 
	 * @return 要导出的excel表格
	 * @throws Exception
	 */
	public WritableWorkbook getExcelWorkbook() throws Exception {
		this.constructExcel();
		return this.workbook;
	}

	/**
	 * 关闭输出工作簿
	 * 
	 * @param workbook
	 *            工作簿对象
	 * @param os
	 *            输出流
	 */
	public void closeWorkbook(WritableWorkbook workbook, OutputStream os) {
		try {
			if (workbook != null) {
				workbook.close();
			}
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
			// Settings | File Templates.
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
			// Settings | File Templates.
		} finally {
			workbook = null;
			os = null;
		}
	}

}
