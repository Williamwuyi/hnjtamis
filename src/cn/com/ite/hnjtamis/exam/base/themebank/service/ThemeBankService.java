package cn.com.ite.hnjtamis.exam.base.themebank.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemePostForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

public interface ThemeBankService extends DefaultService{
	
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
	 *
	 * @author zhujian
	 * @description 审核人导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importThemeBank(File xls,UserSession usersess,String bankType)throws Exception;
	
	/*
	 * 保存题库
	 */
	public void saveThemeBank(ThemeBank bo) throws Exception;
	/*
	 * 保存排序
	 */
	public void saveSort(String[] orders) throws Exception;
	/*
	 * 保存题库的岗位
	 */
	public void savePosts(ThemeBank po,List<ThemePostForm> themePostFormList,UserSession usersess);
	/*
	 * 保存题库的专业
	 */
	public void savePros(ThemeBank po,List<Speciality> specialitys,UserSession usersess);
	/*
	 * 删除题库关联的岗位
	 */
	public void deletePosts(ThemeBank po);
	public void deletePosts(String[] po);
	/*
	 * 删除题库关联的专业
	 */
	public void deletePros(ThemeBank po);
	public void deletePros(String[] po);
	
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
	
	/**
	 *
	 * @author zhujian
	 * @description 获取每个试题的数目
	 * @return
	 * @modified
	 */
	public Map getThemeNumMap();
}
