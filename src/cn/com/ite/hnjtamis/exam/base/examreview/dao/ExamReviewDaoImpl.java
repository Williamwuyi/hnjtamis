package cn.com.ite.hnjtamis.exam.base.examreview.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.examreview.dao.ExamReviewDaoImpl</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年3月13日 下午3:41:47
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamReviewDaoImpl extends HibernateDefaultDAOImpl implements ExamReviewDao{
	
	
	
	/**
	 * 查询试题
	 * @description
	 * @param examId
	 * @param timuids
	 * @param curtimuids
	 * @param state
	 * @param anotherstate
	 * @return
	 * @modified
	 */
	public List<ExamTestpaperTheme> queryExamTestpaperThemeList(String examId,
			LinkedList<String> timuids,LinkedList<String> curtimuids,
			String state,String anotherstate){
		List list = new ArrayList();
		try{
			int fyNum = 40;
			String sql = "SELECT tt.* FROM "+
				           " ( SELECT A.*, ROWNUM RN FROM  "+
       							" (SELECT t.* FROM exam_testpaper_theme t where t.exam_id=? ";
			
			if(timuids!=null && !"".equals(timuids) && !"null".equals(timuids) 
					&& curtimuids!=null && !"".equals(curtimuids) && !"null".equals(curtimuids)){
				
				//StringBuffer param = new StringBuffer("',");
				sql+=" and ((";
				String tids = "";
				for (int i=0,k=0;i<timuids.size();i++) {
					String eid = timuids.get(i);
					tids+="'"+eid+"',";
					if((i>0 && i%fyNum ==0) || i==timuids.size()-1){
						if(k > 0){
							sql+=" and ";
						}
						tids = tids.substring(0,tids.length()-1);
						sql+=" t.EXAM_TESTPAPER_THEME_ID not in ("+tids+") ";
						tids = "";
						k++;
					}
				}
				sql+=" ) ";
				
				tids = "";
				for (int i=0;i<curtimuids.size();i++) {
					String eid = curtimuids.get(i);
					tids+="'"+eid+"',";
					if((i>0 && i%fyNum ==0) || i==curtimuids.size()-1){
						tids = tids.substring(0,tids.length()-1);
						sql+=" or t.EXAM_TESTPAPER_THEME_ID in ("+tids+") ";
						tids = "";
					}
				}
				sql+=" ) ";
				
				/*StringBuffer param2 = new StringBuffer("',");
				for (String string : curtimuids) {
					sql+=" or :curtimuids like '%,'||t.EXAM_TESTPAPER_THEME_ID||',%') ";
				}*/
				
				
			}else if(timuids!=null && !"".equals(timuids) && !"null".equals(timuids)){
				//sql+=" and :timuids not like '%,'||t.EXAM_TESTPAPER_THEME_ID||',%' ";
				sql+=" and (";
				String tids = "";
				for (int i=0,k=0;i<timuids.size();i++) {
					String eid = timuids.get(i);
					tids+="'"+eid+"',";
					if((i>0 && i%fyNum ==0) || i==timuids.size()-1){
						tids = tids.substring(0,tids.length()-1);
						if(k > 0){
							sql+=" and ";
						}
						sql+=" t.EXAM_TESTPAPER_THEME_ID not in ("+tids+") ";
						tids = "";
						k++;
					}
				}
				sql+=" ) ";
			}
			sql+=" and (t.state=? or t.state=? )  "+
       			 " and t.SCORE_TYPE='1' order by t.theme_type_id,t.sort_num) "+ 
       			 " A WHERE ROWNUM <= 10 ) tt  "+
       			 " WHERE RN >= 0 ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.setString(0, examId);
			sqlQuery.setString(1, state);
			sqlQuery.setString(2, anotherstate);
			list = sqlQuery.addEntity("tt", ExamTestpaperTheme.class)
				.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

}
