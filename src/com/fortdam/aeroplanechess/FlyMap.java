
class Cell{
  Cell (Party party){
    area = party;
  }
  private static int increment = 0;
  private int id = increment++;
  private Party area;
  public Cell shortHaul;
  public Cell longHaul;
}

public class FlyMap{
  public getFlyMap() {
    return mapInst;
  }


  private static FlyMap mapInst = new FlyMap();
  private FlyMap(){
    /*Initiate the map*/
    for (int i=0; i<Party.PARTY_NUM; i++){
      for (int j=0; j<(4*3+1); j++){
        int index = (4*3+1)*i+j
        cells[index] = new Cell(Party.getParty(index%Party.PARTY_NUM));
      }
    }

    /*Set short haul*/
    for (int i=0; i<cells.length; i++){
      if (i%10 > 0){
        cells[i].shortHaul = cells[(i+Party.PARTY_NUM)%cells.length];
      }
    }

    /*Set long haul*/
    for (int i=4; i<cells.length; i+=13){
      cells[i].longHaul = cells[i+12];
    }
  };
}
