package cn.com.ite.eap2.common.utils;

import java.text.DecimalFormat;

/**
 *<p>Title:com.ite.oxhide.common.util.NumericUtils</p>
 *<p>Description:数字工具类</p>
 *<p>Copyright: Copyright (c) 2007</p>
 *<p>Company: ITE</p>
 * @author 黄勇
 * @version 1.0
 * @date 2007-6-4
 * @modify 
 * @date
 */
public class NumericUtils extends org.apache.commons.lang.math.NumberUtils{
	
	
	/**
	 * 保留精度
	 * @param number 需要保存的数字
	 * @param type 位数
	 * @return 保留后的数字
	 */
	public static double round(double number, int type) {
		DecimalFormat fomater = new DecimalFormat();
		StringBuffer str = new StringBuffer("###");
		if (type > 0) {
			str.append(".");
		}
		for (int i = 1; i <= type; i++) {
			str.append("0");
		}
		fomater.applyPattern(str.toString());
		String strNumber = fomater.format(number);
		return Double.parseDouble(strNumber);
	}
	/**
	 * 保留精度
	 * @param number 需要保存的数字
	 * @param type 位数
	 * @return 保留后的字符串
	 */
	public static String roundToString(double number, int type) {
		DecimalFormat fomater = new DecimalFormat();
		StringBuffer str = new StringBuffer("###");
		if (type > 0) {
			str.append(".");
		}
		for (int i = 1; i <= type; i++) {
			str.append("0");
		}
		fomater.applyPattern(str.toString());
		String strNumber = fomater.format(number);
		return strNumber;
	}

	/**
	 * 四佘六入五成双保留精度
	 * @param number 需要保存的数字
	 * @param type 位数
	 * @return 保留后的数字
	 */
	public static double newRound(double argvalue, int length) {
		double rvalue = argvalue;
		String value = String.valueOf(argvalue);
		String addvalue = "0.";
		String equsevalue = "13579";
		int position = value.indexOf(".");
		String tempStr;

		if (position == -1) {
			return rvalue;
		}
		if (value.substring(position + 1).length() <= length) {
			return rvalue;
		}
		if (length < 0) {
			return rvalue;
		}
		String tempnumber = "0." + value.substring(position + length + 1);
		if (length > 0) {
			tempStr = value.substring(0, position + length + 1);
			for (int i = 0; i < length - 1; i++) {
				addvalue = addvalue + "0";
			}
			addvalue = addvalue + "1";
		} else {
			tempStr = value.substring(0, position);
			addvalue = "1";
		}

		if (Double.parseDouble(tempnumber) < 0.50) {
			return Double.parseDouble(tempStr);
		}
		if (Double.parseDouble(tempnumber) > 0.50) {
			return round(Double.parseDouble(tempStr)
					+ Double.parseDouble(addvalue), length);
		} else {
			char s2 = tempStr.charAt(tempStr.length() - 1);
			if (equsevalue.indexOf(s2) != -1) {
				return round(Double.parseDouble(tempStr)
						+ Double.parseDouble(addvalue), length);
			} else {
				return Double.parseDouble(tempStr);
			}
		}
	}
    /**
     * 判断字符传是否为一个数字
     * @param NUM String
     * @return 是否为一个数字
     */
   boolean isNaN(String NUM) {
        char perchar;
        int i;
        int j = 0;
        for (i = 0; i <= NUM.length() - 1; i++) {
            perchar = NUM.charAt(i);
            if (perchar == '.') {
                j++;
                if (j > 1) {
                    return false;
                }
                continue;
            }
            if (perchar > '9' || perchar < '0') {
                return false;
            }
        }
        return true;
    }

