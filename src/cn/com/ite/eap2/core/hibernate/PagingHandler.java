package cn.com.ite.eap2.core.hibernate;

import java.util.Map;

/**
 * 
 * <p>Title cn.com.ite.eap2.core.hibernate.PagingHandler</p>
 * <p>Description 具体数据库特殊处理接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 5:58:18 PM
 * @version 2.0
 * 
 * @modified records:
 */
public interface PagingHandler {
	/**
	 * SQL分页处理
	 * @param sql SQL语句
	 * @param startNo 开始行号
	 * @param rowSize 页记录数
	 * @return 处理后的SQL
	 * @modified
	 */
	String sqlPagingHandler(String sql,int startNo,int rowSize);
	
	/**
	 * SQL的求量处理
	 * @param sql 语句
	 * @return 处理的SQL
	 * @modified
	 */
	String sqlCountHandler(String sql);
	/**
	 * SQL排序处理
	 * @param sql SQL语句
	 * @param sortMap 排序
	 * @return  排序处理后SQL
	 */
	@SuppressWarnings("unchecked")
	String sqlSortHandler(String sql,Map sortMap);
}
