package com.fortdam.aeroplane_chess.ui;

import android.graphics.Point;

public class Position {
	public static final int TYPE_ROUTE = 1;
	public static final int TYPE_PORT = 2;
	public static final int TYPE_LANE = 3;
	public static final int TYPE_STATION = 4;
	
    public static Point getRouteCord(int type, int index){
        switch(type){
        case TYPE_ROUTE:
        	if (index <= route.length/2){
        		return new Point(route[index/2], route[index/2 + 1]);
        	}
        	break;
        default:
            break;		
        }
        return null;
    }
    
    private static final int[] route = {
        267, 744,
    	249, 690,
    	249, 641
    };
    

}
