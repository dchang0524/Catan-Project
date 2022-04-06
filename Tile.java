import java.util.ArrayList;

public class Tile {
    Tile t1, t2, t3, t4, t5, t6; //start at upper right and go clockwise
    int number; //number on tile
    int xCoord;
    int yCoord;
    String resource;
    ArrayList<Settlement> settles; //start at top vertex and go clockwise
    boolean robber;


    public Tile(int x, int y, int num, String res) {
        xCoord = x;
        yCoord = y;
        number = num;
        resource = res;
        t1 = null;
        t2 = null;
        t3 = null;
        t4 = null;
        t5 = null;
        t6 = null;
        settles = new ArrayList<>();
        robber = false;
    }
    //getters and setters
    public boolean hasRobber() {
        return robber;
    }
    public void setRobber(boolean b) {robber = b;
    }
    public int getNumber(int i) {
        return number;
    }
    //getters and setters
    public int getX() {
        return xCoord;
    }
    public int getY() {
        return yCoord;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String res) {
        resource = res;
    }
    public void setNumber(int num) {
        number = num;
    }
    public void setT1(Tile t) {
        t1 = t;
    }

}
