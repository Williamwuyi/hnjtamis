package cn.com.ite.eap2.core.hibernate;

import java.util.Iterator;
import java.util.Map;

/**
 * 
 * <p>Title cn.com.ite.eap2.core.hibernate.OraclePagingHandler</p>
 * <p>Description ORACE特殊处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 5:59:02 PM
 * @version 2.0
 * 
 * @modified records:
 */
public class OraclePagingHandler implements PagingHandler{
	/**
	 * SQL分页处理
	 * @param sql SQL语句
	 * @param startNo 开始行号
	 * @param rowSize 页记录数
	 * @return 处理后的SQL
	 * @modified
	 */
	public String sqlPagingHandler(String sql,int startNo,int rowSize){
		if(rowSize==0) 
			return sql;
		String sqlEx = "select pg_a.*,rownum r from ("+sql+") pg_a where rownum<="+(startNo+rowSize);
		sqlEx = "select * from ("+sqlEx+") pg_b where r>="+(startNo+1);
		return sqlEx;
	}

	/**
	 * SQL的求量处理
	 * @param sql 语句
	 * @return 处理的SQL
	 * @modified
	 */
	public String sqlCountHandler(String sql) {
		return "select count(*) from ("+sql+") t";
	}
	
	/**
	 * SQL排序处理
	 * @param sql SQL语句
	 * @param sortMap 排序
	 * @return  排序处理后SQL
	 */
	@SuppressWarnings("unchecked")
	public String sqlSortHandler(String sql,Map sortMap){
		if(sortMap!=null){
			Iterator iterator = sortMap.keySet().iterator();
			sql = "select k.* from ("+sql+") k ";
			if(sortMap.size()>0)
				sql += " order by ";
			boolean start = true;
			while(iterator.hasNext()){
				String columnName = (String)iterator.next();
				Boolean sorted = (Boolean)sortMap.get(columnName);
				if(!start)
					sql += ",";
				start = false;
				sql+="nlssort(k."+columnName+",'NLS_SORT=SCHINESE_PINYIN_M')"+
				(sorted?" asc":" desc");
			}
		}
		return sql;
	}
}
