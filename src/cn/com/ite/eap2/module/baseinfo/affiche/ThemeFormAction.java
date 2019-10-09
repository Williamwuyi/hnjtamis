package cn.com.ite.eap2.module.baseinfo.affiche;


import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;



/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.ThemeFormAction</p>
 * <p>Description 大文本编辑控件的图片处理 </p>
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
	
	private File uploadImg;

	private String uploadImgFileName;

	private String content;
	
	private String width;
	
	private String height;
	
	private ThemeImageForm themeImageForm;
	
	public String uploadImgage(){
		themeImageForm = new ThemeImageForm();
		UserSession usersess = (UserSession)ServletContent.getSession().get("USER_SESSION");
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "saveThemeImg";
	 	}
		try {
			File classPath = new File(this.getClass().getResource("/").toURI());
			File webPath = classPath.getParentFile().getParentFile();
			String newPath = webPath.getPath()+File.separator+"upload"+File.separator+"htmleditorimage";
			File accessoryDir = new File(newPath);
			if(!accessoryDir.exists())
				accessoryDir.mkdirs();
			String newFilePath = newPath+File.separator+uploadImg.getName();
			String[] uploadImgFileNameSplit=uploadImgFileName.split("\\.");
			newFilePath = newFilePath.replaceAll(".tmp", "."+uploadImgFileNameSplit[uploadImgFileNameSplit.length-1]);
			File newFile = new File(newFilePath);
			uploadImg.renameTo(newFile);
			themeImageForm.setUrl(newFilePath.substring(webPath.getPath().length()+1));
			if(content==null || "".equals(content) || "简短的图片说明".equals(content)){
				
			}else{
				themeImageForm.setContent(content);
			}
			if(width!=null){
				themeImageForm.setWidth(width);
			}
			if(height!=null){
				themeImageForm.setHeight(height);
			}
			this.setMsg("上传成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("上传失败！");
		}
		return "saveThemeImg";
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

	public ThemeImageForm getThemeImageForm() {
		return themeImageForm;
	}

	public void setThemeImageForm(ThemeImageForm themeImageForm) {
		this.themeImageForm = themeImageForm;
	}

}