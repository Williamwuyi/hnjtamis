package cn.com.ite.eap2.domain.baseinfo;

import java.io.File;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.Accessory</p>
 * <p>Description 附件PO对象</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-9 时间09:08:25
 * @version 2.0
 * 
 * @modified records:
 */
public class Accessory implements java.io.Serializable {
	private static final long serialVersionUID = -5719218176461976926L;
	private String acceId;
	private String acceType;
	private String itemId;
	private String filePath;
	private String fileName;
	private Double fileSize;
	private Date uploadDate;
	private boolean showImage;
	private String employeeid;
	private Integer orderNo;


	/** default constructor */
	public Accessory() {
	}

	/** full constructor */
	public Accessory(String acceType, String itemId, String filePath,
			String fileName, Double fileSize, Date uploadDate,
			boolean showImage, String employeeid, Integer orderNo) {
		this.acceType = acceType;
		this.itemId = itemId;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.uploadDate = uploadDate;
		this.showImage = showImage;
		this.employeeid = employeeid;
		this.orderNo = orderNo;
	}
	
	public void saveFile(File file) throws Exception{
		if(this.getFileName().indexOf("\\")>0)
			this.setFileName(this.getFileName().substring(this.getFileName().lastIndexOf("\\")+1));
		if(file==null) return;
		//判断文件大小
		double size = file.length()/1024.0;
		double baseSize = Double.parseDouble(
				Config.getPropertyValue("accessory.type."+this.getAcceType()+".size"));
		if(size==0)
			throw new Exception("'"+this.getFileName()+"'文件没有任务数据！");
		if(size>baseSize)
			throw new Exception("'"+this.getFileName()+"'文件大小超过"+baseSize+"K");
		//判断文件扩展类型
		String extend = Config.getPropertyValue("accessory.type."+this.getAcceType()+".extend");
		String regex = "[\\S\\s]{1,}.("+extend.toLowerCase().replaceAll(",", "\\|")+"$)";
		if(!StringUtils.regex(regex, this.getFileName().toLowerCase()))
			throw new Exception("'"+this.getFileName()+"'此文件扩展类型不符合'"+
					extend.replaceAll("\\|", ",")+"'允许范围！");
		if(StringUtils.regex("[\\/:?\"'<>|,#%&*]{1,}", this.getFileName().toLowerCase()))
			throw new Exception("'"+this.getFileName()+"'此文件名中不能包含\\/:?\\\"'<>|,#%&*这些特殊字符！");
		String relativePath = Config.getPropertyValue("accessory.base.path");
		String date = DateUtils.convertDateToStr(new Date(), "yyyy-MM");
		String savePath = relativePath+File.separator+date;
		String[] files = this.getFileName().split("\\.");
		String saveFileName = file.getName().replaceAll(".tmp", "."+this.getFileName().split("\\.")[files.length-1]);
		File classPath = new File(this.getClass().getResource("/").toURI());
		File webPath = classPath.getParentFile().getParentFile();
		File accessoryDir = new File(webPath.getPath()+File.separator+savePath);
		if(!accessoryDir.exists())
			accessoryDir.mkdirs();
		File newFile = new File(webPath.getPath()+File.separator+savePath+File.separator+saveFileName);
		file.renameTo(newFile);
		//FileUtils.saveFile(file, savePath, saveFileName);
		String path = savePath+File.separator+saveFileName;
		this.setFilePath(path);//文件路径
		this.setFileSize(size);//文件大小
		regex = "\\S{1,}.(jpg|jpeg|gif|png|bmp$)";
		this.setShowImage(StringUtils.regex(regex, this.getFileName()));//是否为图片
	}
	
	public static void main(String[] args){
		String regex = "\\S\\s{1,}.(jpg|jpeg|gif|png|bmp)$";
		System.out.println(StringUtils.regex(regex, "2013 .gif"));
	}

	public boolean getShowImage() {
		return showImage;
	}
	
	public boolean isShowImage() {
		return showImage;
	}

	public void setShowImage(boolean showImage) {
		this.showImage = showImage;
	}

	public String getAcceId() {
		return this.acceId;
	}

	public void setAcceId(String acceId) {
		this.acceId = acceId;
	}

	public String getAcceType() {
		return this.acceType;
	}

	public void setAcceType(String acceType) {
		this.acceType = acceType;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Double getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}


	public String getEmployeeid() {
		return this.employeeid;
	}

	public void setEmployeeid(String employeeid) {
		if("".equals(employeeid)) employeeid = null;
		this.employeeid = employeeid;
	}

	public Integer getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

}