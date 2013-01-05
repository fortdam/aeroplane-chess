package com.fortdam.aeroplane_chess;

import java.util.ArrayList;

interface DecisionMaker{
	void decide(Player player, int dice);
}

public class Player {
	
	public interface ActionListener{
		void done();
	}
	
	Player(int id, Printable printer){
		this.id = id;
		
		for (int i=0; i<4; i++){
			planes[i] = new Plane(id, i);
		}
		enumerator = 0;
		board = printer;
		decider = new DecisionMaker(){
			public void decide(Player player, int dice){
				ArrayList<Plane> actions = player.getPossibleActions(dice);
				
				for (int i=0; i<actions.size(); i++){
					Plane plane = actions.get(i);
					if (plane.getState() == Plane.STATE_PARKING){
						player.takeAction(plane.getId());
						return;
					}
				}

				for (int i=0; i<actions.size(); i++){
					Plane plane = actions.get(i);
					if (plane.getState() == Plane.STATE_LANDING && 
							plane.getLaneCellIndex()+dice == 5){
						player.takeAction(plane.getId());
						return;
					}
				}
				
				for (int i=0; i<actions.size(); i++){
					Plane plane = actions.get(i);
					if (plane.getState() == Plane.STATE_MOVING){
						player.takeAction(plane.getId());
						return;
					}
				}
				
				player.takeAction(actions.get(0).getId());
				return;
			}
		};
	}
	
	public void setDecisionMaker(DecisionMaker mk){
		decider = mk;
	}
	
	public ArrayList<Plane> getPossibleActions(int dice){
		ArrayList<Plane> list = new ArrayList<Plane>();
		
		for (int i=0; i<planes.length; i++){
			switch(planes[i].getState()){
			case Plane.STATE_STARTING:
			case Plane.STATE_MOVING:
			case Plane.STATE_LANDING:
				list.add(planes[i]);
				break;
			case Plane.STATE_PARKING:
				if (dice == 6){
					list.add(planes[i]);
				}
			default:
				break;
			}
		}
		return list;
	}
	
	public void takeAction(int id){
		ArrayList<Plane> possibleActions = getPossibleActions(diceNumber);
		
	    for (int i=0; i<possibleActions.size(); i++){
	    	Plane plane = possibleActions.get(i);
	    	
	    	if (plane.getId() == id){
	    		switch(plane.getState()){
	    		case Plane.STATE_PARKING:
	    			plane.start(board);
	    			break;
	    		default:
	    			plane.move(diceNumber, board);	
	    			break;
	    		}
	    		return;
	    	}
	    }
	    //Exception
	}
	
	public void activate(int dice, ActionListener listener){
		ArrayList<Plane> possibleActions = getPossibleActions(dice);
		
		if (possibleActions.size() == 0){
			//No possible action can take
			listener.done();
		}
		else{
			//Pass it to the AI
			diceNumber = dice;
			moveListener = listener;
			
			decider.decide(this, dice);
		}
	}
	
    public void actOnDice(int number){
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
	private int diceNumber;
	private ActionListener moveListener;
	private Printable board;
	private DecisionMaker decider;
}
