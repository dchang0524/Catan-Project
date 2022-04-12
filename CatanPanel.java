import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class CatanPanel extends JPanel implements MouseListener{
    GameState gs;
    BufferedImage startBackground, logo;
    Board board;
    PlayerManager pm;
    File file;

    public CatanPanel() {
        gs = new GameState();
        try{
            startBackground = ImageIO.read(CatanPanel.class.getResource("/misc/CatanBackground.png"));
            logo = ImageIO.read(CatanPanel.class.getResource("/misc/logo.png"));
            file = new File("/misc/CatanRules.pdf");
        }
        catch(Exception e){
            System.out.println("Error loading image");
            return;
        }
        board = new Board();
        setFocusable(true);
        requestFocus();
        addMouseListener(this);

    }

    public void paint(Graphics g) {
        //gameState = 0
        if(gs.getGameState() == 0) {
            menuScreen(g);
        }
        if(gs.getGameState() == 1) {
            g.clearRect(0, 0, 1900, 1000);
        }

    }
    public void mousePressed(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
        if(x > 800 && x < 1100 && y > 500 && y < 600) {
            gs.setGameState(1);
        }
        //help
        if(x > 800 && x < 1100 && y > 650 && y < 750) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if(x > 800 && x < 1100 && y > 800 && y < 900) {
            System.exit(0);
        }
        repaint();
    }
    public void menuScreen(Graphics g) {
        g.drawImage(startBackground, 0, 0, 1900, 1000, null);
        g.drawImage(logo, 475, 100, null);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(800, 500, 300, 100, 20, 20);
        g.fillRoundRect(800, 500, 300, 100, 20, 20);
        g.setColor(Color.YELLOW);
        g.drawString("Start Game", 843, 565);
        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(800, 650, 300, 100, 20, 20);
        g.fillRoundRect(800, 650, 300, 100, 20, 20);
        g.setColor(Color.YELLOW);
        g.drawString("Help", 910, 715);
        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(800, 800, 300, 100, 20, 20);
        g.fillRoundRect(800, 800, 300, 100, 20, 20);
        g.setColor(Color.YELLOW);
        g.drawString("Exit", 910, 865);
        g.setColor(Color.DARK_GRAY);
    }
    public void drawBoard(Graphics g) {
        g.drawImage(board.tiles[0][1].getImage(), 400, 400, null);
    }



    public void mouseReleased(MouseEvent m) {}
    public void mouseEntered(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}
    public void mouseClicked(MouseEvent m) {}
}
