import javax.imageio.ImageIO;
import java.awt.image.*;
import java.util.*;
public class Cards {
    ArrayList<String> devCards = new ArrayList<>();
    static HashMap<String, BufferedImage> cardImages = new HashMap<String, BufferedImage>();
    static HashMap<String, Integer> numDevCards = new HashMap<>();
    static HashMap<String, Integer> numResourceCards = new HashMap<>();
    public Cards() {
        try {
            cardImages.put("brick", ImageIO.read(Cards.class.getResource("/resourceCards/brickCard.png")));
            cardImages.put("ore", ImageIO.read(Cards.class.getResource("/resourceCards/rockCard.png")));
            cardImages.put("sheep", ImageIO.read(Cards.class.getResource("/resourceCards/sheepCard.png")));
            cardImages.put("wheat", ImageIO.read(Cards.class.getResource("/resourceCards/wheatCard.png")));
            cardImages.put("wood", ImageIO.read(Cards.class.getResource("/resourceCards/woodCard.png")));

            cardImages.put("longestRoad", ImageIO.read(Cards.class.getResource("/DevCards/LongestRoadCard.png")));
            cardImages.put("largestArmy", ImageIO.read(Cards.class.getResource("/DevCards/LargestArmyCard.png")));

            cardImages.put("knight", ImageIO.read(Cards.class.getResource("/DevCards/KnightCard.png")));
            cardImages.put("roadBuilding", ImageIO.read(Cards.class.getResource("/DevCards/RoadBuildingCard.png")));
            cardImages.put("monopoly", ImageIO.read(Cards.class.getResource("/DevCards/MonopolyCard.png")));
            cardImages.put("yearOfPlenty", ImageIO.read(Cards.class.getResource("/DevCards/YearOfPlentyCard.png")));

            cardImages.put("university", ImageIO.read(Cards.class.getResource("/DevCards/UniversityCard.png")));
            cardImages.put("library", ImageIO.read(Cards.class.getResource("/DevCards/LibraryCard.png")));
        }
        catch(Exception e){
            System.out.println("Error loading image");
            return;
        }
        numResourceCards.put("brick", 19);
        numResourceCards.put("ore", 19);
        numResourceCards.put("sheep", 19);
        numResourceCards.put("wheat", 19);
        numResourceCards.put("wood", 19);

        //victory point cards
        numDevCards.put("university", 3);
        //numDevCards.put("chapel", 1);
        numDevCards.put("library", 2);
       // numDevCards.put("greatHall", 1);
        //numDevCards.put("market", 1);
        //functional cards
        numDevCards.put("roadBuilding", 2);
        numDevCards.put("yearOfPlenty", 2);
        numDevCards.put("monopoly", 2);
        numDevCards.put("knight", 14);
    }

    public int resourceLeft(String resource) {
        return numResourceCards.get(resource);
    }
    public void putResource(String resource, int num) {
        numResourceCards.put(resource, numResourceCards.get(resource) + num);
    }
    public void removeResource(String resource, int num) {
        numResourceCards.put(resource, numResourceCards.get(resource) - num);
    }

    //TODO: give random dev card
    public static void giveDevCard(Player p) {
        int total = 0;
        for (String key : numDevCards.keySet()) {
            total += numDevCards.get(key);
        }
        int rand = (int) (Math.random() * total);
        int count = 0;
        for (String key : numDevCards.keySet()) {
            count += numDevCards.get(key);
            if (count >= rand) {
                p.newDevCards.put(key, p.newDevCards.get(key) + 1);
                numDevCards.put(key, numDevCards.get(key) - 1);
                System.out.println("Gave " + key + " to " + p);
                System.out.println("Remaining: " + numDevCards);
                return;
            }
        }
    }

    public static int totalDevCardCount() {
        int total = 0;
        for (String key : numDevCards.keySet()) {
            total += numDevCards.get(key);
        }
        return total;
    }
}
