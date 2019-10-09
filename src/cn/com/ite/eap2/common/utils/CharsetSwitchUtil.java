package cn.com.ite.eap2.common.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**

 * <p>Title cn.com.ite.eap2.common.utils.CharsetSwitchUtil</p>
 * <p>Description 字符串编辑转换工具</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:12:06
 * @version 2.0
 * 
 * @modified records:
 */
public final class CharsetSwitchUtil {
    static String classPath = CharsetSwitchUtil.class.toString();

    /**
     * 字符串编码
     * @param str
     * @return
     */
    public static String encode(String str) {
        if (str == null)
            return "";
        int str_length = str.length();
        StringBuffer stringbuffer = new StringBuffer(str_length);
        for (int j = 0; j < str_length; j++) {
            char c;
            if ((c = str.charAt(j)) > '\377') {
                String str1 = Integer.toString(c, 16);
                stringbuffer.append('^');
                for (int k = str1.length(); k < 4; k++)
                    stringbuffer.append('0');

                stringbuffer.append(str1);
            } else if (c < '0' || c > '9' && c < 'A' || c > 'Z' && c < 'a' || c > 'z') {
                String str2 = Integer.toString(c, 16);
                stringbuffer.append('~');
                for (int l = str2.length(); l < 2; l++)
                    stringbuffer.append('0');

                stringbuffer.append(str2);
            } else {
                stringbuffer.append(str.charAt(j));
            }
        }

        return stringbuffer.toString();
    }


    /**
     * 字符串解码
     * @param str
     * @return
     */
    public static String decode(String str) {
        if (str == null)
            return "";
        int str_length = str.length();
        StringBuffer stringbuffer = new StringBuffer();
        for (int j = 0; j < str_length; j++) {
            char c;
            switch (c = str.charAt(j)) {
                case 126: // '~'
                    String str1 = str.substring(j + 1, (j + 4) - 1);
                    stringbuffer.append((char) Integer.parseInt(str1, 16));
                    j += 2;
                    break;

                case 94: // '^'
                    String str2 = str.substring(j + 1, j + 4 + 1);
                    stringbuffer.append((char) Integer.parseInt(str2, 16));
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
     * MD5加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
      return encrypt(password,"MD5");
    }
    
    public static String algorithm_md5 = "MD5";
    public static String algorithm_sha = "SHA";

    /**
     * 按编码类型加密
     * @param text 字符串
     * @param algorithm  MD5|SHA编码类型
     * @return
     */
    public static String encrypt(String text, String algorithm) {
        byte[] unencodedText= text.getBytes();
        MessageDigest md = null;
        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            return text;
        }
        md.reset();
        md.update(unencodedText);       
        byte[] encodedText= md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedText.length; i++) {
            if ((encodedText[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedText[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * BASE64加密
     * @param str
     * @return
     */
    public static String encodeBase64(String str) {
    	Base64 encoder = new Base64();
        return new String(encoder.encode(str.getBytes())).trim();
    }

    /**
     * BASE64解密
     * @param str
     * @return
     */
    public static String decodeBase64(String str) {
    	Base64 dec = new Base64();
    	return new String(dec.decode(str));
    }

    public static void main(String[] args) {
    	String str = "";
        System.out.println("4-----"+decodeBase64(encodeBase64(str))+""+decodeBase64(encodeBase64(str)).length());
    }
}