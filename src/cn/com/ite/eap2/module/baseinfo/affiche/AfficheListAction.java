package cn.com.ite.eap2.module.baseinfo.affiche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.file.FileCopyUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheUser;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.MsgPNGAddNum;
import cn.com.ite.hnjtamis.common.StaticVariable;

/**
 * <p>Title cn.com.ite.eap2.module.baseinfo.affiche.AfficheListAction</p>
 * <p>Description 系统公告LAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-5-21 时间12:24:20
 * @version 2.0
 * 
 * @modified records:
 */
public class AfficheListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = 7116943409771715911L;
	
	private HttpServletRequest request;
	
	/**
	 * 标题查询条件
	 */
	private String titleTerm;
	/**
	 * 开始时间查询条件
	 */
	private Date startTimeTerm;
	/**
	 * 结束时间查询条件
	 */
	private Date endTimeTerm;
	/**
	 * 发布者查询条件
	 */
	private String senderTerm;
	/**
	 * 流程状态
	 */
	private String accessoriesItemId;
	
	private int msgNum;
	
	private String organIdTerm;
	
	private String deptIdTerm;
	
	private String quarterIdTerm;
	
	private String employeeIdTerm;
	
	private String accountTerm;
	
	/**
	 * 查询结果对象
	 */
	private List<SysAffiche> list = new ArrayList<SysAffiche>();
	/**
	 * 查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		list = (List<SysAffiche>)service.queryData("queryHql", this,this.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	/**
	 * 显示有效公告
	 * @return
	 * @modified
	 */
	public String displays(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			this.setTotal(0);
			return "list";
	 	}
		this.setOrganIdTerm(usersess.getOrganId());
		this.setDeptIdTerm(usersess.getDeptId());
		this.setQuarterIdTerm(usersess.getQuarterId());
		this.setEmployeeIdTerm(usersess.getEmployeeId());
		this.setAccountTerm(usersess.getAccount());
		// 修改查询 displayHql -> displayEmployeeAfficheHql
		list = (List<SysAffiche>)service.queryData("displayEmployeeAfficheHql", this,this.getSortMap());
		
		Map termMap = new HashMap();
		termMap.put("accountTerm", usersess.getAccount());
		List<SysAfficheUser> sysAfficheUserlist= (List<SysAfficheUser>)service.queryData("getAllReadAffiche", termMap,null);
		if(sysAfficheUserlist!=null && sysAfficheUserlist.size()>0){
			for(int j=0;j<list.size();j++){
				SysAffiche sysAffiche = list.get(j);
				for(int i=0;i<sysAfficheUserlist.size();i++){
					SysAfficheUser sysAfficheUser = (SysAfficheUser)sysAfficheUserlist.get(i);
					if(sysAffiche.getSaId().equals(sysAfficheUser.getSaId()) && sysAfficheUser.getReadTime()!=null){
						sysAffiche.setUserReadTime(sysAfficheUser.getReadTime());
						break;
					}
				}
			}
		}
		return "list";
	}
	/**
	 * 判断是否存在公告
	 * @return
	 * @modified
	 */
	public String size(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			this.setTotal(0);
			return "list";
	 	}
		this.setOrganIdTerm(usersess.getOrganId());
		this.setDeptIdTerm(usersess.getDeptId());
		this.setQuarterIdTerm(usersess.getQuarterId());
		this.setEmployeeIdTerm(usersess.getEmployeeId());
		this.setAccountTerm(usersess.getAccount());
		// 修改查询 displayHql -> displayEmployeeAfficheHql
		list = (List<SysAffiche>)service.queryData("displayEmployeeAfficheHql", this,this.getSortMap());
		this.setTotal(list.size());
		list.clear();
		list = null;
		return "list";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		for(String id:this.getId().split(",")){
			SysAffiche sa = (SysAffiche)service.findDataByKey(id, SysAffiche.class);
			((AfficheService)service).deleteAffiche(sa);
		}
		this.setMsg("删除系统公告成功！");
		return "delete";
	}
	
	private static String source_path = null;
	private static String target_path = null;
	public String afficheImg(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		//resources\images
		if(source_path==null)source_path = request.getRealPath("")+System.getProperty("file.separator")
				  +"resources"+System.getProperty("file.separator")
				  +"images"+System.getProperty("file.separator");
		if(target_path == null)target_path = request.getRealPath("")+System.getProperty("file.separator")
				  +"upload"+System.getProperty("file.separator")
				  +"msgpng"+System.getProperty("file.separator");
		
		this.setOrganIdTerm(usersess.getOrganId());
		this.setDeptIdTerm(usersess.getDeptId());
		this.setQuarterIdTerm(usersess.getQuarterId());
		this.setEmployeeIdTerm(usersess.getEmployeeId());
		this.setAccountTerm(usersess.getAccount());
		// 修改查询 displayHql -> displayNotReadHql
		List list = (List<SysAffiche>)service.queryData("displayNotReadHql", this,null);
		int size = list.size();
		if(size == 0){
			this.setMsg("true");
			msgNum = 0;
			
			File sourceFile = new File(source_path+"top_menu02.png");
			File targetFile = new File(target_path+"top_menu021_"+usersess.getUserId()+".png");
	    	//System.out.println(targetFile.getPath());
	    	if(!targetFile.isDirectory()){
	    		targetFile.mkdirs();
			}else if(targetFile.exists()){
	    		targetFile.delete();
	    	}
	    	try {
				FileCopyUtil.copyFile(sourceFile, targetFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			File sourceFile = new File(source_path+"top_menu021.png");
			//System.out.println(sourceFile);
	    	File targetFile = new File(target_path+"top_menu021_"+usersess.getUserId()+".png");
	    	//System.out.println(targetFile.getPath());
	    	if(!targetFile.isDirectory()){
	    		targetFile.mkdirs();
			}else if(targetFile.exists()){
	    		targetFile.delete();
	    	}
	    	//MsgPNGAddNum.addPNGFont(sourceFile, targetFile,"10",45,15);//49 45
	    	boolean isCreatPng = false;
	    	if(size<10){
	    		isCreatPng = MsgPNGAddNum.addPNGFont(sourceFile, targetFile,size+"",49,15);
	    	}else if(size<100){
	    		isCreatPng = MsgPNGAddNum.addPNGFont(sourceFile, targetFile,size+"",45,15);
	    	}else{
	    		isCreatPng = MsgPNGAddNum.addPNGFont(sourceFile, targetFile,"M",47,15);
	    	}
	    	if(isCreatPng)this.setMsg("true");
	    	msgNum = size;
		}
		return "save";
	}
	
	
	public String getTitleTerm() {
		return titleTerm;
	}
	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}
	public Date getStartTimeTerm() {
		return startTimeTerm;
	}
	public void setStartTimeTerm(Date startTimeTerm) {
		this.startTimeTerm = startTimeTerm;
	}
	public String getSenderTerm() {
		return senderTerm;
	}
	public void setSenderTerm(String senderTerm) {
		this.senderTerm = senderTerm;
	}
	public Date getEndTimeTerm() {
		return endTimeTerm;
	}
	public void setEndTimeTerm(Date endTimeTerm) {
		this.endTimeTerm = endTimeTerm;
	}
	public List<SysAffiche> getList() {
		return list;
	}
	public void setList(List<SysAffiche> list) {
		this.list = list;
	}
	public String getAccessoriesItemId() {
		return accessoriesItemId;
	}
	public void setAccessoriesItemId(String accessoriesItemId) {
		this.accessoriesItemId = accessoriesItemId;
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	public int getMsgNum() {
		return msgNum;
	}
	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}
	public String getOrganIdTerm() {
		return organIdTerm;
	}
	public void setOrganIdTerm(String organIdTerm) {
		this.organIdTerm = organIdTerm;
	}
	public String getDeptIdTerm() {
		return deptIdTerm;
	}
	public void setDeptIdTerm(String deptIdTerm) {
		this.deptIdTerm = deptIdTerm;
	}
	public String getQuarterIdTerm() {
		return quarterIdTerm;
	}
	public void setQuarterIdTerm(String quarterIdTerm) {
		this.quarterIdTerm = quarterIdTerm;
	}
	public String getEmployeeIdTerm() {
		return employeeIdTerm;
	}
	public void setEmployeeIdTerm(String employeeIdTerm) {
		this.employeeIdTerm = employeeIdTerm;
	}
	public String getAccountTerm() {
		return accountTerm;
	}
	public void setAccountTerm(String accountTerm) {
		this.accountTerm = accountTerm;
	}
	
	
}
