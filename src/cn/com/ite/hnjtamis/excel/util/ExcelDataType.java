package cn.com.ite.hnjtamis.excel.util;


public class ExcelDataType {
	private static String className = "";
    public static int getDataType(Object obj)throws Exception{
        
        className = obj.getClass().toString();
        //System.out.println("==="+className);
        if(!"".equals(className) && className != null){
            className = className.substring
            	(className.lastIndexOf(".")+1,className.length());

            if(className.equals("String")){
                return 0;
            }
            else{
                return 1;
            }
        }
        else return 0;
    }
}
