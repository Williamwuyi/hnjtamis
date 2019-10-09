package cn.com.ite.hnjtamis.document;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Accessory;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;
import cn.com.ite.hnjtamis.document.domain.DocumentSearchkey;


/**
 * 文档库管理
 * @author 朱健
 * @create time: 2016年3月16日 上午9:10:35
 * @version 1.0
 * 
 * @modified records:
 */
public class DocumentLibListAction extends AbstractListAction implements ServletRequestAware{
 
	private static final long serialVersionUID = 2951108490451624080L;

	private HttpServletRequest request;
	
	private List<DocumentLib> list;
	
	private String documentNameTerm;

	private String op;
	
	private String favoriteType;
	
	private String organTerm;
	
	private String specialityTerm;
	
	private String queryTypeTerm;
	
	private String documentTypeTerm;
	
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	/**
	 * 查询列表
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String list(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		DocumentLibService documentLibService = (DocumentLibService)this.getService();
		list = new ArrayList();
		this.setTotal(0);
		Map term = new HashMap();
		term.put("documentTypeTerm", documentTypeTerm);
		term.put("documentNameTerm", documentNameTerm);
		term.put("organTerm", organTerm);
		term.put("specialityTerm", specialityTerm);
		term.put("queryTypeTerm", queryTypeTerm);
		term.put("favoriteEmployeeId", usersess.getEmployeeId()!=null ? usersess.getEmployeeId() : usersess.getAccount());
		term.put("favoriteUserId", usersess.getAccount());
		if("view".equals(op)){
			if(usersess.getQuarterId() == null || "".equals(usersess.getQuarterId())){
				list = new ArrayList();
			}else{
				term.put("stateTerm", "20");
				Quarter quarter= (Quarter)this.getService().findDataByKey(usersess.getQuarterId(), Quarter.class);
				term.put("quarterTrainCode", quarter.getQuarterTrainCode());
				
				list = documentLibService.queryDocumentLibList(term);
				
				//list = this.getService().queryData("getDocumentLibListByQuarterTrainCode", term, new HashMap(),this.getStart(),this.getLimit());
			}
		}else{
			if("input".equals(op) && (usersess.getCurrentOrganId() == null || "".equals(usersess.getCurrentOrganId()))){
				list = new ArrayList();
			}else{
				if("input".equals(op)){
					term.put("organIdTerm", usersess.getCurrentOrganId());
				}
				list = documentLibService.queryDocumentLibList(term);
				//list = this.getService().queryData("getDocumentLibList", term, new HashMap(),this.getStart(),this.getLimit());
			}
		}
		if(list!=null && list.size()>0){
			this.setTotal(list.size());
			
			List tmplist = new ArrayList();
			for(int i=this.getStart();i<this.getStart()+this.getLimit() && i<list.size();i++){
				tmplist.add(list.get(i));
			}
			list = tmplist;
			
			//DocumentLibService documentLibService =(DocumentLibService)this.getService();
			Map accessoryMap = documentLibService.getAccessoryIdInDocument();
			Map favoriteMap = documentLibService.getFavoriteInDocument(
					usersess.getEmployeeId()!=null ? usersess.getEmployeeId() : usersess.getAccount(), 
					usersess.getAccount());
			for(int i=0;i<list.size();i++){
				DocumentLib documentLib = list.get(i);
				String accId = (String)accessoryMap.get(documentLib.getDocumentId());
				documentLib.setAfficheId(accId);
				if(favoriteMap.get(documentLib.getDocumentId())!=null){
					documentLib.setTofavorite("true");
				}else{
					documentLib.setTofavorite("false");
				}
			}
		}
		return "list";
	}
	

	/**
	 * 删除
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String delete() throws EapException{
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		for(int i = 0;i<ids.length ;i++){
			sum++;
			DocumentLib documentLib = (DocumentLib)service.findDataByKey(ids[i], DocumentLib.class);
			if(documentLib!=null && documentLib.getIsAnnounced()!=null &&documentLib.getIsAnnounced().intValue() == 10){
				Map term = new HashMap();
				term.put("itemId", ids[i]);
				List<Accessory> accList = this.getService().queryData("queryAccessoryByItemId", term, new HashMap());
				service.deletes(accList);
				documentLib.setIsDel(1);
				documentLib.setSyncStatus(3);
				service.saveOld(documentLib);
				succ++;
			}
		}
		this.setMsg("选中"+sum+"个,成功删除"+succ+"个,请确定未删除记录只是草稿状态！");
		return "delete";
	}
	
	
	/**
	 * 进行收藏
	 * @description
	 * @return
	 * @modified
	 */
	public String favoriteDocument(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		if("add".equals(favoriteType)){
			DocumentLib documentLib = (DocumentLib)this.getService().findDataByKey(this.getId(), DocumentLib.class);
			DocumentSearchkey documentSearchkey = new DocumentSearchkey();
			documentSearchkey.setFavoriteEmployee(usersess.getEmployeeName()!=null ? usersess.getEmployeeName() : usersess.getAccount());
			documentSearchkey.setFavoriteEmployeeId(usersess.getEmployeeId()!=null ? usersess.getEmployeeId() : usersess.getAccount());
			documentSearchkey.setFavoriteUserId(usersess.getAccount());
			documentSearchkey.setCreatedBy(StringUtils.isEmpty(usersess.getEmployeeName())?usersess.getAccount():usersess.getEmployeeName());
			documentSearchkey.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
			documentSearchkey.setDocumentLib(documentLib);
			documentSearchkey.setSortNum(999);
			try {
				this.getService().add(documentSearchkey);
				this.setMsg("收藏成功!");
			} catch (EapException e) {
				this.setMsg("收藏失败!");
				e.printStackTrace();
			}
		}else if("del".equals(favoriteType)){
			Map term = new HashMap();
			term.put("documentId", this.getId());
			term.put("favoriteEmployeeId", usersess.getEmployeeId()!=null ? usersess.getEmployeeId() : usersess.getAccount());
			term.put("favoriteUserId", usersess.getAccount());
			List bos = this.getService().queryData("queryFavoriteHql", term, null);
			try {
				this.getService().deletes(bos);
				this.setMsg("取消收藏成功!");
			} catch (EapException e) {
				this.setMsg("取消收藏失败!");
				e.printStackTrace();
			}
		}
		return "delete";
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public List<DocumentLib> getList() {
		return list;
	}


	public void setList(List<DocumentLib> list) {
		this.list = list;
	}


	public String getDocumentNameTerm() {
		return documentNameTerm;
	}


	public void setDocumentNameTerm(String documentNameTerm) {
		this.documentNameTerm = documentNameTerm;
	}


	public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


	public String getFavoriteType() {
		return favoriteType;
	}


	public void setFavoriteType(String favoriteType) {
		this.favoriteType = favoriteType;
	}


	public String getOrganTerm() {
		return organTerm;
	}


	public void setOrganTerm(String organTerm) {
		this.organTerm = organTerm;
	}


	public String getSpecialityTerm() {
		return specialityTerm;
	}


	public void setSpecialityTerm(String specialityTerm) {
		this.specialityTerm = specialityTerm;
	}


	public String getQueryTypeTerm() {
		return queryTypeTerm;
	}


	public void setQueryTypeTerm(String queryTypeTerm) {
		this.queryTypeTerm = queryTypeTerm;
	}


	public String getDocumentTypeTerm() {
		return documentTypeTerm;
	}


	public void setDocumentTypeTerm(String documentTypeTerm) {
		this.documentTypeTerm = documentTypeTerm;
	}

	
	
	
}
