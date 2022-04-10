public class GameState {
    static int gameState;
    //0 = initial set up, 1 = round start and trading, 2 = build/buy phase, 3 = game end
    public GameState(){}
    public GameState(int state){
        gameState = state;
    }
    public static void setGameState(int state){
        gameState = state;
    }
    public static int getGameState(){
        return gameState;
    }
}
