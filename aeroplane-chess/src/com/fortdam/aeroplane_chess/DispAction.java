package com.fortdam.aeroplane_chess;

import java.util.ArrayList;

interface Printable {
	void print(DispAction action);	
	boolean isInSync();
}

public class DispAction {
	public static final int ACTION_DICE_ROLL = 1;
	public static final int ACTION_MOVE = 2;
	public static final int ACTION_SYNC = 3;
    public static final int ACTION_REST = 4;
    public static final int ACTION_FIX = 5;
	
	public int actionType;
	
	//For ACTION_DICE_ROLL
	public int playerIndex;
	public int diceNumber;
	
	//For ACTION_MOVE or ACTION_SYNC
	public int chessIndex;
	public Cell pos;
	
  public static DispAction createRestAction(){
    DispAction inst = new DispAction();
    inst.actionType = ACTION_REST;
    return inst;
  }
	
	public static DispAction createDiceRollAction(int num, int player){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_DICE_ROLL;
		inst.diceNumber = num;
		inst.playerIndex = player;
		return inst;
	}
	
	public static DispAction createMoveAction(int aChessIndex, Cell dest){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_MOVE;
		inst.chessIndex = aChessIndex;
		inst.pos = new Cell(dest);
		return inst;
	}
	
	public static DispAction createSyncAction(int aChessIndex, Cell dest){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_SYNC;
		inst.chessIndex = aChessIndex;
		inst.pos = new Cell(dest);
		return inst;
	}
	
	public static DispAction createFixAction(int aChessIndex){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_FIX;
		inst.chessIndex = aChessIndex;
		return inst;
	}
}
