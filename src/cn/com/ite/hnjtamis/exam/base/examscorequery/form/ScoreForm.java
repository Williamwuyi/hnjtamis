package cn.com.ite.hnjtamis.exam.base.examscorequery.form;

import java.util.ArrayList;

public class ScoreForm {
	private String testPaperId;
	private String examName;
	private String userName;
	private double firstScore;
	private double danxuan;
	private double duoxuan;
	private double tiankong;
	private double panduan;
	private double wenda;
	private double shiting;
	private double qita;
	private double keguan;
	private double zhuguan;
	private double xtw;
	
	private double lst;//论述题
	private double jst;//计算题
	private double htt;//画图题
	private ArrayList<String> typeList = new ArrayList<String>();
	
	public String getTestPaperId() {
		return testPaperId;
	}
	public void setTestPaperId(String testPaperId) {
		this.testPaperId = testPaperId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getFirstScore() {
		return firstScore;
	}
	public void setFirstScore(double firstScore) {
		this.firstScore = firstScore;
	}
	public double getDanxuan() {
		return danxuan;
	}
	public void setDanxuan(double danxuan) {
		this.danxuan = danxuan;
	}
	public double getDuoxuan() {
		return duoxuan;
	}
	public void setDuoxuan(double duoxuan) {
		this.duoxuan = duoxuan;
	}
	public double getTiankong() {
		return tiankong;
	}
	public void setTiankong(double tiankong) {
		this.tiankong = tiankong;
	}
	public double getPanduan() {
		return panduan;
	}
	public void setPanduan(double panduan) {
		this.panduan = panduan;
	}
	public double getWenda() {
		return wenda;
	}
	public void setWenda(double wenda) {
		this.wenda = wenda;
	}
	public double getShiting() {
		return shiting;
	}
	public void setShiting(double shiting) {
		this.shiting = shiting;
	}
	public double getQita() {
		return qita;
	}
	public void setQita(double qita) {
		this.qita = qita;
	}
	public ArrayList<String> getTypeList() {
		return typeList;
	}
	public void setTypeList(ArrayList<String> typeList) {
		this.typeList = typeList;
	}
	public double getKeguan() {
		return keguan;
	}
	public void setKeguan(double keguan) {
		this.keguan = keguan;
	}
	public double getZhuguan() {
		return zhuguan;
	}
	public void setZhuguan(double zhuguan) {
		this.zhuguan = zhuguan;
	}
	public double getXtw() {
		return xtw;
	}
	public void setXtw(double xtw) {
		this.xtw = xtw;
	}
	public double getLst() {
		return lst;
	}
	public void setLst(double lst) {
		this.lst = lst;
	}
	public double getJst() {
		return jst;
	}
	public void setJst(double jst) {
		this.jst = jst;
	}
	public double getHtt() {
		return htt;
	}
	public void setHtt(double htt) {
		this.htt = htt;
	}
	
	
}
