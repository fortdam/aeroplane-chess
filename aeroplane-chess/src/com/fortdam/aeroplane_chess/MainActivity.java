package com.fortdam.aeroplane_chess;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

class UIElement {
	UIElement(BitmapDrawable pic){
		
	}
	UIElement(BitmapDrawable pic, Point pt){
		
	}
	
	public void move(Point pt){
		
	}
	
	public void setVisibility(boolean visible){
		if (visible == true){
			picture.setVisibility(VISIBLE);
		}
		else{
			picture.setVisibility(INVISIBLE);
		}
		
	}
	
	public View picture;
	public int x;
	public int y;
}


public class MainActivity extends Activity {
		
	private UIElement[] items;
	private float dispRatio = 100;
	private float startX = 0;
	private float startY = 0;
	
	private Point mapPosition(Point pt){
		return new Point((int)(pt.x*dispRatio), (int)(pt.y*dispRatio));
	}
	
	private void setDispProperties(){
		View map = findViewById(R.id.mapView);
		startX = map.getX();
		startY = map.getY();
		
		//Get the Original size of the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.map, options);

		dispRatio = (float)map.getWidth() / (float)options.outWidth;
	}
	
	public void startTest(View view){
		Context context = getApplicationContext();
		setDispProperties();
		String text = "DD: "+ dispRatio;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
