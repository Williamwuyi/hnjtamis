package cn.com.ite.hnjtamis.exam.study;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cn.com.ite.eap2.core.struts2.AbstractFormAction;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.study.StudyThemeFormAction</p>
 * <p>Description 在线学习试题</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年9月1日 下午2:19:20
 * @version 1.0
 * 
 * @modified records:
 */
public class StudyThemeFormAction extends AbstractFormAction implements  ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = 5603052027990449708L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	@Override
	public void setServletResponse(HttpServletResponse httpservletresponse) {
		this.response = httpservletresponse;
	}

	

}