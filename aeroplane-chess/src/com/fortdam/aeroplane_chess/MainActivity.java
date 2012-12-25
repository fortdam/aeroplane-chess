package com.fortdam.aeroplane_chess;

import java.util.TimerTask;

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
import android.widget.Toast;

import com.fortdam.aeroplane_chess.ui.Position;

class UIElement implements Animation.AnimationListener{
	public static Context context = null;
	public static float dispRatio = 1;
	
	interface AnimationEventHandler {
		public void animEnd();
	}

	UIElement (int resId, Point pt){
		ImageView view = new ImageView(context);
		view.setImageResource(resId);
		view.setAdjustViewBounds(true);
		view.setVisibility(View.VISIBLE);
		
		//Set the proportion of the image.
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, options);
		
		width = (int)(options.outWidth * dispRatio);
		height = (int)(options.outHeight * dispRatio);
		
		view.setMaxWidth(width);
		view.setMaxHeight(height);
		Log.i("Aeroplane", "The size is:"+width+","+height);
		
		icon = view;
		x = (int)(pt.x * dispRatio);
		y = (int)(pt.y * dispRatio);
		Log.i("Aeroplane", "Create a new element "+view+" at:"+x+","+y);
	}
	
	public void move(Point pt, AnimationEventHandler handler){
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
		anim.setAnimationListener(this);
		icon.startAnimation(anim);
		
		//Update the current position
		x = pt.x;
		y = pt.y;
		animHandler = handler;
		Log.i("Aeroplane", "Move the element to:" +x+","+y);
	}
	
	public void insert(ViewGroup parent){
		parent.addView(icon,
				new AbsoluteLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						x - width/2, y - height/2));
		Log.i("Aeroplane", "Insert the element "+icon+"at:"+x+","+y);
	}
	
	public void setVisibility(boolean visible){
		if (visible == true){
			icon.setVisibility(View.VISIBLE);
		}
		else{
			icon.setVisibility(View.INVISIBLE);
		}
		
	}
	
	@Override
	public void onAnimationEnd(Animation anim){
		ViewGroup parent = (ViewGroup)icon.getParent();
		parent.updateViewLayout(icon, 
				new AbsoluteLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				x - width/2, y - height/2));
		
		if (animHandler != null){
			animHandler.animEnd();
		}
		Log.i("Aeroplane", "Animatino end");
	}
	
	@Override
	public void onAnimationRepeat(Animation anim){
		Log.i("Aeroplane", "Animation repeat");
	}
	
	@Override
	public void onAnimationStart(Animation anim){
		Log.i("Aeroplane", "Animation start");
	}
	
	private View icon;
	private int x; //Represents the center of x axis
	private int y; //Represents the center of y axis
	private int width;
	private int height;
	private AnimationEventHandler animHandler = null;
}


public class MainActivity extends Activity 
		implements UIElement.AnimationEventHandler{
	
	private static final String debug="Aeroplane";
		
	private UIElement[] items = {null};
	private float dispRatio = 100;
	private float startX = 0;
	private float startY = 0;
	
	static int index = 2;
	
	public void animEnd(){
	    if(index < 52){
	        items[0].move(Position.getRouteCord(Position.TYPE_ROUTE, index++), this);
	    }
	    else if (index-52 < 24){
	        items[0].move(Position.getRouteCord(Position.TYPE_LANE, index-52), this);
	        index++;
	    }
	    else if (index-52-24 < 16){
	        items[0].move(Position.getRouteCord(Position.TYPE_PORT, index-52-24), this);
	        index++;
	    }
	    else if (index-52-24-16 < 4){
	        items[0].move(Position.getRouteCord(Position.TYPE_START, index-52-24-16), this);
	        index++;
	    }
	}
	
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
	
	class MyJob extends TimerTask{
		
		private Activity app;
		MyJob(Activity app){
			this.app = app;
		}
		@Override
		public void run(){
			app.runOnUiThread (new Runnable(){
				public void run(){
					//items[0].move(Position.getRouteCord(Position.TYPE_ROUTE, 1));
				}
				});
		}
	}
	
	public void startTest(View view){
		Context context = getApplicationContext();
		setDispProperties();
		String text = "DD: "+ dispRatio;
		int duration = Toast.LENGTH_SHORT;
		
		Point begin = Position.getRouteCord(Position.TYPE_ROUTE, 0);
		text += " : " + begin;
		
        items[0] = new UIElement(R.drawable.blue, begin);
		ViewGroup holder = (ViewGroup)findViewById(R.id.layout);
		items[0].insert(holder);

 		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		items[0].move(Position.getRouteCord(Position.TYPE_ROUTE, 1), this);
		//Timer timer = new Timer("tester");
		//timer.schedule(new MyJob(this), 1000);
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
