package com.fortdam.aeroplane_chess;

import java.util.Timer;
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
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.fortdam.aeroplane_chess.ui.Position;

class UIElement {
	public static Context context = null;

	UIElement (int resId, Point pt){
		icon = new ImageView(context);
		((ImageView) icon).setImageResource(resId);
		
		x = pt.x;
		y = pt.y;
	}
	
	public void move(Point pt){
		x = pt.x;
		y = pt.y;
		Log.w("Aeroplane", "The new position is:"+x+","+y);
		ViewGroup parent = (ViewGroup)icon.getParent();
		parent.updateViewLayout(icon, 
				new AbsoluteLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						x,y));
	}
	
	public void insert(ViewGroup parent){
		parent.addView(icon,
				new AbsoluteLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT, x, y));
	}
	
	public void setVisibility(boolean visible){
		if (visible == true){
			icon.setVisibility(View.VISIBLE);
		}
		else{
			icon.setVisibility(View.INVISIBLE);
		}
		
	}
	
	public View icon;
	public int x;
	public int y;
}


public class MainActivity extends Activity {
	
	private static final String debug="Aeroplane";
		
	private UIElement[] items = {null};
	private float dispRatio = 100;
	private float startX = 0;
	private float startY = 0;
	
	private Point mapPosition(Point pt){
		return new Point((int)(pt.x*dispRatio), (int)(pt.y*dispRatio));
	}
	
	private void setDispProperties(){
		View map = findViewById(R.id.mapView);
		startX = map.getLeft() + map.getPaddingLeft();
		startY = map.getTop() + map.getPaddingTop();
		
		Log.i(debug, "The position of the map is:"+startX+","+startY);
		Log.i(debug, "The measured size of the map is:"+map.getMeasuredWidth()+","+map.getMeasuredHeight());
		Log.i(debug, "The size of the map is:"+map.getWidth()+","+map.getHeight());
		Log.i(debug, "The right/bottom position of the map is:"+map.getRight()+","+map.getBottom());
		//Get the Original size of the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.map, options);

		dispRatio = (float)map.getWidth() / (float)options.outWidth;
	}
	
	class MyJob extends TimerTask{
		private Activity app;
		MyJob(Activity app){
			this.app = app;
		}
		@Override
		public void run(){
			final Point newPos = mapPosition(Position.getRouteCord(Position.TYPE_ROUTE, 1));
			Log.i(debug, "The new position is:"+newPos);
			app.runOnUiThread (new Runnable(){
				public void run(){
					items[0].move(newPos);
				}
				});
		}
	}
	
	public void startTest(View view){
		Context context = getApplicationContext();
		setDispProperties();
		String text = "DD: "+ dispRatio;
		int duration = Toast.LENGTH_SHORT;


		
		Point begin = mapPosition(Position.getRouteCord(Position.TYPE_ROUTE, 0));
		text += " : " + begin;
		
        items[0] = new UIElement(R.drawable.blue, begin);
		ViewGroup holder = (ViewGroup)findViewById(R.id.layout);
		items[0].insert(holder);
        holder.invalidate();
        Log.i(debug, "The chess position is:" + items[0].x+","+items[0].y);
        Log.i(debug, "The size of chess is:" + items[0].icon.getWidth()+","+items[0].icon.getHeight());
        Log.i(debug, "The position of layout is:"+holder.getLeft()+","+holder.getTop());
        Log.i(debug, "The size of layout is:"+holder.getWidth()+","+holder.getHeight());
		//text += " "+items[0].icon.getWidth()+" "+items[0].icon.getHeight();
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		Timer timer = new Timer("tester");
		timer.schedule(new MyJob(this), 1000);
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
