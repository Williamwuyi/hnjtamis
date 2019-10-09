package cn.com.ite.hnjtamis.document;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;


/**
 * 文档库管理
 * @author 朱健
 * @create time: 2016年3月16日 上午9:11:15
 * @version 1.0
 * 
 * @modified records:
 */
public interface DocumentLibDao extends DefaultDAO{

	/**
	 * 根据条件查询
	 * @description
	 * @param term
	 * @return
	 * @modified
	 */
	public List<DocumentLib> queryDocumentLibList(Map term);
	
	/**
	 * 获取文档对应的附件ID
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getAccessoryIdInDocument();
	
	/**
	 * 查询个人收藏
	 * @description
	 * @param favoriteEmployeeId 员工ID
	 * @param favoriteUserId 用户名
	 * @return
	 * @modified
	 */
	public Map getFavoriteInDocument(String favoriteEmployeeId,String favoriteUserId);
	
}
