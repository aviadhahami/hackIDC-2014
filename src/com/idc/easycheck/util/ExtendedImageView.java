package com.idc.easycheck.util;

import android.widget.ImageView;

import com.idc.easycheck.check.Costumer;

public class ExtendedImageView {
	public boolean isFull;
	
	public Costumer costumer;
	public ImageView fullImageView;
	public ImageView emptyImageView;
	
	public ExtendedImageView(Costumer costumer, ImageView fullImageView, ImageView emptyImageView, boolean isFull) {
		this.isFull = isFull;
		
		this.costumer = costumer;
		this.fullImageView = fullImageView;
		this.emptyImageView = emptyImageView;
	}
	
	public ImageView getCurrentImageView() {
		return (isFull ? fullImageView : emptyImageView);
	}
	
	public boolean isImageContained(ImageView imageView) {
		return ((imageView == fullImageView) || (imageView == emptyImageView));
	}
	
	public ImageView getCurrent() {
		return (this.isFull ? this.fullImageView : this.emptyImageView);
	}
	
	public void switchState() {
		this.isFull = !this.isFull;
	}
}
