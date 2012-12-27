package com.fortdam.aeroplane_chess;

import java.util.ArrayList;
import java.util.Random;


class Movement{
	public static final int ROUTE = 0;
	public static final int PORT = 1;
	public static final int LANE = 2;
	public static final int START = 3;
	
	public int type;
	public int cellId;
}

class Action{	
	public static final int TAKE_OFF = -1;
	
	public int playerId;
	public int chessId;
	public int step;
}

interface DecisionTaker{
	void move(Action action);
}

interface DecisionMaker{
	void decide (ArrayList <Action> actions, DecisionTaker callback, Game game);
}


interface Printable {
	void print(String json);	
	void refresh(String json);
}

class Chess{
	public static final int PARKING = 0;
	public static final int LAUNCHING = 1;
	public static final int MOVING = 2;
	public static final int LANDING = 3;
	public static final int COMPLETE = 4;
	 
	Chess (int cellId){
		state = PARKING;
		place = cellId;
	}
	public int state;
	public int place; //Expressed by cell ID
}

class Player{
	public int playerId;
	
	public Chess[] chesses;
	public DecisionMaker dm;
	
	Player (int id, DecisionMaker client){
		dm = client;
		playerId = id;
		
		chesses[0] = new Chess(id*4);
        chesses[1] = new Chess(id*4 + 1);
		chesses[3] = new Chess(id*4 + 2);
        chesses[4] = new Chess(id*4 + 3);
	}
}

class Dice {
	private Random generator;
	Dice (){
		generator = new Random();
	}
	public int roll() {
		return (generator.nextInt(6) + 1);
	}
}



public class Game 
	implements DecisionTaker{
	
    Game (Printable target){
    	printer = target;
    }
    
    public void addPlayer (DecisionMaker client){
    	players.add(new Player(players.size(), client));
    }
    
    public void removePlayer (DecisionMaker player) {
    	
    }
    
    private Printable printer;
    private ArrayList <Player> players;
    
    @Override
    public void move(Action action) {
    	//Update the current/impact player status
    	
    	
    	//Update the status to the printable 
    	
    	//Call next player
    }
}
