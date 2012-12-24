package com.fortdam.aeroplane_chess.ui;

import android.graphics.Point;

public class Position {
	public static final int TYPE_ROUTE = 1;
	public static final int TYPE_PORT = 2;
	public static final int TYPE_LANE = 3;
	public static final int TYPE_START = 4;
	
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
        267, 744, //id: 0
    	249, 690,
    	249, 641,
    	268, 585,
    	238, 544, //id: 5
    	174, 570,
    	125, 570,
    	70,  522,
    	51,  500,
    	51,  452, //id: 10
    	51,  405,
    	51,  357,
    	51,  310,
    	68,  258,
    	125, 237, //id: 15
    	174, 237,
    	237, 260,
    	268, 221,
    	248, 166,
    	248, 118, //id: 20
    	268, 63,
    	323, 47,
    	421, 47,
    	471, 47,
    	520, 47,  //id: 25
    	576, 77,
    	594, 120,
    	594, 166,
    	572, 221,
    	603, 264, //id: 30
    	669, 240,
    	718, 240,
    	769, 258,
    	791, 310,
    	791, 357, //id: 35
    	791, 405,
    	791, 452,
    	791, 500,
    	768, 552,
    	718, 570, //id: 40
    	669, 570,
    	608, 550,
    	572, 585,
    	594, 641,
    	594, 690, //id: 45
    	576, 743,
    	519, 760,
    	471, 760,
    	421, 760,
    	372, 760, //id: 50
    	323, 760,
    	-1,-1 //End
    };
    
    private static final int[] lane = {
    	422, 690, //id_party: 0
    	422, 644,
    	422, 596,
    	422, 550,
    	422, 504,
    	422, 446,
    	
    	122, 405, //id_party: 1
    	166, 405, 
    	212, 405,
    	259, 405,
    	310, 405,
    	376, 405,
    	
    	422, 117, //id_party: 2
    	422, 165,
    	422, 210,
    	422, 257,
    	422, 304,
    	422, 361,
    	
    	720, 405, //id_party:3
    	676, 405,
    	629, 405,
    	583, 405,
    	531, 405,
    	465, 405,
    	
    	-1,-1
    };
    
    private static final int[] port = {
    	144, 748, //id_party: 0
    	144, 670,
    	64,  748,
    	64,  670,
    	
    	64,  139, //id_party: 1
    	144, 139,
    	64,  60,
    	144, 60,
    	
    	699, 60, //id_party: 2
    	699, 139,
    	780, 60,
    	699, 139,
    	
    	780, 670, //id_party: 3
    	699, 670,
    	780, 748,
    	699, 748,
    	
    	-1,-1
    };
    
    private static final int[] start = {
    	220, 780, //id_party: 0
    	38,  209, //id_party: 1
    	624, 38,  //id_party: 2
    	805, 598, //id_party: 3
    	-1,-1
    };
}
