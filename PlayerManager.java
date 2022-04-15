import java.util.*;

public class PlayerManager {
    ArrayList<Player> players;
    int currentPlayer;
    public PlayerManager(int k) {
        players = new ArrayList<>();
        ArrayList<String> possColors = new ArrayList<>();
        possColors.add("red");
        possColors.add("blue");
        possColors.add("white");
        possColors.add("yellow");
        for (int i = 0; i < k; i++) {
            String c = possColors.remove((int)(Math.random() * possColors.size()));
            players.add(new Player(c));
        }
    }

    public Player curentPlayer() {
        return players.get(currentPlayer);
    }
    public int currentPlayerIndex() {
        return currentPlayer;
    }
    public void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }

}
