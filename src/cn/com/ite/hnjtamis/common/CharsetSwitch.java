package cn.com.ite.hnjtamis.common;

import java.io.UnsupportedEncodingException;

/**
 * <p>Title com.ite.fuel.common.CharsetSwitch</p>
 * <p>Description 处理中英文字符集转换 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2010</p>
 * @author 朱健
 * @create time: 2010-8-25 上午09:35:12
 * @version 1.0
 * 
 * @modified records:
 */
public class CharsetSwitch {

    /**
     * 将Unicode字符转换为GBK
     * 主要用于解决中文乱码显示
     * @param unicodeString Unicode字符串
     * @return String 中文字符串
     */
    public static String UnicodeToChinese(String unicodeString){
        try{
            if(unicodeString==null || "".equals(unicodeString)) return "";
            String newstring=null;
            newstring=new String(unicodeString.getBytes("ISO8859_1"),"GBK");
            return newstring;
        }
        catch(UnsupportedEncodingException e){
            return unicodeString;
        }
    }

    /**
     * 将中文字符集转换为Unicode码.
     * @param chineseString 中文字符串
     * @return String Unicode字符串 
     */
    public static String ChineseToUnicode(String chineseString){
        try{
            if(chineseString==null|| "".equals(chineseString))
                return "";
            String newstring=null;
            newstring=new String(chineseString.getBytes("GBK"),"ISO8859_1");
            return newstring;
        }
        catch(UnsupportedEncodingException e){
            return chineseString;
        }
    }

    /**
     * 对字符串进行安全性编码，消除如中文、特殊字符在数据传递过程中的乱码问题，
     * 如通过URL传递中文名称参数是就会遇到接收端出现乱码的问题，可以用此方法解决。
     * @param s
     * @return 返回安全性编码的字符串
     */
    public static  String encode(String s){
                if(s == null)
                    return "";
                int i = s.length();
                StringBuffer stringbuffer = new StringBuffer(i);
                for(int j = 0; j < i; j++){
                    char c;
                    if((c = s.charAt(j)) > '\377')
                    {
                        String s1 = Integer.toString(c, 16);
                        stringbuffer.append('^');
                        for(int k = s1.length(); k < 4; k++)
                            stringbuffer.append('0');

                        stringbuffer.append(s1);
                    } else if(c < '0' || c > '9' && c < 'A' || c > 'Z' && c < 'a' || c > 'z'){
                        String s2 = Integer.toString(c, 16);
                        stringbuffer.append('~');
                        for(int l = s2.length(); l < 2; l++)
                            stringbuffer.append('0');

                        stringbuffer.append(s2);
                    } else{
                        stringbuffer.append(s.charAt(j));
                    }
                }

                return stringbuffer.toString();
            }

    /**
     *  对安全性编码了的字符串进行解码处理，还原字符串原始特征。
     * @param s
     * @return  原始字符串
     */
    public static String decode(String s){
            if(s == null)
                return "";
            int i = s.length();
            StringBuffer stringbuffer = new StringBuffer();
            for(int j = 0; j < i; j++){
                char c;
                switch(c = s.charAt(j)){
                case 126: // '~'
                    String s1 = s.substring(j + 1, (j + 4) - 1);
                    stringbuffer.append((char)Integer.parseInt(s1, 16));
                    j += 2;
                    break;

                case 94: // '^'
                    String s2 = s.substring(j + 1, j + 4 + 1);
                    stringbuffer.append((char)Integer.parseInt(s2, 16));
                    j += 4;
                    break;

                default:
                    stringbuffer.append(c);
                    break;
                }
            }

            return stringbuffer.toString();
        }

    /**
	 *
	 * @author zhujian
	 * @description 全部转换成大写
	 * @param str
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public static String CharacterUpperCase(String str) throws Exception {
		if(str==null || "".equals(str))return str;
		char a[] = str.toCharArray();
		try {

			for (int i = 0; i < a.length; i++) {
				if (Character.isLowerCase(a[i])) {
					a[i] = Character.toUpperCase(a[i]);
				} else if (Character.isUpperCase(a[i])) {
					//a[i] = Character.toLowerCase(a[i]);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return new String(a);
	} 
	
	/**
	 *
	 * @author zhujian
	 * @description 全部转换成小写
	 * @param str
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public static String CharacterLowerCase(String str) throws Exception {
		if(str==null || "".equals(str))return str;
		char a[] = str.toCharArray();
		try {

			for (int i = 0; i < a.length; i++) {
				if (Character.isLowerCase(a[i])) {
					//a[i] = Character.toUpperCase(a[i]);
				} else if (Character.isUpperCase(a[i])) {
					a[i] = Character.toLowerCase(a[i]);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return new String(a);
	}

}
