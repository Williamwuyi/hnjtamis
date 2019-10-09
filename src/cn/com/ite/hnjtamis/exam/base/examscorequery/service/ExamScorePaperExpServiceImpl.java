package cn.com.ite.hnjtamis.exam.base.examscorequery.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import javax.imageio.ImageIO;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;

import cn.com.ite.hnjtamis.doc.ExportDocServiceImpl;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserAnswerkey;

public class ExamScorePaperExpServiceImpl extends ExportDocServiceImpl{
	protected void setDocument(Document document,Object form) throws Exception{
		Map value = (Map)form;
		String basepath=(String)value.get("basepath");
		Exam exam = (Exam) value.get("exam");
		LinkedHashMap<String,ArrayList<ExamTestpaperTheme>> resultMap = (LinkedHashMap<String, ArrayList<ExamTestpaperTheme>>) value.get("resultMap");
		LinkedHashMap<String,Double> scoreMap = (LinkedHashMap<String, Double>) value.get("scoreMap");
		String[] choiceNum = (String[]) value.get("choiceNum");
		String[] themeTypeNum = (String[]) value.get("themeTypeNum");
		
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",  "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体            
		Font titleFont1 = new Font(bfChinese, 22, Font.BOLD);  //标题字体风格
		Font titleFont2 = new Font(bfChinese, 16, Font.BOLD);  //标题字体风格 
		Font titleFont3 = new Font(bfChinese, 15, Font.BOLD);  //标题字体风格
		Font contextFont = new Font(bfChinese, 13, Font.NORMAL);//正文字体风格      
	
		document.add(new Paragraph(""));
		Paragraph title = new Paragraph(exam.getExamName()+" 考生答卷");
		title.setAlignment(Element.ALIGN_CENTER); //设置标题格式对齐方式   
	    title.setFont(titleFont1); 
		document.add(title); 
		
		Iterator<String> themeTypeKeys = resultMap.keySet().iterator();
		int typeCount = 0;
		while(themeTypeKeys.hasNext()){
			String typeKey = themeTypeKeys.next();
			ArrayList<ExamTestpaperTheme> themeList = resultMap.get(typeKey);
			Paragraph typeContent = new Paragraph(themeTypeNum[typeCount]+"、"+typeKey+"(共"+themeList.size()+"题,共"+scoreMap.get(typeKey)+"分)");
			typeContent.setAlignment(Element.ALIGN_LEFT);//正文格式左对齐     
			typeContent.setFont(titleFont2);       	       
			typeContent.setSpacingBefore(3); //离上一段落（标题）空的行数             
			typeContent.setFirstLineIndent(0);//设置第一行空的列数
		    document.add(typeContent);
		    
		    for(int i=0;i<themeList.size();i++){
		    	boolean flag = true;
		    	ExamTestpaperTheme po = themeList.get(i);
		    	//'单选题' || tixing.key eq '多选题' || tixing.key eq '判断题'
		    	if(!(typeKey.equals("单选题") || typeKey.equals("多选题") || typeKey.equals("判断题"))){
		    		flag = false;
		    	}
		    	
		    	Paragraph themeNameContent = new Paragraph((i+1)+"、"+po.getThemeName()+"   分值:("+po.getDefaultScore()+")");
		    	themeNameContent.setFont(titleFont3);       	       
		    	themeNameContent.setSpacingBefore(3); //离上一段落（标题）空的行数             
		    	themeNameContent.setFirstLineIndent(5);//设置第一行空的列数
			    document.add(themeNameContent);
			    
			    List<ExamTestpaperAnswerkey> answerList = po.getExamTestpaperAnswerkeies();
			    String rightAnswer = "";
			    String content = "";
			    for(int j=0;j<answerList.size();j++){
			    	ExamTestpaperAnswerkey answer = answerList.get(j);
			    	String colorStyle = "color:#000000;";
			    	if(answer.getIsRight()==10){
			    		rightAnswer += answer.getAnswerkeyValue()+";    ";
			    		colorStyle = "color:green;";
			    	}
			    	
			    	if(flag){
			    		String showLetter=choiceNum[j];
			    		
			    		switch (po.getEachline()) {
						case 0://不换行
							content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span>&nbsp;";
							break;
						case 1://每行一个
							content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span><br>";
							break;
						case 2://每行两个
							if((j+1)%2 == 0){
								content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span><br>";
							}
							if((j+1)%2 != 0){
								content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span>&nbsp;";
							}
							break;
						case 3://每行三个
							if((j+1)%3 == 0){
								content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span><br>";
							}
							if((j+1)%3 != 0){
								content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span>&nbsp;";
							}
							break;
						case 4://每行四个
							if((j+1)%4 == 0){
								content+= "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span><br>";
							}
							if((j+1)%4 != 0){
								content += "<span style='"+colorStyle+"'>"+showLetter+"."+answer.getAnswerkeyValue()+"</span>&nbsp;";
							}
							break;
						default:
							break;
						}
						
			    		
			    	}
			    }
			    
			      List<File> imagelist = new ArrayList<File>();
				  List<String> imageTitlelist =  new ArrayList<String>();
				  
				  
			      List strlist = new ArrayList();
				  String str = content;
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
					
				 
				  StringBuffer sbf = new StringBuffer();
				  int tunum = 1;
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
							String imageTitle = "见下图"+(i+1)+"-"+(tunum)+"";
							sbf.append(imageTitle);
							imageTitlelist.add("图"+(i+1)+"-"+(tunum)+"");
							tunum++;
						}else{
							sbf.append(tmp);
						}
						//System.out.println((String)strlist.get(i));
				  }
				  content = sbf.toString();
				  
			    Paragraph choice = this.getDocHtml(content);
	    		choice.setFont(contextFont);       	       
	    		choice.setSpacingBefore(0); //离上一段落（标题）空的行数             
	    		choice.setFirstLineIndent(5);//设置第一行空的列数
			    document.add(choice);
			    
			      strlist = new ArrayList();
				  str = rightAnswer;
			      index = str.indexOf("<img");
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
					
				 
				  sbf = new StringBuffer();
				  
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
							String imageTitle = "见下图"+(i+1)+"-"+(tunum)+"";
							sbf.append(imageTitle);
							imageTitlelist.add("图"+(i+1)+"-"+(tunum)+"");
							tunum++;
						}else{
							sbf.append(tmp);
						}
						//System.out.println((String)strlist.get(i));
				  }
				  rightAnswer = sbf.toString();

			    String strrightA = "<font color='green'>正确答案: "+(rightAnswer)+"</font>";
			    Paragraph rightA = this.getDocHtml(strrightA);
			    rightA.setFont(contextFont);       	       
			    rightA.setSpacingBefore(0); //离上一段落（标题）空的行数             
			    rightA.setFirstLineIndent(5);//设置第一行空的列数
			    document.add(rightA);
			    
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
			    
			    
			    String userAStr = "<font color='SlateBlue'>考生答案: ";
			    List<ExamUserAnswerkey> userAnswerList = po.getExamUserAnswerkeies();
			    for(ExamUserAnswerkey tmp : userAnswerList){
			    	if(tmp.getAnswerkeyValue()!=null && !"".equals(tmp.getAnswerkeyValue())
			    			&& !"null".equals(tmp.getAnswerkeyValue())){
			    		userAStr+=tmp.getAnswerkeyValue();
			    	}
			    }
			    userAStr+="</font>";
			    Paragraph userA = this.getDocHtml(userAStr);
			    userA.setFont(contextFont);       	       
			    userA.setSpacingBefore(0); //离上一段落（标题）空的行数             
			    userA.setFirstLineIndent(5);//设置第一行空的列数
			    document.add(userA);
			    
			    Paragraph scoreA = new Paragraph("得分"+po.getScore()+"");
			    scoreA.setFont(contextFont);       	       
			    scoreA.setSpacingBefore(0); //离上一段落（标题）空的行数             
			    scoreA.setFirstLineIndent(5);//设置第一行空的列数
			    document.add(scoreA);
			    
		    }
		    typeCount++;
		}
	}
}
