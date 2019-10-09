package cn.com.ite.hnjtamis.jobstandard.scoreproportion;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.jobstandard.domain.ScoreProportion;
/**
 * 
 * <p>Title 岗位达标培训信息系统-岗位标准设定模块</p>
 * <p>岗位标准得分比例设定 ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  6:12:48 PM
 * @version 1.0
 * 
 * @modified records:
 */
public class ScoreProportionListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<ScoreProportion> list = new ArrayList<ScoreProportion>();
	 
	
	// 查询条件 
	private String nameTerm="";  //  
	 
	
	public String list()throws Exception{
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<ScoreProportion>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		// 
		///service.findDataByKey(key, type)
		service.deleteByKeys(this.getId().split(","), ScoreProportion.class);
		this.setMsg("条款删除成功！");
		return "delete";
	}
	
	 
	
	
	public List<ScoreProportion> getList() {
		return list;
	}
	public void setList(List<ScoreProportion> list) {
		this.list = list;
	}
	 
	public String getNameTerm() {
		//setTypenameTerm("3333");
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	 
}
