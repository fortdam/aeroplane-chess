package com.fortdam.aeroplane_chess;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.fortdam.aeroplane_chess.ui.Position;

class UIElement {
	
	public static Context context = null;
	public static float dispRatio = 999; //Just put an invalid number
	
	interface AnimationEventHandler {
		public void animEnd();
	}

	UIElement (int resId) throws Exception{
		if (null == context){
			throw new Exception("The context of UIElement is not initialized");
		}
		
		//Crate the icon item
		ImageView icon = new ImageView(context);
		icon.setImageResource(resId);
		icon.setAdjustViewBounds(true);
		icon.setVisibility(View.VISIBLE);
		
		//Set the proportion of the image.
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, options);
		
		if (dispRatio > 100){
			throw new Exception("dispRatio probably not initialized");
		}
		width = (int)(options.outWidth * dispRatio);
		height = (int)(options.outHeight * dispRatio);
		
		icon.setMaxWidth(width);
		icon.setMaxHeight(height);

		item = icon;

		Log.i("Aeroplane", "Create a new element "+icon+" size ["+width+","+height+"]");
	}
	
	public void move(Point pt, final Animation.AnimationListener handler){
		//Play animation;
		pt.x = (int)(pt.x * dispRatio);
		pt.y = (int)(pt.y * dispRatio);
		
		AnimationSet anim = new AnimationSet(false);
		TranslateAnimation trans = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, pt.x - x,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, pt.y - y);
		
		trans.setDuration(500);
		anim.addAnimation(trans);
		anim.setFillAfter(true);
		anim.setFillEnabled(true);
		anim.setAnimationListener(
				new Animation.AnimationListener(){
					@Override
					public void onAnimationEnd(Animation anim){
						ViewGroup parent = (ViewGroup)item.getParent();
						parent.updateViewLayout(item, 
								new AbsoluteLayout.LayoutParams(
										ViewGroup.LayoutParams.WRAP_CONTENT, 
										ViewGroup.LayoutParams.WRAP_CONTENT, 
										x - width/2, y - height/2));
				
						if (handler != null){
							handler.onAnimationEnd(anim);
						}
						Log.i("Aeroplane", "Animation end");
					}
			
					@Override
					public void onAnimationRepeat(Animation anim){
						if (handler != null){
							handler.onAnimationRepeat(anim);
						}
						Log.i("Aeroplane", "Animation repeat");
					}
			
					@Override
					public void onAnimationStart(Animation anim){
						if (handler != null){
							handler.onAnimationStart(anim);
						}
						Log.i("Aeroplane", "Animation start");
					}
				});
		
		item.startAnimation(anim);
		
		//Update the current position
		x = pt.x;
		y = pt.y;
		Log.i("Aeroplane", "Move the element to:" +x+","+y);
	}
	
	public void insert(ViewGroup parent, Point pt) throws Exception{
		x = (int)(pt.x * dispRatio);
		y = (int)(pt.y * dispRatio);
		
		if (parent.indexOfChild(item) != -1){
			Log.w("Aeroplane", "The view is already inserted into the parent "+item);
			
			parent.updateViewLayout(item, 
					new AbsoluteLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, 
							ViewGroup.LayoutParams.WRAP_CONTENT, 
							x - width/2, y - height/2));		
		}
		else {					
			Log.i("Aeroplane", "Insert the element "+item+"at:"+x+","+y);
			
			parent.addView(item,
					new AbsoluteLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, 
							ViewGroup.LayoutParams.WRAP_CONTENT, 
							x - width/2, y - height/2));

		}
	}
	
	public void setVisibility(boolean visible){
		item.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
	}
	
	private View item;
	private int x; //Represents the center of x axis
	private int y; //Represents the center of y axis
	private int width;
	private int height;
}


public class MainActivity extends Activity {
	
	private static final String debug="Aeroplane";
		
	private UIElement[] items = {null};
	private float dispRatio = 100;
	private float startX = 0;
	private float startY = 0;
	
	
	private void setDispProperties(){
		View map = findViewById(R.id.mapView);
		startX = map.getLeft() + map.getPaddingLeft();
		startY = map.getTop() + map.getPaddingTop();
		
		//Get the Original size of the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.map, options);

		dispRatio = (float)map.getWidth() / (float)options.outWidth;
		UIElement.dispRatio = dispRatio;
		Log.i("Aeroplane", "The disp Ratio is:"+dispRatio);
	}
	
	public void startTest(View view){
		Context context = getApplicationContext();
		setDispProperties();
		
		Point begin = Position.getRouteCord(Position.TYPE_ROUTE, 0);
		
		try {
			items[0] = new UIElement(R.drawable.blue);
			ViewGroup holder = (ViewGroup)findViewById(R.id.layout);
			items[0].insert(holder, begin);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		final UIElement movingItem = items[0];
		
		movingItem.move(Position.getRouteCord(Position.TYPE_ROUTE, 1), 
				new Animation.AnimationListener() {
			 		private int index = 2; //Next position start from 2
			 		
					@Override
					public void onAnimationEnd(Animation anim){
						if(index < 52){
							movingItem.move(Position.getRouteCord(Position.TYPE_ROUTE, index++), this);
					    }
					    else if (index-52 < 24){
					    	movingItem.move(Position.getRouteCord(Position.TYPE_LANE, index-52), this);
					        index++;
					    }
					    else if (index-52-24 < 16){
					    	movingItem.move(Position.getRouteCord(Position.TYPE_PORT, index-52-24), this);
					        index++;
					    }
					    else if (index-52-24-16 < 4){
					    	movingItem.move(Position.getRouteCord(Position.TYPE_START, index-52-24-16), this);
					        index++;
					    }
					}
					
					@Override
					public void onAnimationRepeat(Animation anim){}
					
					@Override
					public void onAnimationStart(Animation anim){}
			
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		UIElement.context = getApplicationContext();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
