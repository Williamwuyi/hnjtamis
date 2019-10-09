package cn.com.ite.hnjtamis.exam.base.theme.form;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.form.ThemeAnswerkeyForm</p>
 * <p>Description 试题答案</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 下午4:54:10
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeAnswerkeyForm {

	
	private String answerkeyId;
	private String answerkeyValue;//答案值
	private boolean isRight;//是否正确 5：否,10：是
	private Short sortNum;//序号
	private String answerRemark;//备注
	public String getAnswerkeyId() {
		return answerkeyId;
	}
	public void setAnswerkeyId(String answerkeyId) {
		this.answerkeyId = answerkeyId;
	}
	public String getAnswerkeyValue() {
		return answerkeyValue;
	}
	public void setAnswerkeyValue(String answerkeyValue) {
		this.answerkeyValue = answerkeyValue;
	}
	public boolean getIsRight() {
		return isRight;
	}
	public void setIsRight(boolean isRight) {
		this.isRight = isRight;
	}
	public Short getSortNum() {
		return sortNum;
	}
	public void setSortNum(Short sortNum) {
		this.sortNum = sortNum;
	}
	public String getAnswerRemark() {
		return answerRemark;
	}
	public void setAnswerRemark(String answerRemark) {
		this.answerRemark = answerRemark;
	}
	
	
	
	
}
