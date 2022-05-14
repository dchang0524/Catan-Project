public class Robber {
    Tile position;
    public Robber(Tile position) {
        this.position = position;
    }
    public Robber() {
        position = null;
    }
    public void setPosition(Tile position) {
        if (position != null) {
            position.robber = false;
        }
        this.position = position;
        position.setRobber(true);
    }
    public Tile getPosition() {
        return position;
    }
}
