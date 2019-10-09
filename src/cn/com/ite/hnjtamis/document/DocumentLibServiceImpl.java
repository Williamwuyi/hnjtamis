package cn.com.ite.hnjtamis.document;
 
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;


/**
 * 文档库管理
 * @author 朱健
 * @create time: 2016年3月16日 上午9:10:52
 * @version 1.0
 * 
 * @modified records:
 */
public class DocumentLibServiceImpl extends DefaultServiceImpl implements DocumentLibService {

	/**
	 * 根据条件查询
	 * @description
	 * @param term
	 * @return
	 * @modified
	 */
	public List<DocumentLib> queryDocumentLibList(Map term){
		DocumentLibDao documentLibDao = (DocumentLibDao)this.getDao();
		return documentLibDao.queryDocumentLibList(term);
	}
	/**
	 * 获取文档对应的附件ID
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getAccessoryIdInDocument(){
		DocumentLibDao documentLibDao = (DocumentLibDao)this.getDao();
		return documentLibDao.getAccessoryIdInDocument();
	}
	
	/**
	 * 查询个人收藏
	 * @description
	 * @param favoriteEmployeeId 员工ID
	 * @param favoriteUserId 用户名
	 * @return
	 * @modified
	 */
	public Map getFavoriteInDocument(String favoriteEmployeeId,String favoriteUserId){
		DocumentLibDao documentLibDao = (DocumentLibDao)this.getDao();
		return documentLibDao.getFavoriteInDocument(favoriteEmployeeId, favoriteUserId);
	}
}
