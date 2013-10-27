package com.example.staggeredgridviewdemo;

import android.graphics.Bitmap;

/**
 * @author javatechig {@link http://javatechig.com} 각각의 커스텀 뷰의 이미지와,텍스트를 수정하기 위한
 *         클래스
 */
public class ImageItem {

	private String title;
	private String writer;
	private String price;

	public ImageItem(String title, String writer, String price) {
		super();
		this.title = title;
		this.writer = writer;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
