import java.lang.reflect.Array;
import java.util.*;

public class Player {
    HashMap<String, Integer> inventory;
    ArrayList<Settlement> settlements;
    int KnightsUsed;
    HashMap<String, Integer> developmentCards;
    int victoryPoints;
    boolean hasLargestArmy;
    boolean hasLongestRoad;
    HashMap<String, Integer> shopRatio;
    String color;
    ArrayList<Road> roads;

    //constructor
    public Player(String name) {
        inventory = new HashMap<String, Integer>();
        settlements = new ArrayList<Settlement>();
        KnightsUsed = 0;
        developmentCards = new HashMap<String, Integer>();
        victoryPoints = 0;
        hasLargestArmy = false;
        hasLongestRoad = false;
        shopRatio = new HashMap<String, Integer>();
        color = name;
        roads = new ArrayList<Road>();
    }
    public ArrayList<Road> getRoads(){
        return roads;
    }

    public void addToInventory(String resource, int amount) {
        if (resource == "Desert") {
            return;
        }
        inventory.put(resource, inventory.get(resource) + amount);
    }

    public void removeFromInventory(String resource, int amount) {
        inventory.put(resource, inventory.get(resource) - amount);
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
    }

    public void addToDevelopmentCards(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) + amount);
    }

    public void removeFromDevelopmentCards(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) - amount);
    }

    public int getInventory(String resource) {
        return inventory.get(resource);
    }

    public int getKnightsUsed() {
        return KnightsUsed;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getDevelopmentCards(String card) {
        return developmentCards.get(card);
    }

    public int getShopRatio(String resource) {
        return shopRatio.get(resource);
    }

    public boolean hasLargestArmy() {
        return hasLargestArmy;
    }

    public boolean hasLongestRoad() {
        return hasLongestRoad;
    }

    public void setLargestArmy(boolean largestArmy) {
        hasLargestArmy = largestArmy;
    }

    public void setLongestRoad(boolean longestRoad) {
        hasLongestRoad = longestRoad;
    }

    public void setKnightCount(int knights) {
        KnightsUsed = knights;
    }

    public void setVictoryPoints(int points) {
        victoryPoints = points;
    }

    public void setDevelopmentCards(String card, int amount) {
        developmentCards.put(card, amount);
    }

    public void addKnight() {
        KnightsUsed++;
    }

    public void addVictoryPoints(int points) {
        victoryPoints += points;
    }

    public void addDevelopmentCard(String card) {
        developmentCards.put(card, developmentCards.get(card) + 1);
    }

    public void addToKnightsUsed(int amount) {
        KnightsUsed += amount;
    }

    public void removeVictoryPoints(int amount) {
        victoryPoints -= amount;
    }

    public void setHasLargestArmy(boolean hasLargestArmy) {
        this.hasLargestArmy = hasLargestArmy;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public ArrayList<Settlement> getSettlements() {
        return settlements;
    }

    public HashMap<String, Integer> getDevelopmentCards() {
        return developmentCards;
    }

    public HashMap<String, Integer> getShopRatio() {
        return shopRatio;
    }

    public int getResourceCount(String resource) {
        return inventory.get(resource);
    }

    public int getDevelopmentCardCount(String card) {
        return developmentCards.get(card);
    }

    public int getTotalResourceCount() {
        int total = 0;
        for (String resource : inventory.keySet()) {
            total += inventory.get(resource);
        }
        return total;
    }

    public int getTotalDevelopmentCardCount() {
        int total = 0;
        for (String card : developmentCards.keySet()) {
            total += developmentCards.get(card);
        }
        return total;
    }
    //set
    public void setInventory(HashMap<String, Integer> inventory) {
        this.inventory = inventory;
    }
    //set
    public void setSettlements(ArrayList<Settlement> settlements) {
        this.settlements = settlements;
    }
    //set
    public void setDevelopmentCards(HashMap<String, Integer> developmentCards) {
        this.developmentCards = developmentCards;
    }
    public int getTotalKnightsUsed() {
        return KnightsUsed;
    }

    public int getTotalVictoryPoints() {
        return victoryPoints;
    }

    public int getNumSettlements() {
        return settlements.size();
    }

    public void addResource(String resource, int amount) {
        inventory.put(resource, inventory.get(resource) + amount);
    }

    public void removeResource(String resource, int amount) {
        inventory.put(resource, inventory.get(resource) - amount);
    }

    public void addDevelopmentCard(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) + amount);
    }

    public void removeDevelopmentCard(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) - amount);
    }

    public void useKnight(int amount) {
        developmentCards.put("knight", developmentCards.get("knight") - amount);
        KnightsUsed += amount;
    }

    public boolean hasDevCard(String card) {
        for (String cardName : developmentCards.keySet()) {
            if (cardName.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public String getColor() {
        return color;
    }
}





