package cn.com.ite.hnjtamis.exam.examControl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;

/**
 * 模拟考试处理
 * <p>Title cn.com.ite.hnjtamis.exam.examControl.MoniExamControlMain</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年10月27日 上午9:02:51
 * @version 1.0
 * 
 * @modified records:
 */
public class MoniExamControlMain extends ExamControlMain{

	private static HttpServletRequest http_request = null;
	
	private static String moniExamUserFilePath = null;//试题路径
	
	private static String moniExamUserAnsFilePath = null;//试题路径
	
	private static String[][]  moniExamTitleInRAM = null;//存放试题到内存
	
	private static int moniExamTitleInRAMLen = 0;//指定数组存放的位置
	
	private static int maxMoniExamTitleInRAMLen = 300;//内存被认为良好时，数组最大可开辟的数量

	public static void setMaxMoniExamTitleInRAMLen(int _maxMoniExamTitleInRAMLen) {
		maxMoniExamTitleInRAMLen = _maxMoniExamTitleInRAMLen;
	}

	
	/**
	 * 从内存中获取试题
	 * @description
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public static String getMoniExamTitleInRAM(String examTestpaperId){
		String examContent = null;
		try{
			if(moniExamTitleInRAM!=null){
				for(int i=0;i<moniExamTitleInRAM.length;i++){
					if(moniExamTitleInRAM[i] == null){
						continue;
					}
					if(moniExamTitleInRAM[i][0] == null){
						break;
					}else if(moniExamTitleInRAM[i][0] == examTestpaperId){
						examContent = moniExamTitleInRAM[i][1];
						break;
					}
				}
			}
		}catch(Exception e){
			examContent = null;
		}
		return examContent;
	}
	
	/**
	 * 从内存中获取考生答案
	 * @description
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public static String getMoniExamUserAnsInRAM(String examTestpaperId){
		String examContent = null;
		try{
			if(moniExamTitleInRAM!=null){
				for(int i=0;i<moniExamTitleInRAM.length;i++){
					if(moniExamTitleInRAM[i] == null){
						continue;
					}
					if(moniExamTitleInRAM[i][0] == null){
						break;
					}else if(moniExamTitleInRAM[i][0] == examTestpaperId){
						examContent = moniExamTitleInRAM[i][2];
						break;
					}
				}
			}
		}catch(Exception e){
			examContent = null;
		}
		return examContent;
	}
	
	/**
	 * 设置试题到内存
	 * @description
	 * @param examTestpaperId
	 * @param examContent
	 * @modified
	 */
	public static void setMoniExamTitleInRAM(String examTestpaperId,String examContent){
		int remIsBetterQz = remIsBetter();
		if(remIsBetterQz>0){
			if(moniExamTitleInRAM == null){
				moniExamTitleInRAM = new String[maxMoniExamTitleInRAMLen*remIsBetterQz][3];
				moniExamTitleInRAMLen = 0;
			}
			moniExamTitleInRAM[moniExamTitleInRAMLen] = null;
			moniExamTitleInRAM[moniExamTitleInRAMLen] = new String[]{examTestpaperId,examContent,null};
			moniExamTitleInRAMLen++;
			if(moniExamTitleInRAMLen>=moniExamTitleInRAM.length){
				moniExamTitleInRAMLen = 0;
			}
		}else{
			moniExamTitleInRAM = null;
			moniExamTitleInRAMLen = 0;
		}
	}
	

	/**
	 * 设置试题到内存
	 * @description
	 * @param examTestpaperId
	 * @param examContent
	 * @modified
	 */
	public static void setMoniExamUserAnsInRAM(String examTestpaperId,String serAns){
		if(moniExamTitleInRAM!=null){
			for(int i=0;i<moniExamTitleInRAM.length;i++){
				if(moniExamTitleInRAM[i] == null){
					continue;
				}
				if(moniExamTitleInRAM[i][0] == null){
					break;
				}else if(moniExamTitleInRAM[i][0] == examTestpaperId){
					moniExamTitleInRAM[i][2] = serAns;
					break;
				}
			}
		}
	}
	
