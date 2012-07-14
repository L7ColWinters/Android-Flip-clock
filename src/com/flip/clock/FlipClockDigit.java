package com.flip.clock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public abstract class FlipClockDigit extends RelativeLayout {

	private ImageView top,bottom,rotater,rotaterBottom;
	private Bitmap prevTop;
	private Bitmap newTop;
	private Bitmap newBottom;
	private Rotate3dAnimation toNinety;
	private Rotate3dAnimation from270;
	public int currentNum;
	public int maxDigit;
	
	public FlipClockDigit(Context context) {
		super(context);
		setupClock(context);
	}

	public FlipClockDigit(Context context,AttributeSet attr){
		super(context,attr);
		setupClock(context);
	}
	
	public FlipClockDigit(Context context,AttributeSet attr,int defStyle){
		super(context,attr,defStyle);
		setupClock(context);
	}
	
	private void setupClock(Context c){
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.flip_clock_digit, null);
		top = (ImageView)v.findViewById(R.id.imageView1);
		bottom = (ImageView)v.findViewById(R.id.imageView3);
		rotater = (ImageView)v.findViewById(R.id.imageView2);
		rotaterBottom = (ImageView)v.findViewById(R.id.imageView4);
		
		Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.zero);
		Bitmap[] splitImages = ImageUtils.splitBitmap(temp, temp.getHeight()/2, temp.getWidth());
		temp.recycle();
		prevTop = splitImages[0];
		top.setImageBitmap(splitImages[0]);
		bottom.setImageBitmap(splitImages[1]);
		bottom.setVisibility(View.VISIBLE);
		addView(v);
		invalidate();
		
		toNinety = new Rotate3dAnimation(0,90,25,50,0,false);
		toNinety.setInterpolator(new AccelerateInterpolator());
		toNinety.setDuration(500);
		toNinety.setAnimationListener(new AnimationListener(){
			public void onAnimationEnd(Animation arg0) {
				prevTop = newTop;
				rotater.setVisibility(View.INVISIBLE);
				rotaterBottom.setVisibility(View.VISIBLE);
				rotaterBottom.startAnimation(from270);
			}

			public void onAnimationRepeat(Animation arg0) {}

			public void onAnimationStart(Animation arg0) {
				rotater.setImageBitmap(prevTop);
				top.setImageBitmap(newTop);
				rotater.setVisibility(View.VISIBLE);
			}
		});
		
		from270 = new Rotate3dAnimation(90,0,25,0,0,false);
		from270.setInterpolator(new DecelerateInterpolator());
		from270.setDuration(500);
		from270.setAnimationListener(new AnimationListener(){
			public void onAnimationEnd(Animation animation) {
				bottom.setImageBitmap(newBottom);
				rotaterBottom.setVisibility(View.INVISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {}

			public void onAnimationStart(Animation animation) {
				rotaterBottom.setImageBitmap(newBottom);
			}
		});
	}

	public void setNumber(int num){
		Bitmap[] splitImages = null;
		Bitmap temp = null;
		switch(num){
		case 0:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.zero);
			break;
		case 1:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.one);
			break;
		case 2:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.two);
			break;
		case 3:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.three);
			break;
		case 4:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.four);
			break;
		case 5:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.five);
			break;
		case 6:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.six);
			break;
		case 7:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.seven);
			break;
		case 8:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.eight);
			break;
		case 9:
			temp = BitmapFactory.decodeResource(getResources(), R.drawable.nine);
			break;
		}
		if(temp != null){
			splitImages = ImageUtils.splitBitmap(temp, temp.getHeight()/2, temp.getWidth());
			temp.recycle();
			newTop = splitImages[0];
			newBottom = splitImages[1];
			currentNum = num;
		}
	}
	
	public void flipDigit(){
		if(currentNum == maxDigit){
			onOverflow();
			setNumber(0);
		}else
			setNumber(++currentNum);
		rotater.startAnimation(toNinety);
	}
	
	public abstract void onOverflow();
}
