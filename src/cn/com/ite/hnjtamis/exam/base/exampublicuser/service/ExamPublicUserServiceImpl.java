package cn.com.ite.hnjtamis.exam.base.exampublicuser.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtilsBean;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.common.utils.XlsUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.ExamPublicUserListAction;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.dao.ExamPublicUserDao;
import cn.com.ite.hnjtamis.exam.base.exampublicuser.form.ExamPublicUserForm;
import cn.com.ite.hnjtamis.exam.base.examroot.util.CheckIdCard;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;

public class ExamPublicUserServiceImpl extends DefaultServiceImpl implements ExamPublicUserService{
	//public static Map<ExamPublic,String> userExamMap = new HashMap<ExamPublic,String>();
	public static Map<String,String> userExamMap = new HashMap<String,String>();
	public static Map<String,ExamPublic> updateExamInfo = new HashMap<String,ExamPublic>();
	
	private String[] examPublicUserCols = new String[]{
			"examPublicId",
			"userOrganName",
			"userDeptName",
			"postName",
			"userName",
			"userSex",
			"userBirthday",
			"userNation",
			"userAddr",
			"userPhone",
			"userInfo",
			"idNumber",
			"remark"
	};
	private String[] examPublicUserTitles = new String[]{
			"考试名称",
			"机构名称",
			"部门名称",
			"岗位名称",
			"姓名",
			"性别",
			"出生年月",
			"名族",
			"地址",
			"联系电话",
			"考生信息",
			"身份证",
			"备注"
	};
	
	public File exportDate(String examPublicId) throws Exception{
		File xls = File.createTempFile("参考考生信息", "xls");
		XlsUtils utils = XlsUtils.createWrite(xls);
		utils.write(0, "信息", new ArrayList(), examPublicUserCols, examPublicUserTitles);
		utils.closeWrite();
		return xls;
	}
	
