public class GameState {
    private int gs;
    private String subState;
    //constructor for GameState
    public GameState(){gs=0;subState=null;}
    public GameState(int gs) {
        this.gs = gs;
        subState = null;
    }
    //getter for gs
    public int getGameState() {
        return gs;
    }
    public String getSubState() {
        return subState;
    }
    //setter for gs
    public void setGameState(int gs) {
        this.gs = gs;
    }
    public void setSubState(String s) {
        this.subState = s;
    }
}
