import java.util.*;

public class PlayerManager {
    ArrayList<Player> players;
    int currentPlayer;
    Cards bank;
    Player largestArmy;
    Player longestRoad;
    public PlayerManager(int k, Cards cards) {
        bank = cards;
        players = new ArrayList<>();
        ArrayList<String> possColors = new ArrayList<>();
        possColors.add("red");
        possColors.add("blue");
        possColors.add("white");
        possColors.add("yellow");
        for (int i = 0; i < k; i++) {
            String c = possColors.remove((int)(Math.random() * possColors.size()));
            players.add(new Player(c, bank, i));
        }
    }
    public Player get(int i) {
        return players.get(i);
    }
    public int size() {
        return players.size();
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
    public void prevPlayer() {
        currentPlayer = (currentPlayer - 1 + players.size()) % players.size();
    }

    public Player toStringReverse(String s) {
        if (s.equals("Player 0")) {
            return players.get(0);
        }
        else if (s.equals("Player 1")) {
            return players.get(1);
        }
        else if (s.equals("Player 2")) {
            return players.get(2);
        }
        else if (s.equals("Player 3")) {
            return players.get(3);
        }
        return null;
    }

    public String steal(Player A, Player toSteal) {
        int rand = (int)(Math.random() * toSteal.getInventorySize());
        int current = 0;
        for (String resource : toSteal.getResources().keySet()) {
            int quantity = toSteal.getResources().get(resource);
            current = current + quantity;
            if (current >= rand) {
                toSteal.removeResource(resource, 1);
                A.addResource(resource, 1);
                return resource;
            }
        }
        return null;
    }

    public void trade(Player A, Player B, HashMap<String, Integer> resource, HashMap<String, Integer> resource2) {
        for (String resourceName : resource.keySet()) {
            int quantity = resource.get(resourceName);
            A.removeResource(resourceName, quantity);
        }
        for (String resourceName : resource2.keySet()) {
            int quantity = resource2.get(resourceName);
            B.removeResource(resourceName, quantity);
        }

        for (String resourceName : resource.keySet()) {
            int quantity = resource.get(resourceName);
            B.addResource(resourceName, quantity);
        }
        for (String resourceName : resource2.keySet()) {
            int quantity = resource2.get(resourceName);
            A.addResource(resourceName, quantity);
        }
    }
}
