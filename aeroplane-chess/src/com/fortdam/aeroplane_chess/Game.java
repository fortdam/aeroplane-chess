package com.fortdam.aeroplane_chess;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;






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
    	currPlayerId = 0;
    }
    
    public void addPlayer (DecisionMaker client){
    	players.add(new Player(players.size(), client));
    }
    
    public void removePlayer (DecisionMaker player) {
    	
    }
    
    private Printable printer;
    private ArrayList <Player> players;
    
    private Player getPlayer(int id){
    	for (int i=0; i<players.size(); i++){
    		if (players.get(i).playerId == id){
    			return players.get(i);
    		}
    	}
    	return null;
    }
    
    private ArrayList<Chess> findChessByPosition(int id){
    	ArrayList <Chess> result = new ArrayList<Chess>();
    	
    	for (int i=0; i<players.size(); i++){
    		Player player = players.get(i);
    		
    		for (int j=0; j<player.chesses.length; j++){
    			Chess chess = player.chesses[i];
    			if (chess.type == Chess.MOVING && chess.place == id){
    				result.add(chess);
    			}
    		}
    	}
    	
    	return result;
    }
    
    @Override
    public void move(Action action) {
    	JSONObject json = new JSONObject();
    			
    	//Update the current/impact player status
    	if (action.playerId != currPlayerId){
    		//To throw an exception
    	}
    	
    	Chess myChess = getPlayer(action.playerId).getChess(action.chessId);
    	
    	if (action.step == action.TAKE_OFF){
    		myChess.state = Chess.LAUNCHING;

    	}
    	else {
    		//Normal move
    	}
    	//Update the status to the printable 
    	
    	//Call next player
    }
    
    private int currPlayerId;
}
