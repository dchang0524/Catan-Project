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
    Tile[][] tiles;
    Dimension dim;
    public CatanPanel() {
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        gs = new GameState();
        try{
            startBackground = ImageIO.read(CatanPanel.class.getResource("/misc/CatanBackground.png"));
            logo = ImageIO.read(CatanPanel.class.getResource("/misc/logo.png"));
        }
        catch(Exception e){
            System.out.println("Error loading image");
            return;
        }
        board = new Board();
        tiles = board.getTiles();

        addMouseListener(this);

    }

    public void paint(Graphics g) {
        //gameState = 0
        if(gs.getGameState() == 0) {
            menuScreen(g);
        }
        if(gs.getGameState() == 1) {
            drawBoard(g);
        }

    }
    public void mousePressed(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();


        if(x > dim.width/2 - 100 && x < dim.width/2 + 100 && y > dim.height/2  && y < dim.height/2 + 80) {
            gs.setGameState(1);
        }
        if(x > dim.width/2 - 60 && x < dim.width/2 + 140 && y > dim.height/2 + 125 && y < dim.height/2 + 225) {
            if (Desktop.isDesktopSupported()) {
                try {
                    File file = new File(this.getClass().getResource("misc/CatanRules.pdf").getFile());
                    Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error opening file");
                }
            }
        }
        if(x > dim.width/2 - 60 && x < dim.width/2 + 140 && y > dim.height/2 + 250 && y < dim.height/2 + 333) {
            System.exit(0);
        }
        repaint();
    }
    public void menuScreen(Graphics g) {
        g.drawImage(startBackground, 0, 0,(int) dim.getWidth(), (int) dim.getHeight(), null);
        g.drawImage(logo, (int) (dim.getWidth()/2 - logo.getWidth()/2), (int) (dim.getHeight()/2 - logo.getHeight()/2 - 200), null);

        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect((int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Start Game")/2 - 50)-28, (int) (dim.getHeight()/2 - logo.getHeight()/2 + 250 - 50), g.getFontMetrics().stringWidth("Start Game") + 155, 80, 20, 20);
        g.fillRoundRect((int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Start Game")/2 - 50)-28, (int) (dim.getHeight()/2 - logo.getHeight()/2 + 250 - 50), g.getFontMetrics().stringWidth("Start Game") + 155, 80, 20, 20);
        g.drawRoundRect((int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Help")/2 - 50), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 375 - 50), g.getFontMetrics().stringWidth("Help") + 100, 80, 20, 20);
        g.fillRoundRect((int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Help")/2 - 50), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 375 - 50), g.getFontMetrics().stringWidth("Help") + 100, 80, 20, 20);
        g.drawRoundRect((int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Exit")/2 - 50), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 500 - 50), g.getFontMetrics().stringWidth("Exit") + 100, 80, 20, 20);
        g.fillRoundRect((int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Exit")/2 - 50), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 500 - 50), g.getFontMetrics().stringWidth("Exit") + 100, 80, 20, 20);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        g.setColor(Color.ORANGE);
        g.drawString("Start Game", (int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Start Game")/2), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 250));
        g.drawString("Help", (int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Help")/2), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 375));
        g.drawString("Exit", (int) (dim.getWidth()/2 - g.getFontMetrics().stringWidth("Exit")/2), (int) (dim.getHeight()/2 - logo.getHeight()/2 + 500));

    }
    public void drawBoard(Graphics g) {

        g.clearRect(0, 0, 1900, 1000);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 1900, 1000);
        g.setColor(Color.WHITE);
        double horIndent = 158;
        double verIndent = 120;
        int width = 160;
        int height = width * (210/182);

        for (int j = 0; j<tiles[0].length; j++) {
            if (tiles[0][j] != null) {
                double x = 90+horIndent*j;
                double y = 90;
                tiles[0][j].setxCoord((int)x);
                tiles[0][j].setyCoord((int)y);
                g.drawImage(tiles[0][j].getImage(), (int)x, (int)y, width, height, null);
                System.out.println(tiles[0][j].number);
            }

        }
        for (int j = 0; j<tiles[1].length; j++) {
            if (tiles[1][j] != null) {
                double x = 170+horIndent*j;
                double y = 90+verIndent;
                tiles[1][j].setxCoord((int)x);
                tiles[1][j].setyCoord((int)y);
                g.drawImage(tiles[1][j].getImage(), (int)x, (int)y, width, height, null);
            }
        }

        for (int j = 0; j<tiles[2].length; j++) {
            if (tiles[2][j] != null) {
                double x = 90+horIndent*j;
                double y = 90+verIndent*2;
                tiles[2][j].setxCoord((int)x);
                tiles[2][j].setyCoord((int)y);
                g.drawImage(tiles[2][j].getImage(), (int)x, (int)y, width, height, null);
            }
        }

        for (int j = 0; j<tiles[3].length; j++) {
            if (tiles[3][j] != null) {
                double x = 170+horIndent*j;
                double y = 90+verIndent*3;
                tiles[3][j].setxCoord((int)x);
                tiles[3][j].setyCoord((int)y);
                g.drawImage(tiles[3][j].getImage(), (int)x, (int)y, width, height, null);
            }
        }

        for (int j = 0; j<tiles[4].length; j++) {
            if (tiles[4][j] != null) {
                double x = 90+horIndent*j;
                double y = 90+verIndent*4;
                tiles[4][j].setxCoord((int)x);
                tiles[4][j].setyCoord((int)y);
                g.drawImage(tiles[4][j].getImage(), (int)x, (int)y, width, height, null);
            }
        }

    }



    public void mouseReleased(MouseEvent m) {}
    public void mouseEntered(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}
    public void mouseClicked(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
       System.out.println("vertex:" + x + " " + y);
    }
}
