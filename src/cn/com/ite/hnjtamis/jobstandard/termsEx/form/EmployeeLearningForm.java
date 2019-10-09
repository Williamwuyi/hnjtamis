package cn.com.ite.hnjtamis.jobstandard.termsEx.form;

/**
 * 合格情况
 * @author 朱健
 * @create time: 2016年3月24日 下午1:48:31
 * @version 1.0
 * 
 * @modified records:
 */
public class EmployeeLearningForm {

	private String employeeId;
	
	private String quartername;//岗位名称
	
	private String lengrningPass;//合格情况
	
	private String passEndDay;//有效结束日期
	
	private int passnum;//合格数量
	
	private int lengrningnum;//学习数量
	
	private int themenum = 0;
	
	private int finthemenum = 0;
	
	private double finthemebfl;

	public String getQuartername() {
		return quartername;
	}

	public void setQuartername(String quartername) {
		this.quartername = quartername;
	}

	public String getLengrningPass() {
		return lengrningPass;
	}

	public void setLengrningPass(String lengrningPass) {
		this.lengrningPass = lengrningPass;
	}

	public String getPassEndDay() {
		return passEndDay;
	}

	public void setPassEndDay(String passEndDay) {
		this.passEndDay = passEndDay;
	}

	public int getPassnum() {
		return passnum;
	}

	public void setPassnum(int passnum) {
		this.passnum = passnum;
	}

	public int getLengrningnum() {
		return lengrningnum;
	}

	public void setLengrningnum(int lengrningnum) {
		this.lengrningnum = lengrningnum;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getThemenum() {
		return themenum;
	}

	public void setThemenum(int themenum) {
		this.themenum = themenum;
	}

	public int getFinthemenum() {
		return finthemenum;
	}

	public void setFinthemenum(int finthemenum) {
		this.finthemenum = finthemenum;
	}

	public double getFinthemebfl() {
		return finthemebfl;
	}

	public void setFinthemebfl(double finthemebfl) {
		this.finthemebfl = finthemebfl;
	}
	
	
	
}
