package cn.com.ite.workflow.face;

import java.util.Map;

/**
 * <p>Title cn.com.ite.workflow.face.IClient</p>
 * <p>Description 工作流使用者要实现的接口方法</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-30 下午02:15:33
 * @version 2.0
 * 
 * @modified records:
 */
public interface IClient {
	/**
	 * 自动执行方法
	 * @param flowCode 流程编码
	 * @param nodeCode 结点编码
	 * @param param 业务参数
	 * @modified
	 */
	void autoExcute(String flowCode,String nodeCode,Map<String,String> param);
}
