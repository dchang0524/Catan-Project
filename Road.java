public class Road {
    private Intersection i1;
    private Intersection i2;
    private Player owner;

    //constructor
    public Road(Intersection i1, Intersection i2, Player owner) {
        this.i1 = i1;
        this.i2 = i2;
        this.owner = owner;
        owner.getRoads().add(this);

    }
    public void setOwner(Road road, Player player) {
        if (owner == null) {
            owner = player;
        }
        //road.addRoad(player);
    }
    /*public void setOwner(Player player) {
        if (owner == null) {
            owner = player;
        }
    }*/
    public Player getOwner() {
        return owner;
    }

    public Intersection getI1() {
        return i1;
    }
    public Intersection getI2() {
        return i2;
    }
    //WIP





}



