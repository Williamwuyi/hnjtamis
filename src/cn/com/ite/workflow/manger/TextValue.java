package cn.com.ite.workflow.manger;

/**
 * <p>Title cn.com.ite.workflow.manger.TextValue</p>
 * <p>Description 用于MAP数据的页面存储与提取</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-4-1 上午10:54:05
 * @version 2.0
 * 
 * @modified records:
 */
public class TextValue implements java.io.Serializable{
	private static final long serialVersionUID = 1929008229481947903L;
	//键
	private String text;
	//值
	private String value;
	public TextValue(String text,String value){
		this.text = text;
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
