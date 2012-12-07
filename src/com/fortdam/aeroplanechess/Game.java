
/*Definition for the contender's color*/
class Party{
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    private static final int YELLOW = 3;
    public static final int PARTY_NUM = 4;
    
    private Party (final int index){
      if (color >= PARTY_NUM) {
        throw new Exception("Out of range");
      }
      this.color = color;  
    }

    static Party[] parties = {
      new Party(0),
      new Party(1),
      new Party(2),
      new Party(3)
    };

    public static Party getParty(int index){
      if (index >= PARTY_NUM){
        throw new Exception("Out of range");
      }
      else {
        return parties[i];
      }
    }
    
    public Party nextParty(){
      return Parties[(color+1)%PARTY_NUM];
    }

    public Party prevParty(){
      return Parties[(color+PARTY_NUM-1)%PARTY_NUM];
    }

    public String getColor(){
      String strColor = "Unknown";
      switch(color){
        case RED:
          strColor = "RED";
          break;
        case GREEN:
          strColor = "GREEN";
          break;
        case BLUE:
          strColor = "BLUE";
          break;
        case YELLOW:
          strColor = "YELLOW";
          break;
        default:
          break;
      }
      return strColor;
    }
    
    private int color;
}

public class Game{
}