    /**
     * 将数字转化成汉语的大写
     * @param Num String 小写数字
     * @throws Exception
     * @return 大写数字
     */
    public String setState(String Num) throws Exception {
        int i = 0;
        for (i = Num.length() - 1; i >= 0; i--) {
            Num = Num.replaceAll(",", ""); //替换tomoney()中的“,”
            Num = Num.replaceAll(" ", ""); //替换tomoney()中的空格
        }
        Num = Num.replaceFirst("￥", ""); //替换掉可能出现的￥字符

        if (!isNaN(Num)) {
            throw new Exception("请检查小写金额是否输入正确!"); //验证输入的字符是否为数字
        }

        //---字符处理完毕，开始转换，转换采用前后两部分分别转换---//
        String part[];
        part = Num.split("\\.");
        String newchar = "";
        //小数点前进行转化
        for (i = part[0].length() - 1; i >= 0; i--) {
            if (part[0].length() > 10) { //alert("位数过大，无法计算");
                return "";
            }
            //若数量超过拾亿单位，提示
            String tmpnewchar = "";
            char perchar = part[0].charAt(i);
            switch (perchar) {
                case '0':
                    tmpnewchar = "零" + tmpnewchar;
                    break;
                case '1':
                    tmpnewchar = "壹" + tmpnewchar;
                    break;
                case '2':
                    tmpnewchar = "贰" + tmpnewchar;
                    break;
                case '3':
                    tmpnewchar = "叁" + tmpnewchar;
                    break;
                case '4':
                    tmpnewchar = "肆" + tmpnewchar;
                    break;
                case '5':
                    tmpnewchar = "伍" + tmpnewchar;
                    break;
                case '6':
                    tmpnewchar = "陆" + tmpnewchar;
                    break;
                case '7':
                    tmpnewchar = "柒" + tmpnewchar;
                    break;
                case '8':
                    tmpnewchar = "捌" + tmpnewchar;
                    break;
                case '9':
                    tmpnewchar = "玖" + tmpnewchar;
                    break;
            }
            switch (part[0].length() - i - 1) {
                case 0:
                    tmpnewchar = tmpnewchar + "元";
                    break;
                case 1:
                    if (perchar != 0) {
                        tmpnewchar = tmpnewchar + "拾";
                    }
                    break;
                case 2:
                    if (perchar != 0) {
                        tmpnewchar = tmpnewchar + "佰";
                    }
                    break;
                case 3:
                    if (perchar != 0) {
                        tmpnewchar = tmpnewchar + "仟";
                    }
                    break;
                case 4:
                    tmpnewchar = tmpnewchar + "万";
                    break;
                case 5:
                    if (perchar != 0) {
                        tmpnewchar = tmpnewchar + "拾";
                    }
                    break;
                case 6:
                    if (perchar != 0) {
                        tmpnewchar = tmpnewchar + "佰";
                    }
                    break;
                case 7:
                    if (perchar != 0) {
                        tmpnewchar = tmpnewchar + "仟";
                    }
                    break;
                case 8:
                    tmpnewchar = tmpnewchar + "亿";
                    break;
                case 9:
                    tmpnewchar = tmpnewchar + "拾";
                    break;
            }
            newchar =  tmpnewchar+newchar;
        }
        System.out.println(newchar);
        //小数点之后进行转化
        if (Num.indexOf(".") != -1) {
            if (part[1].length() > 2) {
                //alert("小数点之后只能保留两位,系统将自动截段");
                part[1] = part[1].substring(0, 2);
            }
            for (i = 0; i < part[1].length(); i++) {
                String tmpnewchar = "";
                char perchar = part[1].charAt(i);
                switch (perchar) {
                    case '0':
                        tmpnewchar = "零" + tmpnewchar;
                        break;
                    case '1':
                        tmpnewchar = "壹" + tmpnewchar;
                        break;
                    case '2':
                        tmpnewchar = "贰" + tmpnewchar;
                        break;
                    case '3':
                        tmpnewchar = "叁" + tmpnewchar;
                        break;
                    case '4':
                        tmpnewchar = "肆" + tmpnewchar;
                        break;
                    case '5':
                        tmpnewchar = "伍" + tmpnewchar;
                        break;
                    case '6':
                        tmpnewchar = "陆" + tmpnewchar;
                        break;
                    case '7':
                        tmpnewchar = "柒" + tmpnewchar;
                        break;
                    case '8':
                        tmpnewchar = "捌" + tmpnewchar;
                        break;
                    case '9':
                        tmpnewchar = "玖" + tmpnewchar;
                        break;
                }
                if (i == 0) {
                    tmpnewchar = tmpnewchar + "角";
                }
                if (i == 1) {
                    tmpnewchar = tmpnewchar + "分";
                }
                newchar = newchar + tmpnewchar;
            }
        }
    	while(true){
    		int length=newchar.length();
        	newchar= newchar.replace("零仟","零");
        	newchar= newchar.replace("零佰","零");
        	newchar= newchar.replace("零拾","零");
        	newchar= newchar.replace("零零","零");
        	newchar= newchar.replace("零万","万");
        	newchar= newchar.replace("零亿","亿");
        	newchar= newchar.replace("零元","元");
        	newchar= newchar.replace("亿万","亿零");
        	newchar= newchar.replace("万仟","万零");
        	newchar= newchar.replace("仟佰","仟零");
        	if(length==newchar.length())
        		break;
    	}
        newchar = newchar.replace("零角", "零");
        newchar = newchar.replace("零分", "");
        if (newchar.endsWith("元")) {
            newchar = newchar + "整";
        }
        return newchar;
    }
}