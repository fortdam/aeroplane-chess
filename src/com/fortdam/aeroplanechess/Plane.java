
public class Plane {
  public static final int WAITING = 0;
  public static final int TAKEOFF = 1;
  public static final int FLYING = 2;
  public static final int LANDING = 3;
  public static final int COMPLETE = 4;

  private int status = WAITING;

  private Party party;
  private Cell position;

  public void takeOff(){
  }
  
  public void land() {
  }

  public void collide() {
  }

  public void complete() {
  }

  public void setPositiion(Cell pos){
    position = pos;
  }
}
