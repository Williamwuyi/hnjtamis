package cn.com.ite.hnjtamis.exam.base.theme;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheIncepter;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.common.RandomGUID;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.common.ThemeMakeCode;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeAnswerkeyForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeFkauditForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeInThemeBankForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkeyHis;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeFkaudit;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeHis;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeInBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeSearchKey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;



/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.ThemeFormAction</p>
 * <p>Description 试题管理 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午9:49:08
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = -1211201056635128278L;
	
	private HttpServletRequest request;
	
	private ThemeForm form;
	
	private File uploadImg;

	private String uploadImgFileName;

	private String content;
	
	private double width;
	
	private double height;
	
	private String imageFileName;
	
	private String imageFilePath;
	
	private String imageContent;
	
	private String isFkAuditXd;
	
	//private ThemeImageForm themeImageForm;
	
	private String op;
	
	private List<ThemeFkauditForm> themeFkauditFormList;
	
	private int themeFkauditFormListTotal;
	
	private final static double img_width_Max = 400.0;
	private final static double img_height_Max = 200.0;
	
	public String saveImgage(){
		//themeImageForm = new ThemeImageForm();
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "saveThemeImgFlag";
	 	}
		try {
			if(uploadImgFileName!=null && !"".equals(uploadImgFileName) && !"null".equals(uploadImgFileName)){
				String[] wjm = uploadImgFileName.split("\\.");
				String newFileName = DateUtils.convertDateToStr(new Date(), "yyyyMMddHHmmssSS")+"_"+RandomGUID.getGUID()+"."+wjm[wjm.length-1];//+"_"+uploadImgFileName;
				File newFile = FileOption.getFile(request,"upload"+System.getProperty("file.separator")+"themeImage", newFileName);	
				FileOption.copyFile(uploadImg, newFile);
				
				BufferedImage sourceImg =ImageIO.read(new FileInputStream(newFile));
    			width = sourceImg.getWidth();
    			height = sourceImg.getHeight();
    			boolean isCreatNewFile = true;
    			if(width>img_width_Max && height>img_height_Max){	
    				height = Math.floor(height*img_width_Max/width);
    				width = img_width_Max;
    			}else if(width>img_width_Max){
    				height = Math.floor(height*img_width_Max/width);
    				width = img_width_Max;
    			}else if(height>img_height_Max){
    				width = Math.floor(width*img_height_Max/height);
    				height = img_height_Max;
    			}else{
    				isCreatNewFile = false;
    			}
    			if(isCreatNewFile){
    				String filePath = newFile.getPath();
    				String fileName = newFile.getName();
    				
    				String fileNewPath = filePath.substring(0,filePath.length() - fileName.length());
    				String fileNewName = "s"+fileName;
    				String fileType = "";
    				StringTokenizer st = new StringTokenizer(fileName,".");
    				while(st.hasMoreElements()){
    					fileType = (String)st.nextElement();
    				}
    				String outputPicName = fileNewPath + fileNewName;
    				try{
    					BufferedImage Bi = ImageIO.read(newFile);
    				    BufferedImage tag = new BufferedImage((int)width,(int)height,BufferedImage.TYPE_INT_RGB);
    				    tag.getGraphics().drawImage(Bi.getScaledInstance((int)width,(int)height,BufferedImage.SCALE_SMOOTH),0,0,null);
    				    File littleFile = new File(outputPicName);
    				    ImageIO.write(tag, fileType, littleFile);	
    				    
    				    newFile = littleFile;
    				}catch(Exception ex){
    					ex.printStackTrace();
    				}
    				
    			}
    			
    			
    			imageFileName = newFile.getName();
    			imageFilePath = "upload/themeImage/"+newFile.getName();
    			if(content==null || "".equals(content) || "简短的图片说明".equals(content)){
					
				}else{
					imageContent = content;
				}
				/*themeImageForm.setImageName(newFile.getName());
				themeImageForm.setImageUrl("upload/themeImage/"+newFile.getName());
				if(content==null || "".equals(content) || "简短的图片说明".equals(content)){
					
				}else{
					themeImageForm.setContent(content);
				}
				themeImageForm.setWidth(width+"");
				themeImageForm.setHeight(height+"");*/
    			
				this.setMsg("上传成功！");
			}else{
				this.setMsg("没有选择图片，上传失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("上传失败！");
		}
		return "saveThemeImgFlag";
	}

	/**
	 * 查询
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String find(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		form = new ThemeForm();
		Theme theme = this.getId()!=null && !"".equals(this.getId()) && !"null".equals(this.getId()) 
				? (Theme) service.findDataByKey(this.getId(), Theme.class) : null;
		if(theme!=null){
			form.setThemeId(theme.getThemeId());
			form.setThemeTypeId(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeId() : null);
			form.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);//题型
			form.setThemeInType(theme.getThemeType()!=null ? theme.getThemeType().getThemeType() : null);
			form.setKnowledgePoint(theme.getKnowledgePoint());//所属知识点
			form.setThemeKeyword(theme.getThemeKeyword());//关键字
			form.setType(theme.getType());//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
			form.setThemeName(theme.getThemeName());//试题
			form.setScoreType(theme.getScoreType());//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
			form.setDegree(theme.getDegree());//难度 5：容易,10：一般15：难,20：很难
			form.setEachline(theme.getEachline());//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
			form.setWriteUser(theme.getWriteUser());//出题人
			form.setExplain(theme.getExplain());//注解
			form.setDefaultScore(theme.getDefaultScore());//默认分数
			form.setThemeSetNum(theme.getThemeSetNum());//出题次数
			form.setThemePeopleNum(theme.getThemePeopleNum());//考试人次
			form.setThemeRightNum(theme.getThemeRightNum());//答题正确数
			form.setRemark(theme.getRemark());//备注
			form.setState(theme.getState());//状态 5:保存10:上报15:发布20:打回
			form.setSortNum(theme.getSortNum());//排序
			form.setIsUse(theme.getIsUse());//有否有效 5:有效 10：无效
			//form.setOrganId(theme.getOrganId());//机构ID
			//form.setOrganName(theme.getOrganName());//机构名称
			//form.setSyncFlag(theme);//同步标志
			//form.setLastUpdateDate(theme);///最后修改时间
			//form.setLastUpdatedBy(theme);//最后修改人
			//form.setLastUpdatedIdBy(theme);//最后修改人ID
			//form.setCreationDate(theme);//创建时间
			//form.setCreatedBy(theme);//创建人
			//form.setCreatedIdBy(theme);//创建人ID
			form.setHaveImages(theme.getHaveImages());//是否存在图片文件
			form.setImagesNames(theme.getImagesNames());//图片文件名字
			form.setThemeVersion(theme.getThemeVersion());//版本号
			form.setThemeCode(theme.getThemeCode());//试题编码
			form.setThemeCrc(theme.getThemeCrc());//试题校验码
			form.setThemeAns(theme.getThemeAns());//显示答案
			form.setThemeHisId(theme.getThemeHisId());//最后一个试题版本ID
			form.setImagesPath(theme.getImagesPath());//图片路径
			form.setImagesPackageName(theme.getImagesPackageName());//图片包名
			form.setImagesSucc(theme.getImagesSucc());//图片是否导入成功说明标识
			form.setAllowFk(theme.getAllowFk());//该试题允许反馈 10-允许 20-不允许
			if("qr".equals(this.getOp())){
				
			}else{
				Employee lastFkAuditEmp = new Employee();
				lastFkAuditEmp.setEmployeeId(theme.getLastFkAuditId());
				lastFkAuditEmp.setEmployeeName(theme.getLastFkAuditName());
				form.setLastFkAuditEmp(lastFkAuditEmp);
			}
			form.setLastFkState(theme.getLastFkState());
			form.setLastFkAuditTime(theme.getLastFkAuditTime());
			
			List<ThemeAnswerkey> ansList = theme.getThemeAnswerkeies();
			if(ansList!=null && ansList.size()>0){
				Iterator<ThemeAnswerkey> its = ansList.iterator();
				while(its.hasNext()){
					ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)its.next();
					ThemeAnswerkeyForm themeAnswerkeyForm = new ThemeAnswerkeyForm();
					BeanUtils.copyProperties(themeAnswerkey,themeAnswerkeyForm);
					themeAnswerkeyForm.setAnswerRemark(themeAnswerkey.getRemark());
					if(themeAnswerkey.getIsRight()!=null && themeAnswerkey.getIsRight().intValue() == 10){//是否正确 5：否,10：是
						themeAnswerkeyForm.setIsRight(true);
					}else{
						themeAnswerkeyForm.setIsRight(false);
					}
					form.getThemeAnswerkeyFormList().add(themeAnswerkeyForm);
				}
			}
			
			/*List<ThemeSearchKey> searchkeylist = theme.getThemeSearchKeies();
			if(searchkeylist!=null && searchkeylist.size()>0){
				Iterator<ThemeSearchKey> its = searchkeylist.iterator();
				while(its.hasNext()){
					ThemeSearchKey themeSearchKey = (ThemeSearchKey)its.next();
					if(themeSearchKey.getPostId()!=null){
						ThemePostForm themePostForm = new ThemePostForm();
						themePostForm.setPostId(themeSearchKey.getPostId());
						themePostForm.setPostName(themeSearchKey.getPostName());
						form.getThemePostFormList().add(themePostForm);
					}else if(themeSearchKey.getProfessionId()!=null){
						ThemeSpecialityForm themeSpecialityForm = new ThemeSpecialityForm();
						themeSpecialityForm.setSpecialityId(themeSearchKey.getProfessionId());
						themeSpecialityForm.setSpecialityName(themeSearchKey.getProfessionName());
						form.getThemeSpecialityFormList().add(themeSpecialityForm);
					}
				}
			}*/
			
			
			List<ThemeInBank> themeInBankList = theme.getThemeInBanks();
			if(themeInBankList!=null && themeInBankList.size()>0){
				Iterator<ThemeInBank> its = themeInBankList.iterator();
				while(its.hasNext()){
					ThemeInBank themeInBank = (ThemeInBank)its.next();
					if(themeInBank.getThemeBank()!=null){
						ThemeInThemeBankForm themeInThemeBankForm = new ThemeInThemeBankForm();
						themeInThemeBankForm.setThemeBankId(themeInBank.getThemeBank().getThemeBankId());
						themeInThemeBankForm.setThemeBankName(themeInBank.getThemeBank().getThemeBankName());
						form.getThemeBankFormList().add(themeInThemeBankForm);
					}
				}
			}
			
		
		}
		return "find";
	}
	
	public String getAuditRemarkList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "themeFkauditFormList";
	 	}
		themeFkauditFormList = new ArrayList<ThemeFkauditForm>();
		if("fk".equals(op)){
			Map term = new HashMap();
			term.put("themeId", this.getId());
			term.put("employeeId", usersess.getEmployeeId());
			//term.put("state", value);
			List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
			for(int i=0;i<fkAuditlist.size();i++){
				ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
				ThemeFkauditForm themeFkauditForm = new ThemeFkauditForm();
				BeanUtils.copyProperties(themeFkaudit,themeFkauditForm);
				themeFkauditFormList.add(themeFkauditForm);
			}
		}else if("audit".equals(op)){
			Map term = new HashMap();
			term.put("themeId", this.getId());
			//term.put("employeeId", usersess.getEmployeeId());
			//term.put("state", "10");
			List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
			for(int i=0;i<fkAuditlist.size();i++){
				ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
				ThemeFkauditForm themeFkauditForm = new ThemeFkauditForm();
				BeanUtils.copyProperties(themeFkaudit,themeFkauditForm);
				themeFkauditFormList.add(themeFkauditForm);
			}
		}else{
			Map term = new HashMap();
			term.put("themeId", this.getId());
			//term.put("employeeId", usersess.getEmployeeId());
			//term.put("state", "10");
			List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
			for(int i=0;i<fkAuditlist.size();i++){
				ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
				ThemeFkauditForm themeFkauditForm = new ThemeFkauditForm();
				BeanUtils.copyProperties(themeFkaudit,themeFkauditForm);
				themeFkauditFormList.add(themeFkauditForm);
			}
		}
		themeFkauditFormListTotal = themeFkauditFormList.size();
		return "themeFkauditFormList";
	}
	
	/**
	 * 保存
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String save() throws EapException{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			form = (ThemeForm) this.jsonToObject(ThemeForm.class);
			Theme theme = null;
			String themeHisId = null;
			String themeAllContent;
			if(form.getThemeId()!=null && !"".equals(form.getThemeId()) && !"null".equals(form.getThemeId())){
				theme = (Theme)this.getService().findDataByKey(form.getThemeId(), Theme.class);
				if("true".equals(isFkAuditXd) && "fpaudit".equals(this.getOp())){//反馈审核
					ThemeHis themeHis = new ThemeHis();
					BeanUtils.copyProperties(theme,themeHis);
					themeHis.setThemeHisId(null);
					themeHis.setThemeVersion(theme.getThemeVersion());
					themeHis.setThemeTypeId(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeId():null);
					themeHis.setThemeSourceId(theme.getThemeId());
					themeHis.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeHis.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
					themeHis.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeHis.setOrganId(usersess.getOrganId());//机构ID
					themeHis.setOrganName(usersess.getOrganAlias());//机构名称
					themeHis.setLockState("10");
					Iterator its = theme.getThemeAnswerkeies().iterator();
					while(its.hasNext()){
						ThemeAnswerkey themeAnswerkey= (ThemeAnswerkey)its.next();
						ThemeAnswerkeyHis themeAnswerkeyHis = new ThemeAnswerkeyHis();
						BeanUtils.copyProperties(themeAnswerkey,themeAnswerkeyHis);
						themeAnswerkeyHis.setThemeHis(themeHis);
						themeAnswerkeyHis.setAnswerkeyId(null);
						themeAnswerkeyHis.setSortNum(themeAnswerkey.getSortNum().shortValue());
						themeAnswerkeyHis.setRemark(themeAnswerkey.getRemark());
						themeAnswerkeyHis.setThemeTypeId(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeId() : null);
						themeAnswerkeyHis.setThemeTypeName(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeName() : null);
						themeHis.getThemeAnswerkeyHises().add(themeAnswerkeyHis);
					}
					service.saveOld(themeHis);
					themeHisId = themeHis.getThemeHisId();
					if(form.getThemeVersion()==null){
						form.setThemeVersion(1);
					}else{
						form.setThemeVersion(form.getThemeVersion()+1);
					}
				}else if("true".equals(isFkAuditXd)){//反馈审核
					ThemeHis themeHis = new ThemeHis();
					BeanUtils.copyProperties(theme,themeHis);
					themeHis.setThemeHisId(null);
					themeHis.setThemeVersion(theme.getThemeVersion());
					themeHis.setThemeTypeId(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeId():null);
					themeHis.setThemeSourceId(theme.getThemeId());
					themeHis.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeHis.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
					themeHis.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeHis.setOrganId(usersess.getOrganId());//机构ID
					themeHis.setOrganName(usersess.getOrganAlias());//机构名称
					themeHis.setLockState("10");
					Iterator its = theme.getThemeAnswerkeies().iterator();
					while(its.hasNext()){
						ThemeAnswerkey themeAnswerkey= (ThemeAnswerkey)its.next();
						ThemeAnswerkeyHis themeAnswerkeyHis = new ThemeAnswerkeyHis();
						BeanUtils.copyProperties(themeAnswerkey,themeAnswerkeyHis);
						themeAnswerkeyHis.setThemeHis(themeHis);
						themeAnswerkeyHis.setAnswerkeyId(null);
						themeAnswerkeyHis.setSortNum(themeAnswerkey.getSortNum().shortValue());
						themeAnswerkeyHis.setRemark(themeAnswerkey.getRemark());
						themeAnswerkeyHis.setThemeTypeId(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeId() : null);
						themeAnswerkeyHis.setThemeTypeName(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeName() : null);
						themeHis.getThemeAnswerkeyHises().add(themeAnswerkeyHis);
					}
					service.saveOld(themeHis);
					themeHisId = themeHis.getThemeHisId();
					if(form.getThemeVersion()==null){
						form.setThemeVersion(1);
					}else{
						form.setThemeVersion(form.getThemeVersion()+1);
					}
				}
			}
			if(theme==null){
				theme = new Theme();
				theme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				theme.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
				theme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				theme.setThemePeopleNum(new Integer(0));//考试人次
				theme.setThemeRightNum(new Integer(0));//答题正确数
				theme.setThemeSetNum(new Integer(0));//出题次数
				theme.setOrganId(usersess.getCurrentOrganId());//机构ID
				theme.setOrganName(usersess.getCurrentOrganName());//机构名称
			}else{
				//theme.setThemeId(form.getThemeId());
				theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
				theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
			}
			ThemeType themeType = (ThemeType)this.getService().findDataByKey(form.getThemeTypeId(), ThemeType.class);
			theme.setThemeType(themeType);
			theme.setThemeTypeName(themeType!=null ? themeType.getThemeTypeName() : null);//题型
			theme.setKnowledgePoint(form.getKnowledgePoint());//所属知识点
			theme.setThemeKeyword(form.getThemeKeyword());//关键字
			theme.setType(form.getType());//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
			theme.setThemeName(form.getThemeName());//试题
			theme.setScoreType(form.getScoreType());//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
			theme.setDegree(form.getDegree());//难度 5：容易,10：一般15：难,20：很难
			theme.setEachline(form.getEachline());//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
			theme.setWriteUser(form.getWriteUser());//出题人
			theme.setExplain(form.getExplain());//注解
			theme.setDefaultScore(form.getDefaultScore());//默认分数		
			theme.setRemark(form.getRemark());//备注
			theme.setState(form.getState());//状态 5:保存10:上报15:发布20:打回
			theme.setIsUse(form.getIsUse());//有否有效 5:有效 10：无效
			theme.setSortNum(new Long(1));//排序
			theme.setHaveImages(form.getHaveImages());//是否存在图片文件
			theme.setImagesNames(form.getImagesNames());//图片文件名字
			if(form.getThemeVersion() == null){
				theme.setThemeVersion(1);//版本号
			}else{
				theme.setThemeVersion(form.getThemeVersion());
			}
			if(form.getThemeCode() == null || "".equals(form.getThemeCode())){
				theme.setThemeCode(ThemeMakeCode.getCode(0));//试题编码
			}else{
				theme.setThemeCode(form.getThemeCode());
			}
			//theme.setThemeCrc(form.getThemeCrc());//试题校验码
			theme.setThemeAns(form.getThemeAns());//显示答案
			theme.setThemeHisId(form.getThemeHisId());//最后一个试题版本ID
			theme.setImagesPath(form.getImagesPath());//图片路径
			theme.setImagesPackageName(form.getImagesPackageName());//图片包名
			theme.setImagesSucc(form.getImagesSucc());//图片是否导入成功说明标识
			if(form.getAllowFk() == null){
				theme.setAllowFk("10");//该试题允许反馈 10-允许 20-不允许
			}else{
				theme.setAllowFk(form.getAllowFk());
			}
			themeAllContent = theme.getThemeName();
			//theme.setSyncFlag();//同步标志
			//service.save(theme);
			//theme.getThemeInBanks().clear();
			
			//theme.getThemeAnswerkeies().clear();
			List<ThemeAnswerkeyForm> ansList = form.getThemeAnswerkeyFormList();
			
			Map<String,ThemeAnswerkey> ansMap = new HashMap<String,ThemeAnswerkey>();
			Iterator<ThemeAnswerkey> itss = theme.getThemeAnswerkeies().iterator();
			while(itss.hasNext()){
				ThemeAnswerkey themeAnswerkey= itss.next();
				boolean isDel = true;
				for(int i = 0;i<ansList.size();i++){
					if(themeAnswerkey.getAnswerkeyId().equals(ansList.get(i).getAnswerkeyId())){
						isDel = false;
						ansMap.put(themeAnswerkey.getAnswerkeyId(), themeAnswerkey);
					}
				}
				if(isDel){
					itss.remove();
				}
			}
			
			
			
			if(ansList!=null && ansList.size()>0){
				Iterator<ThemeAnswerkeyForm> its = ansList.iterator();
				int index = 1;
				while(its.hasNext()){
					ThemeAnswerkeyForm themeAnswerkeyForm = (ThemeAnswerkeyForm)its.next();
					ThemeAnswerkey themeAnswerkey = ansMap.get(themeAnswerkeyForm.getAnswerkeyId());
					boolean isAdd = false;
					if(themeAnswerkey==null){
						themeAnswerkey= new ThemeAnswerkey();
						isAdd =true;
						themeAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						themeAnswerkey.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
						themeAnswerkey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					}else{
						themeAnswerkey.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						themeAnswerkey.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
						themeAnswerkey.setLastUpdatedIdBy(usersess.getEmployeeId());//创建人ID
					}
					themeAnswerkey.setAnswerkeyValue(themeAnswerkeyForm.getAnswerkeyValue());
					themeAnswerkey.setSortNum(index);
					themeAnswerkey.setRemark(themeAnswerkeyForm.getAnswerRemark());
					themeAnswerkey.setTheme(theme);
					if(themeAnswerkeyForm.getIsRight()){//是否正确 5：否,10：是
						themeAnswerkey.setIsRight(10);
					}else{
						themeAnswerkey.setIsRight(5);
					}
					themeAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeAnswerkey.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
					themeAnswerkey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					//service.add(themeAnswerkey);
					if(isAdd){
						theme.getThemeAnswerkeies().add(themeAnswerkey);
					}
					themeAllContent+=","+themeAnswerkey.getAnswerkeyValue();
					index++;
				}
			}
			
			theme.getThemeInBanks().clear();
			theme.getThemeSearchKeies().clear();
			List<ThemeInThemeBankForm> themeBankFormList = form.getThemeBankFormList();
			if(themeBankFormList!=null && themeBankFormList.size()>0){
				for(int ii=0;ii<themeBankFormList.size();ii++){
					ThemeInThemeBankForm themeInThemeBankForm = (ThemeInThemeBankForm)themeBankFormList.get(ii);
					ThemeInBank themeInBank = new ThemeInBank();
					ThemeBank themeBank = (ThemeBank)this.getService().findDataByKey(themeInThemeBankForm.getThemeBankId(), ThemeBank.class);
					if(themeBank!=null){
						themeInBank.setThemeBank(themeBank);
						themeInBank.setTheme(theme);
						themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						themeInBank.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
						themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
						themeInBank.setOrganId(usersess.getCurrentOrganId());//机构ID
						themeInBank.setOrganName(usersess.getCurrentOrganName());//机构名称
						themeInBank.setBankOrganId(themeBank.getOrganId());//题库创建机构ID
						themeInBank.setBankOrganName(themeBank.getOrganName());//题库创建机构
						themeInBank.setBankPublic(themeBank.getBankPublic());//是否公有或私有 10-公有  20-电厂私有
						themeInBank.setBankType(themeBank.getBankType());//题库类型 10-岗位题库  20-专业题库
						
						theme.getThemeInBanks().add(themeInBank);
						if(ii==0){
							try{
								theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
							}catch(Exception e){
								theme.setSortNum(new Long(9999));
								e.printStackTrace();
							}
						}
						
						
						ThemeSearchKey themeSearchKey = new ThemeSearchKey();
						themeSearchKey.setTheme(theme);
						themeSearchKey.setThemeBankId(themeBank.getThemeBankId());
						themeSearchKey.setThemeBankName(themeBank.getThemeBankName());
						themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						themeSearchKey.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
						themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
						themeSearchKey.setOrganId(usersess.getCurrentOrganId());//机构ID
						themeSearchKey.setOrganName(usersess.getCurrentOrganName());//机构名称
						themeSearchKey.setSortNum(ii);
						theme.getThemeSearchKeies().add(themeSearchKey);
						
						
						/*List<ThemeBankProfession> themeSpecialityFormList = themeBank.getThemeBankProfessions();
						if(themeSpecialityFormList!=null && themeSpecialityFormList.size()>0){
							for(int k=0;k<themeSpecialityFormList.size();k++){
								ThemeBankProfession themeSpecialityForm = (ThemeBankProfession)themeSpecialityFormList.get(k);
								if(themeSpecialityForm.getSpeciality()!=null){
									themeSearchKey = new ThemeSearchKey();
									themeSearchKey.setTheme(theme);
									themeSearchKey.setProfessionName(themeSpecialityForm.getSpeciality().getSpecialityname());
									themeSearchKey.setProfessionId(themeSpecialityForm.getSpeciality().getSpecialityid());
									themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
									themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
									themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
									themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
									themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
									themeSearchKey.setSortNum(k);
									theme.getThemeSearchKeies().add(themeSearchKey);
								}
								//service.add(themeSearchKey);
							}
						}
						
						List<ThemeBankPost> themePostFormList = themeBank.getThemeBankPosts();
						if(themePostFormList!=null && themePostFormList.size()>0){
							for(int k=0;k<themePostFormList.size();k++){
								ThemeBankPost themePostForm = (ThemeBankPost)themePostFormList.get(k);
								themeSearchKey = new ThemeSearchKey();
								themeSearchKey.setTheme(theme);
								themeSearchKey.setPostName(themePostForm.getPostName());
								themeSearchKey.setPostId(themePostForm.getPostId());
								themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
								themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
								themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
								themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
								themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
								themeSearchKey.setSortNum(k);
								theme.getThemeSearchKeies().add(themeSearchKey);
								//service.add(themeSearchKey);
							}
						}*/
					
					}
				}
			}
			
			
			
			/*List<ThemeSpecialityForm> themeSpecialityFormList = form.getThemeSpecialityFormList();
			if(themeSpecialityFormList!=null && themeSpecialityFormList.size()>0){
				for(int i=0;i<themeSpecialityFormList.size();i++){
					ThemeSpecialityForm themeSpecialityForm = (ThemeSpecialityForm)themeSpecialityFormList.get(i);
					ThemeSearchKey themeSearchKey = new ThemeSearchKey();
					themeSearchKey.setTheme(theme);
					themeSearchKey.setProfessionName(themeSpecialityForm.getSpecialityName());
					themeSearchKey.setProfessionId(themeSpecialityForm.getSpecialityId());
					themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
					themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
					themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
					themeSearchKey.setSortNum(i);
					theme.getThemeSearchKeies().add(themeSearchKey);
					//service.add(themeSearchKey);
				}
			}
			
			List<ThemePostForm> themePostFormList = form.getThemePostFormList();
			if(themePostFormList!=null && themePostFormList.size()>0){
				for(int i=0;i<themePostFormList.size();i++){
					ThemePostForm themePostForm = (ThemePostForm)themePostFormList.get(i);
					ThemeSearchKey themeSearchKey = new ThemeSearchKey();
					themeSearchKey.setTheme(theme);
					themeSearchKey.setPostName(themePostForm.getPostName());
					themeSearchKey.setPostId(themePostForm.getPostId());
					themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
					themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
					themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
					themeSearchKey.setSortNum(i);
					theme.getThemeSearchKeies().add(themeSearchKey);
					//service.add(themeSearchKey);
				}
			}*/
			
			
			//service.save(theme);
			theme.setCheckRemark(ThemeCheckOption.checkTheme(theme));
			
			
			if(themeAllContent.indexOf("<img")!=-1){
				List imageList = this.getFileList(themeAllContent);
				if(imageList!=null && imageList.size()>0){
					String basepath = FileOption.getFileBasePath(request);
					String imagesSucc = "";
					String imagesPath="";
					String imagesPackageName = "themeImage";
					String imagesNames= "";
					for(int i=0;i<imageList.size();i++){
						String imagepp = (String)imageList.get(i);
						File file = new File(basepath+(String)imageList.get(i));
						if(file.exists()){
							imagesSucc+="T,";
							imagesPath+=file.getPath()+",";
							imagesNames+=file.getName()+",";
						}else{
							imagesSucc+="F,";
						}
						if(imagepp.indexOf("themeImage")==-1){
							imagesPackageName = imagepp.substring(imagepp.indexOf("imp/")+4,imagepp.length());
							imagesPackageName = imagesPackageName.substring(0,imagesPackageName.indexOf("/"));
						}
					}
					if(imagesSucc.length()>0)imagesSucc = imagesSucc.substring(0,imagesSucc.length()-1);
					if(imagesNames.length()>0)imagesNames = imagesNames.substring(0,imagesNames.length()-1);
					if(imagesPath.length()>0)imagesPath = imagesPath.substring(0,imagesPath.length()-1);
					theme.setImagesSucc(imagesSucc);
					theme.setImagesPath(imagesPath);
					theme.setImagesPackageName(imagesPackageName);
					theme.setImagesNames(imagesNames);
					theme.setHaveImages("是");
				}
			}
			theme.setLastFkAuditId(usersess.getEmployeeId()==null ? usersess.getAccount() : usersess.getEmployeeId());
			theme.setLastFkAuditName(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());
			theme.setLastFkAuditTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if("audit".equals(this.getOp())){
				theme.setLastFkState("40");
			}else if("fpaudit".equals(this.getOp())){
				theme.setLastFkState("50");
				theme.setState(15);
				theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
				theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
			}
			service.saveOld(theme);
			if("fpaudit".equals(this.getOp())){//直接修改
				ThemeFkaudit themeFkaudit = new ThemeFkaudit();
				themeFkaudit.setTheme(theme);
				themeFkaudit.setFkauditRemark(form.getFkRemark()!=null && !"".equals(form.getFkRemark())
						 && !"null".equals(form.getFkRemark()) ? form.getFkRemark() : theme.getLastFkAuditName()+"完成试题审核。");
				themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
				themeFkaudit.setFkType("40");//类型 10-提出反馈  20-专家指定  30专家审核 40-反馈确认
				themeFkaudit.setThemeAns(null);//当前答案
				themeFkaudit.setAwardCenter(0.0);//奖励分数
				themeFkaudit.setAwardTime(null);//奖励时间
				themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
				themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
				themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
				themeFkaudit.setFkFinState("10");
				themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
				themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
				themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				themeFkaudit.setOrganId(usersess.getCurrentOrganId());//机构ID
				themeFkaudit.setOrganName(usersess.getCurrentOrganName());//机构名称
				themeFkaudit.setThemeHisId(themeHisId);
				this.getService().save(themeFkaudit);
				
				Map term = new HashMap();
				term.put("themeId", theme.getThemeId());
				//term.put("state", "10");
				List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
				for(int i=0;i<fkAuditlist.size();i++){
					ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
					_themeFkaudit.setFkFinState("20");
					if("10".equals(_themeFkaudit.getFkType())){
						_themeFkaudit.setAwardCenter(new Double(1.0));
					}else{
						_themeFkaudit.setAwardCenter(new Double(0.0));	
					}
					_themeFkaudit.setAwardTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));	
					_themeFkaudit.setState("20");//最后一个肯定是反馈人
					if("10".equals(_themeFkaudit.getFkType())){
						break;
					}
				}
				this.getService().saves(fkAuditlist);
				
				//自动发出通知
				SysAffiche sysAffiche = new SysAffiche();
				sysAffiche.setSender(usersess.getEmployeeName());//发送者
				sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
				sysAffiche.setTitle("试题反馈完成通知");
				String content = "您好，您提交的试题反馈内容，已完成了审核。";
				sysAffiche.setContent(content);
				
				sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate((DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd")).substring(0,10), "yyyy-MM-dd"))+1);
				sysAffiche.setRelationId(theme.getThemeId());
				sysAffiche.setRelationType("themeAuditFin");
				this.getService().add(sysAffiche);
				
				//添加发送对象
				SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
				sysAfficheIncepter.setIncepterId(theme.getLastFkEmployeeId());
				sysAfficheIncepter.setIncepterName(theme.getLastFkEmployeeName());
				sysAfficheIncepter.setIncepterType(4);
				sysAfficheIncepter.setSortNum(new Integer(0));
				sysAfficheIncepter.setSysAffiche(sysAffiche);
				this.getService().add(sysAfficheIncepter);
			}else if("audit".equals(this.getOp())){
				ThemeFkaudit themeFkaudit = new ThemeFkaudit();
				themeFkaudit.setTheme(theme);
				themeFkaudit.setFkauditRemark(form.getFkRemark()!=null && !"".equals(form.getFkRemark())
						 && !"null".equals(form.getFkRemark()) ? form.getFkRemark() : theme.getLastFkAuditName()+"完成试题审核。");
				themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
				themeFkaudit.setFkType("30");//类型 10-提出反馈  20-专家指定  30专家审核
				themeFkaudit.setThemeAns(null);//当前答案
				themeFkaudit.setAwardCenter(0.0);//奖励分数
				themeFkaudit.setAwardTime(null);//奖励时间
				themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
				themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
				themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
				themeFkaudit.setFkFinState("10");
				themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
				themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
				themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				themeFkaudit.setOrganId(usersess.getCurrentOrganId());//机构ID
				themeFkaudit.setOrganName(usersess.getCurrentOrganName());//机构名称
				themeFkaudit.setThemeHisId(themeHisId);
				this.getService().save(themeFkaudit);
			}
			/*if("qr".equals(this.getOp()) ){
				Map term = new HashMap();
				term.put("themeId", theme.getThemeId());
				//term.put("employeeId", usersess.getEmployeeId());
				term.put("state", "10");
				List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
				for(int i=0;i<fkAuditlist.size();i++){
					ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
					themeFkaudit.setState("20");
					themeFkaudit.setThemeHisId(themeHisId);
				}
				this.getService().saves(fkAuditlist);
			}*/
			if("15".equals(theme.getState())){
				this.setMsg("发布成功！");
			}else if("10".equals(theme.getState())){
				this.setMsg("上报成功！");
			}else{
				this.setMsg("保存成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "save";
	}
	
	private List getFileList(String themeContext){
		  List strlist = new ArrayList();
		  String str = themeContext;
	      int index = str.indexOf("<img");
		  while(index!=-1 && str.length()>0){
				String tmp = str.substring(0,index);
				strlist.add(tmp);
				str = str.substring(index, str.length());
				index = str.indexOf(">");
				tmp = str.substring(0,index+1);
				strlist.add(tmp);
				str = str.substring(index+1, str.length());
				if(str.length()==0)break;
				index = str.indexOf("<img");
		  }
		  if(str.length()>0) strlist.add(str);
			
		  List imagelist = new ArrayList();
		  for(int tt=0;tt<strlist.size();tt++){
				String tmp = (String)strlist.get(tt);
				if(tmp.indexOf("<img")!=-1){
					index = tmp.indexOf("src=\"upload");
					tmp = tmp.substring(index+11,tmp.length());
					index = tmp.indexOf("\"");
					tmp = tmp.substring(0,index);
					imagelist.add(tmp);
					
				}
		  }
		  return imagelist;
	}

	/**
	 * 保存
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String saveFk() throws EapException{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try{
			form = (ThemeForm) this.jsonToObject(ThemeForm.class);
			Theme theme = (Theme)this.getService().findDataByKey(form.getThemeId(), Theme.class);
			if(theme!=null){
				if("fp".equals(this.getOp())){//直接否决
					if("-50".equals(form.getLastFkState())){
						theme.setLastFkAuditId(null);
						theme.setLastFkAuditName(null);
						theme.setLastFkState(form.getLastFkState());
						theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
						theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
						theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
						theme.setState(15);
						//this.getService().save(theme);
						
						ThemeFkaudit themeFkaudit = new ThemeFkaudit();
						themeFkaudit.setTheme(theme);
						themeFkaudit.setFkauditRemark(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName()+"否决试题反馈。");
						themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
						themeFkaudit.setThemeAns(null);//当前答案
						themeFkaudit.setAwardCenter(0.0);//奖励分数
						themeFkaudit.setAwardTime(null);//奖励时间
						themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
						themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
						themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
						themeFkaudit.setFkType("40");//类型 10-提出反馈  20-专家指定  30专家审核 40-反馈确认
						themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
						themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
						themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
						themeFkaudit.setOrganId(usersess.getOrganId());//机构ID
						themeFkaudit.setOrganName(usersess.getOrganAlias());//机构名称
						
						themeFkaudit.setFkAuditUserName(null);//指定反馈审核人
						themeFkaudit.setFkAuditUserId(null);//指定反馈审核人ID
						themeFkaudit.setFkFinState("10");
						this.getService().save(themeFkaudit);
						
						Map term = new HashMap();
						term.put("themeId", theme.getThemeId());
						term.put("state", "10");
						List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
						for(int i=0;i<fkAuditlist.size();i++){
							ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
							_themeFkaudit.setState("20");//最后一个肯定是反馈人
						}
						this.getService().saves(fkAuditlist);
					}else{
						Employee emp = form.getLastFkAuditEmp();
						if(emp!=null){
							theme.setLastFkAuditId(emp.getEmployeeId());
							theme.setLastFkAuditName(emp.getEmployeeName());
							theme.setLastFkState("30");
							theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
							theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
							theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
							//this.getService().save(theme);
							
							ThemeFkaudit themeFkaudit = new ThemeFkaudit();
							themeFkaudit.setTheme(theme);
							themeFkaudit.setFkauditRemark(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName()+"指定"+emp.getEmployeeName()+"为审核人。");
							themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
							themeFkaudit.setThemeAns(null);//当前答案
							themeFkaudit.setAwardCenter(0.0);//奖励分数
							themeFkaudit.setAwardTime(null);//奖励时间
							themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
							themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
							themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
							themeFkaudit.setFkType("20");//类型 10-提出反馈  20-专家指定  30专家审核
							themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
							themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
							themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
							themeFkaudit.setOrganId(usersess.getOrganId());//机构ID
							themeFkaudit.setOrganName(usersess.getOrganAlias());//机构名称
							
							themeFkaudit.setFkAuditUserName(emp.getEmployeeName());//指定反馈审核人
							themeFkaudit.setFkAuditUserId(emp.getEmployeeId());//指定反馈审核人ID
							themeFkaudit.setFkFinState("10");
							this.getService().save(themeFkaudit);
							
							
							
							//自动发出通知
							SysAffiche sysAffiche = new SysAffiche();
							sysAffiche.setSender(usersess.getEmployeeName());//发送者
							sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
							sysAffiche.setTitle("试题反馈审核通知");
							String content = "您好，"+usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName()+"在系统中指定了"+emp.getEmployeeName()+"为试题反馈的审核人，请及时进入试题反馈审核页面审核试题。";
							sysAffiche.setContent(content);
							
							sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate((DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd")).substring(0,10), "yyyy-MM-dd"))+1);
							sysAffiche.setRelationId(theme.getThemeId());
							sysAffiche.setRelationType("themeAudit");
							this.getService().add(sysAffiche);
							
							//添加发送对象
							SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
							sysAfficheIncepter.setIncepterId(emp.getEmployeeId());
							sysAfficheIncepter.setIncepterName(emp.getEmployeeName());
							sysAfficheIncepter.setIncepterType(4);
							sysAfficheIncepter.setSortNum(new Integer(0));
							sysAfficheIncepter.setSysAffiche(sysAffiche);
							this.getService().add(sysAfficheIncepter);
						}
					}
				}else{
					theme.setLastFkState("20");
					theme.setState(5);
					theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
					theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
					theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
					theme.setLastFkEmployeeId(usersess.getEmployeeId());
					theme.setLastFkEmployeeName(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());
					theme.setLastFkOrganId(usersess.getOrganId());
					
					ThemeFkaudit themeFkaudit = new ThemeFkaudit();
					themeFkaudit.setTheme(theme);
					themeFkaudit.setFkauditRemark(form.getFkRemark());
					themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
					themeFkaudit.setThemeAns(null);//当前答案
					themeFkaudit.setAwardCenter(0.0);//奖励分数
					themeFkaudit.setAwardTime(null);//奖励时间
					themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
					themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
					themeFkaudit.setState("10");//状态 10-提交反馈  20反馈通过
					themeFkaudit.setFkType("10");//类型 10-提出反馈  20-专家指定  30专家审核
					themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
					themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
					themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeFkaudit.setOrganId(usersess.getOrganId());//机构ID
					themeFkaudit.setOrganName(usersess.getOrganAlias());//机构名称
					themeFkaudit.setFkFinState("10");
					this.getService().save(themeFkaudit);
				}
				this.setMsg("提交完成！");
			}else{
				this.setMsg("没有找到对应的试题，提交失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("提交失败！");
		}
		return "save";
	}
	
	public String saveQr() throws EapException{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try{
			form = (ThemeForm) this.jsonToObject(ThemeForm.class);
			Theme theme = (Theme)this.getService().findDataByKey(form.getThemeId(), Theme.class);
			if(theme!=null && "50".equals(form.getLastFkState())){
				theme.setLastFkState(form.getLastFkState());
				theme.setState(15);
				theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
				theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
				
				ThemeFkaudit themeFkaudit = new ThemeFkaudit();
				themeFkaudit.setTheme(theme);
				themeFkaudit.setFkauditRemark(form.getFkRemark()!=null && !"".equals(form.getFkRemark())
						 && !"null".equals(form.getFkRemark()) ? form.getFkRemark() : (usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName())+"进行了试题最终确认。");
				themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
				themeFkaudit.setThemeAns(null);//当前答案
				themeFkaudit.setAwardCenter(0.0);//奖励分数
				themeFkaudit.setAwardTime(null);//奖励时间
				themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
				themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
				themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
				themeFkaudit.setFkType("40");//类型 10-提出反馈  20-专家指定  30专家审核 40-确认
				themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
				themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
				themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				themeFkaudit.setOrganId(usersess.getOrganId());//机构ID
				themeFkaudit.setOrganName(usersess.getOrganAlias());//机构名称
				themeFkaudit.setFkFinState("20");
				this.getService().save(themeFkaudit);
				
				
				Map term = new HashMap();
				term.put("themeId", theme.getThemeId());
				//term.put("employeeId", usersess.getEmployeeId());
				term.put("state", "20");
				List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
				String _themeHisId = null;
				for(int i=0;i<fkAuditlist.size();i++){
					ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
					if(_themeFkaudit.getThemeHisId()!=null && !"".equals(_themeFkaudit.getThemeHisId())
							 && !"null".equals(_themeFkaudit.getThemeHisId())){
						_themeHisId = _themeFkaudit.getThemeHisId();
						break;
					}
				}
				if(_themeHisId!=null){
					ThemeHis themeHis = (ThemeHis)this.getService().findDataByKey(_themeHisId, ThemeHis.class);
					if(themeHis != null){
						themeHis.setLockState("20");
						this.getService().saveOld(themeHis);
						
						Map term2 = new HashMap();
						term2.put("themeId", theme.getThemeId());
						fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term2, null);
						Map fkTypeMap = new HashMap();
						for(int i=0;i<fkAuditlist.size();i++){
							ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
							//System.out.println(_themeFkaudit.getFkauditRemark());
							if("10".equals(_themeFkaudit.getFkType()) || 
									("30".equals(_themeFkaudit.getFkType()) && fkTypeMap.get("FKTYPE_30") == null)){
								_themeFkaudit.setAwardCenter(new Double(1.0));
							}
							_themeFkaudit.setAwardTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
							_themeFkaudit.setThemeHisId(_themeHisId);
							_themeFkaudit.setFkFinState("20");
							if(!"20".equals(_themeFkaudit.getState())){
								_themeFkaudit.setState("20");
							}
							fkTypeMap.put("FKTYPE_"+_themeFkaudit.getFkType(), "a");
							if("10".equals(_themeFkaudit.getFkType())){
								break;
							}
						}
						this.getService().saves(fkAuditlist);
						
						
						//自动发出通知
						SysAffiche sysAffiche = new SysAffiche();
						sysAffiche.setSender(usersess.getEmployeeName());//发送者
						sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
						sysAffiche.setTitle("试题反馈完成通知");
						String content = "您好，您提交的试题反馈内容，已完成了审核。";
						sysAffiche.setContent(content);
						
						sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate((DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd")).substring(0,10), "yyyy-MM-dd"))+1);
						sysAffiche.setRelationId(theme.getThemeId());
						sysAffiche.setRelationType("themeAuditFin");
						this.getService().add(sysAffiche);
						
						//添加发送对象
						SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
						sysAfficheIncepter.setIncepterId(theme.getLastFkEmployeeId());
						sysAfficheIncepter.setIncepterName(theme.getLastFkEmployeeName());
						sysAfficheIncepter.setIncepterType(4);
						sysAfficheIncepter.setSortNum(new Integer(0));
						sysAfficheIncepter.setSysAffiche(sysAffiche);
						this.getService().add(sysAfficheIncepter);
						
						this.setMsg("提交完成！");
					}else{
						this.setMsg("没有找到修改的试题版本！");
					}
				}else{
					this.setMsg("没有找到修改的试题版本！");
				}
				
			}else if(theme!=null && "-40".equals(form.getLastFkState())){
				Employee emp = form.getLastFkAuditEmp();
				if(emp!=null){
					theme.setLastFkAuditId(emp.getEmployeeId());
					theme.setLastFkAuditName(emp.getEmployeeName());
					theme.setLastFkState(form.getLastFkState());
					theme.setState(5);
					theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
					theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
					theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
					
					Map term = new HashMap();
					term.put("themeId", theme.getThemeId());
					//term.put("employeeId", usersess.getEmployeeId());
					term.put("state", "20");
					List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
					String _themeHisId = null;
					for(int i=0;i<fkAuditlist.size();i++){
						ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
						if(_themeFkaudit.getThemeHisId()!=null && !"".equals(_themeFkaudit.getThemeHisId())
								 && !"null".equals(_themeFkaudit.getThemeHisId())){
							_themeHisId = _themeFkaudit.getThemeHisId();
							break;
						}
					}
					if(_themeHisId!=null){
						ThemeHis themeHis = (ThemeHis)this.getService().findDataByKey(_themeHisId, ThemeHis.class);
						if(themeHis != null){
							theme.setThemeName(themeHis.getThemeName());
							theme.setThemeVersion(themeHis.getThemeVersion());
								
							//清理多的
							if(theme.getThemeAnswerkeies().size()>themeHis.getThemeAnswerkeyHises().size()){
								for(int i=theme.getThemeAnswerkeies().size()-1;i>=themeHis.getThemeAnswerkeyHises().size();
											i--){
									theme.getThemeAnswerkeies().remove(i);
								}
							}
								
								
							Iterator its = themeHis.getThemeAnswerkeyHises().iterator();
							int index = 0;
							while(its.hasNext()){
								ThemeAnswerkeyHis themeAnswerkeyHis= (ThemeAnswerkeyHis)its.next();
								ThemeAnswerkey themeAnswerkey = theme.getThemeAnswerkeies().get(index);
								boolean isAdd = false;
								if(themeAnswerkey==null){
									themeAnswerkey= new ThemeAnswerkey();
									isAdd =true;
									themeAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
									themeAnswerkey.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
									themeAnswerkey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
								}else{
									themeAnswerkey = theme.getThemeAnswerkeies().get(index);
									themeAnswerkey.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
									themeAnswerkey.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
									themeAnswerkey.setLastUpdatedIdBy(usersess.getEmployeeId());//创建人ID
								}
								themeAnswerkey.setAnswerkeyValue(themeAnswerkeyHis.getAnswerkeyValue());
								themeAnswerkey.setSortNum(index);
								themeAnswerkey.setRemark(themeAnswerkeyHis.getRemark());
								themeAnswerkey.setTheme(theme);
								themeAnswerkey.setIsRight(themeAnswerkeyHis.getIsRight());
								themeAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
								themeAnswerkey.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
								themeAnswerkey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
								//service.add(themeAnswerkey);
								if(isAdd){
									theme.getThemeAnswerkeies().add(themeAnswerkey);
								}
								index++;
							}
							this.getService().delete(themeHis);
						}
					}
					
					ThemeFkaudit themeFkaudit = new ThemeFkaudit();
					themeFkaudit.setTheme(theme);
					String rmark = usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName()+"重新指定了审核人"+form.getLastFkAuditEmp().getEmployeeName()+"。";
					themeFkaudit.setFkauditRemark(form.getFkRemark()!=null && !"".equals(form.getFkRemark())
							 && !"null".equals(form.getFkRemark()) ? form.getFkRemark()+"["+rmark+"]" : rmark);
					themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
					themeFkaudit.setThemeAns(null);//当前答案
					themeFkaudit.setAwardCenter(0.0);//奖励分数
					themeFkaudit.setAwardTime(null);//奖励时间
					themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
					themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
					themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
					themeFkaudit.setFkType("20");//类型 10-提出反馈  20-专家指定  30专家审核
					themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
					themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
					themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeFkaudit.setOrganId(usersess.getOrganId());//机构ID
					themeFkaudit.setOrganName(usersess.getOrganAlias());//机构名称
					this.getService().save(themeFkaudit);
					this.setMsg("提交完成！");
					
					
					//自动发出通知
					SysAffiche sysAffiche = new SysAffiche();
					sysAffiche.setSender(usersess.getEmployeeName());//发送者
					sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
					sysAffiche.setTitle("试题反馈审核通知");
					String content = "您好，"+usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName()+"在系统中指定了"+emp.getEmployeeName()+"为试题反馈的审核人，请及时进入试题反馈审核页面审核试题。";
					sysAffiche.setContent(content);
					
					sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate((DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd")).substring(0,10), "yyyy-MM-dd"))+1);
					sysAffiche.setRelationId(theme.getThemeId());
					sysAffiche.setRelationType("themeAudit");
					this.getService().add(sysAffiche);
					
					//添加发送对象
					SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
					sysAfficheIncepter.setIncepterId(emp.getEmployeeId());
					sysAfficheIncepter.setIncepterName(emp.getEmployeeName());
					sysAfficheIncepter.setIncepterType(4);
					sysAfficheIncepter.setSortNum(new Integer(0));
					sysAfficheIncepter.setSysAffiche(sysAffiche);
					this.getService().add(sysAfficheIncepter);
				}else{
					this.setMsg("没有找到指定的审核人，操作失败！");
				}
			}else{
				this.setMsg("没有找到对应的试题，操作失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("提交失败！");
		}
		return "save";
	}
	
	
	
	/**
	 * 否决确认
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String saveUnQr() throws EapException{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try{
			form = (ThemeForm) this.jsonToObject(ThemeForm.class);
			Theme theme = (Theme)this.getService().findDataByKey(form.getThemeId(), Theme.class);
			if(theme!=null){
				Map term = new HashMap();
				term.put("themeId", theme.getThemeId());
				//term.put("employeeId", usersess.getEmployeeId());
				term.put("state", "20");
				List<ThemeFkaudit> fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term, null);
				String _themeHisId = null;
				for(int i=0;i<fkAuditlist.size();i++){
					ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
					if(_themeFkaudit.getThemeHisId()!=null && !"".equals(_themeFkaudit.getThemeHisId())
							 && !"null".equals(_themeFkaudit.getThemeHisId())){
						_themeHisId = _themeFkaudit.getThemeHisId();
						break;
					}
				}
				
				ThemeHis themeHis = (ThemeHis)this.getService().findDataByKey(_themeHisId, ThemeHis.class);
				if(themeHis!=null){
					theme.setThemeVersion(themeHis.getThemeVersion());
					ThemeType themeType = (ThemeType)this.getService().findDataByKey(themeHis.getThemeTypeId(), ThemeType.class);
					theme.setThemeType(themeType);
					theme.setThemeHisId(null);
					theme.setLastFkState(form.getLastFkState());
					theme.setState(15);
					theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
					theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
					theme.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
					
					Iterator its = themeHis.getThemeAnswerkeyHises().iterator();
					theme.getThemeAnswerkeies().clear();
					while(its.hasNext()){
						ThemeAnswerkeyHis themeAnswerkeyHis= (ThemeAnswerkeyHis)its.next();
						ThemeAnswerkey themeAnswerkey = new ThemeAnswerkey();
						BeanUtils.copyProperties(themeAnswerkeyHis,themeAnswerkey);
						themeAnswerkey.setAnswerkeyId(null);
						themeAnswerkey.setSortNum(themeAnswerkeyHis.getSortNum().intValue());
						themeAnswerkey.setRemark(themeAnswerkeyHis.getRemark());
						themeAnswerkey.setThemeTypeId(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeId() : null);
						themeAnswerkey.setThemeTypeName(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeName() : null);
						theme.getThemeAnswerkeies().add(themeAnswerkey);
					}
				}
				
				
				ThemeFkaudit themeFkaudit = new ThemeFkaudit();
				themeFkaudit.setTheme(theme);
				themeFkaudit.setFkauditRemark(form.getFkRemark()!=null && !"".equals(form.getFkRemark())
						 && !"null".equals(form.getFkRemark()) ? form.getFkRemark() : usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName()+"否决了本次反馈的修改。");
				themeFkaudit.setThemeName(theme.getThemeName().length()>1500?theme.getThemeName().substring(0,1450)+"...":theme.getThemeName());//当前试题
				themeFkaudit.setThemeAns(null);//当前答案
				themeFkaudit.setAwardCenter(0.0);//奖励分数
				themeFkaudit.setAwardTime(null);//奖励时间
				themeFkaudit.setThemeVersion(theme.getThemeVersion());//版本号
				themeFkaudit.setThemeCode(theme.getThemeCode());//试题编码
				themeFkaudit.setState("20");//状态 10-提交反馈  20反馈通过
				themeFkaudit.setFkType("30");//类型 10-提出反馈  20-专家指定  30专家审核
				themeFkaudit.setFkFinState("10");
				themeFkaudit.setThemeHisId(null);//试题历史版本ID（来源历史表）
				themeFkaudit.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				themeFkaudit.setCreatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//创建人
				themeFkaudit.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				themeFkaudit.setOrganId(usersess.getOrganId());//机构ID
				themeFkaudit.setOrganName(usersess.getOrganAlias());//机构名称
				this.getService().save(themeFkaudit);
				this.getService().delete(themeHis);
				
				
				
				if(_themeHisId!=null){
					if(themeHis != null){
						//term.put("state", "10");
						Map term2 = new HashMap();
						term2.put("themeId", theme.getThemeId());
						fkAuditlist = this.getService().queryData("queryThemeFkauditHql", term2, null);
						for(int i=0;i<fkAuditlist.size();i++){
							ThemeFkaudit _themeFkaudit = fkAuditlist.get(i);
							_themeFkaudit.setAwardCenter(new Double(0.0));
							_themeFkaudit.setAwardTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
							_themeFkaudit.setThemeHisId(_themeHisId);
							_themeFkaudit.setFkFinState("20");
							if("20".equals(_themeFkaudit.getState())){
								_themeFkaudit.setState("20");//最后一个肯定是反馈人
							}
							if("10".equals(_themeFkaudit.getFkType())){
								break;
							}
						}
						this.getService().saves(fkAuditlist);
						
						
						//自动发出通知
						SysAffiche sysAffiche = new SysAffiche();
						sysAffiche.setSender(usersess.getEmployeeName());//发送者
						sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
						sysAffiche.setTitle("试题反馈完成通知");
						String content = "您好，您提交的试题反馈内容，已完成了审核。";
						sysAffiche.setContent(content);
						
						sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate((DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd")).substring(0,10), "yyyy-MM-dd"))+1);
						sysAffiche.setRelationId(theme.getThemeId());
						sysAffiche.setRelationType("themeAuditFin");
						this.getService().add(sysAffiche);
						
						//添加发送对象
						SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
						sysAfficheIncepter.setIncepterId(theme.getLastFkEmployeeId());
						sysAfficheIncepter.setIncepterName(theme.getLastFkEmployeeName());
						sysAfficheIncepter.setIncepterType(4);
						sysAfficheIncepter.setSortNum(new Integer(0));
						sysAfficheIncepter.setSysAffiche(sysAffiche);
						this.getService().add(sysAfficheIncepter);
						
						this.setMsg("提交完成！");
					}else{
						this.setMsg("没有找到修改的试题版本！");
					}
				}else{
					this.setMsg("没有找到修改的试题版本！");
				}
			}else{
				this.setMsg("没有找到对应的试题，操作失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("提交失败！");
		}
		return "save";
	}
		
	public String getIsFkAuditXd() {
		return isFkAuditXd;
	}

	public void setIsFkAuditXd(String isFkAuditXd) {
		this.isFkAuditXd = isFkAuditXd;
	}

	public ThemeForm getForm() {
		return form;
	}

	public void setForm(ThemeForm form) {
		this.form = form;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	public File getUploadImg() {
		return uploadImg;
	}

	public void setUploadImg(File uploadImg) {
		this.uploadImg = uploadImg;
	}

	public String getUploadImgFileName() {
		return uploadImgFileName;
	}

	public void setUploadImgFileName(String uploadImgFileName) {
		this.uploadImgFileName = uploadImgFileName;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	/*public ThemeImageForm getThemeImageForm() {
		return themeImageForm;
	}

	public void setThemeImageForm(ThemeImageForm themeImageForm) {
		this.themeImageForm = themeImageForm;
	}*/

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public List<ThemeFkauditForm> getThemeFkauditFormList() {
		return themeFkauditFormList;
	}

	public void setThemeFkauditFormList(List<ThemeFkauditForm> themeFkauditFormList) {
		this.themeFkauditFormList = themeFkauditFormList;
	}

	public int getThemeFkauditFormListTotal() {
		return themeFkauditFormListTotal;
	}

	public void setThemeFkauditFormListTotal(int themeFkauditFormListTotal) {
		this.themeFkauditFormListTotal = themeFkauditFormListTotal;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getImageContent() {
		return imageContent;
	}

	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}

	
}