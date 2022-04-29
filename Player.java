import java.lang.reflect.Array;
import java.util.*;

public class Player {
    HashMap<String, Integer> resources = new HashMap<String, Integer>();
    HashMap<String, Integer> devCards = new HashMap<String, Integer>();
    ArrayList<Settlement> settlements;
    int KnightsUsed;
    HashMap<String, Integer> developmentCards;
    int victoryPoints;
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
        shopRatio.put("brick", 4);
        shopRatio.put("ore", 4);
        shopRatio.put("sheep", 4);
        shopRatio.put("wheat", 4);
        shopRatio.put("wood", 4);
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

    public HashSet<Intersection> settleAble() { //returns all intersections that settlements can be placed on
        HashSet<Intersection> settleAble = new HashSet<Intersection>();
        for (int i = 0; i< roads.size(); i++) {
            Intersection temp1 = roads.get(i).getI1();
            Intersection temp2 = roads.get(i).getI2();
            if (temp1 != null && temp1.hasSettlement() == false) {
                settleAble.add(temp1);
            }
            if (temp2 != null && temp2.hasSettlement() == false) {
                settleAble.add(temp2);
            }
        }
        return settleAble;
    }

    //TODO: add method that returns all intersections of settlements that can be upgraded to cities
    //TODO: add method that connects all roads to each other if it is supposed to be connected
    //TODO: add method to return Intersections where new roads can branch out from
    //TODO: add method to return Intersections for second endpoint of the selected intersection
    public int longestRoadLength() {
        int longest = 0;
        for (Road r : roads) {
            if (r.getHeight() > longest) {
                longest = r.getHeight();
            }
        }
        return longest;
    }
}





