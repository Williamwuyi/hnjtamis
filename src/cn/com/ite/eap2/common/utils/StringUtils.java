package cn.com.ite.eap2.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * <p>Title cn.com.ite.eap2.common.utils.StringUtils</p>
 * <p>Description 字符串工具类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 20, 2014 9:53:00 AM
 * @version 2.0
 * 
 * @modified records:
 */
public class StringUtils {
    /**
     * 判断字符串为空
     * @param args 字符串
     * @return true(空) false(非空)
     */
    public static boolean isEmpty(String args) {
        if (args == null || "".equals(args.trim()) || 
        		"null".equals(args.trim().toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串输出(为空也显示）
     * @param args 原字符串
     * @return 字符串
     */
    public static String toString(String args) {
        if (args != null)
            return args;
        else
            return "";
    }

    /**
     * 验证正则表达式判断存在
     * @param regex 正则表达式
     * @param value 字符串
     * @return boolean 是否存在
     */
    public static boolean regex(String regex, String value) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.find();
    }
    /**
     * 正则表达式匹配查询
     * @param regex 匹配表达式
     * @param value 原字符串
     * @return 符合要求的字符串集合
     * @modified
     */
	public static List<String> regexQuery(String regex, String value) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        List<String> vs=new ArrayList<String>();
        int i=0;
        while(m.find()){
        	vs.add(m.group(i));
        	i++;
        }
        return vs;
    }
    
    /**
     * 空格处理，前后空格去掉，中间空格多个变一个
     * @param str 原字符串
     * @return 处理后字符串
     * @modified
     */
    public static String spaceHandler(String src){
        return src.replaceAll("\\s+"," ");
    }
    /**
     * 去除字符串中的空格、回车、换行符、制表符
     * @param str
     * @return
     */
	public static String replaceBlank(String str) {
    	String dest = "";
    	if (str!=null) {
	    	Pattern p = Pattern.compile("\t|\r|\n");
	    	Matcher m = p.matcher(str);
	    	dest = m.replaceAll("");
    	}
    	return dest.replace("\\s*", " ");
    }

    /**
     * 将中文参数转换
     * @param str 字符串
     * @return
     */
    public static String encode(String str) {
        return CharsetSwitchUtil.encode(str);
    }


    /**
     * 将转换后的中文参数重新解码生成正常的中文，与encode成对使用
     * @param str 编码的字符串
     * @return
     */
    public static String decode(String str) {
        return CharsetSwitchUtil.decode(str);
    }

    /**
     * 清除过滤掉 内容中的html标记   一般
     * @param content
     * @return
     */
    public static String filterHtmlTag(String content) {
        boolean flag = true;
        StringBuffer stringBuffer = new StringBuffer(2048);
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '<') {
                flag = false;
                continue;
            }
            if (content.charAt(i) == '>') {
                flag = true;
                continue;
            }
            if (content.charAt(i) == '\n') {
                flag = true;
                continue;
            }
            if (flag) {
                stringBuffer.append(content.charAt(i));
            }
        }
        return stringBuffer.toString().replaceAll("&nbsp;", "").
        replaceAll("&ldquo;", "").replaceAll("&rdquo;", "").replaceAll("\n", "");
    }

    /**
     * 清除过滤掉字符串内容中的html标记,根据正则表达式String   yourRegEx="( <\\s*[a-zA-Z][^> ]*> )|( </\\s*[a-zA-Z][^> ]*> ) ";//这个就是对应的去掉HTML标记的正则表达式
     * @param content 字符串
     * @param regex 正则表达式
     * @return 处理后的字符串
     */
    public static String filterHtmlTagRegex(String content, String regex) {
        if (content == null) content = "";
        String resultstr = " ";
        if (regex == null) regex = "(<\\s*[a-zA-Z][^>]*>)|(</\\s*[a-zA-Z][^>]*>)";//这个就是对应的去掉HTML标记的正则表达式
        try {
            Pattern p = Pattern.compile(regex);//   设置比较模式
            @SuppressWarnings("unused")
			Matcher m = p.matcher(content);
            resultstr = p.matcher(content).replaceAll(resultstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultstr;
    }

    /**
     * 清除过滤掉 内容中的html标记  根据正则表达式
     * @param content 字符串
     * @return 处理后的字符串
     */
    public static String filterHtmlTagRegex(String content) {
        String regex = "(<\\s*[a-zA-Z][^>]*>)|(</\\s*[a-zA-Z][^>]*>)";//这个就是对应的去掉HTML标记的正则表达式
        return filterHtmlTagRegex(content, regex);
    }
    /**
     * ASC码转换
     * @param str 字符串
     * @return ASC码字符串
     */
    public static String native2ascii(String str){
         String tmp;
         StringBuffer sb = new StringBuffer(1000);
         char c;
         int i, j;
         sb.setLength(0);
         for(i = 0;i<str.length();i++){
            c = str.charAt(i);
            if (c > 255) {
                sb.append("\\u");
                j = (c >>> 8);
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1) sb.append("0");
                sb.append(tmp);
                j = (c & 0xFF);
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1) sb.append("0");
                       sb.append(tmp);
             }
             else{
             sb.append(c);
             }
          }
         return new String(sb);
    }
    /**
     * 处理html为纯文本
     * @param inputString HTML字符串
     * @return 纯文件
     */
    public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[s]*?script[^>]*?>[sS]*?<[s]*?/[s]*?script[s]*?>"; // 定义script的正则表达式{或<script [^>]*?>[sS]*?</script>}
			String regEx_style = "<[s]*?style[^>]*?>[sS]*?<[s]*?/[s]*?style[s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[sS]*?</style>}
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		textStr=textStr.replaceAll("&nbsp;", "");//过滤html空格
		return textStr;// 返回文本字符串
	}
    /**
     * 验证手机号
     * @param phone 手机号码
     * @return 是否手机号
     */
    public static boolean validatePhone(String phone){
    	String pattern="^0{0,1}(13|15|18)[0-9]{9}"; 
    	return phone.matches(pattern);
    }
    
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}  
	
	public static String tranEncode(String str,String encode) {   
        try {   
            String strEncode = getEncoding(str);   
            String temp = new String(str.getBytes(strEncode), encode);   
            return temp;   
        } catch (java.io.IOException ex) {
            return null;   
        }   
    }   



    public static void main(String[] args) {    
    	//ÖÐ¹úÈË--ISO-8859-1
    	//�й�-UTF-8
        System.out.println(getEncoding("中国人"));
    }
}