import java.util.*;
/*
public class Player {
    HashMap<String, Integer> inventory;
    HashSet<Settlement> settlements;
    int KnightsUsed;
    HashMap<String, Integer> developmentCards;
    int victoryPoints;
    boolean hasLargestArmy;
    boolean hasLongestRoad;
    HashMap<String, Integer> shopRatio;
    String playerName;

    //constructor
    public Player(String name) {
        inventory = new HashMap<String, Integer>();
        settlements = new HashSet<Settlement>();
        KnightsUsed = 0;
        developmentCards = new HashMap<String, Integer>();
        victoryPoints = 0;
        hasLargestArmy = false;
        hasLongestRoad = false;
        shopRatio = new HashMap<String, Integer>();
        playerName = name;
    }
    //TO DO: fix this
    public void addToInventory(String resource, int amount) {
        if (resource == "Desert") {
            return;
        }
        inventory.put(resource, inventory.get(resource) + amount);
    }

    public void removeFromInventory(String resource, int amount) {
        inventory.put(resource, inventory.get(resource) - amount);
    }

    public void addToSettlements(Settlement settlement) {
        settlements.add(settlement);
    }

    public void removeFromSettlements(Settlement settlement) {
        settlements.remove(settlement);
    }

    public void addToDevelopmentCards(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) + amount);
        if (card.equals("Victory Point")) {
            victoryPoints++;
        }
    }

    public void removeFromDevelopmentCards(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) - amount);
    }

    public void addToShopRatio(String resource, int amount) {
        shopRatio.put(resource, shopRatio.get(resource) + amount);
    }

    public void removeFromShopRatio(String resource, int amount) {
        shopRatio.put(resource, shopRatio.get(resource) - amount);
    }

    public int getInventory(String resource) {
        return inventory.get(resource);
    }

    public int getKnightCount() {
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

    public boolean getLargestArmy() {
        return hasLargestArmy;
    }

    public boolean getLongestRoad() {
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

    public void setShopRatio(String resource, int amount) {
        shopRatio.put(resource, amount);
    }

    public void addKnight() {
        KnightsUsed++;
    }

    public void removeKnight() {
        KnightsUsed--;
    }

    public void addVictoryPoints(int points) {
        victoryPoints += points;
    }

    public void removeVictoryPoints(int points) {
        victoryPoints -= points;
    }

    public void addDevelopmentCard(String card) {
        developmentCards.put(card, developmentCards.get(card) + 1);
    }

    public void addToKnightsUsed(int amount) {
        KnightsUsed += amount;
    }

    public void removeFromKnightsUsed(int amount) {
        KnightsUsed -= amount;
    }

    public void addToVictoryPoints(int amount) {
        victoryPoints += amount;
    }

    public void removeFromVictoryPoints(int amount) {
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

    public HashSet<Settlement> getSettlements() {
        return settlements;
    }

    public int getKnightsUsed() {
        return KnightsUsed;
    }

    public HashMap<String, Integer> getDevelopmentCards() {
        return developmentCards;
    }

    public boolean getHasLargestArmy() {
        return hasLargestArmy;
    }

    public boolean getHasLongestRoad() {
        return hasLongestRoad;
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

    public int getTotalKnightsUsed() {
        return KnightsUsed;
    }

    public int getTotalVictoryPoints() {
        return victoryPoints;
    }

    public int getTotalSettlements() {
        return settlements.size();
    }

    public HashMap<String, Integer> getTotalDevelopmentCards() {
        HashMap<String, Integer> total = new HashMap<>();
        for (String card : developmentCards.keySet()) {
            total.put(card, developmentCards.get(card));
        }
        return total;
    }

    public void addResource(String resource, int amount) {
        inventory.put(resource, inventory.get(resource) + amount);
    }

    public void removeResource(String resource, int amount) {
        inventory.put(resource, inventory.get(resource) - amount);
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
    }

    public void removeSettlement(Settlement settlement) {
        settlements.remove(settlement);
    }

    public void addDevelopmentCard(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) + amount);
    }

    public void removeDevelopmentCard(String card, int amount) {
        developmentCards.put(card, developmentCards.get(card) - amount);
    }

    public void addKnight(int amount) {
        KnightsUsed += amount;
    }

    public void removeAllResources() {
        inventory.clear();
    }

    public void removeAllDevelopmentCards() {
        developmentCards.clear();
    }

    public void removeAllShopRatio() {
        shopRatio.clear();
    }

    public void removeAllSettlements() {
        settlements.clear();
    }

    public void removeAll() {
        removeAllResources();
        removeAllDevelopmentCards();
        removeAllShopRatio();
        removeAllSettlements();
    }

    public void removeAllExcept(String resource) {
        HashMap<String, Integer> temp = new HashMap<>();
        temp.put(resource, inventory.get(resource));
        inventory = temp;
    }

    public boolean hasLargestArmy() {
        return hasLargestArmy;
    }

    public boolean hasCard(String card) {
        for (String cardName : developmentCards.keySet()) {
            if (cardName.equals(card)) {
                return true;
            }
        }
        return false;
    }
}
    */




