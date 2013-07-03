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
    	
    	DispAction action = DispAction.createMoveAction(toIndex(), DispAction.CELL_START_POINT, ownerId);
    	target.print(action);
    }
    
    public void move(int step){

    }
    
    public void display(Printable target){
    	DispAction action;
    	switch(state){
    	case STATE_PARKING:
    		action = DispAction.createSyncAction(toIndex(), DispAction.CELL_AIRPORT, toIndex());
    		break;
    	case STATE_STARTING:
    		action = DispAction.createSyncAction(toIndex(), DispAction.CELL_START_POINT, ownerId);
    		break;
    	case STATE_MOVING:
    		action = DispAction.createSyncAction(toIndex(), DispAction.CELL_ROUTE, position);
    		break;
    	case STATE_LANDING:
    		action = DispAction.createSyncAction(toIndex(), DispAction.CELL_LANE, ownerId * PlayRule.getLandLength() + position);
    		break;
    	case STATE_COMPLETE:
    		action = DispAction.createSyncAction(toIndex(), DispAction.CELL_AIRPORT, toIndex());
    		break;
    	default:
    		return;
    	}
    	target.print(action);
    	return;
    }
    
    public void move(int step, Printable target){
    	int landDir = 1;

    	PlayRule  rule = new PlayRule(ownerId);
    	
    	while (step-- > 0){
    		if (state == STATE_STARTING){
    			state = STATE_MOVING;
    			position = rule.getStartCell();
    			target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_ROUTE, position));
    		}
    	    else if (state == STATE_MOVING){
    			if (rule.getLandCell() == position){
    				state = STATE_LANDING;
    				position = 0;
    				target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_LANE, ownerId * PlayRule.getLandLength()));
    			}
    			else{
    				position++;
    				target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_ROUTE, position));
    			}
    		}
    		else if (state == STATE_LANDING){
    			if (landDir == 1){
    				position++;
    				
    				if(position == PlayRule.getLandLength()-1){
    					landDir = -1;
    				}
    			}
    			else {
    				//Backward direction
    				position--;						
    			}
    			target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_LANE, ownerId * PlayRule.getLandLength() + position));
    		}
    	}    
    	
    	if (state == STATE_MOVING && rule.getShortcut(position) > 0){
    		position = rule.getShortcut(position);
    		target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_ROUTE, position));
    	}
    }
    
    public void shoot(){
    	state = STATE_PARKING;
    }
    
    public void shoot(Printable target){
    	shoot();
    	
    	target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_AIRPORT, ownerId));
    }
    
    public void complete(){
    	state = STATE_COMPLETE;
    }
    
    public void complete(Printable target){
    	complete();
    	
    	target.print(DispAction.createMoveAction(toIndex(), DispAction.CELL_AIRPORT, ownerId));
    }
    
    public int getRouteCellIndex() {
    	if (state == STATE_MOVING){
    		return position;
    	}
    	else {
    		return -1;
    	}
    }
    
    public int getLaneCellIndex(){
    	if (state == STATE_LANDING){
    		return position;
    	}
    	else{
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
    
    public int getId(){
    	return id;
    }
    
	private int ownerId;
	private int id;
	
	private int state;
	private int position;
}
