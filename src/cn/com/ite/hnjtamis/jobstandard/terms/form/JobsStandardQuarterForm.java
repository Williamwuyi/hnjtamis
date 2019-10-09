package cn.com.ite.hnjtamis.jobstandard.terms.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;

public class JobsStandardQuarterForm {
	
	private String quarterName;
	private String quarterCode;
	private String type;
	
	/**
	 * 收缩（关闭）图标
	 */
	private String closeIcon;
	/**
	 * 展开（打开 ）图标
	 */
	private String icon;
	
	/**
	 * 复选框，为null表示不带复选框，真假表示复选状态
	 */
	private Boolean checked;
	
	private List quarters = new ArrayList();
	
	
	
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getCloseIcon() {
		return closeIcon;
	}

	public void setCloseIcon(String closeIcon) {
		this.closeIcon = closeIcon;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getQuarterCode() {
		return quarterCode;
	}

	public void setQuarterCode(String quarterCode) {
		this.quarterCode = quarterCode;
	}

	public List getQuarters() {
		return quarters;
	}

	public void setQuarters(List quarters) {
		this.quarters = quarters;
	}
	
	public static boolean select(List<JobsStandardQuarterForm> childs,String term){
		if(StringUtils.isEmpty(term)) return true;
		boolean selected = false;
		for(int i=0;i<childs.size();i++){
			boolean cSelected = false;
			JobsStandardQuarterForm node = childs.get(i);
			if(node.getQuarters()!=null&&node.getQuarters().size()>0)//支条
			   if(select(node.getQuarters(),term))
				   cSelected = true;
			if(node.getQuarterName().indexOf(term)>-1)//匹配的
				cSelected = true;
			if(!cSelected)
				childs.remove(i--);
			else
				selected = true;
		}
		return selected;
	}
}
