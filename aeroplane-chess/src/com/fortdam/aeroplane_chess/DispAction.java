package com.fortdam.aeroplane_chess;

import java.util.ArrayList;

interface Printable {
	void print(DispAction action);	
	boolean isInSync();
}

class DispMovement{
	public int chessIndex;
	public int cellType;
	public int cellIndex;
}

public class DispAction {
	public static final int ACTION_DICE_ROLL = 1;
	public static final int ACTION_MOVE = 2;
	public static final int ACTION_SYNC = 3;
	
	public static final int CELL_AIRPORT = 1;
	public static final int CELL_START_POINT = 2;
	public static final int CELL_ROUTE = 3;
	public static final int CELL_LANE = 4;
	
	private int actionType;
	
	//For ACTION_DICE_ROLL
	private int playerIndex;
	private int diceNumber;
	
	//For ACTION_MOVE or ACTION_SYNC
	private int chessIndex;
	private ArrayList<DispMovement> actions;
	private int enumerator;
	
	public static DispAction createSyncAction(){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_SYNC;
		inst.actions = new ArrayList<DispMovement>();
		return inst;
	}
	
	public static DispAction createDiceRollAction(int num, int player){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_DICE_ROLL;
		inst.diceNumber = num;
		inst.playerIndex = player;
		return inst;
	}
	
	public static DispAction createMoveAction(){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_MOVE;
		inst.actions = new ArrayList<DispMovement>();
		return inst;
	}
	
	public void addMove(int cellType, int cellIndex){
		 DispMovement move = new DispMovement();
		 move.chessIndex = this.chessIndex;
		 move.cellType = cellType;
		 move.cellIndex = cellIndex;
		 actions.add(move);
	}
	
	public void addMove(int chessIndex, int cellType, int cellIndex){
		if (actionType == ACTION_MOVE){
			this.chessIndex = chessIndex;
		}
		
		DispMovement move = new DispMovement();
		move.chessIndex = chessIndex;
		move.cellType = cellType;
		move.cellIndex = cellIndex;
		actions.add(move);		
	}
	
	public int getActionType(){
		return actionType;
	}
	
	public int getDiceNumber(){
		return diceNumber;
	}
	
	public int getMoveNumber(){
		return actions.size();
	}
	
	public DispMovement getMove(int index){
		return actions.get(index);
	}
	
	public DispMovement getFirstMove(){
		enumerator = 0;
		return actions.get(enumerator++);
	}
	
	public DispMovement getNextMove(){
		if(enumerator < actions.size()){
			return actions.get(enumerator++);
		}
		else{
			return null;
		}
	}
}
