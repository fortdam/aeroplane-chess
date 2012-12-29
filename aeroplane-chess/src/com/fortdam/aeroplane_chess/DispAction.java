package com.fortdam.aeroplane_chess;

import java.util.ArrayList;

class DispMove{
	public int type;
	public int cellId;
	
	DispMove(int type, int cellId){
		this.type = type;
		this.cellId = cellId;
	}
}

public class DispAction {
	public static final int DICE_ROLL = 1;
	public static final int PLANE_MOVE = 2;
	
	private int type;
	private int diceNumber;
	private ArrayList<DispMove> moves;
	
	private int playerId;
	private int planeId;
	
	DispAction(int action, int player, int num){
		type = action;
		playerId = player;
		
		if (type == DICE_ROLL){
			diceNumber = num;
		}
		else {
			planeId = num;
			moves = new ArrayList<DispMove>();
		}
	}
	
	public void addMove(int type, int cellId){
		moves.add(new DispMove(type, cellId));
	}

	public int getType(){
		return type;
	}
	
	public int getDiceNumber(){
		return diceNumber;
	}
	
	public int getChessId(){
		return (playerId*4 + planeId);
	}
	
	public int getMoveActionNum(){
		return moves.size();
	}
	
	public DispMove getMoveAction(int index){
		return moves.get(index);
	}
	
	public staic int getCellType(int type){
		switch (type){
		case Plane.PARKING:
			
		}
		
	}
}
