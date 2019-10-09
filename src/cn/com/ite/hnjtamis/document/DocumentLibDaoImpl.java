package cn.com.ite.hnjtamis.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;

/**
 * 文档库管理
 * @author 朱健
 * @create time: 2016年3月16日 上午9:11:08
 * @version 1.0
 * 
 * @modified records:
 */
public class DocumentLibDaoImpl extends HibernateDefaultDAOImpl implements DocumentLibDao{

	/**
	 * 根据条件查询
	 * @description
	 * @param term
	 * @return
	 * @modified
	 */
	public List<DocumentLib> queryDocumentLibList(Map term){
		List<DocumentLib> list = null;
		try{
			String hql = " from DocumentLib t where t.isDel = 0  " ;
			String documentNameTerm = (String)term.get("documentNameTerm");
			if(documentNameTerm!=null && !"".equals(documentNameTerm) && !"null".equals(documentNameTerm)){
				hql+=" and t.documentName like '%' || :documentNameTerm || '%' ";
			}
			String stateTerm = (String)term.get("stateTerm");
			if(stateTerm!=null && !"".equals(stateTerm) && !"null".equals(stateTerm)){
				hql+="  and ',' || :stateTerm || ',' like '%,' || t.isAnnounced || ',%' ";
			}
			String organIdTerm = (String)term.get("organIdTerm");
			if(organIdTerm!=null && !"".equals(organIdTerm) && !"null".equals(organIdTerm)){
				hql+=" and t.originalOrganId = :organIdTerm ";
			}
			String quarterTrainCode = (String)term.get("quarterTrainCode");
			if(quarterTrainCode!=null && !"".equals(quarterTrainCode) && !"null".equals(quarterTrainCode)){
				hql+= " and ( not exists (select 1 from DocumentSearchkey v where v.documentLib.documentId = t.documentId  "+
	               		"and v.quarterTrainCode is not null)  "+
	                 " or exists(select 1 from DocumentSearchkey v where v.documentLib.documentId = t.documentId  "+
	                 " and v.quarterTrainCode = :quarterTrainCode))  ";
			}
			
			String organTerm = (String)term.get("organTerm");
			if(organTerm!=null && !"".equals(organTerm) && !"null".equals(organTerm)){
				hql+=" and t.originalOrganId = :organTerm ";
			}
			String specialityTerm = (String)term.get("specialityTerm");
			if(specialityTerm!=null && !"".equals(specialityTerm) && !"null".equals(specialityTerm)){
				hql+= " and exists (select 1 from DocumentSearchkey v where v.documentLib.documentId = t.documentId  "+
	               		"and v.specialityCode = :specialityTerm)   ";
			}
			String queryTypeTerm = (String)term.get("queryTypeTerm");
			if("favoriteType".equals(queryTypeTerm)){
				//String favoriteEmployeeId = (String)term.get("favoriteEmployeeId");
				//String favoriteUserId = (String)term.get("favoriteUserId");
				hql+= " and exists (select 1 from DocumentSearchkey v where v.documentLib.documentId = t.documentId  "+
	               		"and v.favoriteEmployeeId = :favoriteEmployeeId and v.favoriteUserId = :favoriteUserId)   ";
			}
			String documentTypeTerm = (String)term.get("documentTypeTerm");
			if(documentTypeTerm!=null && !"".equals(documentTypeTerm) && !"null".equals(documentTypeTerm)){
				hql+=" and t.documentTypeId = :documentTypeTerm ";
			}
			hql+=" order by t.creationDate desc ";
			list = this.queryHql(hql, term, null, DocumentLib.class);
		}catch(Exception e){
			list = new ArrayList();
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 获取文档对应的附件ID
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getAccessoryIdInDocument(){
		Map value = new HashMap();
		try{
			String sql = "select t.acce_id,d.document_id from accessory_file t,document_lib d where t.item_id = d.document_id";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			List list = sqlQuery
					.addScalar("acce_id", StringType.INSTANCE)
					.addScalar("document_id", StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				Object[] v = (Object[])list.get(i);
				value.put((String)v[1], (String)v[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
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
		Map value = new HashMap();
		try{
			String sql = "select t.document_id from DOCUMENT_SEARCHKEY t "
					+ " where t.favorite_employee_id = ? and t.favorite_user_id = ? ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, favoriteEmployeeId);
			sqlQuery.setString(1, favoriteUserId);
			List<String> list = sqlQuery.addScalar("document_id", StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				String document_id = (String)list.get(i);
				value.put(document_id, document_id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
}
