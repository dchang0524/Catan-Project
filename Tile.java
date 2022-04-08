import java.util.ArrayList;

public class Tile {
    Tile t1, t2, t3, t4, t5, t6; //start at upper right and go clockwise
    int number; //number on tile
    int xCoord;
    int yCoord;
    String resource;
    ArrayList<Settlement> settles; //start at top vertex and go clockwise
    boolean robber;
    BufferedImage image;
    public Tile(String res, BufferedImage img) {
        resource = res;
        xCoord = -1;
        yCoord = -1;
        number = -1;
        resource = res;
        t1 = null;
        t2 = null;
        t3 = null;
        t4 = null;
        t5 = null;
        t6 = null;
        settles = new ArrayList<Settlement>();
        robber = false;
    }

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
        settles = new ArrayList<Settlement>();
        robber = false;
    }
    //getters and setters
    public boolean hasRobber() {
        return robber;
    }
    public void setRobber(boolean b) {
        robber = b;
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
    public void setT2(Tile t) {
        t2 = t;
    }
    public void setT3(Tile t) {
        t3 = t;
    }
    public void setT4(Tile t) {
        t4 = t;
    }
    public void setT5(Tile t) {
        t5 = t;
    }
    public void setT6(Tile t) {
        t6 = t;
    }

}
