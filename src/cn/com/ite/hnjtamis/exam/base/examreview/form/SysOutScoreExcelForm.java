package cn.com.ite.hnjtamis.exam.base.examreview.form;

public class SysOutScoreExcelForm {
	private String examPaperThemeId;
	private String examName;
	private int tihao;
	private String timu;
	private String userName;
	private String zkzNum;
	private double score;
	private double defaultScore;
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public int getTihao() {
		return tihao;
	}
	public void setTihao(int tihao) {
		this.tihao = tihao;
	}
	public String getTimu() {
		return timu;
	}
	public void setTimu(String timu) {
		this.timu = timu;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getZkzNum() {
		return zkzNum;
	}
	public void setZkzNum(String zkzNum) {
		this.zkzNum = zkzNum;
	}
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	public String getExamPaperThemeId() {
		return examPaperThemeId;
	}
	public void setExamPaperThemeId(String examPaperThemeId) {
		this.examPaperThemeId = examPaperThemeId;
	}
	
	public double getDefaultScore() {
		return defaultScore;
	}
	public void setDefaultScore(double defaultScore) {
		this.defaultScore = defaultScore;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.examName+"  "+this.tihao+"  "+this.timu+"  "+this.zkzNum+"  "+this.score;
	}
}
