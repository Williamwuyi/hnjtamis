package cn.com.ite.hnjtamis.exam.base.examroot;

import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.JsonUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamRoot;
/*
 * 考点管理 维护 --list
 */
public class ExamRootListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = 7278331445187127649L;
	private HttpServletRequest request;
	private String titleTerm;
	private List<ExamRoot> list = new ArrayList<ExamRoot>();
	private List<ExamRoot> queryRootCom = new ArrayList<ExamRoot>();
	/*
	 * 考点 下拉框
	 */
	public String queryRootCom(){
		try {
			queryRootCom = service.queryData("queryRootCom",null,null,ExamRoot.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "queryRootCom";
	}
	/*
	 * 列表查询
	 */
	public String list() throws Exception{
		try {
			list = service.queryData("queryHql", this, null, ExamRoot.class, this.getStart(), this.getLimit());
			this.setTotal(service.countData("queryHql", this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	/*
	 * 删除
	 */
	public String delete() throws Exception{
		try {
			service.deleteByKeys(this.getId().split(","), ExamRoot.class);
			this.setMsg("考点删除成功");
		} catch (Exception e) {
			this.setMsg("考点删除失败");
			e.printStackTrace();
		}
		return "delete";
	}
	
	public void ky(){
		boolean jsonP = false;
		HttpServletResponse response = ServletActionContext.getResponse(); 
		String cb = request.getParameter("jsonPCallback");
		String dataBlock = "{id:'hello',name:'world'}";
		if (cb != null) {
		    jsonP = true;
		    response.setContentType("text/javascript");
		} else {
		    response.setContentType("application/x-json");
		}
		try {
			
			if (jsonP) {
				response.getWriter().write(cb + "(");
			}
			response.getWriter().write(dataBlock);
			if (jsonP) {
				response.getWriter().write(");");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<ExamRoot> getList() {
		return list;
	}

	public void setList(List<ExamRoot> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}
	public List<ExamRoot> getQueryRootCom() {
		return queryRootCom;
	}
	public void setQueryRootCom(List<ExamRoot> queryRootCom) {
		this.queryRootCom = queryRootCom;
	}
	
}
