package com.coahr.cvfan.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.coahr.cvfan.adapter.GalleryAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class UtilTools {
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {

        GalleryAdapter gAdapter = (GalleryAdapter) gridView.getAdapter();

        if (gAdapter == null) {

            return;

        }

        int totalHeight = 0;

        if (gAdapter.getCount() > 0) {
            totalHeight = (gAdapter.getCount() % 3) != 0 ? 1 : 0;
            View listItem = gAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            
            Log.e("行数", gAdapter.getCount() / 3 + totalHeight + " ");
            
            totalHeight = listItem.getMeasuredHeight()
                    * (gAdapter.getCount() / 3 + totalHeight)
                    + listItem.getVerticalFadingEdgeLength()
                    * (gAdapter.getCount() / 3 + totalHeight - 1);

            ViewGroup.LayoutParams params = gridView.getLayoutParams();

            params.height = totalHeight
                    + (gridView.getVerticalSpacing() * (gAdapter.getCount() / 3 - 1));

            gridView.setLayoutParams(params);

            Log.e("行高", listItem.getMeasuredHeight() + "");
            
            Log.e("整高", params.height + "");
            
        }
    }
    
    public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
    
    public  static String returnImageurlSmall(String url)
    {
    	if(url==null||"".equals(url))
    	{
    		return "";
    	}
    	return url.replace(".", "-small.");
    }
    
    public  static String rsfLogo(String url)
    {
    	if(url==null||"".equals(url))
    	{
    		return "";
    	}
		if(url.contains("brand"))
		{
		    return url;
		}
		return url.replace(".", "-small.");
    }
}
