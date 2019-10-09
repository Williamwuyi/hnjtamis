package cn.com.ite.eap2.core.struts2;

import java.io.Serializable;
import java.util.*;

import cn.com.ite.eap2.common.utils.JsonUtils;

/**
 * <p>Title cn.com.ite.eap2.core.hibernate.DefaultDAO</p>
 * <p>Description 抽象ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 9:07:31 AM
 * @version 2.0
 * 
 * @modified records:
 */
public abstract class AbstractListAction extends AbstractAction implements Serializable{
	private static final long serialVersionUID = -8731767713995926488L;
	/**
	 * 开始行号
	 */
	private int start;
	/**
	 * 而记录数
	 */
	private int limit;
	/**
	 * 页码
	 */
	private int page;
	/**
	 * 父主健值，用于表格树
	 */
	private String parentId;
	/**
	 * 父结点类型
	 */
	private String parentType;
	/**
	 * 过滤ID数组
	 */
	private List<String> filterIds;
	/**
	 * 总记录数
	 */
	private int total;
	/**
	 * 排序字段
	 */
	private String sort;
	/**
	 * 排序映射
	 */
	private Map<String,Boolean> sortMap = new LinkedHashMap<String,Boolean>();
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getSort() {
		return sort;
	}
	@SuppressWarnings("unchecked")
	public void setSort(String sort) {
		sortMap.clear();
		List list = JsonUtils.fromJson(sort, List.class);
		for(Map map:(List<Map>)list){
			sortMap.put((String)map.get("property"), map.get("direction").equals("ASC"));
		}
		this.sort = sort;
	}
	@SuppressWarnings("unchecked")
	public Map getSortMap() {
		return sortMap;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		if("".equals(parentId))
			parentId = null;
		this.parentId = parentId;
	}
	public List<String> getFilterIds() {
		return filterIds;
	}
	public void setFilterIds(List<String> filterIds) {
		this.filterIds = filterIds;
	}
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
}