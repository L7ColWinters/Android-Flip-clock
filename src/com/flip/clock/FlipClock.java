package com.flip.clock;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class FlipClock extends LinearLayout {
	
	private FlipClockDigit HoursFirstDigit;
	private FlipClockDigit HoursSecondDigit;
	private FlipClockDigit MinutesFirstDigit;
	private FlipClockDigit MinutesSecondDigit;
	private Timer timer;
	private TimerTask task;
	private Calendar cal;
	private int hours;
	private int minutes;
	private Handler handy;

	public FlipClock(Context context) {
		super(context);
		setupClock(context);
	}
	
	public FlipClock(Context context,AttributeSet attrs){
		super(context,attrs);
		setupClock(context);
	}
	
	public FlipClock(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		setupClock(context);
	}
	
	private void setupClock(Context c){
		setOrientation(LinearLayout.HORIZONTAL);
		handy = new Handler(){
			public void handleMessage(Message m){
				MinutesSecondDigit.flipDigit();
			}
		};
		
		HoursFirstDigit = new FlipClockDigit(c){
			@Override
			public void onOverflow() {
				//this will never happen
			}
		};
		HoursFirstDigit.maxDigit = 2;
		addView(HoursFirstDigit);
		HoursSecondDigit = new FlipClockDigit(c){
			@Override
			public void onOverflow() {
				HoursFirstDigit.flipDigit();
			}
		};
		HoursSecondDigit.maxDigit = 9;
		addView(HoursSecondDigit);
		MinutesFirstDigit = new FlipClockDigit(c){
			@Override
			public void onOverflow() {
				HoursSecondDigit.flipDigit();
			}
		};
		MinutesFirstDigit.maxDigit = 5;
		addView(MinutesFirstDigit);
		MinutesSecondDigit = new FlipClockDigit(c){
			@Override
			public void onOverflow() {
				MinutesFirstDigit.flipDigit();
			}
		};
		MinutesSecondDigit.maxDigit = 9;
		addView(MinutesSecondDigit);
		
		cal = Calendar.getInstance();
		hours = cal.get(Calendar.HOUR_OF_DAY);
		if (hours < 10){
			HoursFirstDigit.setNumber(0);
			HoursSecondDigit.setNumber(hours);
		}else{
			String s = String.valueOf(hours);
			HoursFirstDigit.setNumber(s.charAt(0) - '0');
			HoursSecondDigit.setNumber(s.charAt(1) - '0');
		}
		minutes = cal.get(Calendar.MINUTE);
		if(minutes < 10){
			MinutesFirstDigit.setNumber(0);
			MinutesSecondDigit.setNumber(minutes);
		}else{
			String s = String.valueOf(minutes);
			MinutesFirstDigit.setNumber(s.charAt(0) - '0');
			MinutesSecondDigit.setNumber(s.charAt(1) - '0');
		}
		timer = new Timer();
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility){
		if(visibility == View.INVISIBLE || visibility == View.GONE){
			if(task != null)
				task.cancel();
		}else{
			task = new TimerTask(){
				@Override
				public void run() {
					if(cal.get(Calendar.SECOND) != minutes){
						handy.sendEmptyMessage(0);
					}
				}
			};
			timer.schedule(task,0,2000);
		}
	}
}
