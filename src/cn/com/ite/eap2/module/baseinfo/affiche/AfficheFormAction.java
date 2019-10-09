package cn.com.ite.eap2.module.baseinfo.affiche;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;



import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheUser;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheUserDel;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.common.RandomGUID;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.workflow.face.IWorkFlow;

/**
 * <p>Title cn.com.ite.eap2.module.baseinfo.affiche.AfficheFormAction</p>
 * <p>Description 系统公告FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-12 时间03:26:14
 * @version 2.0
 * 
 * @modified records:
 */
public class AfficheFormAction extends AbstractFormAction implements ServletRequestAware{
	private static final long serialVersionUID = -3738505545805860813L;
	
	private HttpServletRequest request;
	
	//系统公告
	private SysAffiche form;
	private String itemId;
	private String key;
	private String value;
	
	private String accountTerm;
	
	
	
	private final static double img_width_Max = 400.0;
	private final static double img_height_Max = 200.0;
	private String uploadImgFileName;
	private String width;
	private String height;
	private String imageFileName;
	private String imageFilePath;
	private File uploadImg;
	private String content;
	private String imageContent;
	public String uploadImgage(){
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
				boolean isCreatNewFile = true;
				double _width = -1;
				double _height = -1;
				if(width!=null && !"".equals(width) && !"".equals(width)){
					_width = Double.parseDouble(width);
					_height = Math.floor(sourceImg.getHeight()*_width/sourceImg.getWidth());
				}else{
					_width = sourceImg.getWidth();
	    			_height = sourceImg.getHeight();
	    			if(_width>img_width_Max && _height>img_height_Max){	
	    				_height = Math.floor(_height*img_width_Max/_width);
	    				_width = img_width_Max;
	    			}else if(_width>img_width_Max){
	    				_height = Math.floor(_height*img_width_Max/_width);
	    				_width = img_width_Max;
	    			}else if(_height>img_height_Max){
	    				_width = Math.floor(_width*img_height_Max/_height);
	    				_height = img_height_Max;
	    			}else{
	    				isCreatNewFile = false;
	    			}
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
    				    BufferedImage tag = new BufferedImage((int)_width,(int)_height,BufferedImage.TYPE_INT_RGB);
    				    tag.getGraphics().drawImage(Bi.getScaledInstance((int)_width,(int)_height,BufferedImage.SCALE_SMOOTH),0,0,null);
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
    			width = NumericUtils.roundToString(_width,2);
    			height = NumericUtils.roundToString(_height,2);
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
		return "saveThemeImg";
	}
	
	/**
	 * 读配置
	 * @return
	 */
	public String readConfig(){
		ServletContent.getAllNameSpace();
		form = new SysAffiche();
		if(!key.equals("jdbc.url")&&!key.equals("jdbc.username")
			&&!key.equals("jdbc.password"))
		 form.setContent(Config.getPropertyValue(key));
		return "find";
	}
	public String writeConfig(){
		Config.setPropertyValue(key, value);
		return null;
	}
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String find(){
		form = (SysAffiche)service.findDataByKey(this.getId(), SysAffiche.class);
		return "find";
	}
	
	
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String userDelAffiche(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			form = new SysAffiche();
			return "find";
	 	}
		form = (SysAffiche)service.findDataByKey(this.getId(), SysAffiche.class);

		if(form!=null){
			//保存阅读时间
			try {
				Map term = new HashMap();
				term.put("saId", form.getSaId());
				term.put("accountTerm", usersess.getAccount());
				List<SysAfficheUserDel> ls = service.queryData("getUserDelAffiche", term, null);
				SysAfficheUserDel sysAfficheUserDel = null;
				if(ls!=null && ls.size()>0){
					sysAfficheUserDel = ls.get(0);
				}else{
					sysAfficheUserDel = new SysAfficheUserDel();
					sysAfficheUserDel.setAccount(usersess.getAccount());
					sysAfficheUserDel.setSaId(form.getSaId());
				}
				sysAfficheUserDel.setEmployeeId(usersess.getEmployeeId());
				sysAfficheUserDel.setEmployeeName(usersess.getEmployeeName());
				sysAfficheUserDel.setDelTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				service.saveOld(sysAfficheUserDel);
				this.setMsg("删除系统公告成功！");
				this.setSuccess(true);
			} catch (EapException e) {
				this.setMsg("删除系统公告失败！");
				this.setSuccess(false);
				e.printStackTrace();
			}
		}else{
			this.setMsg("删除系统公告失败！");
			this.setSuccess(false);
		}
		return "delete";
	}
	
	/**
	 * 查询结果
	 * @return
	 * @modified
	 */
	public String readAffiche(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			form = new SysAffiche();
			return "find";
	 	}
		form = (SysAffiche)service.findDataByKey(this.getId(), SysAffiche.class);

