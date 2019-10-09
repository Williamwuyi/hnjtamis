package cn.com.ite.hnjtamis.doc;

import java.io.File;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;


/**
 * <p>Title cn.com.ite.ydkh.doc.ExportDocDemoServiceImpl</p>
 * <p>Description 导出Word的例子</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2012</p>
 * @author 朱健
 * @create time: 2012-11-16 上午10:30:44
 * @version 1.0
 * 
 * @modified records:
 */
public class ExportDocDemoServiceImpl extends ExportDocServiceImpl {

	/**
	 * @author 朱健
	 * @description
	 * @param document
	 * @modified
	 */
	protected void setDocument(Document document,Object form) throws Exception{
		 Map value = (Map)form;
		
		 BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",  "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体            
		  Font titleFont1 = new Font(bfChinese, 19, Font.BOLD);  //标题字体风格
		  Font titleFont2 = new Font(bfChinese, 16, Font.BOLD);  //标题字体风格    
		  //Font titleFont3 = new Font(bfChinese, 14, Font.BOLD);  //标题字体风格    
		  Font contextFont = new Font(bfChinese, 13, Font.NORMAL);//正文字体风格       
	
		  document.add(new Paragraph(""));
		  
		  Paragraph title = new Paragraph("指标对标分析简报");
		  title.setAlignment(Element.ALIGN_CENTER); //设置标题格式对齐方式   
	      title.setFont(titleFont1); 
		  document.add(title); 
	    
		  Paragraph context = new Paragraph("一、	前言",titleFont2);
		  context.setAlignment(Element.ALIGN_CENTER);//正文格式左对齐     
		  context.setFont(contextFont);       	       
		  context.setSpacingBefore(5); //离上一段落（标题）空的行数             
		  context.setFirstLineIndent(20);//设置第一行空的列数
		  document.add(context);
		 
		 String msg=(String)value.get("MSG");
		 if(msg!=null && !"".equals(msg) && !"null".equals(msg)){ 
			 //----------可以使用HTML的内容----------------
			 context =this.getDocHtml(msg); //----------可以使用HTML的内容----------------
			 context.setAlignment(Element.ALIGN_LEFT);//正文格式左对齐     
			 context.setFont(contextFont);       	       
			 context.setSpacingBefore(5); //离上一段落（标题）空的行数             
			 context.setFirstLineIndent(20);//设置第一行空的列数       
			 document.add(context);
		 }
		 document.add(new Paragraph(""));
		 
		 document.newPage();
		 context = new Paragraph("二、	综合指标情况",titleFont2);               
		 context.setAlignment(Element.ALIGN_CENTER);//正文格式左对齐     
		 context.setFont(contextFont);       	       
		 context.setSpacingBefore(5); //离上一段落（标题）空的行数             
		 context.setFirstLineIndent(20);//设置第一行空的列数       
		 document.add(context);
		 

		 document.add(new Paragraph(""));
		 
		 document.newPage();
		 context = new Paragraph("三、	单项指标情况",titleFont2);               
		 context.setAlignment(Element.ALIGN_CENTER);//正文格式左对齐     
		 context.setFont(contextFont);       	       
		 context.setSpacingBefore(5); //离上一段落（标题）空的行数             
		 context.setFirstLineIndent(20);//设置第一行空的列数       
		 document.add(context);
			
		 String imgpath = "/filename/a.jsp";
		 File file=new File("/filename");
		 if(file.exists()){
				//添加图片 Image.getInstance即可以放路径又可以放二进制字节流      
				Image img = Image.getInstance(imgpath);       
				img.setAbsolutePosition(0, 0);       
				img.setAlignment(Image.ALIGN_CENTER);// 设置图片显示位置       
				img.scaleAbsolute(500, 300);// 直接设定显示尺寸       
				//img.scalePercent(100);//表示显示的大小为原尺寸的50%       
				//img.scalePercent(25, 12);//图像高宽的显示比例       
				//img.setRotation(30);//图像旋转一定角度       
				document.add(img);
				document.add(new Paragraph(""));
		}
	}

	

}
