import java.util.ArrayList;

public class Intersection {
    Intersection i1;
    Intersection i2;
    Intersection i3;                                           //        i3
    Settlement s;                                             //   T1    i      T2
    Road r1;                                             //        i2   T3   i1
    Road r2;
    Road r3;
    Tile t1;
    Tile t2;
    Tile t3;
    int x;
    int y;
    int portTrade;
    String portResource;

    public Intersection() {
        i1 = null;
        i2 = null;
        i3 = null;
        s = null;
        r1 = null;
        r2 = null;
        r3 = null;
        t1 = null;
        t2 = null;
        t3 = null;
        portTrade = 0;
        portResource = null;
    }

    public void setPortTrade(int trade) {
        portTrade = trade;
    }
    public void setPortResource(String resource) {
        portResource = resource;
    }

    public void setI1(Intersection i) {
        i1 = i;
        if (i != null) {
            i.setI1Help(this);
        }
    }
    public void setI1Help(Intersection i) {
        if (i != null) {
            i1 = i;
        }
    }

    public void setI2(Intersection i) {
        i2 = i;
        if (i != null) {
            i.setI2Help(this);
        }
    }
    public void setI2Help(Intersection i) {
        if (i != null) {
            i2 = i;
        }
    }

    public void setI3(Intersection i) {
        i3 = i;
        if (i != null) {
            i.setI3Help(this);
        }
    }
    public void setI3Help(Intersection i) {
        if (i != null) {
            i3 = i;
        }
    }
    public boolean noAdjacentSettlement() {
        if ((i1 == null || i1.hasSettlement() == false) && (i2 == null || i2.hasSettlement() == false) && (i3 == null || i3.hasSettlement() == false)) {
            return true;
        }
        return false;
    }
    public ArrayList<Intersection> getAdjacentIntersections() {
        ArrayList<Intersection> adj = new ArrayList<Intersection>();
        if (i1 != null) {
            adj.add(i1);
        }
        if (i2 != null) {
            adj.add(i2);
        }
        if (i3 != null) {
            adj.add(i3);
        }
        return adj;
    }
    public boolean hasSettlement() {
        if (s != null) {
            return true;
        }
        return false;
    }
    public void setSettlement(Player p) {
        this.s = new Settlement(this, p);
    }
    public Settlement getSettlement() {
        return s;
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
    public void setR1(Road r) {
        r1 = r;
    }
    public void setR2(Road r) {
        r2 = r;
    }
    public void setR3(Road r) {
        r3 = r;
    }
    public Road getR1() {
        return r1;
    }
    public Road getR2() {
        return r2;
    }
    public Road getR3() {
        return r3;
    }
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public int getX() {return x;}
    public int getY() {return y;}
    public Intersection getI1() {return i1;}
    public Intersection getI2() {return i2;}
    public Intersection getI3() {return i3;}

    public String toString() {
        return x + " " + y;
    }
}
