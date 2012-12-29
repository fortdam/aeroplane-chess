package com.fortdam.aeroplane_chess;

import java.util.ArrayList;

interface ActionRegister{
	void notifyDone();
}

interface DecisionMaker{
	public static final int DONE = 0;
	public static final int IN_PROGRESS = 1;
	

}


public class Player {
	
	Player(int id){
		this.id = id;
		
		for (int i=0; i<4; i++){
			planes[i] = new Plane(id, i);
		}
		enumerator = 0;
	}
	
    public int actOnDice(int number){
    	ArrayList<Plane> possibleActions = new ArrayList<Plane>();
    	
    	for (int i=0; i<planes.length; i++){
    		switch (planes[i].getState()){
    		case Plane.STATE_STARTING:
    		case Plane.STATE_MOVING:
    		case Plane.STATE_LANDING:
    			possibleActions.add(planes[i]);
    			break;
    		case Plane.STATE_PARKING:
    			if (number == 6){
    				possibleActions.add(planes[i]);
    			}
    			break;
    		default:
    			break;
    		}
    	}
    	
    }
	
	public int getPlaneNum(){
		return planes.length;
	}
	
	public Plane getPlane(int index){
		return planes[index];
	}
	
	public Plane getFirstPlane(){
		return planes[0];
	}
	
	public Plane getNextPlane(){
		if (enumerator >= 4){
			return null;
		}
		else{
			return planes[enumerator++];
		}
	}
	
	private Plane[] planes;
	private int id;
	
	private int enumerator;
}
