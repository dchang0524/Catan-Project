import java.lang.reflect.Array;
import java.util.*;

public class Player {
    HashMap<String, Integer> resources = new HashMap<String, Integer>();
    HashMap<String, Integer> devCards = new HashMap<String, Integer>();
    ArrayList<Settlement> settlements;
    int KnightsUsed;
    HashMap<String, Integer> developmentCards;
    int victoryPoints;
    boolean hasLargestArmy;
    boolean hasLongestRoad;
    HashMap<String, Integer> shopRatio;
    String color;
    ArrayList<Road> roads;
    Cards bank;
    int playerIndex;

    //constructor
    public Player(String name, Cards c, int i) {
        resources = new HashMap<String, Integer>();
        settlements = new ArrayList<Settlement>();
        KnightsUsed = 0;
        developmentCards = new HashMap<String, Integer>();
        victoryPoints = 0;
        hasLargestArmy = false;
        hasLongestRoad = false;
        shopRatio = new HashMap<String, Integer>();
        color = name;
        roads = new ArrayList<Road>();
        bank = c;
        resources.put("brick", 0);
        resources.put("ore", 0);
        resources.put("sheep", 0);
        resources.put("wheat", 0);
        resources.put("wood", 0);
        playerIndex = i;
    }
    public void setPlayerIndex(int i) {
        playerIndex = i;
    }
    public String toString() {
        return "Player " + playerIndex;
    }
    public int getInventorySize() {
        int total = 0;
        for (String resource : resources.keySet()) {
            total += resources.get(resource);
        }
        return total;
    }
    public ArrayList<Road> getRoads(){
        return roads;
    }

    public ArrayList<Settlement> getSettlements(){
        return settlements;
    }
    public void addResource(String resource, int amount) {
        if (amount > bank.resourceLeft(resource)) {
            resources.put(resource, resources.get(resource) + bank.resourceLeft(resource));
        }
        else {
            resources.put(resource, resources.get(resource) + amount);
        }

    }
    public void removeResource(String resource, int amount) {
        resources.put(resource, resources.get(resource) - amount);
        bank.putResource(resource, amount);
    }
    public String getColor() {
        return color;
    }

    public int getResourceCount(String resource) {
        return resources.get(resource);
    }
    public HashMap<String, Integer> getResources() {
        return resources;
    }



}





