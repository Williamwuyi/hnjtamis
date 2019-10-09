package cn.com.ite.hnjtamis.common;


import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 数据签名
 * @create time: 2016年2月2日 下午2:27:38
 * @version 1.0
 * 
 * @modified records:
 */
public class Signature {

	
	public static boolean check(String signature,String timetamp,String nonce,String token){
		if(signature!=null && signature.equals(getSignature(timetamp,nonce,token))){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 获取签名
	 * @param timetamp
	 * @param nonce
	 * @param token
	 * @return
	 * @modified
	 */
	public static String getSignature(String timetamp,String nonce,String token){
		List<String> list = new ArrayList<String>();
		list.add(timetamp);
		list.add(nonce);
		list.add(token);
		return getSignatureCode(list);
	}
	
	
	/**
	 * 获取数据签名
	 * @author 朱健
	 * @param list 数据list
	 * @return
	 * @modified
	 */
	public static String getSignatureCode(List<String> list){
		String s = "";
		//先从大到小排序
		if(list!=null && list.size()>0){
			if(list.size()>1){
				Collections.sort(list, new Comparator<String>() {
		 			public int compare(String o1, String o2) {
		 				return o1.compareTo(o2);
		 			}
		 		});
			}
			for(int i=0;i<list.size();i++){
				s+=list.get(i);
			}
			//System.out.println("  S = "+s);
			s = encrypt(s);
		}
		return s;
	}
	
	
	
	/**
     * SHA编码类型加密
     * @param text 字符串
     * @return
     */
	private static String encrypt(String text) {
		if(text!=null){
			String algorithm_sha = "SHA";
	        byte[] unencodedText= text.getBytes();
	        MessageDigest md = null;
	        try {
	            // first create an instance, given the provider
	            md = MessageDigest.getInstance(algorithm_sha);
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
		}else{
			return text;
		}
    }
	
	public static void main(String args[]){
		System.out.println("SHA = "+Signature.getSignature("s3a", "3da", "1aa"));
	}
}
