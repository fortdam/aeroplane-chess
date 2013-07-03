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
	
	public static final int CELL_ROUTE = 1;
	public static final int CELL_AIRPORT = 2;
	public static final int CELL_LANE = 3;
	public static final int CELL_START_POINT = 4;
	
	public int actionType;
	
	//For ACTION_DICE_ROLL
	public int playerIndex;
	public int diceNumber;
	
	//For ACTION_MOVE or ACTION_SYNC
	public int chessIndex;
	public int cellType;
	public int cellIndex;
	
	
	public static DispAction createDiceRollAction(int num, int player){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_DICE_ROLL;
		inst.diceNumber = num;
		inst.playerIndex = player;
		return inst;
	}
	
	public static DispAction createMoveAction(int aChessIndex, int aCellType, int aCellIndex){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_MOVE;
		inst.chessIndex = aChessIndex;
		inst.cellType = aCellType;
		inst.cellIndex = aCellIndex;
		return inst;
	}
	
	public static DispAction createSyncAction(int aChessIndex, int aCellType, int aCellIndex){
		DispAction inst = new DispAction();
		inst.actionType = ACTION_SYNC;
		inst.chessIndex = aChessIndex;
		inst.cellType = aCellType;
		inst.cellIndex = aCellIndex;
		return inst;
	}
}
