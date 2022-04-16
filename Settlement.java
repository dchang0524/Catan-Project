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
}
