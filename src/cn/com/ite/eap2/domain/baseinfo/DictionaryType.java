package cn.com.ite.eap2.domain.baseinfo;

import java.util.*;

import cn.com.ite.eap2.domain.funres.AppSystem;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.DictionaryType</p>
 * <p>Description 字典类型</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 上午09:50:03
 * @version 2.0
 * 
 * @modified records:
 */
public class DictionaryType implements java.io.Serializable {
	private static final long serialVersionUID = 1389782430769017712L;
	private String dtId;
	private DictionaryType dictionaryType;
	private String dtName;
	private String dtCode;
	private int sysType;
	private String levelCode;
	private Integer orderNo;
	private String remark;
	private List<DictionaryType> dictionaryTypes = new ArrayList<DictionaryType>(0);
	private List<Dictionary> dictionaries = new ArrayList<Dictionary>(0);
	private List<AppSystem> sysDics = new ArrayList<AppSystem>();

	// Constructors

	/** default constructor */
	public DictionaryType() {
	}

	// Property accessors

	public List<AppSystem> getSysDics() {
		return sysDics;
	}

	public void setSysDics(List<AppSystem> sysDics) {
		this.sysDics = sysDics;
	}

	public String getDtId() {
		return this.dtId;
	}

	public void setDtId(String dtId) {
		this.dtId = dtId;
	}

	public DictionaryType getDictionaryType() {
		return this.dictionaryType;
	}

	public void setDictionaryType(DictionaryType dictionaryType) {
		this.dictionaryType = dictionaryType;
	}

	public String getDtName() {
		return this.dtName;
	}

	public void setDtName(String dtName) {
		this.dtName = dtName;
	}

	public String getDtCode() {
		return this.dtCode;
	}

	public void setDtCode(String dtCode) {
		this.dtCode = dtCode;
	}

	public int getSysType() {
		return this.sysType;
	}

	public void setSysType(int sysType) {
		this.sysType = sysType;
	}

	public String getLevelCode() {
		return this.levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public Integer getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<DictionaryType> getDictionaryTypes() {
		return this.dictionaryTypes;
	}

	public void setDictionaryTypes(List<DictionaryType> dictionaryTypes) {
		this.dictionaryTypes = dictionaryTypes;
	}

	public List<Dictionary> getDictionaries() {
		return this.dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
}