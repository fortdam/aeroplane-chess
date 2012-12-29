package com.fortdam.aeroplane_chess;

public class Plane {
	public static final int PARKING = 0;
	public static final int LAUNCHING = 1;
	public static final int MOVING = 2;
	public static final int LANDING = 3;
	public static final int COMPLETE = 4;
	
	Plane(int ownerId, int id){
		this.playerID = ownerId;
		this.id = id;
	}
	
	public move(int step){
		
	}
	
	public move(int step, Printable target){
		
	}
		
	private playerId;
	private id;
}
