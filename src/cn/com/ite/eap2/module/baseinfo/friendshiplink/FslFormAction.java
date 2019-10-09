package cn.com.ite.eap2.module.baseinfo.friendshiplink;


import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.baseinfo.FriendshipLink;

/**
 * <p>Title cn.com.ite.eap2.module.baseinfo.friendshiplink.FslFormAction</p>
 * <p>Description 友情链结FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-12 时间09:15:50
 * @version 2.0
 * 
 * @modified records:
 */
public class FslFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061920327425264872L;
	/**
	 * 友情链结
	 */
	private FriendshipLink form;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (FriendshipLink)service.findDataByKey(this.getId(), FriendshipLink.class);
		return "find";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String save() throws Exception{
		form = (FriendshipLink)this.jsonToObject(FriendshipLink.class);
		service.save(form);
		return "save";
	}
	public FriendshipLink getForm() {
		return form;
	}

	public void setForm(FriendshipLink form) {
		this.form = form;
	}
}