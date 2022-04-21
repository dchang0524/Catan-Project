public class Settlement {
    boolean isCity;
    Intersection position;
    Tile t1, t2, t3;
    Player owner;
    int xCoord;
    int yCoord;

    public Settlement(Intersection pos, Player p) {
        position = pos;
        t1 = pos.t1;
        t2 = pos.t2;
        t3 = pos.t3;
        isCity = false;
        owner = p;
        xCoord = pos.getX();
        yCoord = pos.getY();
        p.getSettlements().add(this);
    }
    public void upgrade() {
        isCity = true;
    }
    public Player getOwner() {
        return owner;
    }
    public void setX(int x) {
        xCoord = x;
    }
    public void setY(int y) {
        yCoord = y;
    }
    public int getX() {
        return xCoord;
    }
    public int getY() {
        return yCoord;
    }
    public String toString() {
        return "Settlement at " + position.toString();
    }
    public Intersection getPosition() {
        return position;
    }
    public void giveAllResource() {
        int amount = 1;
        if (isCity) {
            amount = 2;
        }
        if (t1 != null && !t1.resource.equals("desert")) {
            owner.addResource(t1.getResource(),amount);
        }
        if (t2 != null && !t2.resource.equals("desert")) {
            owner.addResource(t2.getResource(),amount);
        }
        if (t3 != null && !t3.resource.equals("desert")) {
            owner.addResource(t3.getResource(),amount);
        }
    }
    public void giveResource(Tile t) {
        if (isCity == false) {
            owner.addResource(t.getResource(),1);
        }
        else {
            owner.addResource(t.getResource(),2);
        }
    }
    public void distributeAllResources() {
        if (t1 != null) {
            owner.addResource(t1.getResource(), 1);
        }
        else if (t2 != null) {
            owner.addResource(t2.getResource(), 1);
        }
        else if (t3 != null) {
            owner.addResource(t3.getResource(), 1);
        }
    }
}
