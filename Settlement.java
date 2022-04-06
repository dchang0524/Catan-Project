public class Settlement {
    boolean isCity;
    Intersection position;
    Tile t1, t2, t3;
    Player owner;
    public Settlement(Intersection pos, Player p) {
        position = pos;
        t1 = pos.t1;
        t2 = pos.t2;
        t3 = pos.t3;
        isCity = false;
        owner = p;
    }
    public void upgrade() {
        isCity = true;
    }
    public Player getOwner() {
        return owner;
    }
}
