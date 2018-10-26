package com.coahr.cvfan.displayer;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.coahr.cvfan.util.BitmapUtils;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public class CircularBitmapDisplayer implements BitmapDisplayer {

	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom) {
		// TODO Auto-generated method stub
		Bitmap ret = null;
		if(bitmap!=null){
			ret = BitmapUtils.getPortraitBitmap(bitmap);	
			imageView.setImageBitmap(ret);
		}

		return ret;
	}

}
