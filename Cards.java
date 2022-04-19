import javax.imageio.ImageIO;
import java.awt.image.*;
import java.util.*;
public class Cards {
    ArrayList<String> devCards;
    HashMap<String, BufferedImage> cardImages;
    HashMap<String, Integer> numDevCards;
    HashMap<String, Integer> numResourceCards;
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

        numDevCards.put("university", 1);
        numDevCards.put("roadBuilding", 1);
        numDevCards.put("yearOfPlenty", 3);
        numDevCards.put("sheep", 19);
        numDevCards.put("wheat", 19);
        numDevCards.put("wood", 19);
    }
}
