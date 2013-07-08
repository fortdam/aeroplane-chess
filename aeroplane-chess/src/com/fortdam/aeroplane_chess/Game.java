package com.fortdam.aeroplane_chess;

import java.util.ArrayList;
import java.util.Random;

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
	implements Player.ActionListener{
	
	private int round = 0;
	private boolean redo = false;
	
    private Game (Printable target){
    	board = target;
    	dice = new Dice();
    }
    
    static private Game instance = null;
    
    public static Game createGameInstance(Printable target){
    	if (instance == null){
    		instance = new Game(target);
    	}
    	return instance;
    }
    
    public static Game getGameInstance(){
    	return instance;
    }
    
    public int addPlayer(){
    	players.add(new Player(players.size(), board));
    	return players.size();
    }
    
    public int addPlayer(DecisionMaker client){
    	int index = addPlayer()-1;
    	players.get(index).setDecisionMaker(client);
    	return index;
    }
    
    public void done(){
        board.print(DispAction.createRestAction());
        if (redo){
        	redo = false;
        }
        else{
    	  currPlayerId++;
        }
        
    	if (currPlayerId == players.size()){
    		currPlayerId = 0;
    	}

    	next();
    }
    
    public void next(){
    	int diceNumber = dice.roll();
    	Player currPlayer = players.get(currPlayerId);
    	
    	board.print(DispAction.createDiceRollAction(diceNumber, currPlayerId));
    	if (diceNumber == 6){
    	  redo = true;
    	}
    	currPlayer.activate(diceNumber, this);
    	
    }
    
    public void startGame(){
    	currPlayerId = 0;

    	for (int i=0; i<players.size(); i++){
    		Player player = players.get(i);
    		
    		for (int j=0; j<player.getPlaneNum(); j++){
    			Plane plane = player.getPlane(j);
    			plane.display(board);
    		}
    	}
    	
    	next();
    }
    
    public int getPlayerNum(){
    	return players.size();
    }
    
    public Player getPlayer(int i){
    	return players.get(i);
    }
    
    private Printable board;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Dice dice;
    
    private int currPlayerId;
}