	/**
	 * 设置试题到内存
	 * @description
	 * @param examTestpaperId
	 * @param examContent
	 * @modified
	 */
	public static void cleanMoniExamUserAnsInRAM(String examTestpaperId){
		if(moniExamTitleInRAM!=null){
			for(int i=0;i<moniExamTitleInRAM.length;i++){
				if(moniExamTitleInRAM[i] == null){
					continue;
				}
				if(moniExamTitleInRAM[i][0] == null){
					break;
				}else if(moniExamTitleInRAM[i][0] == examTestpaperId){
					moniExamTitleInRAM[i] = null;
					break;
				}
			}
		}
	}
	
	/**
	 * 从文件中获取试题
	 * @description
	 * @param request
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public static String getMoniExamTitleInFile(HttpServletRequest request,String examTestpaperId){
		String examContent = getMoniExamTitleInRAM(examTestpaperId);
		if(examContent == null){
			if(moniExamUserFilePath == null){
				if(request == null && http_request!=null){
					request = http_request;
				}
				if(http_request == null && request!=null){
					http_request = request;
				}
				moniExamUserFilePath = ExamVariable.getMoniExamFilePath(request);
			}
			String filePath = moniExamUserFilePath+System.getProperty("file.separator")+"title_"+examTestpaperId+".txt";
			File file = new File(filePath);
			if(file.exists() && file.isFile()){
				examContent = FileOption.readFile(file);
				setMoniExamTitleInRAM(examTestpaperId,examContent);
			}
		}
		return examContent;
	}
	
	/**
	 * 从文件中获取考生答案
	 * @description
	 * @param request
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public static String getMoniExamUserAnsInFile(HttpServletRequest request,String examTestpaperId){
		String userAns = getMoniExamUserAnsInRAM(examTestpaperId);
		if(userAns == null){
			if(moniExamUserAnsFilePath == null){
				if(request == null && http_request!=null){
					request = http_request;
				}
				if(http_request == null && request!=null){
					http_request = request;
				}
				moniExamUserAnsFilePath = ExamVariable.getMoniExamUserAnsFilePath(request);
			}
			String filePath = moniExamUserAnsFilePath+System.getProperty("file.separator")+"userans_"+examTestpaperId+".txt";
			File file = new File(filePath);
			if(file.exists() && file.isFile()){
				userAns = FileOption.readFile(file);
				setMoniExamUserAnsInRAM(examTestpaperId,userAns);
			}
		}
		return userAns;
	}
	
	/**
	 * 写入文件到试题中去
	 * @description
	 * @param request
	 * @param examTestpaperId
	 * @param examContent
	 * @modified
	 */
	public static void saveMoniExamTitleToFile(HttpServletRequest request,String examTestpaperId,String examContent){
		setMoniExamTitleInRAM(examTestpaperId,examContent);
		if(moniExamUserFilePath == null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			moniExamUserFilePath = ExamVariable.getMoniExamFilePath(request);
		}
		String filePath = moniExamUserFilePath+System.getProperty("file.separator")+"title_"+examTestpaperId+".txt";
		FileOption.saveString(filePath, examContent);
	}
	
	
	/**
	 * 写入文件到试题中去
	 * @description
	 * @param request
	 * @param examTestpaperId
	 * @param examContent
	 * @modified
	 */
	public static void saveMoniExamUserAnsToFile(HttpServletRequest request,String examTestpaperId,String userAns){
		setMoniExamUserAnsInRAM(examTestpaperId,userAns);
		if(moniExamUserAnsFilePath == null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			moniExamUserAnsFilePath = ExamVariable.getMoniExamUserAnsFilePath(request);
		}
		String filePath = moniExamUserAnsFilePath+System.getProperty("file.separator")+"userans_"+examTestpaperId+".txt";
		FileOption.saveString(filePath, userAns);
	}
	
	
	/**
	 * 写入文件到试题中去
	 * @description
	 * @param request
	 * @param examTestpaperId
	 * @param examContent
	 * @modified
	 */
	public static void delMoniExamUserAnsFile(HttpServletRequest request,String examTestpaperId){
		cleanMoniExamUserAnsInRAM(examTestpaperId);
		if(moniExamUserAnsFilePath == null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			moniExamUserAnsFilePath = ExamVariable.getMoniExamUserAnsFilePath(request);
		}
		String filePath = moniExamUserAnsFilePath+System.getProperty("file.separator")+"userans_"+examTestpaperId+".txt";
		File file = new File(filePath);
		if(file.exists() && file.isFile()){
			file.delete();
		}
	}
}
