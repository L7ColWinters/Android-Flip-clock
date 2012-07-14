package com.flip.clock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ImageUtils {

	public static Bitmap cropBitmap(Bitmap original, int height, int width) {
	    Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    Canvas canvas = new Canvas(croppedImage);
	 
	    Rect srcRect = new Rect(0, 0, original.getWidth(), original.getHeight());
	    Rect dstRect = new Rect(0, 0, width, height);
	 
	    int dx = (srcRect.width() - dstRect.width()) / 2;
	    int dy = (srcRect.height() - dstRect.height()) / 2;
	 
	    // If the srcRect is too big, use the center part of it.
	    srcRect.inset(Math.max(0, dx), Math.max(0, dy));
	 
	    // If the dstRect is too big, use the center part of it.
	    dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));
	 
	    // Draw the cropped bitmap in the center
	    canvas.drawBitmap(original, srcRect, dstRect, null);
	 
	    original.recycle();
	 
	    return croppedImage;
	}
	
	public static Bitmap[] splitBitmap(Bitmap original, int height, int width){
		Bitmap TopImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Bitmap BottomImage = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
	    Canvas TopCanvas = new Canvas(TopImage);
	    Canvas BottomCanvas = new Canvas(BottomImage);
	 
	    Rect srcRect = new Rect(0, 0, width, height);
	    Rect TopDstRect = new Rect(0, 0, width, height);
	 
	    // Draw the cropped bitmap in the center
	    TopCanvas.drawBitmap(original, srcRect, TopDstRect, null);
	    srcRect = new Rect(0,height,width,original.getHeight());
	    BottomCanvas.drawBitmap(original, srcRect, TopDstRect, null);
	 
	    original.recycle();
	 
	    return new Bitmap[]{TopImage,BottomImage};
	}
}
