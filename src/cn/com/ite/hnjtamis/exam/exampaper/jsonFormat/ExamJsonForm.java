package cn.com.ite.hnjtamis.exam.exampaper.jsonFormat;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamJsonForm</p>
 * <p>Description 考试格式化并生成Json格式类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月17日 上午9:53:24
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamJsonForm {

	
	
	/**
	 * 处理特殊字符转换
	 * @author zhujian
	 * @description
	 * @param s
	 * @return
	 * @modified
	 */
	protected static String stringToJson(String s) {
		if(s==null || "".equals(s)){
			return s;
		}
		
		/*for(int i=0;i<30;i++){
			s = s.replaceAll(" ", "");
			s = s.replaceAll(" ", "");
			s = s.replaceAll(" ", "");
		}*/
		//System.out.println("s = "+s);
	    StringBuffer sb = new StringBuffer ();     
         for (int i=0; i<s.length(); i++) {     
             char c = s.charAt(i);     
             switch (c) {
             case '？':     
                 sb.append("");     
                 break;
             //case '：':     
                // sb.append("\\：");     
                // break;
            // case ':':     
                // sb.append("\\:");     
                // break;
            // case '\'':     
                 //sb.append("\\\'");     
                // break;
            // case '{':     
                // sb.append("\\{");     
                // break;
             //case '}':     
               //  sb.append("\\}");     
                // break;
             //case '｛':     
                // sb.append("\\｛");     
                 //break;
             //case '｝':     
                // sb.append("\\｝");     
                 //break;
             //case ',':     
                 //sb.append("\\,");     
                // break;
             //case '，':     
                 //sb.append("\\，");     
                 //break;
             //case '[':     
                 //sb.append("\\[");     
                // break;
             //case ']':     
                 //sb.append("\\]");     
                 //break;
             //case '\"':     
                 //sb.append("\\\"");     
                // break;     
             //case '\\':   //如果不处理单引号，可以释放此段代码，若结合下面的方法处理单引号就必须注释掉该段代码
                 //sb.append("\\\\");     
                 //break;     
             //case '/':     
                // sb.append("\\/");     
                 //break;     
             //case '\b':      //退格
                // sb.append("\\b");     
                // break;     
            // case '\f':      //走纸换页
               //  sb.append("");     
               //  break;     
             case '\n':     
                 sb.append("<br>"); //换行
             case '\r':   //回车 
                 sb.append("<br>"); //换行
                 break;
           /*case '\r':      //回车
                 sb.append("\\r"); 
                 break;     */    
             //case '\t':      //横向跳格
                // sb.append(" ");     
                // break;
            // case '<':     
                 //sb.append("&lt;");     
                 //break;
             //case '>':     
               //  sb.append("&gt;");     
               //  break;
             default:     
            	if ((c >= 0 && c <= 31)||c ==127){//在ASCⅡ码中，第0～31号及第127号(共33个)是控制字符或通讯专用字符
				}else{
					sb.append(c);
				}
 
             }}
         s = sb.toString();
         //System.out.println("ss = "+s);
         return s;     
      }
}
