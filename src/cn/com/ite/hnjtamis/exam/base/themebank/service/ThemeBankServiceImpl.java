package cn.com.ite.hnjtamis.exam.base.themebank.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemePostForm;
import cn.com.ite.hnjtamis.exam.base.themebank.dao.ThemeBankDao;
import cn.com.ite.hnjtamis.exam.base.themebank.form.ThemeBankNumForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBankPost;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBankProfession;

public class ThemeBankServiceImpl extends DefaultServiceImpl implements ThemeBankService{
	
	/**
	 * 查询专业题库（顶级）
	 * @description
	 * @return
	 * @modified
	 */
	public List<ThemeBank> getProfessionTopThemeBanks(){
		ThemeBankDao themeBankDao =(ThemeBankDao)this.getDao();
		return themeBankDao.getProfessionTopThemeBanks();
	}
	
	/**
	 * 更新关联表对应题库的一些信息
	 * @description
	 * @param theme_bank_id
	 * @param bankOrganId
	 * @param bankOrganName
	 * @param bank_public
	 * @param bank_type
	 * @modified
	 */
	public void updateBankInRelation(String theme_bank_id,String bankOrganId,
			String bankOrganName,String bank_public,String bank_type){
		ThemeBankDao themeBankDao =(ThemeBankDao)this.getDao();
		themeBankDao.updateBankInRelation(theme_bank_id, bankOrganId,
				bankOrganName, bank_public, bank_type);
	}
	
	/**
	 * 删除题库对应的试题关联
	 * @description
	 * @param theme_bank_id
	 * @modified
	 */
	public void deleteThemeInBank(String theme_bank_id){
		ThemeBankDao themeBankDao =(ThemeBankDao)this.getDao();
		themeBankDao.deleteThemeInBank(theme_bank_id);
	}
	
	/**
	 * 获取题库对应有效的试题数目
	 * @description
	 * @param theme_bank_id
	 * @return
	 * @modified
	 */
	public int getThemeNumInBank(String theme_bank_id){
		ThemeBankDao themeBankDao =(ThemeBankDao)this.getDao();
		return themeBankDao.getThemeNumInBank(theme_bank_id);
	}
	

