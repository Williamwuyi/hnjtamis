package cn.com.ite.hnjtamis.baseinfo.speciality;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityStandardTypes;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityType;
import cn.com.ite.hnjtamis.baseinfo.specialitytype.SpecialityTypeService;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBankProfession;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
import cn.com.ite.hnjtamis.jobstandard.jobunionsepciality.JobUnionSpecialityService;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业接口实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class SpecialityServiceImpl extends DefaultServiceImpl implements
		SpecialityService {
	private SpecialityTypeService specialitytypeServer; // 专业类型
	private JobUnionSpecialityService jobunionspecialityServer; /// 岗位关联专业
	public SpecialityTypeService getSpecialitytypeServer() {
		return specialitytypeServer;
	}
	public void setSpecialitytypeServer(SpecialityTypeService specialitytypeServer) {
		this.specialitytypeServer = specialitytypeServer;
	}
	
	/**
	 * 同步岗位对应专业
	 * @description
	 * @modified
	 */
	public void saveSyncJobsUnionSpeciality()throws Exception{
		SpecialityDao specialityDao = (SpecialityDao)this.getDao();
		specialityDao.saveSyncJobsUnionSpeciality();
	}
	/**
	 *
	 * @author zhujian
	 * @description 专业导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importSpeciality(File xls,UserSession usersess)throws Exception{
			 String msg = "";
			 String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
			 Workbook wb = null;
			 Sheet sheet = null;
			 int specialityType_index = -1;
		     int specialityName_index = -1;
		     int specialityCode_index = -1;
		     int model_index = -1;
		     int childModel_index = -1;
		     //int themeBankCode_index = -1;
		     
		     Map<String,SpecialityType> specialityTypeMap = new HashMap<String,SpecialityType>();//分类
		     Map<String,Speciality> specialityMap = new HashMap<String,Speciality>();//专业
		     Map<String,StandardTerms> standardTermsMap = new HashMap<String,StandardTerms>();
			//题库
		     //Map<String,List<ThemeBankProfession>> themeBankProfessionMap = new HashMap<String,List<ThemeBankProfession>>();
		     //Map<String,ThemeBankProfession> themeBankProfessionKeyMap = new HashMap<String,ThemeBankProfession>();
		     //Map<String,ThemeBank> themeBankMap = new HashMap<String,ThemeBank>();
			 try{
				 wb = Workbook.getWorkbook(xls);
				 Sheet[] sheets =  wb.getSheets();
				 sheet = sheets[0];
			 }catch(Exception e){
				 msg = "导入错误，只能选择EXCEL文件";
				 throw new Exception(msg);
			 }
			 
			 try{
					//分类
					List<SpecialityType> specialityTypelist = (List<SpecialityType>)this.queryAllDate(SpecialityType.class);
					if(specialityTypelist!=null && specialityTypelist.size()>0){
						for(int i=0;i<specialityTypelist.size();i++){
							SpecialityType specialityType = specialityTypelist.get(i);
							specialityTypeMap.put(specialityType.getTypename(), specialityType);
						}
					}
					
					//专业
					List<Speciality> specialitylist = (List<Speciality>)this.queryAllDate(Speciality.class);
					if(specialitylist!=null && specialitylist.size()>0){
						for(int i=0;i<specialitylist.size();i++){
							Speciality speciality = specialitylist.get(i);
							if(speciality.getSpecialitycode()!=null){
								specialityMap.put(speciality.getSpecialitycode(), speciality);
							}
							String spname = "";
							if(speciality.getSpecialityType()!=null){
								spname +=speciality.getSpecialityType().getTypename()+"_";
							}
							spname+=speciality.getSpecialityname();
							specialityMap.put(spname, speciality);
						}
					}
				
					//获取题库对应的专业
					/*List<ThemeBankProfession> themeBankProfessionlist =  (List<ThemeBankProfession>)this.queryAllDate(ThemeBankProfession.class);
					if(themeBankProfessionlist!=null && themeBankProfessionlist.size()>0){
						for(int i=0;i<themeBankProfessionlist.size();i++){
							ThemeBankProfession themeBankProfession = themeBankProfessionlist.get(i);
							if(themeBankProfession.getThemeBank() == null || themeBankProfession.getSpeciality()==null){
								continue;
							}
							String key = themeBankProfession.getSpeciality().getSpecialityid();
							String key2 = key+"_"+themeBankProfession.getThemeBank().getThemeBankCode();
							themeBankProfessionKeyMap.put(key2, themeBankProfession);
							List<ThemeBankProfession> tmplist = themeBankProfessionMap.get(key);
							if(tmplist == null){
								tmplist = new ArrayList();
							}
							tmplist.add(themeBankProfession);
							themeBankProfessionMap.put(key,tmplist);
						}
					}
				
					List<ThemeBank> themeBanklist =  (List<ThemeBank>)this.queryAllDate(ThemeBank.class);
					if(themeBanklist!=null && themeBanklist.size()>0){
						for(int i=0;i<themeBanklist.size();i++){
							ThemeBank themeBank = themeBanklist.get(i);
							themeBankMap.put(themeBank.getThemeBankCode(),themeBank);
						}
					}*/
					List<StandardTerms> standardTermslist =  (List<StandardTerms>)this.queryAllDate(StandardTerms.class);
					if(standardTermslist!=null && standardTermslist.size()>0){
						for(int i=0;i<standardTermslist.size();i++){
							StandardTerms standardTerms = standardTermslist.get(i);
							StandardTypes standardTypes = standardTerms.getStandardTypes();
							if(standardTypes!=null){
								standardTermsMap.put(standardTypes.getTypename()+"_"+standardTerms.getStandardname(),standardTerms);
							}else{
								standardTermsMap.put(standardTerms.getStandardname(),standardTerms);
							}
							
						}
					}
				
					String[][] xls_speciality_exptemplate = {
								{"企业类型","specialityType","-1","field"},
								{"专业","specialityName","-1","field"},
								{"专业编码","specialityCode","-1","field"},
								{"模块","model","-1","field"},
								{"子模块","childModel","-1","field"}};//,{"题库编码","themeBankCode","-1","field"}
					
				     for(int i = 0 ;i<sheet.getColumns();i++){
				        	Cell tmpColumns = (Cell)sheet.getCell(i, 0);
				        	for(int j = 0 ;j<xls_speciality_exptemplate.length;j++){
				        		if(xls_speciality_exptemplate[j][0].equals(tmpColumns.getContents())&& "-1".equals(xls_speciality_exptemplate[j][2])){
				        			xls_speciality_exptemplate[j][2] = i+"";
				        			
				        			if("specialityType".equals(xls_speciality_exptemplate[j][1])){
				        				specialityType_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("specialityName".equals(xls_speciality_exptemplate[j][1])){
				        				specialityName_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("specialityCode".equals(xls_speciality_exptemplate[j][1])){
				        				specialityCode_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("model".equals(xls_speciality_exptemplate[j][1])){
				        				model_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}else if("childModel".equals(xls_speciality_exptemplate[j][1])){
				        				childModel_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}/*else if("themeBankCode".equals(xls_speciality_exptemplate[j][1])){
				        				themeBankCode_index = Integer.parseInt(xls_speciality_exptemplate[j][2]);
				        			}*/
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
			     	List<Speciality> saveList = new ArrayList<Speciality>();
			     	List<Speciality> specialityList = new ArrayList<Speciality>();
			     	for (int j = 0; j < rowNum; j++) {
			     		specialityList.add(null);
			     	}
			     	for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
			     		SpecialityType specialityType = null;
			     		Speciality speciality = null;
				     	if(specialityType_index>=0){
				     		Cell tmpColumns = (Cell)sheet.getCell(specialityType_index, j);
				        	if(!(tmpColumns==null || tmpColumns.getContents()==null || "".equals(tmpColumns.getContents().trim()) 
				        			|| "null".equals(tmpColumns.getContents()))){
				        		String contents = tmpColumns.getContents();
				        		specialityType = specialityTypeMap.get(contents);
				        		if(specialityType == null){
				        			specialityType = new SpecialityType();
				        			specialityType.setTypename(contents);
				        			specialityType.setOrderno(1+this.getFieldMax(SpecialityType.class, "orderno"));
				        			specialityType.setIsavailable(1);
					        		specialityType.setStatus(1);
					        		specialityType.setCreationDate(nowTime);//创建时间
					        		specialityType.setCreatedBy(usersess.getEmployeeName());//创建人
					        		this.saveOld(specialityType);
					        		specialityTypeMap.put(contents, specialityType);
				        		}else if(!contents.equals(specialityType.getTypename())){
				        			specialityType.setTypename(contents);
				        			this.saveOld(specialityType);
				        		}
				        		
				        	}
			     		}
				     	if(specialityCode_index>=0){
				     		Cell tmpColumns3 = (Cell)sheet.getCell(specialityCode_index, j);
				        	if(tmpColumns3==null || tmpColumns3.getContents()==null 
				        			|| "".equals(tmpColumns3.getContents().trim()) 
				        			|| "null".equals(tmpColumns3.getContents())){
				        		continue;
				        	}else{
				        		String contents = tmpColumns3.getContents();
				        		Cell tmpColumns2 = (Cell)sheet.getCell(specialityName_index, j);//名字
				        		speciality = specialityMap.get(contents);
				        		if(speciality == null && 
				        				!(tmpColumns2==null || tmpColumns2.getContents()==null 
					        			|| "".equals(tmpColumns2.getContents().trim()) 
					        			|| "null".equals(tmpColumns2.getContents()))){
				        			speciality = specialityMap.get(specialityType.getTypename()+"_"+tmpColumns2.getContents());
					        	}
				        		if(speciality == null){
				        			speciality = new Speciality();
				        			if(specialityType!=null){
				        				speciality.setSpecialitycode(contents);
				        				speciality.setSpecialityType(specialityType);
				        				speciality.setOrderno(1+this.getFieldMax(Speciality.class, "orderno"));
				        			}
				        			speciality.setIsavailable(1);
				        			speciality.setStatus(1);
				        			speciality.setCreationDate(nowTime);//创建时间
				        			speciality.setCreatedBy(usersess.getEmployeeName());//创建人
				        		}else{
				        			speciality.setSpecialitycode(contents);
				        			System.out.println(speciality.getSpecialityname());
				        		}
				        		
					        	if(!(tmpColumns2==null || tmpColumns2.getContents()==null 
					        			|| "".equals(tmpColumns2.getContents().trim()) 
					        			|| "null".equals(tmpColumns2.getContents()))){
					        		speciality.setSpecialityname(tmpColumns2.getContents());
					        	}
					        	saveList.add(speciality);
					        	specialityList.set(j, speciality);
				        	}
				        	String[] parentModelArr = null;
				        	if(model_index>=0){
				        		Cell tmpColumnsM = (Cell)sheet.getCell(model_index, j);
				        		if(tmpColumnsM!=null && !(tmpColumnsM.getContents()==null
				        				|| "".equals(tmpColumnsM.getContents().trim()) 
				        				|| "null".equals(tmpColumnsM.getContents().trim()))){
				        			String contents = tmpColumnsM.getContents();
				        			contents = contents.replaceAll("；", ";");
				        			if(contents.length()>2000){
				        				speciality.setParentTypesNames(contents.substring(0,1990)+"...");
				        			}else{
				        				speciality.setParentTypesNames(contents);
				        			}
				        			parentModelArr = contents.split(";");
				        		}
				        	}
				        	if(childModel_index>=0 && parentModelArr!=null){
				        		Cell tmpColumnsCM = (Cell)sheet.getCell(childModel_index, j);
				        		if(tmpColumnsCM!=null && !(tmpColumnsCM.getContents()==null
				        				|| "".equals(tmpColumnsCM.getContents().trim()) 
				        				|| "null".equals(tmpColumnsCM.getContents().trim()))){
				        			speciality.getSpecialityStandardTypeslist().clear();
				        			String contents = tmpColumnsCM.getContents();
				        			contents = contents.replaceAll("；", ";");
				        			if(contents.length()>2000){
				        				speciality.setTypesNames(contents.substring(0,1990)+"...");
				        			}else{
				        				speciality.setTypesNames(contents);
				        			}
				        			
				        			String[] modelArr = contents.split(";");
				        			Map values = new HashMap();
				        			for(int i=0;i<parentModelArr.length;i++){
				        				for(int k=0;k<modelArr.length;k++){
					        				String sname = parentModelArr[i]+"_"+modelArr[k];
					        				StandardTerms standardTerms = standardTermsMap.get(sname);
					        				if(standardTerms!=null && values.get(sname) == null){
						        				SpecialityStandardTypes specialityStandardTypes = new SpecialityStandardTypes();
						        				specialityStandardTypes.setSpeciality(speciality);
						        				StandardTypes standardTypes = standardTerms.getStandardTypes();
												if(standardTypes!=null){
													specialityStandardTypes.setParentTypesName(standardTypes.getTypename());
													specialityStandardTypes.setParentTypesId(standardTypes.getJstypeid());
												}
						        				specialityStandardTypes.setTypesName(standardTerms.getStandardname());
						        				specialityStandardTypes.setTypesId(standardTerms.getStandardid());
						        				specialityStandardTypes.setOrderno(k);
						        				specialityStandardTypes.setCreationDate(nowTime);
						        				specialityStandardTypes.setCreatedBy(usersess.getEmployeeName());
						        				
						        				speciality.getSpecialityStandardTypeslist().add(specialityStandardTypes);
						        				values.put(sname,sname);
					        				}
					        			}
				        			}
				        		}
				        	}
				     	}
			     	}
			     	if(saveList.size()>0){
						this.saves(saveList);
						msg = "成功保存"+saveList.size()+"个专业！";
					 }
			     	
			     	//-------------------------------------以下为处理题库-------------------------------------
			     	/*if(themeBankCode_index>=0){
				     	List savetbspList = new ArrayList();
				     	List deltbspList = new ArrayList();
				     	for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
				     		Cell tmpColumns = (Cell)sheet.getCell(themeBankCode_index, j);
				     		if(tmpColumns==null || tmpColumns.getContents()==null 
				        			|| "".equals(tmpColumns.getContents().trim()) || "null".equals(tmpColumns.getContents())){
				        		continue;
				        	}
				     		String contents = tmpColumns.getContents();
				     		Speciality speciality = specialityList.get(j);
				     		if(speciality!=null){
				     			//如果为空则全部清除
						     	if(contents==null || "".equals(contents) || "null".equals(contents)){
									List plist = themeBankProfessionMap.get(speciality.getSpecialityid());
									if(plist!=null && plist.size()>0){
										deltbspList.addAll(plist);
									}
								}else{
									contents = contents.replaceAll("，", ",");
									contents = contents.replaceAll(" ", ",");
									contents = contents.replaceAll("\\n", "");
									String[] bankCodes = contents.split(",");
									//添加没有的
									for(int t=0;t<bankCodes.length;t++){
										ThemeBank themeBank = themeBankMap.get(bankCodes[t]);
										if(themeBank!=null 
												&& themeBankProfessionKeyMap.get(speciality.getSpecialityid()+"_"+bankCodes[t])==null){
											List plist = themeBankProfessionMap.get(speciality.getSpecialityid());
											ThemeBankProfession themeBankProfession = new ThemeBankProfession();
											themeBankProfession.setThemeBank(themeBank);
											themeBankProfession.setSpeciality(speciality);
											themeBankProfession.setProfessionName(speciality.getSpecialityname());
											themeBankProfession.setSortNum(t);
											themeBankProfession.setCreationDate(nowTime);//创建时间
											themeBankProfession.setCreatedBy(usersess.getEmployeeName());//创建人
											themeBankProfession.setCreatedIdBy(usersess.getEmployeeId());//创建人
											themeBankProfession.setSyncFlag("1");
											savetbspList.add(themeBankProfession);
										}else if(themeBank!=null 
												&& themeBankProfessionKeyMap.get(speciality.getSpecialityid()+"_"+bankCodes[t])!=null){
											ThemeBankProfession themeBankProfession = themeBankProfessionKeyMap.get(speciality.getSpecialityid()+"_"+bankCodes[t]);
											themeBankProfession.setProfessionName(speciality.getSpecialityname());
											themeBankProfession.setLastUpdateDate(nowTime);//创建时间
											themeBankProfession.setLastUpdatedBy(usersess.getEmployeeName());//创建人
											themeBankProfession.setLastUpdatedIdBy(usersess.getEmployeeId());//创建人
											themeBankProfession.setSyncFlag("1");
											savetbspList.add(themeBankProfession);
										}
									}
									//删除没有的
									List plist = themeBankProfessionMap.get(speciality.getSpecialityid());
									if(plist!=null){
										contents = ","+contents+",";
										for(int i=0;i<plist.size();i++){
											ThemeBankProfession themeBankProfession = (ThemeBankProfession)plist.get(i);
											if(themeBankProfession.getThemeBank()==null || 
													contents.indexOf(themeBankProfession.getThemeBank().getThemeBankCode()) == -1){
												deltbspList.add(themeBankProfession);
											}
										}
									}
								}
				     		}
				     	}
				     	if(deltbspList.size()>0){
				     		this.deletes(deltbspList);
				     	}
				     	if(savetbspList.size()>0){
							this.saves(savetbspList);
						} 
			     	}*/
			 }catch(Exception e){
				e.printStackTrace();
				msg = "导入错误，请联系管理员！";
				throw new Exception(msg);
			 }
		return msg;
	}
	
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findSpecialitiesTree(String topTypeId,String typeName)throws Exception{
		Map term = new HashMap();
		term.put("jobscode", topTypeId);
		List<TreeNode> list = (List<TreeNode>)getDao().queryConfigQl("queryTreeHql", term, null, TreeNode.class);
		
	//// 默认勾选复选框
		List<JobsUnionSpeciality> ulist = getJobunionspecialityServer().findDataByJobId(topTypeId);
		
//		Map<String,TreeNode> ms = new LinkedHashMap<String,TreeNode>();
		for(TreeNode node:list){
			TreeNode f = node;
			f.setType("speciality");
			Iterator<JobsUnionSpeciality> it = ulist.iterator();
			while (it.hasNext()){
				JobsUnionSpeciality us = it.next();
				String specialityId=us.getSpeciality().getSpecialityid();
				if (f.getId().equals(specialityId)){
					f.setChecked(true); //// 添加默认复选框
					it.remove();
					break;
				}
			}
		}
		
		/// 添加专业类型
		list.addAll(getSpecialitytypeServer().findSpecialTypeTree("", typeName));		
		TreeNode.putTypeIncon("speciality", "resources/icons/fam/theme.gif", "");
		TreeNode.putTypeIncon("specialitytype", "resources/icons/fam/plugin_add.gif", "");
		return TreeNode.toTree(list,true,null,"");
	}
	
	/**
	 * 根据类型查询专业
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public List<Speciality> findSpecialitiesByTypeId(String typeId,String typeName) throws Exception{
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("nameTerm", typeName);
		term.put("bstid", typeId);
		Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		sortMap.put("specialityname", true);
		List<Speciality> list = getDao().queryConfigQl("queryHqlByTypeId", term, sortMap, Speciality.class);
		return list;
	}
	
	public JobUnionSpecialityService getJobunionspecialityServer() {
		return jobunionspecialityServer;
	}
	public void setJobunionspecialityServer(
			JobUnionSpecialityService jobunionspecialityServer) {
		this.jobunionspecialityServer = jobunionspecialityServer;
	}
}
