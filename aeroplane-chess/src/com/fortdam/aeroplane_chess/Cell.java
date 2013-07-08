package com.fortdam.aeroplane_chess;

public class Cell {
	public static final int TYPE_ROUTE = 1;
	public static final int TYPE_AIRPORT = 2;
	public static final int TYPE_LANE = 3;
	public static final int TYPE_START_POINT = 4;
	
	private int type;
	private int index;
	
	Cell(int aType, int aPlayerId, int aPlaneIndex, int aPosition){
		type = aType;
		
		switch(aType){
		case TYPE_ROUTE:
			index = aPosition;
			break;
		case TYPE_AIRPORT:
			index = aPlayerId * PlayRule.PLANE_NUM + aPlaneIndex;
			break;
		case TYPE_LANE:
			index = aPlayerId * PlayRule.LANE_LENGTH + aPosition;
			break;
		case TYPE_START_POINT:
			index = aPlayerId;
			break;
		default:
			break;
		}
		
	}
	
	Cell(int aType, int aIndex){
		type  = aType;
		index = aIndex;
	}
	
	Cell(Cell aObj){
	    type = aObj.type;
	    index = aObj.index;
	}
	
	public int getType(){
		return type;
	}
	
	public int getIndex(){
		return index;
	}
}