	/**
	 *
	 * @author zhujian
	 * @description 审核人导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importThemeBank(File xls,UserSession usersess,String bankType)throws Exception{
			 String msg = "";
			 if(bankType == null || "".equals(bankType)  || "null".equals(bankType)  || "undefined".equals(bankType)){
				 bankType = "10";
			 }
			 String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
			 Workbook wb = null;
			 Sheet sheet = null;
			 int themeBankName_index = -1;
			 int themeBankCode_index = -1; 
			 int themeAuditName_index = -1;
			 int themeAuditCode_index = -1;
			 Map<String,ThemeBank> themeBankMap = new HashMap<String,ThemeBank>();
			 Map<String,Employee> employeeMap = new HashMap<String,Employee>();
			 try{
				 wb = Workbook.getWorkbook(xls);
				 Sheet[] sheets =  wb.getSheets();
				 sheet = sheets[0];
			 }catch(Exception e){
				 msg = "导入错误，只能选择EXCEL文件";
				 throw new Exception(msg);
			 }
			 
			 try{
				 	List<ThemeBank> themeBanklist =  (List<ThemeBank>)this.queryAllDate(ThemeBank.class);
					if(themeBanklist!=null && themeBanklist.size()>0){
						for(int i=0;i<themeBanklist.size();i++){
							ThemeBank themeBank = themeBanklist.get(i);
							themeBankMap.put(themeBank.getThemeBankCode(),themeBank);
						}
					}
					
					List<Employee> employeelist =  (List<Employee>)this.queryAllDate(Employee.class);
					if(employeelist!=null && employeelist.size()>0){
						for(int i=0;i<employeelist.size();i++){
							Employee employee = employeelist.get(i);
							employeeMap.put(employee.getEmployeeCode(),employee);
						}
					}
				
					String[][] xls_speciality_exptemplate = {
								{"题库名称","themeBankName","-1","field"},
								{"题库编码","themeBankCode","-1","field"},
								{"审核人","themeAuditName","-1","field"},
								{"员工编码","themeAuditCode","-1","field"}};
					
				     for(int i = 0 ;i<sheet.getColumns();i++){
				        	Cell tmpColumns = (Cell)sheet.getCell(i, 0);
				        	for(int j = 0 ;j<xls_speciality_exptemplate.length;j++){
				        		if(xls_speciality_exptemplate[j][0].equals(tmpColumns.getContents())&& "-1".equals(xls_speciality_exptemplate[j][2])){
				        			xls_speciality_exptemplate[j][2] = i+"";
				        			
				        			if("themeBankName".equals(xls_speciality_exptemplate[j][1])){
				        				themeBankName_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("themeBankCode".equals(xls_speciality_exptemplate[j][1])){
				        				themeBankCode_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("themeAuditName".equals(xls_speciality_exptemplate[j][1])){
				        				themeAuditName_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("themeAuditCode".equals(xls_speciality_exptemplate[j][1])){
				        				themeAuditCode_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}
				        		}
				        	}
				      }
				}catch(Exception e){
						e.printStackTrace();
						msg = "导入初始化数据失败！";
						throw new Exception(msg);
				 }
				 
				 
				 try{
					int rowNum = sheet.getRows();
			     	Map<String,Integer> saveMap = new HashMap<String,Integer>();
			     	for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
			     		Cell tmpColumns = (Cell)sheet.getCell(themeBankCode_index, j);
			        	if(!(tmpColumns==null || tmpColumns.getContents()==null || "".equals(tmpColumns.getContents().trim()) 
			        			|| "null".equals(tmpColumns.getContents()))){
			        		String contents = tmpColumns.getContents().trim();
			        		ThemeBank themeBank = themeBankMap.get(contents);
			        		if(themeBank == null){
			        			themeBank = new ThemeBank();
			        			Cell tmpColumns2 = (Cell)sheet.getCell(themeBankName_index, j);
					        	if(!(tmpColumns2==null || tmpColumns2.getContents()==null 
					        			|| "".equals(tmpColumns2.getContents().trim()) 
					        			|| "null".equals(tmpColumns2.getContents()))){
					        		String contents2 = tmpColumns2.getContents().trim();
					        		themeBank.setThemeBankName(contents2);
					        	}
					        	themeBank.setBankType(bankType);
			        			themeBank.setThemeBankCode(contents);
			        			themeBank.setCreatedBy(usersess.getEmployeeName());
			        			themeBank.setCreatedIdBy(usersess.getEmployeeId());
			        			themeBank.setCreationDate(nowTime);
			        			themeBank.setOrganId(usersess.getCurrentOrganId());
			        			themeBank.setOrganName(usersess.getCurrentOrganName());
			        			themeBank.setBankPublic("20");
			        		}
			        		
			        		Employee employee = null;
			        		Cell tmpColumns3 = (Cell)sheet.getCell(themeAuditCode_index, j);
				        	if(!(tmpColumns3==null || tmpColumns3.getContents()==null 
				        			|| "".equals(tmpColumns3.getContents().trim()) 
				        			|| "null".equals(tmpColumns3.getContents()))){
				        		String contents3 = tmpColumns3.getContents().trim();
				        		employee = employeeMap.get(contents3);
				        	}
				        	
				        	if(employee != null){
				        		boolean isFlag = false;
				        		if(themeBank.getThemeAuditId() == null){
				        			themeBank.setThemeAuditId(employee.getEmployeeId());
				        			themeBank.setThemeAuditName(employee.getEmployeeName());
				        			isFlag = true;
				        		}else if(themeBank.getThemeAuditId().indexOf(employee.getEmployeeId())==-1){
				        			themeBank.setThemeAuditId(themeBank.getThemeAuditId()+","+employee.getEmployeeId());
				        			themeBank.setThemeAuditName(themeBank.getThemeAuditName()+","+employee.getEmployeeName());
				        			isFlag = true;
				        		}
				        		if(isFlag){
					        		Integer savenum = (Integer)saveMap.get(themeBank.getThemeBankCode());
					        		if(savenum == null){
					        			savenum = new Integer(1);
					        		}else{
					        			savenum++;
					        		}
					        		saveMap.put(themeBank.getThemeBankCode(), savenum);
					        		themeBankMap.put(themeBank.getThemeBankCode(),themeBank);
				        		}
				        	}
			        		
			        		
			        	}
			     	}
			     	if(!saveMap.keySet().isEmpty()){
			     		Iterator its = saveMap.keySet().iterator();
			     		int savenumall = 0;
			     		List saveList = new ArrayList();
			     		while(its.hasNext()){
			     			String themeBankCode = (String)its.next();
			     			Integer savenum = (Integer)saveMap.get(themeBankCode);
			     			if(savenum!=null){
			     				savenumall+=savenum.intValue();
			     			}
			     			ThemeBank themeBank = themeBankMap.get(themeBankCode);
			     			if(themeBank!=null){
			     				saveList.add(themeBank);
			     			}
			     		}
			     		
						this.saves(saveList);
						msg = "成功保存"+saveList.size()+"个题库对应的共"+savenumall+"审核人！";
					 }else{
						 msg = "未成功保存题库审核人！";
					 }
			 }catch(Exception e){
				e.printStackTrace();
				msg = "导入错误，请联系管理员！";
				throw new Exception(msg);
			 }
		return msg;
	}
	
	/**
	 * 获取专业与题库树
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> specialityThemeBankTree(Map paramMap)throws Exception{
		ThemeBankDao themeBankDao = (ThemeBankDao)this.getDao();
		return themeBankDao.specialityThemeBankTree(paramMap);
	}
	
	/**
	 * 查询专业题库
	 * @description
	 * @return
	 * @modified
	 */
	public TreeNode getProThemeBankTree(){
		ThemeBankDao themeBankDao = (ThemeBankDao)this.getDao();
		return themeBankDao.getProThemeBankTree();
	}
	
