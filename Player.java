import java.lang.reflect.Array;
import java.util.*;

public class Player {
    HashMap<String, Integer> resources = new HashMap<String, Integer>();
    HashMap<String, Integer>  devCards  = new HashMap<String, Integer>();
    HashMap<String, Integer> newDevCards = new HashMap<>();
    ArrayList<Settlement> settlements;
    int knightsUsed = 0;
    HashMap<String, Integer> developmentCards;
    int victoryPoints;
    int hiddenVP;
    HashMap<String, Integer> shopRatio;
    String color;
    ArrayList<Road> roads;
    Cards bank;
    int playerIndex;
    boolean longestRoad = false;
    boolean largestArmy = false;
    //constructor
    public Player(String name, Cards c, int i) {
        resources = new HashMap<String, Integer>();
        settlements = new ArrayList<Settlement>();
        knightsUsed = 0;
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

        devCards.put("university", 0);
        devCards.put("library", 0);
        devCards.put("roadBuilding", 0);
        devCards.put("yearOfPlenty", 0);
        devCards.put("monopoly", 0);
        devCards.put("knight", 0);

        newDevCards.put("university", 0);
        newDevCards.put("library", 0);
        newDevCards.put("roadBuilding", 0);
        newDevCards.put("yearOfPlenty", 0);
        newDevCards.put("monopoly", 0);
        newDevCards.put("knight", 0);
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
            bank.removeResource(resource, bank.resourceLeft(resource));
        }
        else {
            resources.put(resource, resources.get(resource) + amount);
            bank.removeResource(resource, amount);
        }

    }
    public void removeResource(String resource, int amount) {
        resources.put(resource, resources.get(resource) - amount);
        bank.putResource(resource, amount);
    }

    public void useKnight() {
        if (devCards.get("knight") > 0) {
            knightsUsed++;
            devCards.put("knight", devCards.get("knight") - 1);
        }
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
            if (temp1 != null && temp1.hasSettlement() == false && temp1.noAdjacentSettlement() == true) {
                settleAble.add(temp1);
            }
            if (temp2 != null && temp2.hasSettlement() == false && temp2.noAdjacentSettlement() == true) {
                settleAble.add(temp2);
            }
        }
        return settleAble;
    }

    public HashSet<Intersection> upgradeAble() {
        HashSet<Intersection> upgradeAble = new HashSet<Intersection>();
        for (int i = 0; i< settlements.size(); i++) {
            if (settlements.get(i).isCity == false) {
                upgradeAble.add(settlements.get(i).getPosition());
            }
        }
        return upgradeAble;
    }
    public void manageRoads() {
        for (int i = 0; i< roads.size(); i++) {
            for (int j = 0; j< roads.size(); j++) {
                if (i != j) {
                    roads.get(i).connect(roads.get(j));
                    roads.get(i).disconnect(roads.get(j));
                }
            }
        }
    }

    public void manageDevCards() {
        for (String card : newDevCards.keySet()) {
            if (newDevCards.get(card) > 0) {
                devCards.put(card, devCards.get(card) + newDevCards.get(card));
                newDevCards.put(card, 0);
            }
        }
        newDevCards = new HashMap<String, Integer>();
        newDevCards.put("university", 0);
        newDevCards.put("library", 0);
        newDevCards.put("roadBuilding", 0);
        newDevCards.put("yearOfPlenty", 0);
        newDevCards.put("monopoly", 0);
        newDevCards.put("knight", 0);
    }
    public HashSet<Intersection> possibleRoadI1() {
        HashSet<Intersection> possibleRoadI1 = new HashSet<Intersection>();
        for (int i = 0; i< roads.size(); i++) {
            if (roads.get(i).getI1() != null && roads.get(i).getI1().getNumRoads() <3 &&(
                    roads.get(i).getI1().hasSettlement() == false ||
                    roads.get(i).getI1().getSettlement().getOwner() == this)) {
                possibleRoadI1.add(roads.get(i).getI1());
            }
            if (roads.get(i).getI2() != null && roads.get(i).getI2().getNumRoads() <3 &&(
                    roads.get(i).getI2().hasSettlement() == false ||
                    roads.get(i).getI2().getSettlement().getOwner() == this)) {
                possibleRoadI1.add(roads.get(i).getI2());
            }
        }
        return possibleRoadI1;
    }

    public HashSet<Intersection> possibleRoadI2(Intersection i) {
        HashSet<Intersection> possibleRoadI2 = new HashSet<Intersection>();
        if (i.r1 == null ) {
            possibleRoadI2.add(i.i1);
        }
        if (i.r2 == null) {
            possibleRoadI2.add(i.i2);
        }
        if (i.r3 == null) {
            possibleRoadI2.add(i.i3);
        }
        return possibleRoadI2;
    }


    public int longestRoadLength() {
        int longest = 0;
        manageRoads();
        for (Road r : roads) {
            if (r.getHeight() > longest) {
                longest = r.getHeight();
            }
        }
        return longest;
    }




    public boolean enoughResourcesRoad() {
        if (resources.get("brick") >= 1 && resources.get("wood") >= 1) {
            if (this.roadsLeft() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean enoughResourcesSettlement() {
        if (resources.get("brick") >= 1 && resources.get("wood") >= 1 && resources.get("wheat") >= 1 && resources.get("sheep") >= 1) {
            if (this.settlementsLeft() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean enoughResourcesCity() {
        if (resources.get("ore") >= 3 && resources.get("wheat") >= 2) {
            if (this.citiesLeft() > 0) {
                return true;
            }
        }
        return false;
    }
    public boolean enoughResourcesCard() {
        if (resources.get("wheat") >= 1 && resources.get("sheep") >= 1 && resources.get("ore") >= 1) {
            if (Cards.numDevCards.size() > 0) {
                return true;
            }
        }
        return false;
    }
    // get victory points
    public int updateVP() {
        int points = 0;
        for (Settlement s : settlements) {
            points += 1;
            if (s.isCity()) {
                points+=1;
            }
        }
        if (longestRoad) {
            points += 2;
        }
        if (largestArmy) {
            points += 2;
        }
        victoryPoints = points;
        return victoryPoints;
    }
    public int updateHiddenVP() {
        int points = 0;
        points += devCards.get("library");
        points += devCards.get("university");
        points += newDevCards.get("library");
        points += newDevCards.get("university");
        hiddenVP = points;
        return hiddenVP;
    }
    public int roadsLeft() {
        return 15 - roads.size();
    }
    public int settlementsLeft() {
        int start = 5;
        for (Settlement s : settlements) {
            if (s.isCity() == false) {
                start--;
            }
        }
        return start;
    }
    public int citiesLeft() {
        int start = 4;
        for (Settlement s : settlements) {
            if (s.isCity() == true) {
                start--;
            }
        }
        return start;
    }
    public int totalVP() {
        updateVP();
        updateHiddenVP();
        return victoryPoints + hiddenVP;
    }
    public int totalResources() {
        int total = 0;
        for (String s : resources.keySet()) {
            total += resources.get(s);
        }
        return total;
    }
    public int totalDevCards() {
        int total = 0;
        for (String s : devCards.keySet()) {
            total += devCards.get(s);
        }
        return total;
    }
}





