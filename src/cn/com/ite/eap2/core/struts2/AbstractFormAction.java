package cn.com.ite.eap2.core.struts2;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import cn.com.ite.eap2.common.utils.JsonUtils;
import cn.com.ite.eap2.domain.baseinfo.Accessory;

/**
 * <p>Title cn.com.ite.eap2.core.struts2.AbstractFormAction</p>
 * <p>Description 抽象FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-12 下午03:14:07
 * @version 2.0
 * 
 * @modified records:
 */
public abstract class AbstractFormAction extends AbstractAction implements Serializable{
	private static final long serialVersionUID = 6359528539821051083L;
	/**
	 * 前台传过来JSON数据
	 */
	@SuppressWarnings("unused")
	private String json="";
	/**
	 * 提交的文件，由于json无法处理文件
	 */
	private Map<String,File> fileMap = new HashMap<String,File>();
	
	/**
	 * 排序的ID数组
	 */
	private String[] sortIds;
	/**
	 * 设置JSON
	 * @param json
	 * @modified
	 */
	public void setJson(String json) {
		this.json = json;
	}
	/**
	 * 把JSON数据转换成指定类型的对象
	 * @param clazz
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public Object jsonToObject(Class clazz) throws Exception{
		Object src = JsonUtils.fromWebJson("json", clazz);
		//附件处理
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();  
        for(int i=0; i<fields.length; i++){  
    	    java.lang.reflect.Field f = fields[i]; 
    	    f.setAccessible(true);
    	    Object v = f.get(src);
    	    if(v instanceof java.util.Collection){
    	    	Iterator iterator = ((java.util.Collection)v).iterator();
    	    	int j = 0;
    	    	boolean isFile = false;
    	    	while(iterator.hasNext()){
    	    		Object item = iterator.next();
    	    		if(isFile||item instanceof Accessory){
    	    			isFile = true;
    	    			File file = (File)this.getFileMap().get(f.getName()+((Accessory)item).getOrderNo());
	    				if(file!=null){
	    					((Accessory)item).saveFile(file);
	    				}
	    				if(((Accessory)item).getUploadDate()==null)
	    					((Accessory)item).setUploadDate(new Date(System.currentTimeMillis()));
	    				j++;
	    				((Accessory)item).setOrderNo(j);
    	    		}
    	    	}
    	    }
        }
		return src;
	}
	public Map<String, File> getFileMap() {
		return fileMap;
	}
	public void setFileMap(Map<String, File> fileMap) {
		this.fileMap = fileMap;
	}
	public String[] getSortIds() {
		return sortIds;
	}
	public void setSortIds(String[] sortIds) {
		this.sortIds = sortIds;
	}
}