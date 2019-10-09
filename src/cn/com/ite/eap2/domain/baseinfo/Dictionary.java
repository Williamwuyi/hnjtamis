package cn.com.ite.eap2.domain.baseinfo;

/**
 * 
 * <p>Title cn.com.ite.eap2.domain.baseinfo.Dictionary</p>
 * <p>Description 字典数据</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 上午09:49:21
 * @version 2.0
 * 
 * @modified records:
 */
public class Dictionary implements java.io.Serializable {
	private static final long serialVersionUID = 6615314161078299059L;
	private String dicId;
	private DictionaryType dictionaryType;
	private String dataName;
	private String dataKey;
	private String description;
	private Integer sortNo;

	// Constructors

	/** default constructor */
	public Dictionary() {
	}

	/** full constructor */
	public Dictionary(DictionaryType dictionaryType, String dataName,
			String dataKey, String description, Integer sortNo) {
		this.dictionaryType = dictionaryType;
		this.dataName = dataName;
		this.dataKey = dataKey;
		this.description = description;
		this.sortNo = sortNo;
	}

	// Property accessors

	public String getDicId() {
		return this.dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public DictionaryType getDictionaryType() {
		return this.dictionaryType;
	}

	public void setDictionaryType(DictionaryType dictionaryType) {
		this.dictionaryType = dictionaryType;
	}

	public String getDataName() {
		return this.dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataKey() {
		return this.dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}