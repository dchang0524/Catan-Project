
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;
import java.nio.file.*;
import java.io.*;

public class CatanPanel extends JPanel implements MouseListener{
    private static final long serialVersionUID = 1L;
    GameState gs;
    BufferedImage startBackground, logo, portBrick, portWood, portSheep, portWheat, portOre, portUnknown, dice, robberImg;
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
    static int die1, die2, sum;
    ArrayList<Player> tradeITOrder = new ArrayList<Player>();
    ArrayList<HashMap<String, Integer>> offers = new ArrayList<>();
    HashMap<String, Integer> currentPlayerWant = new HashMap<>();
    ArrayList<HashMap<String, Integer>> finalOffers = new ArrayList<>();
    ArrayList<Player> finalOfferPlayers = new ArrayList<>();

    public CatanPanel() {
        //dim = Toolkit.getDefaultToolkit().getScreenSize();
        gs = new GameState();
        try{
            startBackground = ImageIO.read(CatanPanel.class.getResource("/misc/CatanBackground.png"));
            logo = ImageIO.read(CatanPanel.class.getResource("/misc/logo.png"));
            dice = ImageIO.read(CatanPanel.class.getResource("/misc/dice.png"));
            portBrick = ImageIO.read(CatanPanel.class.getResource("/PortImages/port_brick.png"));
            portWood = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_lumber.png")));
            portSheep = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_sheep.png")));
            portWheat = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_wheat.png")));
            portOre = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_ore.png")));
            portUnknown = (ImageIO.read(CatanPanel.class.getResource("/PortImages/port_unknown.png")));
            robberImg = (ImageIO.read(CatanPanel.class.getResource("/misc/robber.png")));
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
        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[i].length; j++) {
                if (tiles[i][j] != null && tiles[i][j].getResource().equals("desert")) {
                    robber = new Robber(tiles[i][j]);
                }
            }
        }
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
                drawGameLog(g);
                drawRoads(g);
                drawPorts(g);
                drawRobber(g);
                drawTrade(g);
                drawBuild(g);
                drawPlayerInfo(g);
                drawNextTurnButton(g);
                if (gs.getSubState().equals("settlement")) {
                    changeColor(g);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 45));
                    if(adjacent == false){
                        g.drawString("Choose starting settlement", 800, 100);
                    } else{
                        g.drawString("Cannot place settlement on adjacent intersection", 800, 50);
                        g.drawString("Choose a different starting settlement", 800, 100);
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
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 45));
                    if(adjacent == false) {
                        g.drawString("Choose second settlement", 800, 100);
                    } else{
                        g.drawString("Cannot place settlement on adjacent intersection", 800, 50);
                        g.drawString("Choose a different second starting settlement", 800, 100);
                        adjacent = false;
                    }
                }
                else if (gs.getSubState().equals("road2")) {
                    changeColor(g);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 45));
                    g.drawString("Choose road for the second settlement", 800, 100);
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
            drawGameLog(g);
            drawRobber(g);
            drawTrade(g);
            drawBuild(g);
            drawPlayerInfo(g);
            drawNextTurnButton(g);
            //TODO: drawDevCards();
            if (!gs.getSubState().equals("discard")) {
                drawCards(g, currentPlayer);
            }
            else if (gs.getSubState().equals("discard")) {
                drawCards(g, toDiscard.get(0));
                g.drawString("Player " + toDiscard.get(0).playerIndex + " must discard " + numDiscard.get(0) + " cards", 500, 100);
            }

            //System.out.println("current player: " + currentPlayer.getResources().keySet());
            changeColor(g);
            g.fillRect(30,130,100,100); //dice button
            g.drawImage(dice, 35, 135, 90, 90, null);
            if (rolledDice == false)  {
                g.drawString("Roll Dice", 800, 100);
            }
            else {
                g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                g.drawString(die1 + " + " + die2 + " = " + sum, 20, 70);
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

    public void drawTrade(Graphics g) {
        changeColor(g);
        g.fillRect(1500, 200, 170, 60);
        g.setColor(Color.cyan);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Trade", 1605, 200+40);
    }
    public void drawGameLog(Graphics g) {
        changeColor(g);
        g.fillRect(1600, 280, 170, 60);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Game Log", 1605, 280+40);
    }
    public void drawBuild(Graphics g) {
        changeColor(g);
        g.fillRect(1600, 360, 170, 60);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Build", 1605, 360+40);
    }
    public void drawPlayerInfo(Graphics g) {
        changeColor(g);
        g.fillRect(1600, 440, 170, 60);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Inventories", 1605, 440+40);
    }
    public void drawNextTurnButton(Graphics g) {
        changeColor(g);
        g.fillRect(1600, 520, 170, 60);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString("Next Turn", 1605, 520+40);
    }

    public void drawRobber(Graphics g) {
        //g.drawImage(tiles[1][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
        g.drawImage(robberImg, robber.getPosition().getX()+53, robber.getPosition().getY()+30, 54, 111, null);
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
                    die1 = (int)(Math.random()*6+1);
                    die2 = (int)(Math.random()*6+1);
                    sum = die1 + die2;
                    System.out.println("dice: " + die1 + " " + die2 + " sum: " + sum);
                    if (sum == 7) {
                        gs.setSubState("robber");
                    }
                    else {
                        board.distributeResources(sum);
                    }
                }
            }
            else if (gs.getSubState().equals("")) {
                if (x>=1500 && x<=1500+170 && y>=520 && y<=580) { //if next turn button (1500, 520, 170, 60)
                    pManage.nextPlayer();
                    rolledDice = false;
                }
                if (x>=1600 && x<=1600+170 && y>=200 && y<=200+60) { //if trade button
                    String[] tradeTypes = new String[2];
                    tradeTypes[0] = "Trade With Players";
                    tradeTypes[1] = "Trade With Bank or Ports";
                    String picked = (String) JOptionPane.showInputDialog(null, "Which type of trade do you want?", "Trade Type", JOptionPane.QUESTION_MESSAGE, null, tradeTypes, tradeTypes[0]);
                    if (picked.equals("Trade With Players"))    {
                        gs.setSubState("domesticWant"); //current player adds items he wants to trade
                        tradeITOrder.add(currentPlayer);
                        HashMap<String, Integer> temp = new HashMap<String, Integer>();
                        temp.put("wood", 0);
                        temp.put("brick", 0);
                        temp.put("sheep", 0);
                        temp.put("wheat", 0);
                        temp.put("ore", 0);
                        offers.add(temp);
                    }
                    else if (picked.equals("Trade With Bank or Ports")) {
                        gs.setSubState("maritime");
                    }
                }

            }
            else if (gs.getSubState().equals("domesticWant")) {
                if (x>=1600 && x<=1600+170 && y>=520 && y<=580) { //if done button
                    tradeITOrder.remove(0);
                    currentPlayerWant = offers.remove(0);
                    if (tradeITOrder.size() == 0) {
                        gs.setSubState("domesticPlayers");
                        for (int i=0; i<pManage.players.size(); i++) {
                            if (pManage.players.get(i) != currentPlayer) {
                                if (pManage.players.get(i).getResources().get("wood") >= currentPlayerWant.get("wood") &&
                                pManage.players.get(i).getResources().get("brick") >= currentPlayerWant.get("brick") && pManage.players.get(i).getResources().get("sheep") >= currentPlayerWant.get("sheep") && pManage.players.get(i).getResources().get("wheat") >= currentPlayerWant.get("wheat") && pManage.players.get(i).getResources().get("ore") >= currentPlayerWant.get("ore")) {
                                   //if enough resources to trade
                                    tradeITOrder.add(pManage.players.get(i));
                                    HashMap<String, Integer>  temp = new HashMap<String, Integer>();
                                    temp.put("wood", 0);
                                    temp.put("brick", 0);
                                    temp.put("sheep", 0);
                                    temp.put("wheat", 0);
                                    temp.put("ore", 0);
                                    offers.add(temp);
                                }
                            }
                        }
                    }
                }
                else if (offers.size()>0 && tradeITOrder.size()>0) {
                    String resource = coordToResource(x, y);
                    HashMap<String, Integer> currentOffer = offers.get(0);
                    if (resource != null) {
                        if (tradeITOrder.get(0).getResourceCount(resource) > currentOffer.get(resource)){
                            currentOffer.put(resource, currentOffer.get(resource)+1);
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("domesticPlayers")) {
                if (x>=1600 && x<=1600+170 && y>=520 && y<=580) { //if done button
                    HashMap<String, Integer> currentOffer = offers.remove(0);
                    Player currentTradePlayer = tradeITOrder.remove(0);

                    int sum = 0;
                    for (String r : currentOffer.keySet()) {
                        sum+= currentOffer.get(r);
                    }

                    if (sum > 0) {
                        //check if trade is valid
                    }
                }
                else if (offers.size()>0 && tradeITOrder.size()>0) {
                    String resource = coordToResource(x, y);
                    HashMap<String, Integer> currentOffer = offers.get(0);
                    if (resource != null) {
                        if (tradeITOrder.get(0).getResourceCount(resource) > currentOffer.get(resource)){
                            currentOffer.put(resource, currentOffer.get(resource)+1);
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("robber")) {
                for (int i = 0; i<tiles.length; i++) {
                    for (int j = 0; j<tiles[i].length; j++) {
                        if (tiles[i][j] != null && x>= tiles[i][j].getX()+52 && x<= tiles[i][j].getX()+52+55 && y>= tiles[i][j].getY()+50 && y<= tiles[i][j].getY()+100) {
                            robber.setPosition(tiles[i][j]);
                            gs.setSubState("");
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
        g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
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
