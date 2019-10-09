package cn.com.ite.hnjtamis.excel.util;


import java.io.File;

import jxl.write.WritableImage;

public class ExcelImage {
	private String imagePath;// 图片路径
	private int col;// 起始列
	private int row;// 起始行
	private int width;// 所占列数
	private int height;// 所占行数

	public WritableImage getImage() {
		File imageFile = new File(imagePath);
		WritableImage image = new WritableImage(col, row, width, height, imageFile);
		return image;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