	public List<String> importDate(File xls) throws Exception{
		List<String> resultInfo = new ArrayList<String>();
		
		try {
			UserSession usersess = LoginAction.getUserSessionInfo();
			PropertyUtilsBean pU = new PropertyUtilsBean();
			List<ExamPublicUserForm> examPublicUserList = XlsUtils.read(xls,"信息",ExamPublicUserForm.class,examPublicUserCols);
			List<ExamPublicUser> savesPojo = new ArrayList<ExamPublicUser>();
			int successCount = 0; //成功导入条数
			int failureCount = 0; //导入失败条数
			for (ExamPublicUserForm current : examPublicUserList) {
				String isValidPoStr = isValidPo(current);
				if(StringUtils.isEmpty(isValidPoStr)){
					continue;
				}else if(!isValidPoStr.equals("YES")){
					failureCount++;
					resultInfo.add(isValidPoStr);
					continue;
				}
				String names = current.getUserOrganName().trim()+"@"+current.getUserDeptName().trim()+"@"+current.getPostName().trim();
				String empFlag = current.getUserDeptName().trim()+"@"+current.getPostName().trim()+"@"+current.getUserName().trim();
				 
				String id = ExamPublicUserListAction.organDeptQuarterMap.get(names);
				if(!StringUtils.isEmpty(id)){//查询到有 机构 部门 岗位id 组合的话
					String[] ids = id.split("@");
					String organId = ids[0];
					String deptId = ids[1];
					String quarterId = ids[2];
					
					ExamPublicUser savePo = new ExamPublicUser();
					pU.copyProperties(savePo, current);//拷贝属性
					savePo.setUserOrganId(organId);
					savePo.setUserDeptId(deptId);
					savePo.setPostId(quarterId);
					ExamPublic examPublic = ExamPublicUserListAction.examPublicMap.get(current.getExamPublicId());
					
					if(examPublic == null){//未查询到 此 考试信息
						failureCount++;
						resultInfo.add("未查询到 "+savePo.getUserName()+"("+savePo.getIdNumber()+") 的考试信息");
						continue;
					}
					
					if(!(!StringUtils.isEmpty(savePo.getIdNumber()) && new CheckIdCard(savePo.getIdNumber()).validate())){//验证身份证 格式有效性
						failureCount++;
						resultInfo.add("考生 "+savePo.getUserName()+"("+savePo.getIdNumber()+") 身份证 格式错误");
						continue;
					}
					
					String examPublicId = examPublic.getPublicId();
					if(userExamMap.containsKey(examPublicId)){//如果有此考试信息  验证此身份证  此场考试 是否已参加报名
						updateExamInfo.put(examPublic.getPublicId(), examPublic);//将保存后需要更新考试信息  临时保存
						if(userExamMap.get(examPublicId).indexOf(savePo.getIdNumber())!=-1){
							failureCount++;
							resultInfo.add("考生 "+savePo.getUserName()+"("+savePo.getIdNumber()+") 已报名此次考试");
							continue;
						}
					}else{//map 中 未查到此考试信息 则 数据库中进行查询
						List<ExamPublicUser> tmpUser = examPublic.getExamPublicUsers();
						updateExamInfo.put(examPublic.getPublicId(), examPublic);//将保存后需要更新考试信息  临时保存
						if(tmpUser!=null && tmpUser.size()>0){
							String tmpIdNum = "";
							for (ExamPublicUser examPublicUser : tmpUser) {
								tmpIdNum += examPublicUser.getIdNumber()+",";
							}
							userExamMap.put(examPublicId, tmpIdNum);
						}
						if(userExamMap.get(examPublicId)!=null && userExamMap.get(examPublicId).indexOf(savePo.getIdNumber())!=-1){
							failureCount++;
							resultInfo.add("考生 "+savePo.getUserName()+"("+savePo.getIdNumber()+") 已报名此次考试");
							continue;
						}
					}
					String empId = ExamPublicUserListAction.employeeMap.get(empFlag);
					
					if(!StringUtils.isEmpty(empId)){
						savePo.setEmployeeId(empId);
						savePo.setEmployeeName(current.getUserName());
					}
					
					savePo.setExamPublic(examPublic);
					if(!StringUtils.isEmpty(savePo.getUserSex()) && savePo.getUserSex().trim().equals("男")){
						savePo.setUserSex("1");
					}else if(!StringUtils.isEmpty(savePo.getUserSex()) && savePo.getUserSex().trim().equals("女")){
						savePo.setUserSex("2");
					}
					
					savePo.setCreatedBy(usersess.getEmployeeName());
					savePo.setCreatedIdBy(usersess.getEmployeeId());
					savePo.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					savePo.setIsExp("1");
					savePo.setIsDel("0");
					savePo.setState("20");
					savePo.setBmType("10");
					savePo.setSyncFlag("1");
					savesPojo.add(savePo);
					successCount++;
				}else{
					resultInfo.add("未查询到 "+current.getUserName()+"("+current.getIdNumber()+") 的机构部门岗位信息");
					failureCount++;
				}
			}
			if(savesPojo.size()>0){
				this.saves(savesPojo);
			}
			resultInfo.add(0,"成功导入考生信息 "+successCount+" 条;导入失败 "+failureCount+" 条");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultInfo;
	}
	
	public String isValidPo(ExamPublicUserForm po){
		if((po.getUserOrganName()==null || StringUtils.isEmpty(po.getUserOrganName()))
				&& (po.getUserDeptName()==null || StringUtils.isEmpty(po.getUserDeptName()))
				&& (po.getPostName()==null || StringUtils.isEmpty(po.getPostName()))
				&& (po.getUserName()==null || StringUtils.isEmpty(po.getUserName()))
				&& (po.getIdNumber()==null || StringUtils.isEmpty(po.getIdNumber()))){
			return null;
		}
		if(po.getUserOrganName()==null || StringUtils.isEmpty(po.getUserOrganName())){
			return "无机构信息";
		}
		if(po.getUserDeptName()==null || StringUtils.isEmpty(po.getUserDeptName())){
			return "无部门信息";
		}
		if(po.getPostName()==null || StringUtils.isEmpty(po.getPostName())){
			return "无岗位信息";
		}
		if(po.getUserName()==null || StringUtils.isEmpty(po.getUserName())){
			return "无姓名信息";
		}
		if(po.getIdNumber()==null || StringUtils.isEmpty(po.getIdNumber())){
			return "无身份证信息";
		}
		return "YES";
	}
	
	//更新此场考试的人员进入map
	public void updateExamUser(){
		Iterator<String> updateIt = updateExamInfo.keySet().iterator();
		while(updateIt.hasNext()){
			String key = updateIt.next();
			Map term = new HashMap();
			term.put("publicId", key);
			ExamPublic t = updateExamInfo.get(key);
			this.getDao().refreshObject(t);
			List<ExamPublicUser> tmpUser = t.getExamPublicUsers();
			if(tmpUser!=null && tmpUser.size()>0){
				String tmpIdNum = "";
				for (ExamPublicUser examPublicUser : tmpUser) {
					tmpIdNum += examPublicUser.getIdNumber()+",";
				}
				System.out.println(key+"   "+tmpIdNum);
				userExamMap.put(t.getPublicId(), tmpIdNum);
			}
		}
		updateExamInfo.clear();
	}

	/*public static Map<ExamPublic, String> getUserExamMap() {
		return userExamMap;
	}

	public static void setUserExamMap(Map<ExamPublic, String> userExamMap) {
		ExamPublicUserServiceImpl.userExamMap = userExamMap;
	}*/
	
	/**
	 * 保存状态
	 * @author 朱健
	 * @param updateIds
	 * @return
	 * @modified
	 */
	public int savePublicUserState(String updateIds){
		ExamPublicUserDao examPublicUserDao = (ExamPublicUserDao)this.getDao();
		return examPublicUserDao.savePublicUserState(updateIds);
	}
	
}
