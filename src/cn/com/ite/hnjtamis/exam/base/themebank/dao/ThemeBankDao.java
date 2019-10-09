package cn.com.ite.hnjtamis.exam.base.themebank.dao;


import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

public interface ThemeBankDao extends DefaultDAO{
	
	/**
	 * 查询专业题库（顶级）
	 * @description
	 * @return
	 * @modified
	 */
	public List<ThemeBank> getProfessionTopThemeBanks();
	
	/**
	 * 更新关联表对应题库的一些信息
	 * @description
	 * @param theme_bank_id
	 * @param bankOrganId
	 * @param bankOrganName
	 * @param bank_public
	 * @param bank_type
	 * @modified
	 */
	public void updateBankInRelation(String theme_bank_id,String bankOrganId,
			String bankOrganName,String bank_public,String bank_type);
	
	
	/**
	 * 删除题库对应的试题关联
	 * @description
	 * @param theme_bank_id
	 * @modified
	 */
	public void deleteThemeInBank(String theme_bank_id);
	
	/**
	 * 获取题库对应有效的试题数目
	 * @description
	 * @param theme_bank_id
	 * @return
	 * @modified
	 */
	public int getThemeNumInBank(String theme_bank_id);

	/**
	 * 获取专业与题库树
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public List<TreeNode> specialityThemeBankTree(Map paramMap)throws Exception;
	
	/**
	 * 查询专业题库
	 * @description
	 * @return
	 * @modified
	 */
	public TreeNode getProThemeBankTree();
	
	/**
	 * 获取题库树
	 * @description
	 * @param topId
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<TreeNode> getThemeBankTree(String topId,Map paramMap);
}
