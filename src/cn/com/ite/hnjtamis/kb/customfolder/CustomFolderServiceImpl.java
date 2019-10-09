package cn.com.ite.hnjtamis.kb.customfolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.kb.domain.CustomFolder;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.customfolder.CustomFolderServiceImpl
 * </p>
 * <p>
 * Description 自定义文件夹服务实现
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-30
 * @version 1.0
 * 
 * @modified
 */
public class CustomFolderServiceImpl extends DefaultServiceImpl implements
		CustomFolderService {
	/**
	 * 自定义文件夹树
	 * 
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked"})
	public List<TreeNode> findFolderTree(String createUserId) throws Exception {
		Map<String, String> term = new HashMap<String, String>();
		term.put("createUserId", createUserId);
		List<TreeNode> list = (List<TreeNode>) getDao().queryConfigQl(
				"queryFTreeHql", term, null, TreeNode.class);
		Map<String, TreeNode> ms = new HashMap<String, TreeNode>();
		for (TreeNode node : list) {
			node.setType("folder");
			if (!ms.containsKey(node.getId()))
				ms.put(node.getId(), node);
			TreeNode f = node;
			while (!StringUtils.isEmpty(f.getParentId())) {
				CustomFolder folder = (CustomFolder) getDao().findEntityBykey(
						CustomFolder.class, f.getParentId());
				if (folder != null) {
					TreeNode newNode = TreeNode.objectToTree(folder,
							"id", "parentFolder.id", "name");
					newNode.setType("folder");
					if (!ms.containsKey(newNode.getId()))
						ms.put(newNode.getId(), newNode);
					f = newNode;
				} else
					break;
			}
		}
		list.clear();
		list.addAll(ms.values());
		TreeNode.putTypeIncon("folder", "resources/icons/fam/folder_go.gif", "");
		List leafTypes = new ArrayList();
		leafTypes.add("folder");
		return TreeNode.toTree(list, true, leafTypes);
	}
}
