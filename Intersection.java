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
        portResource = "";
    }

    public void setI1(Intersection i) {
        i1 = i;
        i.setI1Help(this);
    }
    public void setI1Help(Intersection i) {
        i1 = i;
    }

    public void setI2(Intersection i) {
        i2 = i;
        i.setI2Help(this);
    }
    public void setI2Help(Intersection i) {
        i2 = i;
    }

    public void setI3(Intersection i) {
        i3 = i;
        i.setI3Help(this);
    }
    public void setI3Help(Intersection i) {
        i3 = i;
    }

    public void setSettlement(Player p) {
        this.s = new Settlement(this, p);
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

}
