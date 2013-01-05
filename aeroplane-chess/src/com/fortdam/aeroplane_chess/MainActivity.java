package com.fortdam.aeroplane_chess;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

class DiceElement {
	public static Context context = null;
	public static float dispRatio = 999; //Just put an invalid number
	
	private ImageView view;
	private static final int UPDATE_PERIOD = 100;
	private int frame = 0;
	private int rollTime = 1500; //in ms
	
	private int[][] imageMatrix = {
			{R.drawable.blue0, R.drawable.blue1, R.drawable.blue2, R.drawable.blue3, R.drawable.blue4, R.drawable.blue5, R.drawable.blue6},
			{R.drawable.green0, R.drawable.green1, R.drawable.green2, R.drawable.green3, R.drawable.green4, R.drawable.green5, R.drawable.green6},
			{R.drawable.red0, R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6},
			{R.drawable.yellow0, R.drawable.yellow1, R.drawable.yellow2, R.drawable.yellow3, R.drawable.yellow4, R.drawable.yellow5, R.drawable.yellow6}
	};
	
	
	public void bind(ImageView diceView){
		view = diceView;
		
		//Set the proportion of the image.
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), imageMatrix[0][0], options);
		
		view.setAdjustViewBounds(true);
		view.setMaxWidth((int)(options.outWidth * dispRatio));
		view.setMaxHeight((int)(options.outHeight * dispRatio));
	}
	
	public void setRollTime(int period){
		rollTime = period;
	}
	
	public void setWait(int colorIndex){
		view.setImageResource(imageMatrix[colorIndex][0]);
	}
	
	public void roll(final int colorIndex, final int num, final Animation.AnimationListener handler){

		final int frames = this.rollTime/this.UPDATE_PERIOD;
		final Timer timer = new Timer();
		final Handler messageHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if (msg.what == 1){
					view.setImageResource(imageMatrix[msg.arg1][msg.arg2]);
				}
				else{
					timer.cancel();
					if (handler != null){
						handler.onAnimationEnd(null);
					}
				}
			}
		};
		
		timer.scheduleAtFixedRate(
				new TimerTask(){
					private int remain = frames;
					
					@Override
					public void run(){
						
						if (remain > 0){
							Random rand = new Random();
							int num = rand.nextInt(6)+1;
							
							messageHandler.obtainMessage(1,colorIndex,num).sendToTarget();
						}
						else{
							messageHandler.obtainMessage(2).sendToTarget();
						}
						
						remain--;						
					}
				}
				, UPDATE_PERIOD, UPDATE_PERIOD);
	}
}

class ChessElement {
	
	public static Context context = null;
	public static float dispRatio = 999; //Just put an invalid number

	
	ChessElement (int resId) throws Exception{
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
	
	public void setPosition(ViewGroup parent, Point pt) throws Exception{
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
		
	private ChessElement[] items = {null};
	private ArrayList<DispAction> actionQueue;
	private DispAction currentAction;
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
		ChessElement.dispRatio = dispRatio;
		DiceElement.dispRatio = dispRatio;
		Log.v("Aeroplane", "The disp Ratio is:"+dispRatio);
	}
	
	private void processAction(){
		if (actionQueue.size() > 0){
			currentAction = actionQueue.get(0);
			actionQueue.remove(0);
			
			switch(currentAction.getActionType()){
			case DispAction.ACTION_DICE_ROLL:
				break;
			case DispAction.ACTION_SYNC:
				
				break;
			case DispAction.ACTION_MOVE:
				break;
			}
		}
	}
	
	public void testDice(View view){
		Context context = getApplicationContext();
		setDispProperties();
		
		
		
		DiceElement dice = new DiceElement();
		dice.bind((ImageView)findViewById(R.id.diceView));
		dice.roll(0, 5, null);
	}
	
	public void testChess(View view){
		Context context = getApplicationContext();
		setDispProperties();
		
		Point begin = Position.getRouteCord(Position.TYPE_ROUTE, 0);
		
		try {
			items[0] = new ChessElement(R.drawable.blue);
			ViewGroup holder = (ViewGroup)findViewById(R.id.layout);
			items[0].setPosition(holder, begin);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		final ChessElement movingItem = items[0];
		
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
		
		actionQueue = new ArrayList<DispAction>();
		ChessElement.context = getApplicationContext();
		DiceElement.context = ChessElement.context;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
