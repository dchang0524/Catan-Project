import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

public class CatanPanel extends JPanel implements MouseListener{
    GameState gs;
    BufferedImage startBackground, logo;
    Board board;
    PlayerManager pm;
    File file;
    Tile[][] tiles;
    Intersection[][] intersections;
    //Dimension dim;
    ArrayList<Intersection> toHighlight = new ArrayList<Intersection>();
    public CatanPanel() {
        //dim = Toolkit.getDefaultToolkit().getScreenSize();
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
        intersections = board.getIntersections();
        addMouseListener(this);

    }

    public void paint(Graphics g) {
        //gameState = 0
        if(gs.getGameState() == 0) {
            menuScreen(g);
        }
        if(gs.getGameState() == 1) {
            drawTiles(g);
            g.setColor(Color.CYAN);
            drawIntersections(g);
            System.out.println(intersections[6][0] + " " + intersections[6][0].i1 + " " + intersections[6][0].i2 + " " + intersections[6][0].i3);
            for (int i = 0; i<toHighlight.size(); i++) {
                    g.setColor(Color.RED);
                    g.fillRect(toHighlight.get(i).getX()-10, toHighlight.get(i).getY()-10, 10, 10);
                    g.setColor(Color.CYAN);
            }
            toHighlight.clear();
        }

    }
    public void mousePressed(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
        // comment
        if (gs.getGameState() == 0) {
        if(x > 800 && x < 1100 && y > 500 && y < 600) {
            gs.setGameState(1);
        }
        if(x > 800 && x < 1100 && y > 650 && y < 750) {
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
        if(x > 800 && x < 1100 && y > 800 && y < 900) {
            System.exit(0);
        }
        repaint();
    }
        else if (gs.getGameState() == 1) {
            for (int i = 0; i < intersections.length; i++) {
                for (int j = 0; j < intersections[i].length; j++) {
                    if (intersections[i][j] != null && intersections[i][j].getX()-10<=x && x<=intersections[i][j].getX()+10 && intersections[i][j].getY()-10<=y && y<=intersections[i][j].getY()+10) {
                        toHighlight.add(intersections[i][j]);
                        if (intersections[i][j].i1 != null) {
                            toHighlight.add(intersections[i][j].i1);
                        }
                        if (intersections[i][j].i2 != null) {
                            toHighlight.add(intersections[i][j].i2);
                        }
                        if (intersections[i][j].i3 != null) {
                            toHighlight.add(intersections[i][j].i3);
                        }

                    }
                }
            }
            repaint();
        }


    }
    public void menuScreen(Graphics g) {
        g.drawImage(startBackground, 0, 0,1900, 1000, null);
        g.drawImage(logo, 475, 100, null);

        g.setColor(Color.DARK_GRAY);
        g.drawRoundRect(800, 500, 300, 100, 20, 20);
        g.fillRoundRect(800, 500, 300, 100, 20, 20);
        g.drawRoundRect(800, 650, 300, 100, 20, 20);
        g.fillRoundRect(800, 650, 300, 100, 20, 20);
        g.drawRoundRect(800, 800, 300, 100, 20, 20);
        g.fillRoundRect(800, 800, 300, 100, 20, 20);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        g.setColor(Color.ORANGE);
        g.drawString("Start Game", 843, 565);
        g.drawString("Help", 910, 715);
        g.drawString("Exit", 910, 865);

    }
    public void drawTiles(Graphics g) {

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
                g.drawImage(tiles[0][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
            }

        }
        for (int j = 0; j<tiles[1].length; j++) {
            if (tiles[1][j] != null) {
                double x = 170+horIndent*j;
                double y = 90+verIndent;
                tiles[1][j].setxCoord((int)x);
                tiles[1][j].setyCoord((int)y);
                g.drawImage(tiles[1][j].getImage(), (int)x, (int)y, width, height, null);
                g.drawImage(tiles[1][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
            }
        }

        for (int j = 0; j<tiles[2].length; j++) {
            if (tiles[2][j] != null) {
                double x = 90+horIndent*j;
                double y = 90+verIndent*2;
                tiles[2][j].setxCoord((int)x);
                tiles[2][j].setyCoord((int)y);
                g.drawImage(tiles[2][j].getImage(), (int)x, (int)y, width, height, null);
                g.drawImage(tiles[2][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
            }
        }

        for (int j = 0; j<tiles[3].length; j++) {
            if (tiles[3][j] != null) {
                double x = 170+horIndent*j;
                double y = 90+verIndent*3;
                tiles[3][j].setxCoord((int)x);
                tiles[3][j].setyCoord((int)y);
                g.drawImage(tiles[3][j].getImage(), (int)x, (int)y, width, height, null);
                g.drawImage(tiles[3][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
            }
        }

        for (int j = 0; j<tiles[4].length; j++) {
            if (tiles[4][j] != null) {
                double x = 90+horIndent*j;
                double y = 90+verIndent*4;
                tiles[4][j].setxCoord((int)x);
                tiles[4][j].setyCoord((int)y);
                g.drawImage(tiles[4][j].getImage(), (int)x, (int)y, width, height, null);
                g.drawImage(tiles[4][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
            }
        }
    }
    public void drawIntersections(Graphics g) {
        g.setColor(Color.CYAN);
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null) {
                    g.fillRect(intersections[i][j].getX()-10, intersections[i][j].getY()-10, 10, 10);
                }
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
