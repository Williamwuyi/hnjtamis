package cn.com.ite.eap2.module.baseinfo.friendshiplink;

import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.FriendshipLink;

/**
 * <p>Title cn.com.ite.eap2.module.baseinfo.friendshiplink.FslListAction</p>
 * <p>Description 友情链结ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-12 时间09:14:50
 * @version 2.0
 * 
 * @modified records:
 */
public class FslListAction extends AbstractListAction{
	private static final long serialVersionUID = -7244753777816555403L;
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	/**
	 * 查询结果
	 */
	private List<FriendshipLink> list;
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		list = (List<FriendshipLink>)service.queryData("queryHql", this,this.getSortMap(),
				this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), FriendshipLink.class);
		this.setMsg("系统公告删除成功！");
		return "delete";
	}
	public List<FriendshipLink> getList() {
		return list;
	}
	public void setList(List<FriendshipLink> list) {
		this.list = list;
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
}
