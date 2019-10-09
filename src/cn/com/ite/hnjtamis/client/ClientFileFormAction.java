package cn.com.ite.hnjtamis.client;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.hnjtamis.client.domain.ClientFile;
import cn.com.ite.hnjtamis.common.FileOption;

public class ClientFileFormAction extends AbstractFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7697745129825229682L;

	private ClientFile form;

	private File clientFile;
	private String clientFileFileName;

	/**
	 * 查询数据
	 * 
	 * @return
	 * @modified
	 */
	public String find() {
		form = (ClientFile) service.findDataByKey(this.getId(),
				ClientFile.class);
		return "find";
	}

	/**
	 * 保存主对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		try {
			form = (ClientFile) this.jsonToObject(ClientFile.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(form.getId()))
			form.setId(null);

		try {
			form.setFileName(clientFileFileName);
			form.setFileSize(clientFile.length());
			form.setUploadTime(DateUtils.convertDateToStr(new Date(),
					"yyyy-MM-dd HH:mm:ss"));
			form.setFileContent(FileOption.file2Bytes(clientFile));
			form.setIsRecoverVer(1);
			form.setLastUpdateTime(DateUtils.convertDateToStr(new Date(
					clientFile.lastModified()), "yyyy-MM-dd HH:mm:ss"));
			service.save(form);

			// 设置以前版本手动恢复状态为0
			((ClientFileService) service).saveRecoverVersion(
					clientFileFileName, form.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		this.setMsg("保存成功！");
		return "save";
	}

	/**
	 * 手动指定恢复版本
	 * 
	 * @return
	 * @throws Exception
	 */
	public String recover() throws Exception {
		String fileName = new String(clientFileFileName.getBytes("iso-8859-1"),
				"utf-8");
		if (!StringUtils.isEmpty(fileName)) {
			//将其它文件设置为不指定
			((ClientFileService) service).saveRecoverVersion(fileName, this
					.getId());
			
			//设置当前选择的文件为人工指定
			ClientFile file = (ClientFile)service.findDataByKey(this.getId(), ClientFile.class);
			file.setIsRecoverVer(1);
			service.update(file);
			this.setMsg("操作成功！");
		} else {
			this.setMsg("获取文件名失败");
		}
		return "save";
	}

	public ClientFile getForm() {
		return form;
	}

	public void setForm(ClientFile form) {
		this.form = form;
	}

	public File getClientFile() {
		return clientFile;
	}

	public void setClientFile(File clientFile) {
		this.clientFile = clientFile;
	}

	public String getClientFileFileName() {
		return clientFileFileName;
	}

	public void setClientFileFileName(String clientFileFileName) {
		this.clientFileFileName = clientFileFileName;
	}
}
