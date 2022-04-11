import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CatanPanel extends JPanel implements MouseListener{
    GameState gs;
    BufferedImage startBackground, logo;
    public CatanPanel() {
        gs = new GameState();
        try{
            startBackground = ImageIO.read(CatanPanel.class.getResource("/miscPics/CatanBackground.png"));
            logo = ImageIO.read(CatanPanel.class.getResource("/miscPics/logo.png"));
        }
        catch(Exception e){
            System.out.println("Error loading image");
            return;
        }
        setFocusable(true);
        requestFocus();
        addMouseListener(this);

    }

    public void paint(Graphics g) {
        //gameState = 0
        g.drawImage(startBackground, 0, 0, 1900, 1000, null);
        g.drawImage(logo, 500, 100, null);
    }
    public void mousePressed(MouseEvent m) {

    }


    public void mouseReleased(MouseEvent m) {}
    public void mouseEntered(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}
    public void mouseClicked(MouseEvent m) {}
}
