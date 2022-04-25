
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.*;
import java.nio.*;
import java.io.*;

public class CatanPanel extends JPanel implements MouseListener{
    private static final long serialVersionUID = 1L;
    GameState gs;
    BufferedImage startBackground, logo, portBrick, portWood, portSheep, portWheat, portOre, portUnknown;
    Board board;
    PlayerManager pManage;
    Player currentPlayer;
    File file;
    Tile[][] tiles;
    Intersection[][] intersections;
    boolean startGame = false, adjacent = false, firstTimeGameState1 = true;
    //Dimension dim;
    ArrayList<Intersection> toHighlight = new ArrayList<Intersection>();
    Cards bank;
    ArrayList<BufferedImage> portImages = new ArrayList<BufferedImage>();
    boolean rolledDice = false;
    Robber robber;
    ArrayList<Player> toDiscard = new ArrayList<Player>();
    ArrayList<Integer> numDiscard = new ArrayList<Integer>();

    public CatanPanel() {
        //dim = Toolkit.getDefaultToolkit().getScreenSize();
        gs = new GameState();
        try{
            startBackground = ImageIO.read(CatanPanel.class.getResource("/misc/CatanBackground.png"));
            logo = ImageIO.read(CatanPanel.class.getResource("/misc/logo.png"));

            portBrick = ImageIO.read(CatanPanel.class.getResource("/PortImages/port_brick.png"));
            portWood = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_lumber.png")));
            portSheep = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_sheep.png")));
            portWheat = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_wheat.png")));
            portOre = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_ore.png")));
            portUnknown = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_unknown.png")));

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error loading image");
            return;
        }
        board = new Board();
        tiles = board.getTiles();
        intersections = board.getIntersections();
        bank = new Cards();
        robber = new Robber();
        addMouseListener(this);
        portImages.add(portBrick);
        portImages.add(portWood);
        portImages.add(portSheep);
        portImages.add(portWheat);
        portImages.add(portOre);
        portImages.add(portUnknown);
        portImages.add(portUnknown);
        portImages.add(portUnknown);
        portImages.add(portUnknown);
        Collections.shuffle(portImages);
    }


