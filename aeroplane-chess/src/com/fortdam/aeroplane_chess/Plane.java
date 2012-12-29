package com.fortdam.aeroplane_chess;


public class Plane {
	public static final int STATE_PARKING = 0;
	public static final int STATE_STARTING = 1;
	public static final int STATE_MOVING = 2;
	public static final int STATE_LANDING = 3;
	public static final int STATE_COMPLETE = 4;
	
	Plane (int ownerId, int id){
		this.ownerId = ownerId;
		this.id = id;
		
		state = STATE_PARKING;
	}
	
    public void start(){
    	state = STATE_STARTING;
    }
    
    public void start(Printable target){
    	start();
    	
    	DispAction action = DispAction.createMoveAction();
    	action.addMove(toIndex(), DispAction.CELL_START_POINT, ownerId);
    }
    
    public void move(int step){

    }
    
    public void move(int step, Printable target){
    	int landDir = 1;
    	DispAction action = DispAction.createMoveAction();
    	PlayRule  rule = new PlayRule(ownerId);
    	
    	while (step-- > 0){
    		if (state == STATE_STARTING){
    			state = STATE_MOVING;
    			position = rule.getStartCell();
    			action.addMove(toIndex(), DispAction.CELL_ROUTE, position);
    		}
    	    else if (state == STATE_MOVING){
    			if (rule.getLandCell() == position){
    				state = STATE_LANDING;
    				position = 0;
    				action.addMove(toIndex(), DispAction.CELL_LANE, ownerId * PlayRule.getLandLength());
    			}
    			else{
    				position++;
    				action.addMove(toIndex(), DispAction.CELL_ROUTE, position);
    			}
    		}
    		else if (state == STATE_LANDING){
    			if (landDir == 1){
    				positiion++;
    				
    				if(position == PlayRule.getLandLength()-1){
    					landDir = -1;
    				}
    			}
    			else {
    				//Backward direction
    				position--;						
    			}
    			action.addMove(toIndex(), DispAction.CELL_LANE, ownerId * PlayRule.getLandLength() + position);
    		}
    	}    
    	
    	if (state == STATE_MOVING && rule.getShortcut(position) > 0){
    		position = rule.getShortcut(position);
    		action.addMove(toIndex(), DispAction.CELL_ROUTE, position);
    	}
    }
    
    public void shoot(){
    	state = STATE_PARKING;
    }
    
    public void shoot(Printable target){
    	shoot();
    	
    	DispAction action = DispAction.createMoveAction();
    	action.addMove(toIndex(), DispAction.CELL_AIRPORT, ownerId);
    }
    
    public void complete(){
    	state = STATE_COMPLETE;
    }
    
    public void complete(Printable target){
    	complete();
    	
    	DispAction action = DispAction.createMoveAction();
    	action.addMove(toIndex(), DispAction.CELL_AIRPORT, ownerId);
    }
    
    public int getRouteCellIndex() {
    	if (state == STATE_MOVING){
    		return position;
    	}
    	else {
    		return -1;
    	}
    }
    
    public boolean isReadyComplete(){
    	return (state==STATE_LANDING && position==PlayRule.getLandLength()-1);
    }
    
    private int toIndex(){
    	return (ownerId*4 + id);
    }
    
    public int getState(){
    	return state;
    }
    
	private int ownerId;
	private int id;
	
	private int state;
	private int position;
}