		//保存阅读时间
		try {
			Map term = new HashMap();
			term.put("saId", form.getSaId());
			term.put("accountTerm", usersess.getAccount());
			List<SysAfficheUser> ls = service.queryData("getReadAffiche", term, null);
			SysAfficheUser sysAfficheUser = null;
			if(ls!=null && ls.size()>0){
				sysAfficheUser = ls.get(0);
			}else{
				sysAfficheUser = new SysAfficheUser();
				sysAfficheUser.setAccount(usersess.getAccount());
				sysAfficheUser.setSaId(form.getSaId());
			}
			sysAfficheUser.setEmployeeId(usersess.getEmployeeId());
			sysAfficheUser.setEmployeeName(usersess.getEmployeeName());
			sysAfficheUser.setReadTime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			service.saveOld(sysAfficheUser);
		} catch (EapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "find";
	}
	
	
	@SuppressWarnings("unchecked")
	public String findFile(){
		Map term = new HashMap();
		term.put("itemId", this.getId());
		form = new SysAffiche();
		form.getAccessories().addAll(service.queryData("fileHql", term, null));
		return "find";
	}
	/**
	 * 输出附件
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String outputFile() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		Accessory accessory = (Accessory)service.findDataByKey(this.getId(), Accessory.class);
		File file = null;
		FileInputStream in = null;
		String fileName = accessory.getFileName();
	    try{
		    file = new File(
		    		ServletActionContext.getRequest().getSession().getServletContext().getRealPath("/")
		    		+accessory.getFilePath());
		    if(!file.exists()){
		    	response.setContentType("text/html;charset=GBK");
		    	response.getWriter().write("未发现此文件'"+fileName+"',可能已经删除或移除!");
		    	return null;
		    }
		    in = new java.io.FileInputStream(file);
		    response.setContentType("application/x-msdownload");
		    response.addHeader("Content-Disposition", "attachment; " +
		    		"filename=\""+new String(fileName.getBytes(),"ISO-8859-1") + "\";" +
		    				"filename*=utf-8''"+new String(fileName.getBytes(),"ISO-8859-1"));
			byte[] chrBuffer = new byte[1024]; //缓冲 
			int off = 0;
			while ((off = in.read(chrBuffer)) != -1 & in != null) {
				response.getOutputStream().write(chrBuffer,0,off);
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    	if(file!=null) file.delete();
	    	response.setContentType("text/html;charset=GBK");
	    	response.getWriter().write("未发现此文件'"+fileName+"',可能已经删除或移除!");
	    }finally{
		   if(in!=null) in.close();
	    }
		return null;
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save() throws Exception{
		form = (SysAffiche)this.jsonToObject(SysAffiche.class);
		if("".equals(form.getSaId()))
			form.setSaId(null);
		form.setSendTime(new java.sql.Date(System.currentTimeMillis()));		
		//业务处理
		service.save(form);
		this.setMsg("系统公告保存成功！");
		return "save";
	}
	/**
	 * 撤消方法
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String undo() throws Exception{
		form = (SysAffiche)service.findDataByKey(this.getId(), SysAffiche.class);
		IWorkFlow flowService = (IWorkFlow)SpringContextUtil.getBean("workflowServer");
		Map param = new HashMap();
		param.put("id",form.getSaId());
		//撤消操作
		String[] states = flowService.undo("1", param, form.getAccessoriesItemId(), 
				LoginAction.getUserSessionInfo().getEmployeeId());
		form.setAccessoriesItemId(states[0]);//还原状态，关键地址
	    service.update(form);
		return "save";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String saveFile() throws Exception{
		Map term = new HashMap();
		term.put("itemId", this.getItemId());
		List olds = service.queryData("fileHql", term, null);
		form = (SysAffiche)this.jsonToObject(SysAffiche.class);
		//附件处理
		List fildIds = new ArrayList();
		for(Accessory acc:form.getAccessories()){
			acc.setItemId(this.getItemId());
			if(StringUtils.isNotEmpty(acc.getAcceId()))
				fildIds.add(acc.getAcceId());
		}
		for(Accessory acc:form.getAccessories())
		   service.save(acc);		
		for(Accessory acc:(List<Accessory>)olds){
			if(!fildIds.contains(acc.getAcceId())){
				service.delete(acc);
			}
		}
		this.setMsg("附件保存成功！");
		return "save";
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public SysAffiche getForm() {
		return form;
	}
	public void setForm(SysAffiche form) {
		this.form = form;
	}
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public String getUploadImgFileName() {
		return uploadImgFileName;
	}

	public void setUploadImgFileName(String uploadImgFileName) {
		this.uploadImgFileName = uploadImgFileName;
	}

	public File getUploadImg() {
		return uploadImg;
	}

	public void setUploadImg(File uploadImg) {
		this.uploadImg = uploadImg;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAccountTerm() {
		return accountTerm;
	}

	public void setAccountTerm(String accountTerm) {
		this.accountTerm = accountTerm;
	}
	
	
}