    public void paint(Graphics g) {
        //gameState = 0, menuscreen, choose starting settlements
        if(gs.getGameState() == 0) {
            menuScreen(g);
            if(startGame == true){
                currentPlayer = pManage.curentPlayer();
                System.out.println("game state " + gs.getGameState() + " subState " + gs.getSubState() + " startgame " + startGame);
                //do not paint anything before drawTiles
                drawTiles(g);
                drawIntersections(g);
                drawPlayer(g, currentPlayer);
                drawSettlements(g);
                //drawGameLog(g);
                drawRoads(g);
                drawPorts(g);
                if (gs.getSubState().equals("settlement")) {
                    changeColor(g);
                    if(adjacent == false){
                        g.drawString("Choose starting settlement", 800, 100);
                    } else{
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 45)); g.drawString("Cannot place settlement on adjacent intersection", 800, 100);
                        g.drawString("Choose a different starting settlement", 800, 150); g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
                        adjacent = false;
                    }
                    if (currentPlayer.getSettlements().size() == 1) {
                        gs.setSubState("road");
                    }
                }
                else if (gs.getSubState().equals("road")) {
                    changeColor(g);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 55)); g.drawString("Choose endpoint of road for the settlement", 800, 100); g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
                }
                else if (gs.getSubState().equals("settlement2")) {
                    changeColor(g);
                    if(adjacent == false) {
                        g.drawString("Choose second settlement", 800, 100);
                    } else{
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 45)); g.drawString("Cannot place settlement on adjacent intersection", 800, 100);
                        g.drawString("Choose a different second starting settlement", 800, 150); g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
                        adjacent = false;
                    }
                }
                else if (gs.getSubState().equals("road2")) {
                    changeColor(g);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 55)); g.drawString("Choose road for the second settlement", 800, 100); g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
                }
            }
        }
        //gameState = 1, game loop and trade phase
        else if(gs.getGameState() == 1) {
            System.out.println();
            g.setColor(Color.darkGray);
            g.fillRect(790,0,1900,220);
            firstTimeGameState1 = false;
            currentPlayer = pManage.curentPlayer();
            System.out.println("game state " + gs.getGameState() + " " +"subState " + gs.getSubState());
            drawTiles(g);
            drawPlayer(g, currentPlayer);
            drawIntersections(g);
            drawPorts(g);
            drawSettlements(g);
            drawRoads(g);
            //TODO: drawRobber();
            //TODO: drawDevCards();
            //TODO: drawTradeButton();
            //TODO: drawLogButton();
            //TODO: drawBuildButton();
            if (!gs.getSubState().equals("discard")) {
                drawCards(g, currentPlayer);
            }
            else if (gs.getSubState().equals("discard")) {
                drawCards(g, toDiscard.get(0));
                g.drawString("Player " + toDiscard.get(0).playerIndex + " must discard " + numDiscard.get(0) + " cards", 500, 100);
            }

            //System.out.println("current player: " + currentPlayer.getResources().keySet());
            changeColor(g);
            //TODO: make a proper dice button
            g.fillRect(30,130,100,100); //dice button

            if (rolledDice == false)  {
                g.drawString("Roll Dice", 800, 100);
            }
            else {
                //TODO: display sum of the dice rolls
            }
            if (gs.getSubState().equals("robber")) {
                g.drawString("Choose tile to place robber", 800, 120);
                //TODO: print all players' inventory counts
            }
        }
        //gameState = 2, buy phase
        else if(gs.getGameState() == 2) {

        }
    }

    private void drawGameLog(Graphics g) {
    }



    public void mousePressed(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
        System.out.println("click: " + x + " " + y);
        System.out.println("mousePresed: gameState: " + gs.getGameState() + " " + "subState: " + gs.getSubState());
        // menu screen
        if (gs.getGameState() == 0 && startGame == false) {
        if(x > 800 && x < 1100 && y > 500 && y < 600) {
            pManage = new PlayerManager(Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Please enter the number of players(3-4):",
                    "Number of Players", JOptionPane.QUESTION_MESSAGE)), bank);
            startGame = true;
            gs.setSubState("settlement");
        }
        if(x > 800 && x < 1100 && y > 650 && y < 750) {
            if (Desktop.isDesktopSupported()) {
                try {
                    InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream("misc/CatanRules.pdf");

                    Path tempOutput = Files.createTempFile("TempManual", ".pdf");
                    tempOutput.toFile().deleteOnExit();

                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);

                    File userManual = new File(tempOutput.toFile().getPath());
                    if (userManual.exists()) {
                        Desktop.getDesktop().open(userManual);
                    }
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
        //choose starting settlements
        else if (gs.getGameState() == 0 && startGame) {
            if(gs.getSubState().equals("settlement")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX()-14<=x && x<=intersections[i][j].getX()+14 && intersections[i][j].getY()-14<=y && y<=intersections[i][j].getY()+14) {
                            System.out.println("intersection: " + intersections[i][j].getX() + " " + intersections[i][j].getY());
                            intersections[i][j].setSettlement(currentPlayer);
                            gs.setSubState("road");
                        }
                        else if(intersections[i][j] != null && !intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX()-14<=x && x<=intersections[i][j].getX()+14 && intersections[i][j].getY()-14<=y && y<=intersections[i][j].getY()+14){
                            adjacent = true;
                        }
                    }
                }
            }
            else if(gs.getSubState().equals("road")) {
                Intersection temp = currentPlayer.getSettlements().get(0).getPosition();
                ArrayList<Intersection> tempList = temp.getAdjacentIntersections();
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getX()-14<=x && x<=tempList.get(i).getX()+14 && tempList.get(i).getY()-14<=y && y<=tempList.get(i).getY()+14) {
                        Road tempRoad = new Road(temp, tempList.get(i), currentPlayer);
                        if (temp.i1 == tempList.get(i)) {
                            temp.setR1(tempRoad);
                            tempList.get(i).setR1(tempRoad);
                        }
                        else if (temp.i2 == tempList.get(i)) {
                            temp.setR2(tempRoad);
                            tempList.get(i).setR2(tempRoad);
                        }
                        else if (temp.i3 == tempList.get(i)) {
                            temp.setR3(tempRoad);
                            tempList.get(i).setR3(tempRoad);
                        }

                        if (pManage.currentPlayerIndex()<pManage.size()-1) {
                            pManage.nextPlayer();
                            gs.setSubState("settlement");
                        }
                        else if (pManage.currentPlayerIndex()==pManage.size()-1) {
                            gs.setSubState("settlement2");
                        }
                    }
                }
            }
            else if(gs.getSubState().equals("settlement2")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX()-14<=x && x<=intersections[i][j].getX()+14 && intersections[i][j].getY()-14<=y && y<=intersections[i][j].getY()+14) {
                            System.out.println("intersection: " + intersections[i][j].getX() + " " + intersections[i][j].getY());
                            intersections[i][j].setSettlement(currentPlayer);
                            intersections[i][j].s.giveAllResource();
                            gs.setSubState("road2");
                        }
                        if (intersections[i][j] != null && !intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX()-14<=x && x<=intersections[i][j].getX()+14 && intersections[i][j].getY()-14<=y && y<=intersections[i][j].getY()+14) {
                            adjacent = true;
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("road2")) {
                Intersection temp = currentPlayer.getSettlements().get(1).getPosition();
                ArrayList<Intersection> tempList = temp.getAdjacentIntersections();
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getX()-14<=x && x<=tempList.get(i).getX()+14 && tempList.get(i).getY()-14<=y && y<=tempList.get(i).getY()+14) {
                        Road tempRoad = new Road(temp, tempList.get(i), currentPlayer);
                        if (temp.i1 == tempList.get(i)) {
                            temp.setR1(tempRoad);
                            tempList.get(i).setR1(tempRoad);
                        }
                        else if (temp.i2 == tempList.get(i)) {
                            temp.setR2(tempRoad);
                            tempList.get(i).setR2(tempRoad);
                        }
                        else if (temp.i3 == tempList.get(i)) {
                            temp.setR3(tempRoad);
                            tempList.get(i).setR3(tempRoad);
                        }


                        if (pManage.currentPlayerIndex()>0) {
                            pManage.prevPlayer();
                            gs.setSubState("settlement2");
                        }
                        else if (pManage.currentPlayerIndex()==0) {
                            gs.setGameState(1);
                            gs.setSubState("");
/*
                            // robber testing
                            gs.setSubState("robber");
                            rolledDice = true;
*/

                        }
                    }
                }
            }
            repaint();
        }
        else if (gs.getGameState()==1) {
            if (rolledDice == false) {
                if (x>=30 && x<=130 && y>=130 && y<=230) {
                    rolledDice = true;
                    int die1 = (int)(Math.random()*6+1);
                    int die2 = (int)(Math.random()*6+1);
                    int sum = die1 + die2;
                    System.out.println("dice: " + die1 + " " + die2 + " sum: " + sum);
                    if (sum == 7) {
                        gs.setSubState("robber");
                    }
                    else {
                        board.distributeResources(sum);
                    }
                }
            }
            else if (gs.getSubState().equals("robber")) {
                for (int i = 0; i<tiles.length; i++) {
                    for (int j = 0; j<tiles[i].length; j++) {
                        if (tiles[i][j] != null && x>= tiles[i][j].getX()+52 && x<= tiles[i][j].getX()+52+55 && y>= tiles[i][j].getY()+50 && y<= tiles[i][j].getY()+100) {
                            robber.setPosition(tiles[i][j]);
                            System.out.println("robber moved to " + i + " " + j + " " + tiles[i][j].getResource());
                            //stealing
                            ArrayList<String> owners = new ArrayList<String>();
                            for (int k = 0; k<tiles[i][j].settles.size(); k++) {
                                if (tiles[i][j].settles.get(k).getOwner() != currentPlayer && owners.contains(tiles[i][j].settles.get(k).getOwner().toString()) == false && tiles[i][j].settles.get(k).getOwner().getInventorySize()>0) {
                                    owners.add(tiles[i][j].settles.get(k).getOwner().toString());
                                }
                            }
                            System.out.println("could steal from" + owners);
                            if (owners.size()>0) {
                                String[] owns = new String[owners.size()];
                                for (int n = 0; n<owners.size(); n++) {
                                    owns[n] = owners.get(n) + "(Inventory Size: " + pManage.toStringReverse(owners.get(n)).getInventorySize() + " Color: " + pManage.toStringReverse(owners.get(n)).color.toUpperCase() + ")";
                                }
                                System.out.println("choose player to steal");
                                String picked = (String) JOptionPane.showInputDialog(null, "What player do you want to rob?", "Rob Player", JOptionPane.QUESTION_MESSAGE, null, owns, owns[0]);
                                Player toSteal = pManage.get(Integer.parseInt("" + picked.charAt(7)));
                                pManage.steal(currentPlayer, toSteal);
                            }
                            //discarding
                            toDiscard = new ArrayList<Player>();
                            numDiscard = new ArrayList<Integer>();
                            for (int b= 0; b<pManage.size(); b++) {
                                if (pManage.get(b).getInventorySize()>7 && pManage.get(b) != currentPlayer) {
                                        toDiscard.add(pManage.get(b));
                                        numDiscard.add((int)Math.floor(pManage.get(b).getInventorySize()/2.0));
                                }
                            }
                            if (toDiscard.size()>0) {
                                gs.setSubState("discard");
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("discard")) {
                if (numDiscard.get(0)>0) {
                    Player temp = toDiscard.get(0);
                    String resource = coordToResource(x, y);
                    System.out.println("discarding " + resource);
                    if (resource != null) {
                        if (temp.getResourceCount(resource)>0) {
                            temp.removeResource(resource, 1);
                            numDiscard.set(0, numDiscard.get(0)-1);
                            if (numDiscard.get(0)==0) {
                                toDiscard.remove(0);
                                numDiscard.remove(0);
                                if (toDiscard.size() == 0) {
                                    gs.setSubState("");
                                }
                            }
                        }
                    }
                }
                else {
                    gs.setSubState("");
                }
            }
            repaint();
        }

    }

    public String coordToResource(int x, int y) {
        HashMap<String, Integer> resources = pManage.curentPlayer().getResources();
        Set<String> keys = resources.keySet();
        if (keys != null) {
            Iterator<String> iter = keys.iterator();
            double ratio = 454.0/296.0;
            int width = 100;
            int height = (int) (width*ratio);
            int count = 0;
            int horDiff = width + 30;
            while (iter.hasNext()) {
                String resource = iter.next();
                int amount = resources.get(resource);
                if (x>=400+horDiff*count && x<=400+horDiff*count+width && y>=800 && y<=800+height) {
                    return resource;
                }
                //g.drawImage(img, 400+horDiff*count, 800, width, height, null);
                //changeColor(g);
                //g.drawString(""+currentPlayer.getResourceCount(resource), 400+horDiff*count, 850);
                count++;
            }
        }
        return null;
    }

    public void drawCards(Graphics g, Player p) {
        double ratio = 454.0/296.0;
        HashMap<String, Integer> resources = p.getResources();
        Set<String> keys = resources.keySet();
        if (keys != null) {
            Iterator<String> iter = keys.iterator();
            int width = 100;
            int height = (int) (width*ratio);
            int count = 0;
            int horDiff = width + 30;
            while (iter.hasNext()) {
                String resource = iter.next();
                int amount = resources.get(resource);
                BufferedImage img = Cards.cardImages.get(resource);
                g.drawImage(img, 400+horDiff*count, 800, width, height, null);
                changeColor(g, p);
                g.drawString(""+p.getResourceCount(resource), 400+horDiff*count, 850);
                count++;
            }
        }

    }
    public void drawSettlements(Graphics g) {
        for (int i = 0; i < pManage.size(); i++) {
            for (int j = 0; j < pManage.get(i).getSettlements().size(); j++) {
                if (pManage.get(i).getColor().equals("red")) {
                    g.setColor(Color.RED);
                }
                else if (pManage.get(i).getColor().equals("blue")) {
                    g.setColor(new Color(31, 69, 252));
                }
                else if (pManage.get(i).getColor().equals("white")) {
                    g.setColor(Color.WHITE);
                }
                else if (pManage.get(i).getColor().equals("yellow")) {
                    g.setColor(Color.YELLOW);
                }
                //System.out.println("Will Highlight: " + pManage.get(i).getSettlements().get(j));

                g.fillRect(pManage.get(i).getSettlements().get(j).getX()-10, pManage.get(i).getSettlements().get(j).getY()-10, 20, 20);
            }
        }
    }
    public void drawRoads(Graphics g) {
        for (int i = 0; i < pManage.size(); i++) {
            for (int j = 0; j < pManage.get(i).getRoads().size(); j++) {
                if (pManage.get(i).getColor().equals("red")) {
                    g.setColor(Color.RED);
                }
                else if (pManage.get(i).getColor().equals("blue")) {
                    g.setColor(new Color(31, 69, 252));
                }
                else if (pManage.get(i).getColor().equals("white")) {
                    g.setColor(Color.WHITE);
                }
                else if (pManage.get(i).getColor().equals("yellow")) {
                    g.setColor(Color.YELLOW);
                }
                Road temp = pManage.get(i).getRoads().get(j);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(8));
                g.drawLine(temp.getI1().getX(), temp.getI1().getY(), temp.getI2().getX(), temp.getI2().getY());
            }
        }
    }
    public void changeColor(Graphics g) {
        if (currentPlayer.getColor().equals("red")) {
            g.setColor(Color.RED);
        }
        else if (currentPlayer.getColor().equals("blue")) {
            g.setColor(new Color(31, 69, 252));
        }
        else if (currentPlayer.getColor().equals("white")) {
            g.setColor(Color.WHITE);
        }
        else if (currentPlayer.getColor().equals("yellow")) {
            g.setColor(Color.YELLOW);
        }
    }
    public void changeColor(Graphics g, Player p) {
        if (p.getColor().equals("red")) {
            g.setColor(Color.RED);
        }
        else if (p.getColor().equals("blue")) {
            g.setColor(new Color(31, 69, 252));
        }
        else if (p.getColor().equals("white")) {
            g.setColor(Color.WHITE);
        }
        else if (p.getColor().equals("yellow")) {
            g.setColor(Color.YELLOW);
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
    private void drawPorts(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int x = 246;
        int y = 47;
        g2.drawImage(portImages.get(0), x-50, y-50, 100, 100,  null);
        g2.drawImage(portImages.get(1), 84-50, 273-30, 100, 100,  null);
        g2.drawImage(portImages.get(2), 84-50, 534-50, 100, 100,  null);
        g2.drawImage(portImages.get(3), 240-10, 760-50, 100, 100,  null);
        g2.drawImage(portImages.get(4), 559-20, 760-50, 100, 100,  null);
        g2.drawImage(portImages.get(5), 795-50, 639-35, 100, 100,  null);
        g2.drawImage(portImages.get(6), 940-50, 404-50, 100, 100,  null);
        g2.drawImage(portImages.get(7), 788-30, 162-50, 100, 100,  null);
        g2.drawImage(portImages.get(8), 698-150, y-50, 100, 100,  null);

    }

    public void drawPlayer(Graphics g, Player p) {
        if (p.getColor().equals("red")) {
            g.setColor(Color.RED);
            //System.out.println("set color to red");
        }
        else if (p.getColor().equals("blue")) {
            g.setColor(new Color(31, 69, 252));
            //System.out.println("set color to blue");
        }
        else if (p.getColor().equals("white")) {
            g.setColor(Color.WHITE);
            //System.out.println("set color to white");
        }
        else if (p.getColor().equals("yellow")) {
            g.setColor(Color.YELLOW);
            //System.out.println("set color to yellow");
        }
        g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
        g.drawString("Player " + p.playerIndex, 30, 850);
    }

    public void mouseReleased(MouseEvent m) {}
    public void mouseEntered(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}
    public void mouseClicked(MouseEvent m) {
        //int x = m.getX();
        //int y = m.getY();
       //System.out.println("click:" + x + " " + y);
    }

}