	/**
	 * 获取题库树
	 * @description
	 * @param topId
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<TreeNode> getThemeBankTree(String topId,Map paramMap){
		ThemeBankDao themeBankDao = (ThemeBankDao)this.getDao();
		return themeBankDao.getThemeBankTree(topId,paramMap);
	}
	
	/*
	 * 保存题库
	 */
	public void saveThemeBank(ThemeBank bo) throws Exception{
		boolean change = true;
		if(!StringUtils.isEmpty(bo.getThemeBankId())){
			change = false;
			ThemeBank old = (ThemeBank) this.findDataByKey(bo.getThemeBankId(), ThemeBank.class);
			if((bo.getThemeBank()==null || StringUtils.isEmpty(bo.getThemeBank().getThemeBankId()))&&old.getThemeBank()!=null){
				change = true;
			}else if(bo.getThemeBank()!=null&&bo.getThemeBank().getThemeBankId()!=null&&
					(old.getThemeBank()==null||!old.getThemeBank().getThemeBankId().equals(bo.getThemeBank().getThemeBankId()))){
				change = true;
			}
		}
		if(change){
			if(bo.getThemeBank()!=null && !StringUtils.isEmpty(bo.getThemeBank().getThemeBankId())){
				bo.setSortNum(1+this.getFieldMax(ThemeBank.class,
						"sortNum", "themeBank.themeBankId", bo.getThemeBank().getThemeBankId()));
			}else{
				bo.setSortNum(1+this.getFieldMax(ThemeBank.class,"sortNum"));
			}
			
		}
		super.save(bo);
		this.levelCodeHandler(bo, "themeBankId", "themeBank", "themeBanks", "sortNum", "bankLevelCode");
	}
	/*
	 * 保存排序
	 */
	public void saveSort(String[] orders) throws Exception{
		int index = 0;
		List saves = new ArrayList();
		for(String id:orders){
			ThemeBank tt = (ThemeBank) this.findDataByKey(id, ThemeBank.class);
			tt.setSortNum(index++);
			this.levelCodeHandler(tt, "themeBankId", "themeBank", "themeBanks", "sortNum", "bankLevelCode");
			saves.add(tt);
		}
		super.saves(saves);
	}
	/*
	 * 保存题库的岗位
	 */
	public void savePosts(ThemeBank po,List<ThemePostForm> themePostFormList,UserSession usersess){
		List<ThemeBankPost> themeBankPosts = new ArrayList<ThemeBankPost>();
		String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		for (ThemePostForm formP : themePostFormList) {
			ThemeBankPost t = new ThemeBankPost();
			t.setPostId(formP.getPostId());
			t.setPostName(formP.getPostName());
			t.setThemeBank(po);
			t.setCreatedBy(usersess.getEmployeeName());
			t.setCreatedIdBy(usersess.getEmployeeName());
			t.setCreationDate(creationDate);
			t.setOrganId(usersess.getCurrentOrganId());
			t.setOrganName(usersess.getCurrentOrganName());
			themeBankPosts.add(t);
		}
		try {
			this.saves(themeBankPosts);
		} catch (EapException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 保存题库的专业
	 */
	public void savePros(ThemeBank po,List<Speciality> specialitys,UserSession usersess){
		List<ThemeBankProfession> themeBankProfessions = new ArrayList<ThemeBankProfession>();
		String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		for (Speciality formP : specialitys) {
			ThemeBankProfession t = new ThemeBankProfession();
			t.setSpeciality(formP);
			t.setProfessionName(formP.getSpecialityname());
			t.setThemeBank(po);
			t.setCreatedBy(usersess.getEmployeeName());
			t.setCreatedIdBy(usersess.getEmployeeName());
			t.setCreationDate(creationDate);
			t.setOrganId(usersess.getCurrentOrganId());
			t.setOrganName(usersess.getCurrentOrganName());
			themeBankProfessions.add(t);
		}
		try {
			this.saves(themeBankProfessions);
		} catch (EapException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 删除题库关联的岗位
	 */
	public void deletePosts(ThemeBank po){
		try {
			Map term = new HashMap();
			term.put("themeBankId", new String[]{po.getThemeBankId()});
			List<ThemeBankPost> deletes = this.queryData("queryPostsByBankId", term, null, ThemeBankPost.class);
			this.deletes(deletes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 删除题库关联的岗位
	 */
	public void deletePosts(String[] po){
		try {
			Map term = new HashMap();
			term.put("themeBankId", po);
			List<ThemeBankPost> deletes = this.queryData("queryPostsByBankId", term, null, ThemeBankPost.class);
			this.deletes(deletes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 删除题库关联的专业
	 */
	public void deletePros(ThemeBank po){
		try {
			Map term = new HashMap();
			term.put("themeBankId", new String[]{po.getThemeBankId()});
			List<ThemeBankProfession> deletes = this.queryData("queryProsByBankId", term, null, ThemeBankProfession.class);
			this.deletes(deletes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 删除题库关联的专业
	 */
	public void deletePros(String[] po){
		try {
			Map term = new HashMap();
			term.put("themeBankId", po);
			List<ThemeBankProfession> deletes = this.queryData("queryProsByBankId", term, null, ThemeBankProfession.class);
			this.deletes(deletes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 获取每个试题的数目
	 * @return
	 * @modified
	 */
	public Map getThemeNumMap(){
		Map themeNumMap =  new HashMap();
		List themeList = this.queryData("findThemeNumSql", null, null, ThemeBankNumForm.class);
		for(int i=0;i<themeList.size();i++){
			ThemeBankNumForm themeBank = (ThemeBankNumForm)themeList.get(i);
			//System.out.println(themeBank.getThemeBankId()+" "+ themeBank.getThemeNum());
			themeNumMap.put(themeBank.getThemeBankId(), themeBank.getThemeNum()+"");
		}
		return themeNumMap;
	}
}
