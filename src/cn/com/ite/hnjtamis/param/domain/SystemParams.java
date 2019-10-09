package cn.com.ite.hnjtamis.param.domain;

/**
 * SystemParams entity. @author MyEclipse Persistence Tools
 */

public class SystemParams implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5608586106126091134L;
	private String id;
	private String code;
	private String name;
	private String value;
	private Integer type;
	private String selectItems;
	private String sort;
	private Integer sortNo;

	// Constructors

	/** default constructor */
	public SystemParams() {
	}

	/** full constructor */
	public SystemParams(String code, String name, String value, Integer type,
			String selectItems, String sort, Integer sortNo) {
		this.code = code;
		this.name = name;
		this.value = value;
		this.type = type;
		this.selectItems = selectItems;
		this.sort = sort;
		this.sortNo = sortNo;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSelectItems() {
		return this.selectItems;
	}

	public void setSelectItems(String selectItems) {
		this.selectItems = selectItems;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}