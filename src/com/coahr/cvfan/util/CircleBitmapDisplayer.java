package com.coahr.cvfan.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

public class CircleBitmapDisplayer implements BitmapDisplayer {

    protected  final int margin ;

    public CircleBitmapDisplayer() {
        this(0);
    }

    public CircleBitmapDisplayer(int margin) {
        this.margin = margin;
    }

	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView,
			LoadedFrom loadedFrom) {
		if (!(imageView instanceof ImageView)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

		imageView.setImageDrawable(new CircleDrawable(bitmap, margin));
		return null;
	}
    
}