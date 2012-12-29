package com.fortdam.aeroplane_chess;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

class PlayerRule {
	
	PlayerRule(int pId){
		playerId = pId;
	}
	
	public int getStartCell(){
		
		switch (this.playerId){
		case 0:
			return p0_info[0];
		case 1:
			return p1_info[0];
		case 2:
			return p2_info[0];
		case 3:
			return p3_info[0];
		default:
			return -1;	
		}
	}
	
	public int getLandCell(){
		switch (playerId){
		case 0:
			return p0_info[1];
		case 1:
			return p1_info[1];
		case 2:
			return p2_info[1];
		case 3:
			return p3_info[1];
		default:
			return -1;	
		}
	}
	
	public int getShortcut(int cellId){
		int[] shortcuts;
		
		switch (playerId){
		case 0:
			shortcuts = p0_info;
			break;
		case 1:
			shortcuts = p1_info;
			break;
		case 2:
			shortcuts = p2_info;
			break;
		case 3:
			shortcuts = p3_info;
			break;
		default:
			break;
		}
		
		for (int i=1; i*2 < shortcuts.length; i++){
			if (shortcuts[i*2] == cellId){
				return shortcuts[i*2+1];
			}
		}
		return -1;
	}
	
	private static int[] p0_info = {
		0,49, //Start,End
		
		1,5,
		5,9,
		9,13,
		13,17,
		17,29,
		21,25,
		25,29,
		29,33,
		33,37,
		37,41,
		41,45,
		45,49
	};
	
	private static int[] p1_info = {
		13,10, //Start, End
		
		14,18,
		18,22,
		22,26,
		26,30,
		30,42,
		34,38,
		38,42,
		42,46,
		46,50,
		50,2,
		2,6,
		6,10
	};
	
	private static int[] p2_info = {
		26, 23, //Start, End
		
		27,31,
		31,35,
		35,39,
		39,43,
		43,3,
		47,51,
		51,3,
		3,7,
		7,11,
		11,15,
		15,19,
		19,23
	};
	
	private static int[] p3_info = {
		39, 36, //Start, End
		
		40,44,
		44,48,
		48,0,
		0,4,
		4,16,
		8,12,
		12,16,
		16,20,
		20,24,
		24,28,
		28,32,
		32,36
	};
	
	private int playerId;
}


interface DecisionTaker{
	void move(UserAction action);
}

interface DecisionMaker{
	void decide (ArrayList <UserAction> actions, DecisionTaker callback, Game game);
}


interface Printable {
	void print(String json);	
	void refresh(String json);
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
	
	public Chess getChess(int id){
		return chesses[id];
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
    
    public JSONArray generateMovement(Action action){
    	JSONArray totalActs = new JSONArray();
    	int dir = 1;//Only for landing status
    	
    	Chess chess = getPlayer(action.playerId).getChess(action.chessId);
    	PlayerRule rule = new PlayerRule(action.playerId);
    	
    	if (action.step == Action.TAKE_OFF){
    		JSONObject act = new JSONObject();
    		JSONObject singleMove = new JSONObject();
    		JSONArray moveSeq = new JSONArray();
    		
    		singleMove.put("type", "Route");
    		singleMove.put("id", action.playerId);
    		moveSeq.put(singleMove);
    		
    		act.put("pid", action.playerId);
    		act.put("cid", action.chessId);
    		act.put("move", moveSeq);
    		
    		totalActs.put(act);
    	}
    	else{
    		JSONObject act = new JSONObject();
    		JSONArray moveSeq = new JSONArray();   		
    		JSONObject singleMove;
    		
    		for (int step=1; step<=action.step; step++){
    			
    			switch(chess.state){
    			case Chess.PARKING:
    				//Throw exception
    				break;
    			case Chess.LAUNCHING:
    				chess.state = Chess.MOVING;
    				chess.place = rule.getStartCell();
    				
    				singleMove = new JSONObject();   				
    				singleMove.put("type", "Route");
    				singleMove.put("id", chess.place);
    				moveSeq.put(singleMove);
    				break;
    				
    			case Chess.MOVING:
    				if (chess.place == rule.getLandCell()){
    					chess.state = Chess.LANDING;
    					chess.place = 0;
    					
    				    singleMove = new JSONObject();
    					singleMove.put("type", "Land");
    					singleMove.put("id", chess.place);
    					moveSeq.put(singleMove);
    				}
    				else{
    					chess.place++;
    					
    					singleMove = new JSONObject();
        				singleMove.put("type", "Route");
        				singleMove.put("id", chess.place);
        				moveSeq.put(singleMove);
    				}
    				break;
    				
    			case Chess.LANDING:
    				if (dir == 1){
    					chess.place++;
    					
    					singleMove = new JSONObject();
    					singleMove.put("type", "Land");
    					singleMove.put("id", chess.place);
    					moveSeq.put(singleMove);
    					
    					if (chess.place == 5){
    						dir = -1; //Turn backward
    					}
    				}
    				else{
    					chess.place--;
    					
    					singleMove = new JSONObject();
    					singleMove.put("type", "Land");
    					singleMove.put("id", chess.place);
    					moveSeq.put(singleMove);				
    				}
    				break;
    			}
    			
    		}
    		

			if (chess.type == Chess.LANDING && chess.place == 5){
				chess.type = Chess.COMPLETE;
				chess.place = action.chessId;
				
				singleMove = new JSONObject();
				singleMove.put("type", "Complete");
				singleMove.put("id", chess.place);
				moveSeq.put(singleMove);
			}
			//Last step, consider the haul
		    else if (chess.type == Chess.MOVING){
				if (rule.getShortcut(chess.place) > 0){
					chess.place = rule.getShortcut(chess.place);
					
					singleMove = new JSONObject();
					singleMove.put("type", "Route");
					singleMove.put("id", chess.place);
					moveSeq.put(singleMove);
				}
			}
			act.put("pid", action.playerId);
			act.put("cid", action.chessId);
			act.put("move", moveSeq);
			totalActs.put(act);
			
			
			for (int i=0; i<players.size(); i++){
				Player testPlayer = players.get(i);
				
				if (testPlayer.playerId != action.playerId){
					for (int j=0; j<testPlayer.chesses.length; j++){
						Chess testChess = testPlayer.chesses[j];
						if (testChess.state == Chess.MOVING && testChess.place == chess.place){
							testChess.state = Chess.PARKING;
							testChess.place = j;
							
							act = new JSONObject();
							singleMove = new JSONObject();
							moveSeq = new JSONArray();
							
							singleMove.put("type", "Park");
							singleMove.put("id", j);
							moveSeq.put(singleMove);
							act.put("pid", i);
							act.put("cid", j);
							act.put("move", moveSeq);
							totalActs.put(act);
						}
					}
				}
			
			}
    	}
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
