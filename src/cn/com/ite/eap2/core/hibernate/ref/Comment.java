package cn.com.ite.eap2.core.hibernate.ref;

/**
 * <p>Title cn.com.ite.eap2.core.hibernate.ref.Comment</p>
 * <p>Description HBM注释</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-18 上午11:08:40
 * @version 2.0
 * 
 * @modified records:
 */
public class Comment {
	/**
	 * 实体注释
	 */
	public static int TYPE_ENTITY = 1;
	/**
	 * 属性注释
	 */
	public static int TYPE_FIELD = 2;
	public static int FIELD_TYPE_ID = 1;
	public static int FIELD_TYPE_FID = 2;//外健
	public static int FIELD_TYPE_PID = 3;//父外健
	public static int FIELD_TYPE_TITLE = 4;
	public static int FIELD_TYPE_CREATEID = 5;//创建者
	public static int FIELD_TYPE_CREATER = 6;
	public static int FIELD_TYPE_CREATETIME = 7;
	public static int FIELD_TYPE_MODIFYID = 8;//修改者
	public static int FIELD_TYPE_MODIFYER = 9;
	public static int FIELD_TYPE_MODIFYTIME = 10;
	public static int FIELD_TYPE_IP = 11;//IP地址
	public static int FIELD_TYPE_GRGAN = 12;
	public static int FIELD_TYPE_DEPT = 13;
	public static int FIELD_TYPE_STATE = 14;//数据状态，0新增1修改2删除
	public static int FIELD_TYPE_LEVELCODE = 15;
	public static int FIELD_TYPE_ORDER = 16;
	public static String[] ENTITY_ITEM = {"FOREIGN","IS_LOGIC_DELETE","IS_LOG","TITLE_PROPERTY"};
	public static String[] FIELD_ITEM = {"TYPE","IS_NULL_DELETE","IS_UNIQUE"};
	/**
	 * 类型，对应以上两种类型
	 */
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
