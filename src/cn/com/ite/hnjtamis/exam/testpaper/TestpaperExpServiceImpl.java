package cn.com.ite.hnjtamis.exam.testpaper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.doc.ExportDocServiceImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperExpServiceImpl</p>
 * <p>Description 导出试卷</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月5日 上午10:41:10
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperExpServiceImpl extends ExportDocServiceImpl {
	
	
	

	/**
	 * @author 朱健
	 * @description
	 * @param document
	 * @modified
	 */
	protected void setDocument(Document document,Object form) throws Exception{
		 Map value = (Map)form;
		 Testpaper testpaper = (Testpaper)value.get("testpaper");
		 String showRight = (String)value.get("showRight");
		 String basepath=(String)value.get("basepath");
		 List<TestpaperTheme> testpaperthemeList = (List<TestpaperTheme>)value.get("testpaperthemeList");
		 Map<String,ThemeType> themeTypeMap = (Map<String,ThemeType> )value.get("themeTypeMap");
		 
		 BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",  "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体            
		  Font titleFont1 = new Font(bfChinese, 19, Font.BOLD);  //标题字体风格
		  Font titleFont2 = new Font(bfChinese, 16, Font.BOLD);  //标题字体风格    
		  Font contextFont = new Font(bfChinese, 13, Font.NORMAL);//正文字体风格       
	
		  document.add(new Paragraph(""));
		  
		  Paragraph title = new Paragraph(testpaper.getTestpaperName());
		  title.setAlignment(Element.ALIGN_CENTER); //设置标题格式对齐方式   
	      title.setFont(titleFont1); 
		  document.add(title); 

		  ThemeType oldthemeType = null;
		  int titleIndex = 0;
		  for(int k=0;k<testpaperthemeList.size();k++){
			  TestpaperTheme testpaperTheme = testpaperthemeList.get(k);  
			  
			  if(oldthemeType == null || oldthemeType.getThemeTypeId()!= testpaperTheme.getThemeType().getThemeTypeId()){
				  oldthemeType = themeTypeMap.get(testpaperTheme.getThemeType().getThemeTypeId());
				  Paragraph contextThemeType = new Paragraph(StaticVariable.numberSort[titleIndex]+"、"+oldthemeType.getThemeTypeName(),titleFont2);
				  contextThemeType.setAlignment(Element.ALIGN_LEFT);//正文格式左对齐     
				  contextThemeType.setFont(contextFont);       	       
				  contextThemeType.setSpacingBefore(0); //离上一段落（标题）空的行数             
				  contextThemeType.setFirstLineIndent(0);//设置第一行空的列数
				  document.add(contextThemeType);
				  titleIndex++;
			  }
			  
			  
			  
			 // for(int i = 0 ;i<testpaper.getTestpaperThemes().size();i++){
				  //TestpaperTheme testpaperTheme = testpaper.getTestpaperThemes().get(i);
				 // if(themeType.getThemeTypeId().equals(testpaperTheme.getThemeType().getThemeTypeId())){
					  String themeContext = (k+1)+"、"+replaceToHtml(testpaperTheme.getThemeName());
					  if(testpaperTheme.getDefaultScore()!=null){
						  themeContext+="（"+testpaperTheme.getDefaultScore()+"分）";
					  }
					 
					  if(testpaperTheme.getThemeType().getThemeType()!=null){
						  int themeInType = Integer.parseInt(testpaperTheme.getThemeType().getThemeType());
						  int eachline = testpaperTheme.getEachline()==null?1:testpaperTheme.getEachline().intValue();
						  if(themeInType == 5  || themeInType == 10  || themeInType == 25){	
							  themeContext+="<br>";
						  }
						  if(testpaperTheme.getExplain()!=null && !"".equals(testpaperTheme.getExplain())&& !"null".equals(testpaperTheme.getExplain())){
							 if(themeInType == 5  || themeInType == 10  || themeInType == 25){	
								 themeContext+="注解："+replaceToHtml(testpaperTheme.getExplain())+"<br>";	 
							 }else{
								 themeContext+="<br>注解："+replaceToHtml(testpaperTheme.getExplain())+"";  
							 }
						  }
						   
						  
						  if(themeInType == 5  || themeInType == 10  || themeInType == 25){		  
							  String rightThemeContext= "";
							  for(int j = 0;j<testpaperTheme.getTestpaperThemeAnswerkeies().size();j++){
								  TestpaperThemeAnswerkey testpaperThemeAnswerkey = testpaperTheme.getTestpaperThemeAnswerkeies().get(j);
								  //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
								  if(themeInType == 5  || themeInType == 10  || themeInType == 25){
									  themeContext += StaticVariable.themeAnsSort[j]+"、"+replaceToHtml(testpaperThemeAnswerkey.getAnswerkeyValue());	
									  if(testpaperThemeAnswerkey.getIsRight()!=null && testpaperThemeAnswerkey.getIsRight().intValue() == 10){
										  rightThemeContext+=StaticVariable.themeAnsSort[j]+"、";
									  }
									  if(j == testpaperTheme.getTestpaperThemeAnswerkeies().size()-1 ){
										  
									  }else if(eachline == 1  || (eachline>0 && j>0 && (j+1)%(eachline)==0)){
										  themeContext +="<br>";
									  }else{
										  themeContext +="&nbsp;&nbsp;&nbsp;&nbsp;";
									  }
								  }else{
									  themeContext +="<br>";
								  }
							  }
							  if("true".equals(showRight)){
								  themeContext +="<br><b>正确答案：</b>"+(rightThemeContext.length()>0?rightThemeContext.substring(0,rightThemeContext.length()-1):"无"); 
							  }
						   }else if(themeInType == 15){
							   int rplen = 8;
							   while(themeContext.indexOf("()")!=-1 && rplen>0){
								   themeContext = themeContext.replace("()", "＿＿＿＿＿＿＿＿"); 
								   rplen--;
							   }
							   rplen = 8;
							   while(themeContext.indexOf("（）")!=-1 && rplen>0){
								   themeContext = themeContext.replace("（）", "＿＿＿＿＿＿＿＿");
								   rplen--;
							   }
							   
							   if("true".equals(showRight)){
								   String rightThemeContext= "";
								   for(int j = 0;j<testpaperTheme.getTestpaperThemeAnswerkeies().size();j++){
									  TestpaperThemeAnswerkey testpaperThemeAnswerkey = testpaperTheme.getTestpaperThemeAnswerkeies().get(j);
									  //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
									  rightThemeContext += replaceToHtml(testpaperThemeAnswerkey.getAnswerkeyValue())+"&nbsp;&nbsp;&nbsp;&nbsp;";
								   }
								   themeContext +="<br><b>参考答案：</b>"+(rightThemeContext.length()>0?rightThemeContext:"无"); 
								}
						   }else{
							   themeContext +="<br><br><br><br><br>";  
							   if("true".equals(showRight)){
								   String rightThemeContext= "";
								   for(int j = 0;j<testpaperTheme.getTestpaperThemeAnswerkeies().size();j++){
									  TestpaperThemeAnswerkey testpaperThemeAnswerkey = testpaperTheme.getTestpaperThemeAnswerkeies().get(j);
									  //类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
									  rightThemeContext += replaceToHtml(testpaperThemeAnswerkey.getAnswerkeyValue())+"&nbsp;&nbsp;&nbsp;&nbsp;";
								   }
								   themeContext +="<br><b>参考答案：</b>"+(rightThemeContext.length()>0?rightThemeContext:"无"); 
								}
						   }
					  }
					  List strlist = new ArrayList();
					  String str = themeContext;
				      int index = str.indexOf("<img");
					  while(index!=-1 && str.length()>0){
							String tmp = str.substring(0,index);
							strlist.add(tmp);
							str = str.substring(index, str.length());
							index = str.indexOf(">");
							tmp = str.substring(0,index+1);
							strlist.add(tmp);
							str = str.substring(index+1, str.length());
							if(str.length()==0)break;
							index = str.indexOf("<img");
					  }
					  if(str.length()>0) strlist.add(str);
						
					  List<File> imagelist = new ArrayList<File>();
					  List<String> imageTitlelist =  new ArrayList<String>();
					  StringBuffer sbf = new StringBuffer();
					  for(int tt=0;tt<strlist.size();tt++){
							String tmp = (String)strlist.get(tt);
							if(tmp.indexOf("<img")!=-1){
								index = tmp.indexOf("src=\"upload");
								tmp = tmp.substring(index+11,tmp.length());
								index = tmp.indexOf("\"");
								tmp = tmp.substring(0,index);
								//System.out.println(basepath+tmp);
								File file= new File(basepath+tmp);
								imagelist.add(file);
								String imageTitle = "见下图"+(k+1)+"-"+(tt+1)+"";
								sbf.append(imageTitle);
								imageTitlelist.add("图"+(k+1)+"-"+(tt+1)+"");
							}else{
								sbf.append(tmp);
							}
							//System.out.println((String)strlist.get(i));
					  }
						
					  themeContext = sbf.toString();
					  Paragraph context =this.getDocHtml(themeContext); //----------可以使用HTML的内容----------------
					  
					  //for(int j=0;j<context.size();j++){
					  if(context.size()>0){
						  com.lowagie.text.Chunk v = (com.lowagie.text.Chunk)context.get(context.size()-1);
						  System.out.println(v.getContent());
						  if("".equals(v.getContent().trim())){
							  context.remove(context.size()-1);
						  }
					  }
					  //}
					  context.setAlignment(Element.ALIGN_LEFT);//正文格式左对齐     
					  context.setFont(contextFont);       	       
					  context.setSpacingBefore(0); //离上一段落（标题）空的行数             
					  context.setFirstLineIndent(0);//设置第一行空的列数       
					  document.add(context);  
					  
					  if(imagelist.size()>0){
						   for(int kk=0;kk<imagelist.size();kk++){
							  File image = imagelist.get(kk);
							  BufferedImage sourceImg =ImageIO.read(new FileInputStream(image));
			        			float width = sourceImg.getWidth();
			        			float height = sourceImg.getHeight();
							 //添加图片 Image.getInstance即可以放路径又可以放二进制字节流      
							  Image img = Image.getInstance(image.getPath());       
							  img.setAbsolutePosition(0, 0);       
							  img.setAlignment(Image.ALIGN_CENTER);// 设置图片显示位置       
							  img.scaleAbsolute(width, height);// 直接设定显示尺寸       
							  //img.scalePercent(100);//表示显示的大小为原尺寸的50%       
								//img.scalePercent(25, 12);//图像高宽的显示比例       
								//img.setRotation(30);//图像旋转一定角度     
								img.setBorder(2);
								img.setBorderWidth(2);
								document.add(img);
								Paragraph context2 =new Paragraph(imageTitlelist.get(kk)); //----------可以使用HTML的内容----------------
								context2.setAlignment(Element.ALIGN_CENTER);//正文格式左对齐     
								context2.setFont(contextFont);       	       
								context2.setSpacingBefore(0); //离上一段落（标题）空的行数             
								context2.setFirstLineIndent(0);//设置第一行空的列数         
								document.add(context2);
						  }
				    }
			 // }
		   // }
		 }
	}

	

}
