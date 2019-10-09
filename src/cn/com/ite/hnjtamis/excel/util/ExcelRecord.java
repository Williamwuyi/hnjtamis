package cn.com.ite.hnjtamis.excel.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.format.Colour;

public class ExcelRecord {
	private HashMap record = null;//记录对象，一般以集合形式用来存储记录数据
    private List recordFields = new ArrayList();//声明记录对象
    private int fontSize = 9;//行记录的字体大小，默认是10
    private boolean fontBold = false;//行记录是否加粗
    private Colour backGroundColor = null;//行记录的背景色
    private Colour frontColor = null;//行记录的前景色
    private int rowHeight = 400;//行记录的高度
    private boolean haveSeriesNum = false;//是否有序号
    private boolean isBorder = true;	//是否带边框，默认带
    
    private List<ExcelField> fields = new ArrayList<ExcelField>();//记录字段对象集合
    

	public ExcelRecord(){
        record = new HashMap();
    }

    public boolean isHaveSeriesNum() {
        return haveSeriesNum;
    }

    public void setHaveSeriesNum(boolean haveSeriesNum) {
        this.haveSeriesNum = haveSeriesNum;
    }

    /**
     * 设置行记录
     * @param name 字段名称
     * @param obj 设置值对象
     */
    public void set(String name,Object obj){
        this.record.put(name,obj);
        this.recordFields.add(obj);
    }
    /**
     * 设置行记录
     * @param name
     * @param number
     */
    public void setNumber(String name,Number number){
        this.record.put(name,number);
    }
    public void setString(String name,String value){
        this.record.put(name,value);
    }
    public Object get(String name){
        return this.record.get(name);
    }
    /**
     * 得到记录中的字段集合
     * @return
     */
    public Iterator recordKeySetIterator(){
        return this.record.keySet().iterator();
    }
    public List getRecordFieldList(){
        return this.recordFields;
    }
    public Number getNumber(String name){
        return (Number)this.record.get(name);
    }
    public String getString(String name){
        return (String)this.record.get(name);
    }
    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public Colour getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(Colour frontColor) {
        this.frontColor = frontColor;
    }

    public Colour getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(Colour backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isFontBold() {
        return fontBold;
    }

    public void setFontBold(boolean fontBold) {
        this.fontBold = fontBold;
    }

	public List<ExcelField> getFields() {
		return fields;
	}

	public void setFields(List<ExcelField> fields) {
		this.fields = fields;
	}

	public boolean isBorder() {
		return isBorder;
	}

	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}
}
