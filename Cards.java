import javax.imageio.ImageIO;
import java.awt.image.*;
import java.util.*;
public class Cards {
    ArrayList<String> devCards = new ArrayList<>();
    static HashMap<String, BufferedImage> cardImages = new HashMap<String, BufferedImage>();
    HashMap<String, Integer> numDevCards = new HashMap<>();
    HashMap<String, Integer> numResourceCards = new HashMap<>();
    public Cards() {
        try {
            cardImages.put("brick", ImageIO.read(Cards.class.getResource("/resourceCards/brickCard.png")));
            cardImages.put("ore", ImageIO.read(Cards.class.getResource("/resourceCards/rockCard.png")));
            cardImages.put("sheep", ImageIO.read(Cards.class.getResource("/resourceCards/sheepCard.png")));
            cardImages.put("wheat", ImageIO.read(Cards.class.getResource("/resourceCards/wheatCard.png")));
            cardImages.put("wood", ImageIO.read(Cards.class.getResource("/resourceCards/woodCard.png")));
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
        numDevCards.put("university", 1);
        numDevCards.put("chapel", 1);
        numDevCards.put("library", 1);
        numDevCards.put("greatHall", 1);
        numDevCards.put("market", 1);
        //functional cards
        numDevCards.put("roadBuilding", 2);
        numDevCards.put("yearOfPlenty", 2);
        numDevCards.put("monopoly", 2);
        numDevCards.put("yearOfPlenty", 2);
        numDevCards.put("knight", 14);
    }

    public int resourceLeft(String resource) {
        return numResourceCards.get(resource);
    }
    public void putResource(String resource, int num) {
        numResourceCards.put(resource, numResourceCards.get(resource) + num);
    }

}
