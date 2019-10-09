package cn.com.ite.eap2.module.funres.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.funres.AppSystem;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.funres.app.AppListAction</p>
 * <p>Description 应用系统ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public class AppListAction extends AbstractListAction{
	private static final long serialVersionUID = -2652717947349804498L;
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	/**
	 * 查询结果
	 */
	private List<AppSystem> list;
	/**
	 * 导入文件
	 */
	private File xls;
	/**
	 * 导出文件流
	 */
	private InputStream inputStream;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 导入操作
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String importXls() throws Exception{
		AppService appService= (AppService)service;
		try{
		  appService.importDate(xls);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "save";
	}
	/**
	 * 导出文件
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String exportXls() throws Exception{
		AppService appService= (AppService)service;
		File exportXlsFile = appService.exportDate(this.getId());
		inputStream = new FileInputStream(exportXlsFile);
		this.fileName = new String("系统模块资源菜单及字典.xls".getBytes(),"ISO-8859-1");
		return "export";
	}
	/**
	 * 列表查询数据
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		UserSession us = LoginAction.getUserSessionInfo();
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("userId", us.getUserId());
		term.put("nameTerm", this.getNameTerm());
		list = (List<AppSystem>)service.queryData("queryHql", term,this.getSortMap(),this.getStart(), this.getLimit());
		java.util.Collections.sort(list, new Comparator(){
			public int compare(Object o1, Object o2) {
				AppSystem as1 = (AppSystem)o1;
				AppSystem as2 = (AppSystem)o2;
				return as1.getOrderNo().compareTo(as2.getOrderNo());
			}});
		this.setTotal(service.countData("queryHql", term));
		return "list";
	}
	
	public String all(){
		list = (List<AppSystem>)service.queryData("queryAllHql", new HashMap(),this.getSortMap());
		return "list";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), AppSystem.class);
		this.setMsg("系统删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public List<AppSystem> getList() {
		return list;
	}
	public void setList(List<AppSystem> list) {
		this.list = list;
	}
	public File getXls() {
		return xls;
	}
	public void setXls(File xls) {
		this.xls = xls;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}