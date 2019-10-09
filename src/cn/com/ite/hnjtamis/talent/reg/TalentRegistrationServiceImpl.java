package cn.com.ite.hnjtamis.talent.reg;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import jxl.BooleanCell;
import jxl.BooleanFormulaCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.DateFormulaCell;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistration;
import cn.com.ite.hnjtamis.talent.domain.TalentRegistrationSpeciality;

public class TalentRegistrationServiceImpl extends DefaultServiceImpl implements
		TalentRegistrationService {
	
	/**
	 * 更新员工的岗位
	 * @description
	 * @modified
	 */
	public void updateEmployeeQuarter(){
		TalentRegistrationDao talentRegistrationDao = (TalentRegistrationDao)this.getDao();
		talentRegistrationDao.updateEmployeeQuarter();
	}
	/**
	 * 获取用户反馈审核的次数
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,Integer> getFkThemeAuditNumMap(){
		TalentRegistrationDao talentRegistrationDao = (TalentRegistrationDao)this.getDao();
		return talentRegistrationDao.getFkThemeAuditNumMap();
	}
	/**
	 * 获取用户阅卷的次数
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,Integer> getExamMarkNumMap(){
		TalentRegistrationDao talentRegistrationDao = (TalentRegistrationDao)this.getDao();
		return talentRegistrationDao.getExamMarkNumMap();
	}
	/**
	 * 同步专家对应题库
	 * @description
	 * @modified
	 */
	public void saveSyncBank()throws Exception{
		TalentRegistrationDao talentRegistrationDao = (TalentRegistrationDao)this.getDao();
		talentRegistrationDao.saveSyncBank();
	}
	/**
	 *
	 * @author zhujian
	 * @description 专家库信息导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importTalentRegistration(File xls,UserSession usersess)throws Exception{
			 String msg = "";
			 TalentRegistrationDao talentRegistrationDao = (TalentRegistrationDao)this.getDao();
			 String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
			 Workbook wb = null;
			 Sheet sheet = null;
			 
			 try{
				 wb = Workbook.getWorkbook(xls);
				 Sheet[] sheets =  wb.getSheets();
				 sheet = sheets[0];
			 }catch(Exception e){
				 msg = "导入错误，只能选择EXCEL文件";
				 throw new Exception(msg);
			 }
			 int rowNum = sheet.getRows();
			 int columnsNum = sheet.getColumns();
			 int startRow = 1;
			 String[][] xls_talentRegistration_exptemplate = {
					 	{"所在单位","organName","-1","employeefield"},
						{"姓名","employeeName","-1","employeefield"},
						{"性别","employeeSex","-1","employeefield"},
						{"出生年月","employeeBir","-1","employeefield"},
						{"参加工作年月","wordDay","-1","field"},
						{"现岗位及职务","quarterName","-1","employeefield"},
						{"专业技术资格情况","zzjgqk","-1","zzjgqk_parent"},
						{"专业技术资格情况_现资格名称","specialityGrade","-1","zzjgqk"},
						{"专业技术资格情况_资格年限（年）","specialityGradeYear","-1","zzjgqk"},
						{"技能等级情况","jndjqk","-1","jndjqk_parent"},
						{"技能等级情况_现技能等级","skillGrade","-1","jndjqk"},
						{"技能等级情况_资格年限（年）","skillGradeYear","-1","jndjqk"},
						{"推荐专业","speciality","-1","speciality_parent"},
						{"推荐专业_1","speciality1","-1","speciality"},
						{"推荐专业_2","speciality2","-1","speciality"},
						{"推荐专业_3","speciality3","-1","speciality"},
						{"推荐专业_4","speciality4","-1","speciality"},
						{"推荐专业组长","specialityzz","-1","specialityzz_parent"},
						{"推荐专业组长_1","specialityzz1","-1","specialityzz"},
						{"推荐专业组长_2","specialityzz2","-1","specialityzz"},
						{"推荐专业组长_3","specialityzz3","-1","specialityzz"},
						{"推荐专业组长_4","specialityzz4","-1","specialityzz"},
						{"推荐专业副组长","specialityfzz","-1","specialityfzz_parent"},
						{"推荐专业副组长_1","specialityfzz1","-1","specialityfzz"},
						{"推荐专业副组长_2","specialityfzz2","-1","specialityfzz"},
						{"推荐专业副组长_3","specialityfzz3","-1","specialityfzz"},
						{"推荐专业副组长_4","specialityfzz4","-1","specialityfzz"},
						//{"112人才情况","explain211","-1","field"},
						{"集团及以上级荣誉","explainGroup","-1","field"},
						{"联系电话","employeePhone","-1","employeefield"},
						{"备注","remark","-1","field"},
						{"员工编码","employeeCode","-1","employeefield"}};
			 Map<String,List<Employee>> employeeMap = new HashMap<String,List<Employee>>();
			 Map<String,Speciality> specialityMap = new HashMap<String,Speciality>();
			 Map<String,TalentRegistration> talentRegistrationMap = new HashMap<String,TalentRegistration>();
			 try{
				  boolean isFlag = false;
				  for (int r = 0; r < rowNum; r++) {//第一行为标题行，从第二行开始读
					for(int i = 0 ;i<columnsNum;i++){
				        	Cell tmpParentColumns = (Cell)sheet.getCell(i, r);
				        	for(int j = 0 ;j<xls_talentRegistration_exptemplate.length;j++){
				        		if(xls_talentRegistration_exptemplate[j][0].equals(tmpParentColumns.getContents())
				        				&& "-1".equals(xls_talentRegistration_exptemplate[j][2])){
				        			if(!isFlag){
				        				isFlag = true;
				        				startRow = r+2;
				        			}
				        			if(xls_talentRegistration_exptemplate[j][3].indexOf("parent")!=-1){
				        				String columntype = xls_talentRegistration_exptemplate[j][3].split("_")[0];
				        				String sname = xls_talentRegistration_exptemplate[j][0];
				        				//尾部是统一类型的进行处理
				        				for(int ii = i ;ii<columnsNum;ii++){
				        					Cell tmpColumns = (Cell)sheet.getCell(ii, r+1);
				        					if(tmpColumns.getContents()!=null && !"".equals(tmpColumns.getContents())){
				        					String child_sname = sname+"_"+tmpColumns.getContents();
					        					for(int jj = j ;jj<xls_talentRegistration_exptemplate.length;jj++){
						        					if(xls_talentRegistration_exptemplate[jj][0].equals(child_sname)
									        				&& "-1".equals(xls_talentRegistration_exptemplate[jj][2])
									        				&& columntype.equals(xls_talentRegistration_exptemplate[jj][3])){
						        						xls_talentRegistration_exptemplate[jj][2] = ii+"";
									        			//System.out.println(child_sname+" "+tmpColumns.getContents()+"  "+ii);
						        					}
					        					}
				        					}
				        				}
				        			}else{
				        				xls_talentRegistration_exptemplate[j][2] = i+"";
					        			//System.out.println(tmpParentColumns.getContents()+"  "+i);
				        			}
				        		}
				        	}
				      }
					if(isFlag){
						break;
					}
				  }
				  
				  List<Speciality> specialitylist = this.queryAllDate(Speciality.class);
				  for(int i=0;i<specialitylist.size();i++){
					  Speciality speciality = specialitylist.get(i);
					  if(speciality.getSpecialitycode()!=null && !"".equals(speciality.getSpecialitycode())){
						  specialityMap.put(speciality.getSpecialitycode(), speciality);
					  }
				  }
				  
				  List<TalentRegistration> talentRegistrationlist = this.queryAllDate(TalentRegistration.class);
				  for(int i=0;i<talentRegistrationlist.size();i++){
					  TalentRegistration talentRegistration = talentRegistrationlist.get(i);
					  if(talentRegistration.getEmployee()!=null){
						  talentRegistrationMap.put(talentRegistration.getEmployee().getEmployeeId(), talentRegistration);
					  }
				  }
				  
				  List<Object[]> emplist = talentRegistrationDao.queryEmployeeList();;
				  for(int i=0;i<emplist.size();i++){
						  Object[] object = emplist.get(i);
						  Organ organ = (Organ)object[0];
						  Dept dept = (Dept)object[1];
						  Quarter quarter = (Quarter)object[2];		  
						  Employee employee = (Employee)object[3];
						  dept.setOrgan(organ);
						  quarter.setDept(dept);
						  employee.setDept(dept);
						  employee.setQuarter(quarter);
					  
					      String employeename = employee.getEmployeeName();
						  List tmplist = employeeMap.get(organ.getOrganName()+"_"+employeename);
						  if(tmplist == null){
							  tmplist = new ArrayList();
						  }
						  tmplist.add(employee);
						  employeeMap.put(organ.getOrganName()+"_"+employeename,tmplist);
						  
						  
						  
						  tmplist = employeeMap.get(organ.getOrganName()+"_"+quarter.getQuarterName()+"_"+employeename);
						  if(tmplist == null){
							  tmplist = new ArrayList();
						  }
						  tmplist.add(employee);
						  employeeMap.put(organ.getOrganName()+"_"+quarter.getQuarterName()+"_"+employeename,tmplist);
						  
						  if(employee.getEmployeeCode()!=null){
							  tmplist = employeeMap.get(employee.getEmployeeCode());
							  if(tmplist == null){
								  tmplist = new ArrayList();
							  }
							  tmplist.add(employee);
							  employeeMap.put(employee.getEmployeeCode(),tmplist);
						  }
	
				  }
				}catch(Exception e){
						e.printStackTrace();
						msg = "导入初始化数据失败！";
						throw new Exception(msg);
				 }

				 try{
					List saveList = new ArrayList();
					for (int j = startRow; j < rowNum; j++) {//第一行为标题行，从第二行开始读
						System.out.println("==============================================================");
						String organName = "";
						String employeeName = "";
						String employeeCode = "";
						String employeeSex = "";
						String employeeBir = "";
						String quarterName = "";
						Employee employee = null;
						for(int i = 0 ;i<xls_talentRegistration_exptemplate.length;i++){
							String expCode = (String)xls_talentRegistration_exptemplate[i][1];
							int columns_index = Integer.parseInt(xls_talentRegistration_exptemplate[i][2]);
							if(columns_index>=0){
								Cell tmpColumns = (Cell)sheet.getCell(columns_index, j);
								if("employeeName".equals(expCode)){
									employeeName = tmpColumns.getContents();
								}else if("organName".equals(expCode)){
									organName = tmpColumns.getContents();
								}else if("employeeCode".equals(expCode)){
									employeeCode = tmpColumns.getContents();
								}else if("employeeSex".equals(expCode)){
									employeeSex = tmpColumns.getContents();
								}else if("employeeBir".equals(expCode)){
									employeeBir = tmpColumns.getContents();
								}else if("quarterName".equals(expCode)){
									quarterName = tmpColumns.getContents();
								}
							}
						}
						System.out.println(organName);
						System.out.println(employeeName);
						System.out.println(employeeCode);
						System.out.println(employeeSex);
						System.out.println(employeeBir);
						System.out.println(quarterName);
						
						List<Employee> employeelist = employeeMap.get(organName+"_"+employeeName);
						if(employeelist!=null && employeelist.size()==1){
							employee = employeelist.get(0);
						}else if(employeelist!=null && employeelist.size()>1){
							for(int i=0;i<employeelist.size();i++){
								Employee tmpEmployee  = employeelist.get(i);
								int empSex = tmpEmployee.getSex();
								String empSexName = "男";
								if(empSex == 1){
									empSexName = "女";
								}
								if(tmpEmployee.getBirthday()!=null && tmpEmployee.getQuarter()!=null 
										&& tmpEmployee.getQuarter().getQuarterName()!=null){
									String empbirth = DateUtils.convertDateToStr(tmpEmployee.getBirthday(), "yyyyMM");
									if(empbirth.equals(employeeBir)
											&& tmpEmployee.getQuarter().getQuarterName().equals(quarterName)){
										employee = tmpEmployee;
										break;
									}else if(employeeSex!=null && employeeSex.equals(empSexName)
											&& tmpEmployee.getQuarter().getQuarterName().equals(quarterName)){
										employee = tmpEmployee;
										break;
									}
								}
							}
						}
						if(employee==null && employeeCode!=null){
							employeelist =  employeeMap.get(employeeCode);
							if(employeelist!=null && employeelist.size()==1){
								employee = employeelist.get(0);
							}
						}
						
						if(employee!=null){
							TalentRegistration talentRegistration = talentRegistrationMap.get(employee.getEmployeeId());
							if(talentRegistration == null){
								talentRegistration = new TalentRegistration();
								talentRegistration.setCreatedBy(usersess.getEmployeeCode());
								talentRegistration.setCreationDate(nowTime);
							}else{
								talentRegistration.setLastUpdateDate(nowTime);
								talentRegistration.setLastUpdatedBy(usersess.getEmployeeCode());
								talentRegistration.getTalentRegistrationBanks().clear();
								talentRegistration.getSpecialitys().clear();
							}
							talentRegistration.setEmployee(employee);
							talentRegistration.setName(employee.getEmployeeName());
							talentRegistration.setSex(employee.getSex()+1);
							talentRegistration.setQuarter(employee.getQuarter());
							talentRegistration.setIsDel(0);
							talentRegistration.setSyncStatus(1);
							talentRegistration.setOrgan(employee.getDept().getOrgan());
							talentRegistration.setBirthday(DateUtils.convertDateToStr(employee.getBirthday(), "yyyy-MM-dd"));
							
							
							for(int i = 0 ;i<xls_talentRegistration_exptemplate.length;i++){
								String expCode = (String)xls_talentRegistration_exptemplate[i][1];
								if("employeefield".equals(xls_talentRegistration_exptemplate[i][3])
										|| xls_talentRegistration_exptemplate[i][3].indexOf("parent")!=-1){
									continue;
								}
								int columns_index = Integer.parseInt(xls_talentRegistration_exptemplate[i][2]);
								if(columns_index>=0){
									Cell tmpColumns = (Cell)sheet.getCell(columns_index, j);
									if(tmpColumns == null){
										continue;
									}
									if("quarterName".equals(xls_talentRegistration_exptemplate[i][3])){
										talentRegistration.setQuarterDuty(tmpColumns.getContents());
									}else  if("speciality".equals(xls_talentRegistration_exptemplate[i][3])){
										Speciality speciality = specialityMap.get(tmpColumns.getContents());
										if(speciality!=null){
											TalentRegistrationSpeciality trspeciality= new TalentRegistrationSpeciality();
											trspeciality.setSpeciality(speciality);
											talentRegistration.getSpecialitys().add(trspeciality);
											trspeciality.setTalentRegistration(talentRegistration);
										}
									}else if("specialityzz".equals(xls_talentRegistration_exptemplate[i][3])){
										for(int t=0;t<talentRegistration.getSpecialitys().size();t++){
											TalentRegistrationSpeciality trspeciality= talentRegistration.getSpecialitys().get(t);
											if(trspeciality!=null){
												Speciality speciality = trspeciality.getSpeciality();
												if(speciality!=null && speciality.getSpecialitycode()!=null && 
														speciality.getSpecialitycode().equals(tmpColumns.getContents())){
													trspeciality.setToZz("true");
												}
											}
										}
									}else if("specialityfzz".equals(xls_talentRegistration_exptemplate[i][3])){
										for(int t=0;t<talentRegistration.getSpecialitys().size();t++){
											TalentRegistrationSpeciality trspeciality= talentRegistration.getSpecialitys().get(t);
											if(trspeciality!=null){
												Speciality speciality = trspeciality.getSpeciality();
												if(speciality!=null && speciality.getSpecialitycode()!=null && 
														speciality.getSpecialitycode().equals(tmpColumns.getContents())){
													trspeciality.setToFzz("true");
												}
											}
										}
									}else if("zzjgqk".equals(xls_talentRegistration_exptemplate[i][3])
											|| "jndjqk".equals(xls_talentRegistration_exptemplate[i][3])
											|| "field".equals(xls_talentRegistration_exptemplate[i][3])){
										Class fieldClass = PropertyUtils.getPropertyType(talentRegistration, expCode);
										CellType cellType = tmpColumns.getType();
										if(fieldClass.isAssignableFrom(String.class))
						        			 PropertyUtils.setProperty(talentRegistration, expCode, tmpColumns.getContents());
						        		 else if(fieldClass.isAssignableFrom(Date.class)){
						        			 if(cellType.equals(CellType.DATE)){//日期类型
						            			 DateCell dateCell = (DateCell)tmpColumns;
						            			 PropertyUtils.setProperty(talentRegistration, expCode, dateCell.getDate());
						            		 }else if(cellType.equals(CellType.DATE_FORMULA)){
						            			 DateFormulaCell dateFormulaCell = (DateFormulaCell)tmpColumns;
						            			 PropertyUtils.setProperty(talentRegistration, expCode, dateFormulaCell.getDate());
						            		 }
						        		 }else if(fieldClass.getName().equals("java.lang.Boolean")||fieldClass.getName().equals("boolean")){//布尔类型
						        			 if(cellType.equals(CellType.BOOLEAN)){
						            			 BooleanCell booleanCell = (BooleanCell)tmpColumns;
						            			 PropertyUtils.setProperty(talentRegistration, expCode, booleanCell.getValue());
						            		 }else if(cellType.equals(CellType.BOOLEAN_FORMULA)){
						            			 BooleanFormulaCell booleanFormulaCell = (BooleanFormulaCell)tmpColumns;
						            			 PropertyUtils.setProperty(talentRegistration, expCode, booleanFormulaCell.getValue());
						            		 }
						        		 }else{
						        			double d = 0;
						        			if(cellType.equals(CellType.NUMBER)){
						            			 NumberCell numberCell = (NumberCell)tmpColumns;
						            			 d = numberCell.getValue();
						            		}else if(cellType.equals(CellType.NUMBER_FORMULA)){
						            			 NumberFormulaCell numberFormulaCell = (NumberFormulaCell)tmpColumns;
						            			 d = numberFormulaCell.getValue();            			 
						            		}
											if(fieldClass.getName().equals("java.lang.Integer")||
								        		 fieldClass.getName().equals("int")){
								        		 PropertyUtils.setProperty(talentRegistration, expCode,new Double(d).intValue());
								        	}else if(fieldClass.getName().equals("java.lang.Long")||
								        		 fieldClass.getName().equals("long")){
								        		 PropertyUtils.setProperty(talentRegistration, expCode, new Double(d).longValue());
								        	}else if(fieldClass.getName().equals("java.lang.Float")||
								        		fieldClass.getName().equals("float")){
								        		PropertyUtils.setProperty(talentRegistration, expCode, new Double(d).floatValue());
								        	}else{
								        		 PropertyUtils.setProperty(talentRegistration, expCode, d);
								        	}
						        		 }		
									}
								}
							}
							
							List<TalentRegistrationSpeciality> specialitys = talentRegistration.getSpecialitys();
							String specialityNames = "";
							if(!specialitys.isEmpty()){
								for(int i=0;i<specialitys.size();i++){
									TalentRegistrationSpeciality talentRegistrationSpeciality = (TalentRegistrationSpeciality)specialitys.get(i);
									if(talentRegistrationSpeciality.getSpeciality()!=null &&
											talentRegistrationSpeciality.getSpeciality().getSpecialityname()!=null){
										String specialityname = talentRegistrationSpeciality.getSpeciality().getSpecialityname();
										talentRegistrationSpeciality.setSpecialityname(specialityname);
										specialityNames+=specialityname+",";
										talentRegistrationSpeciality.setOrderno(i);
										//talentRegistrationSpeciality.setTalentRegistration(talentRegistration);
									}
								}
								if(specialityNames.length()>0)specialityNames=specialityNames.substring(0,specialityNames.length()-1);
							}
							talentRegistration.setSpecialityNames(specialityNames);
							
							saveList.add(talentRegistration);
						}
						System.out.println("============================================================");
					}

			     	if(saveList.size()>0){
						this.saves(saveList);
					}
			     	msg = "成功保存"+saveList.size()+"个专家人员信息！";
			     	
			 }catch(Exception e){
				e.printStackTrace();
				msg = "导入错误，请联系管理员！";
				throw new Exception(msg);
			 }
		return msg;
	}
	
	
	/**
	 * 查询数据并返回树形结构
	 * 
	 * @param organId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> findTreeList(String organId) throws Exception {
		List<TreeNode> list = new ArrayList<TreeNode>();
		Map<String, TreeNode> ms = new LinkedHashMap<String, TreeNode>();
		Map<String, String> param = new HashMap<String, String>();

		// 查询数据
		param.put("organTerm", organId);
		List<TalentRegistration> qlist = new ArrayList<TalentRegistration>();
		try {
			qlist = getDao().queryConfigQl("queryTreeSql", param, null,
					TalentRegistration.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 构造树
		for (TalentRegistration talent : qlist) {
			// 专家节点
			TreeNode talentNode = new TreeNode();
			talentNode.setId(talent.getId());
			talentNode.setTitle(talent.getName());
			talentNode.setType("talent");
			talentNode.setParentId(talent.getQuarter().getDept().getDeptId());
			ms.put(talent.getId(), talentNode);

			// 部门
			TreeNode deptNode = new TreeNode();
			Dept dept = talent.getQuarter().getDept();
			deptNode.setId(dept.getDeptId());
			deptNode.setTitle(dept.getDeptName());
			deptNode.setType("dept");
			Dept parent = dept.getDept();
			if (parent == null) {
				Organ organ = dept.getOrgan();
				deptNode.setParentId(organ.getOrganId());
			}
			if (!ms.containsKey(deptNode.getId()))
				ms.put(deptNode.getId(), deptNode);
			boolean loop = false;
			// 加上级部门数据
			while (parent != null) {
				if (!loop)
					deptNode.setParentId(parent.getDeptId());
				TreeNode newNode = TreeNode.objectToTree(parent, "deptId",
						"dept.deptId", "deptName");
				newNode.setType("dept");
				if (!ms.containsKey(newNode.getId())) {
					if (StringUtils.isEmpty(newNode.getParentId())) {
						Dept d = parent.getDept();
						newNode.setParentId(d.getDeptId());
					}
					ms.put(newNode.getId(), newNode);
				}
				loop = true;
				parent = parent.getDept();
			}

			// 机构
			TreeNode organNode = new TreeNode();
			Organ organ = dept.getOrgan();
			organNode.setId(organ.getOrganId());
			organNode.setTitle(organ.getOrganName());
			organNode.setType("organ");
			Organ o = organ.getOrgan();
			if (!ms.containsKey(organNode.getId()))
				ms.put(organNode.getId(), organNode);
			loop = false;

			// 加上级机构数据
			while (o != null) {
				if (!loop)
					organNode.setParentId(o.getOrganId());
				TreeNode newNode = TreeNode.objectToTree(o, "organId",
						"organ.organId", "organName");
				newNode.setType("organ");
				if (!ms.containsKey(newNode.getId())) {
					if (StringUtils.isEmpty(newNode.getParentId())) {
						if (o.getOrgan() != null)
							newNode.setParentId(o.getOrgan().getOrganId());
					}
					ms.put(newNode.getId(), newNode);
				}
				loop = true;
				o = o.getOrgan();
			}
		}

		list.addAll(ms.values());
		TreeNode.putTypeIncon("organ", "resources/icons/fam/organ.gif", "");
		TreeNode.putTypeIncon("dept", "resources/icons/fam/dept.gif", "");
		TreeNode.putTypeIncon("talent", "resources/icons/fam/user.png", "");
		List<String> leafTypes = new ArrayList<String>();
		leafTypes.add("organ");
		leafTypes.add("dept");
		leafTypes.add("talent");
		return TreeNode.toTree(list, true, leafTypes);
	}
}
