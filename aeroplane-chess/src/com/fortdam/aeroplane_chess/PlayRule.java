package com.fortdam.aeroplane_chess;

public class PlayRule {
	
	PlayRule(int playerId){
		this.playerId = playerId;
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
		int[] shortcuts = {};
		
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
	
	public static int getRouteLength(){
		return 52;
	}
	
	public static int getLandLength(){
		return 6;
	}
	
	private int playerId;
}


