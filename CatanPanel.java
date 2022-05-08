
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

public class CatanPanel extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    GameState gs;
    BufferedImage startBackground, logo, portBrick, portWood, portSheep, portWheat, portOre,
            portUnknown, dice, robberImg, buildCosts, armyImg, roadImg, devFaceDown, resourceFaceDown;
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

    ArrayList<Player> tradeITOrder = new ArrayList<Player>(); //order to iterate thru the players when trading
    ArrayList<HashMap<String, Integer>> offers = new ArrayList<>(); //offers for the players when trading
    HashMap<String, Integer> currentPlayerWant = new HashMap<>(); //offered by the current player
    ArrayList<HashMap<String, Integer>> finalOffers = new ArrayList<>(); //offers that will be offered to the current player
    ArrayList<Player> finalOfferPlayers = new ArrayList<>(); //the players for each offer in final offers

    Player toViewInven;

    Intersection roadBuildI1;

    boolean usedDevCard = false;

    boolean showExpected = false;

    public CatanPanel() {
        //dim = Toolkit.getDefaultToolkit().getScreenSize();
        gs = new GameState();
        try {
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
            buildCosts = ImageIO.read(CatanPanel.class.getResource("/misc/build costs.png"));
            armyImg = ImageIO.read(CatanPanel.class.getResource("/DevCards/LargestArmyCard.png"));
            roadImg = ImageIO.read(CatanPanel.class.getResource("/DevCards/LongestRoadCard.png"));
            devFaceDown = ImageIO.read(CatanPanel.class.getResource("/DevCards/devFaceDown.jpg"));
            resourceFaceDown = ImageIO.read(CatanPanel.class.getResource("/DevCards/resourceFaceDown.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading image");
            return;
        }
        board = new Board();
        tiles = board.getTiles();
        intersections = board.getIntersections();
        bank = new Cards();
        robber = new Robber();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
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
        setPort(intersections[0][0], portImages.get(0));
        setPort(intersections[1][0], portImages.get(0));
        setPort(intersections[3][0], portImages.get(1));
        setPort(intersections[4][0], portImages.get(1));
        setPort(intersections[7][0], portImages.get(2));
        setPort(intersections[8][0], portImages.get(2));
        setPort(intersections[10][0], portImages.get(3));
        setPort(intersections[11][0], portImages.get(3));
        setPort(intersections[10][2], portImages.get(4));
        setPort(intersections[11][1], portImages.get(4));
        setPort(intersections[9][3], portImages.get(5));
        setPort(intersections[8][4], portImages.get(5));
        setPort(intersections[5][5], portImages.get(6));
        setPort(intersections[6][5], portImages.get(6));
        setPort(intersections[2][3], portImages.get(7));
        setPort(intersections[3][4], portImages.get(7));
        setPort(intersections[0][1], portImages.get(8));
        setPort(intersections[1][2], portImages.get(8));
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null) {
                    System.out.println("Intersection [" + i + ", " + j + "]");
                    System.out.println(intersections[i][j].t1);
                    System.out.println(intersections[i][j].t2);
                    System.out.println(intersections[i][j].t3);
                    System.out.println();
                }
            }

        }
    }
    public void setPort (Intersection i, BufferedImage img) {
        if (img == portBrick) {
            i.setPortResource("brick");
            i.setPortTrade(2);
        }
        else if (img == portWood) {
            i.setPortResource("wood");
            i.setPortTrade(2);
        }
        else if (img == portSheep) {
            i.setPortResource("sheep");
            i.setPortTrade(2);
        }
        else if (img == portWheat) {
            i.setPortResource("wheat");
            i.setPortTrade(2);
        }
        else if (img == portOre) {
            i.setPortResource("ore");
            i.setPortTrade(2);
        }
        else if (img == portUnknown) {
            i.setPortTrade(3);
        }
    }

    public void paint(Graphics g) {
        //gameState = 0, menuscreen, choose starting settlements
        if (gs.getGameState() == 0) {
            menuScreen(g);
            if (startGame == true) {
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
                drawExpected(g);

                if (gs.getSubState().equals("settlement")) {
                    changeColor(g);
                    g.setFont(new Font("Helvetica", Font.PLAIN, 45));
                    if (adjacent == false) {
                        g.drawString("Choose starting settlement", 800, 100);
                    } else {
                        g.drawString("Cannot place settlement on adjacent intersection", 800, 50);
                        g.drawString("Choose a different starting settlement", 800, 100);
                        adjacent = false;
                    }
                    if (currentPlayer.getSettlements().size() == 1) {
                        gs.setSubState("road");
                    }
                } else if (gs.getSubState().equals("road")) {
                    changeColor(g);
                    g.setFont(new Font("Helvetica", Font.PLAIN, 55));
                    g.drawString("Choose endpoint of road for the settlement", 800, 100);
                    g.setFont(new Font("Helvetica", Font.PLAIN, 70));
                } else if (gs.getSubState().equals("settlement2")) {
                    changeColor(g);
                    g.setFont(new Font("Helvetica", Font.PLAIN, 45));
                    if (adjacent == false) {
                        g.drawString("Choose second settlement", 800, 100);
                    } else {
                        g.drawString("Cannot place settlement on adjacent intersection", 800, 50);
                        g.drawString("Choose a different second starting settlement", 800, 100);
                        adjacent = false;
                    }
                } else if (gs.getSubState().equals("road2")) {
                    changeColor(g);
                    g.setFont(new Font("Helvetica", Font.PLAIN, 45));
                    g.drawString("Choose road for the second settlement", 800, 100);
                }
            }
        }
        //gameState = 1, game loop and trade phase
        else if (gs.getGameState() == 1) {
            System.out.println();
            System.out.println("Paint: game state " + gs.getGameState() + " subState " + gs.getSubState() + " startgame " + startGame);
            System.out.println("current player: " + currentPlayer);
            g.setColor(Color.darkGray);
            g.fillRect(790, 0, 1900, 220);
            firstTimeGameState1 = false;
            currentPlayer = pManage.curentPlayer();
            System.out.println("game state " + gs.getGameState() + " " + "subState " + gs.getSubState());
            drawTiles(g);
            drawPlayer(g, currentPlayer);
            drawIntersections(g);
            drawPorts(g);
            drawSettlements(g);
            drawRoads(g);
            drawGameLog(g);
            drawRobber(g);
            drawTrade(g);
            drawBuild1(g);
            drawPlayerInfo(g);
            drawNextTurnButton(g);
            drawDevCards(g);
            drawExpected(g);
            if (!gs.getSubState().equals("discard")) {
                drawCards(g, currentPlayer);
            } else if (gs.getSubState().equals("discard")) {
                drawCards(g, toDiscard.get(0));
                g.drawString("Player " + toDiscard.get(0).playerIndex + " must discard " + numDiscard.get(0) + " cards", 800, 100);
            }
            if (gs.getSubState().equals("showInventory")) {
                drawCards(g, toViewInven);
                g.setFont(new Font("Helvetica", Font.PLAIN, 25));
                g.setColor(Color.white);
                g.fillRect(300, 900, 60, 30);
                g.setColor(Color.black);
                g.drawString("Back", 300, 925);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click back to return", 800, 100);
            }
            if (gs.getSubState().equals("domesticWant")) {
                g.drawString("Choose what cards you want to get by clicking on them", 800, 50);
                g.drawString("Right click to remove cards", 800, 100);
                System.out.println("currently wants " + currentPlayerWant);
                if (currentPlayerWant != null) {
                    drawTradeNums(g, offers.get(0), currentPlayer);
                }
                g.setFont(new Font("Helvetica", Font.PLAIN, 25));
                g.setColor(Color.white);
                g.fillRect(300, 900, 60, 30);
                g.setColor(Color.black);
                g.drawString("Done", 300, 925);
            } else if (gs.getSubState().equals("domesticPlayers")) {
                g.setFont(new Font("Helvetica", Font.PLAIN, 30));
                changeColor(g);
                g.drawString("Choose what cards you want to get for  " + currentPlayerWant, 800, 25);
                g.drawString("Will automatically skip over player if they don't have what the current player wants", 800, 75);
                g.drawString("Click on the cards you want to get. To remove a card, right click on the card.", 800, 125);
                changeColor(g, tradeITOrder.get(0));
                g.drawString("Current chooser " + tradeITOrder.get(0), 900, 150);
                drawCards(g, tradeITOrder.get(0));
                drawTradeNums(g, offers.get(0), tradeITOrder.get(0));
                g.setColor(Color.white);
                g.fillRect(300, 900, 60, 30);
                g.setColor(Color.black);
                g.setFont(new Font("Helvetica", Font.PLAIN, 25));
                g.drawString("Done", 300, 930);
            } else if (gs.getSubState().equals("domesticFinal")) {
                drawCards(g, currentPlayer);
                gs.setSubState("domesticFinal2");
                repaint();
            } else if (gs.getSubState().equals("domesticFinal2")) {
                String[] options = new String[finalOffers.size() + 1];
                for (int i = 0; i < finalOffers.size(); i++) {
                    options[i] = finalOfferPlayers.get(i) + ": " + finalOffers.get(i);
                }
                options[options.length - 1] = "Decline All";
                String picked = (String) JOptionPane.showInputDialog(null, "Choose offer to accept for " + currentPlayerWant, "Offers", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (picked != null) {
                    if (!picked.equals("Decline All")) {
                        int playerNum = Integer.parseInt("" + picked.charAt(7));
                        Player tradingPlayer = pManage.get(playerNum);
                        int index = 0;
                        for (int i = 0; i < finalOfferPlayers.size(); i++) {
                            if (finalOfferPlayers.get(i).equals(tradingPlayer)) {
                                index = i;
                            }
                        }
                        pManage.trade(currentPlayer, tradingPlayer, finalOffers.get(index), currentPlayerWant);
                    }
                }
                ArrayList<Player> tradeITOrder = new ArrayList<Player>(); //order to iterate thru the players when trading
                ArrayList<HashMap<String, Integer>> offers = new ArrayList<>(); //offers for the players when trading
                HashMap<String, Integer> currentPlayerWant = new HashMap<>(); //offered by the current player
                ArrayList<HashMap<String, Integer>> finalOffers = new ArrayList<>(); //offers that will be offered to the current player
                ArrayList<Player> finalOfferPlayers = new ArrayList<>(); //the players for each offer in final offers
                gs.setSubState("");
                drawCards(g, currentPlayer);
                repaint();
            }
            else if(gs.getSubState().equals("maritime")){
                System.out.println("shopRatio: " + currentPlayer.shopRatio);
                if (currentPlayer.getResourceCount("wheat") >= currentPlayer.shopRatio.get("wheat") ||
                        currentPlayer.getResourceCount("sheep") >= currentPlayer.shopRatio.get("sheep") ||
                        currentPlayer.getResourceCount("wood") >= currentPlayer.shopRatio.get("wood") ||
                        currentPlayer.getResourceCount("brick") >= currentPlayer.shopRatio.get("brick") ||
                        currentPlayer.getResourceCount("ore") >= currentPlayer.shopRatio.get("ore")) {
                    String[] options = new String[5];
                    options[0] = "Ore"; options[1] = "Wheat"; options[2] = "Wood"; options[3] = "Brick"; options[4] = "Sheep";;
                    String picked = (String) JOptionPane.showInputDialog(null, "What one resource do you want from this trade?", "Choose Resource", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked != null && bank.resourceLeft(picked.toLowerCase()) > 0) {
                        ArrayList<String> potentialOptions = new ArrayList<>();
                        for (String resource : currentPlayer.shopRatio.keySet()) {
                            if (currentPlayer.getResourceCount(resource) >= currentPlayer.shopRatio.get(resource)) {
                                if (resource != picked.toLowerCase()) {
                                    potentialOptions.add(resource);
                                }
                            }
                        }
                        String[] options2 = new String[potentialOptions.size()];
                        for (int i = 0; i < potentialOptions.size(); i++) {
                            options2[i] = potentialOptions.get(i) + ": " + currentPlayer.shopRatio.get(potentialOptions.get(i));
                        }
                        String picked2 = (String) JOptionPane.showInputDialog(null, "What resource will you pay with?", "Choose Resource", JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
                        if (picked2 != null) {
                            int index = picked2.indexOf(":");
                            String toPay = picked2.substring(0, index);
                            int amountToPay = currentPlayer.shopRatio.get(toPay);
                            currentPlayer.removeResource(toPay, amountToPay);
                            currentPlayer.addResource(picked.toLowerCase(), 1);
                            drawCards(g, currentPlayer);
                            repaint();
                        }
                        else {
                            gs.setSubState("");
                            repaint();
                        }
                    }
                    else {
                        gs.setSubState("");
                        repaint();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "You do not have enough resources to make any maritime trades.", "NOTICE", JOptionPane.ERROR_MESSAGE);
                    gs.setSubState("");
                    repaint();
                }



            }
            //System.out.println("current player: " + currentPlayer.getResources().keySet());
            changeColor(g);
            g.fillRect(30, 130, 100, 100); //dice button
            g.drawImage(dice, 35, 135, 90, 90, null);
            if (rolledDice == false) {
                if (!(gs.getSubState().equals("knight")
                        || gs.getSubState().equals("roadBuilding1") || gs.getSubState().equals("roadBuilding2") ||
                        gs.getSubState().equals("roadBuilding3") || gs.getSubState().equals("roadBuilding4"))) {
                    g.drawString("Roll Dice", 800, 100);
                    g.setFont(new Font ("TimesRoman", Font.PLAIN, 20));
                    g.drawString("if you want to use a development card, click on it", 800, 780);
                }
                else {
                    g.drawString("Choose tile to place robber", 800, 120);
                }
            }
            else {
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString(die1 + " + " + die2 + " = " + sum, 20, 100);
                g.setFont(new Font ("TimesRoman", Font.PLAIN, 20));
                g.drawString("if you want to use a development card, click on it", 800, 780);
            }
            if (gs.getSubState().equals("robber") || gs.getSubState().equals("knight")) {
                g.drawString("Choose tile to place robber", 800, 120);

                //TODO: print all players' inventory counts
            }
            if (gs.getSubState().equals("roadBuilding1") || gs.getSubState().equals("roadBuilding3")) {
                highlightRoadAble1(g);
                g.setColor(Color.cyan);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click on first intersection to build the road", 800, 100);
            }
            if (gs.getSubState().equals("roadBuilding2") || gs.getSubState().equals("roadBuilding4")) {
                highlightRoadAble2(g);
                g.setColor(Color.cyan);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click on second intersection to build the road", 800, 100);
            }
        }
        //gameState = 2, buy phase
        else if (gs.getGameState() == 2) {
            System.out.println();
            System.out.println("Paint: game state " + gs.getGameState() + " subState " + gs.getSubState() + " startgame " + startGame);
            System.out.println("current player: " + currentPlayer);
            g.setColor(Color.darkGray);
            g.fillRect(790, 0, 1900, 220);
            firstTimeGameState1 = false;
            currentPlayer = pManage.curentPlayer();
            System.out.println("game state " + gs.getGameState() + " " + "subState " + gs.getSubState());
            drawTiles(g);
            drawPlayer(g, currentPlayer);
            drawIntersections(g);
            drawPorts(g);
            drawSettlements(g);
            drawRoads(g);
            drawGameLog(g);
            drawRobber(g);
            drawTradeGray(g);
            drawBuild(g);
            drawPlayerInfo(g);
            drawNextTurnButton(g);
            drawDevCards(g);
            drawCards(g, currentPlayer);
            drawExpected(g);
            if (gs.getSubState().equals("knight")) {
                g.drawString("Choose tile to place robber", 800, 120);
            }
            if (gs.getSubState().equals("showInventory")) {
                drawCards(g, toViewInven);
                g.setFont(new Font("Helvetica", Font.PLAIN, 25));
                g.setColor(Color.white);
                g.fillRect(300, 900, 60, 30);
                g.setColor(Color.black);
                g.drawString("Back", 300, 925);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click back to return", 800, 100);
            }
            else if (gs.getSubState().equals("settlement")) {
                highlightSettleAble(g);
                g.setColor(Color.cyan);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click on highlighted intersection to build a settlment", 800, 100);
            }
            else if (gs.getSubState().equals("road") || gs.getSubState().equals("roadBuilding1") || gs.getSubState().equals("roadBuilding3")) {
               highlightRoadAble1(g);
                g.setColor(Color.cyan);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click on first intersection to build the road", 800, 100);
            }
            else if (gs.getSubState().equals("road1") || gs.getSubState().equals("roadBuilding2") || gs.getSubState().equals("roadBuilding4")) {
                highlightRoadAble2(g);
                g.setColor(Color.cyan);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click on second intersection to build the road", 800, 100);
            }
            else if (gs.getSubState().equals("city")) {
                highlightUpgradeAble(g);
                g.setColor(Color.cyan);
                g.setFont(new Font("Helvetica", Font.PLAIN, 40));
                g.drawString("Click on highlighted intersection to build a city", 800, 100);
            }
        }
        else if (gs.getGameState() == 3) {
            changeColor(g);
            g.clearRect(0, 0, 1900, 220);
            g.setColor(Color.black);
            g.setFont(new Font("Helvetica", Font.PLAIN, 100));
            g.drawString(currentPlayer + " wins!", 800, 100);
        }
    }


    public void mousePressed(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
        System.out.println("click: " + x + " " + y);
        System.out.println("mousePresed: gameState: " + gs.getGameState() + " " + "subState: " + gs.getSubState());
        // menu screen
        if (gs.getGameState() == 0 && startGame == false) {
            if (x > 800 && x < 1100 && y > 500 && y < 600) {
                pManage = new PlayerManager(Integer.parseInt(JOptionPane.showInputDialog(null,
                        "Please enter the number of players(3-4):",
                        "Number of Players", JOptionPane.QUESTION_MESSAGE)), bank);
                startGame = true;
                gs.setSubState("settlement");
            }
            if (x > 800 && x < 1100 && y > 650 && y < 750) {
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
            if (x > 800 && x < 1100 && y > 800 && y < 900) {
                System.exit(0);
            }
            repaint();
        }
        //choose starting settlements
        else if (gs.getGameState() == 0 && startGame) {
            if (x>=1600 && x<=1600+170 && y>=280 && y<=280+60) { //1600, 280, 170, 60
                if (showExpected) {
                    showExpected = false;
                }
                else {
                    showExpected = true;
                }
            }
            if (gs.getSubState().equals("settlement")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            System.out.println("intersection: " + intersections[i][j].getX() + " " + intersections[i][j].getY());
                            intersections[i][j].setSettlement(currentPlayer);
                            gs.setSubState("road");
                        } else if (intersections[i][j] != null && !intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            adjacent = true;
                        }
                    }
                }
            } else if (gs.getSubState().equals("road")) {
                Intersection temp = currentPlayer.getSettlements().get(0).getPosition();
                ArrayList<Intersection> tempList = temp.getAdjacentIntersections();
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getX() - 14 <= x && x <= tempList.get(i).getX() + 14 && tempList.get(i).getY() - 14 <= y && y <= tempList.get(i).getY() + 14) {
                        Road tempRoad = new Road(temp, tempList.get(i), currentPlayer);
                        if (temp.i1 == tempList.get(i)) {
                            temp.setR1(tempRoad);
                            tempList.get(i).setR1(tempRoad);
                        } else if (temp.i2 == tempList.get(i)) {
                            temp.setR2(tempRoad);
                            tempList.get(i).setR2(tempRoad);
                        } else if (temp.i3 == tempList.get(i)) {
                            temp.setR3(tempRoad);
                            tempList.get(i).setR3(tempRoad);
                        }

                        if (pManage.currentPlayerIndex() < pManage.size() - 1) {
                            pManage.nextPlayer();
                            gs.setSubState("settlement");
                        } else if (pManage.currentPlayerIndex() == pManage.size() - 1) {
                            gs.setSubState("settlement2");
                        }
                    }
                }
            } else if (gs.getSubState().equals("settlement2")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            System.out.println("intersection: " + intersections[i][j].getX() + " " + intersections[i][j].getY());
                            intersections[i][j].setSettlement(currentPlayer);
                            intersections[i][j].s.giveAllResource();
                            gs.setSubState("road2");
                        }
                        if (intersections[i][j] != null && !intersections[i][j].noAdjacentSettlement() && !intersections[i][j].hasSettlement()
                                && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            adjacent = true;
                        }
                    }
                }
            } else if (gs.getSubState().equals("road2")) {
                Intersection temp = currentPlayer.getSettlements().get(1).getPosition();
                ArrayList<Intersection> tempList = temp.getAdjacentIntersections();
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getX() - 14 <= x && x <= tempList.get(i).getX() + 14 && tempList.get(i).getY() - 14 <= y && y <= tempList.get(i).getY() + 14) {
                        Road tempRoad = new Road(temp, tempList.get(i), currentPlayer);
                        if (temp.i1 == tempList.get(i)) {
                            temp.setR1(tempRoad);
                            tempList.get(i).setR1(tempRoad);
                        } else if (temp.i2 == tempList.get(i)) {
                            temp.setR2(tempRoad);
                            tempList.get(i).setR2(tempRoad);
                        } else if (temp.i3 == tempList.get(i)) {
                            temp.setR3(tempRoad);
                            tempList.get(i).setR3(tempRoad);
                        }


                        if (pManage.currentPlayerIndex() > 0) {
                            pManage.prevPlayer();
                            gs.setSubState("settlement2");
                        } else if (pManage.currentPlayerIndex() == 0) {
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
        else if (gs.getGameState() == 1) {
            if (x>=1600 && x<=1600+170 && y>=280 && y<=280+60) { //1600, 280, 170, 60
                if (showExpected) {
                    showExpected = false;
                }
                else {
                    showExpected = true;
                }
            }
            if (!rolledDice) {
                if (x >= 30 && x <= 130 && y >= 130 && y <= 230) {
                    rolledDice = true;
                    die1 = (int) (Math.random() * 6 + 1);
                    die2 = (int) (Math.random() * 6 + 1);
                    sum = die1 + die2;
                    System.out.println("dice: " + die1 + " " + die2 + " sum: " + sum);
                    if (sum == 7) {
                        gs.setSubState("robber");
                    } else {
                        board.distributeResources(sum);
                    }
                }
                //development cards
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("monopoly") && currentPlayer.devCards.get("monopoly") > 0 && usedDevCard == false) {
                    String[]  options = new String[5];
                    options[0] = "Wheat";
                    options[1] = "Ore";
                    options[2] = "Sheep";
                    options[3] = "Wood";
                    options[4] = "Brick";

                    String picked = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked != null) {
                        currentPlayer.devCards.put("monopoly", currentPlayer.devCards.get("monopoly") - 1);
                        Cards.numDevCards.put("monopoly", Cards.numDevCards.get("monopoly") + 1);
                        pManage.monopoly(currentPlayer, picked.toLowerCase());
                        usedDevCard = true;
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("yearOfPlenty") && currentPlayer.devCards.get("yearOfPlenty") > 0 && usedDevCard == false) {
                    ArrayList<String> options1 = new ArrayList<>();
                    for (String s : Cards.numResourceCards.keySet()) {
                        if (Cards.numResourceCards.get(s) > 0) {
                            options1.add(s);
                        }
                    }
                    String[] options = new String[options1.size()];
                    for (int i = 0; i < options1.size(); i++) {
                        options[i] = options1.get(i);
                    }
                    String picked1 = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource 1", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked1 != null) {
                        ArrayList<String> options2 = new ArrayList<>();
                        for (String s : Cards.numResourceCards.keySet()) {
                            if (s.equals(picked1)) {
                                if (Cards.numResourceCards.get(s) > 1) {
                                    options2.add(s);
                                }
                            }
                            else {
                                if (Cards.numResourceCards.get(s) > 0) {
                                    options2.add(s);
                                }
                            }
                        }
                        String[] options22 = new String[options2.size()];
                        for (int i = 0; i < options2.size(); i++) {
                            options22[i] = options2.get(i);
                        }
                        String picked2 = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource 2", JOptionPane.QUESTION_MESSAGE, null, options22, options22[0]);
                        if (picked2 != null) {
                            currentPlayer.devCards.put("yearOfPlenty", currentPlayer.devCards.get("yearOfPlenty") - 1);
                            Cards.numDevCards.put("yearOfPlenty", Cards.numDevCards.get("yearOfPlenty") + 1);
                            pManage.yearOfPlenty(currentPlayer, picked1.toLowerCase(), picked2.toLowerCase());
                            usedDevCard = true;
                        }
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("knight") && currentPlayer.devCards.get("knight") > 0 && usedDevCard == false) {
                    gs.setSubState("knight");
                    currentPlayer.useKnight();
                    pManage.updateLargestArmy();
                    usedDevCard = true;
                }
                else if (gs.getSubState().equals("knight")) {
                    for (int i = 0; i < tiles.length; i++) {
                        for (int j = 0; j < tiles[i].length; j++) {
                            if (tiles[i][j] != null && x >= tiles[i][j].getX() + 52 && x <= tiles[i][j].getX() + 52 + 55 && y >= tiles[i][j].getY() + 50 && y <= tiles[i][j].getY() + 100 && tiles[i][j] != robber.getPosition() && tiles[i][j].getResource() != "desert") {
                                robber.setPosition(tiles[i][j]);
                                gs.setSubState("");
                                System.out.println("robber moved to " + i + " " + j + " " + tiles[i][j].getResource());
                                boolean movedRobber = false;
                                //stealing
                                ArrayList<String> owners = new ArrayList<String>();
                                System.out.println(tiles[i][j].settles.size());
                                for (int k = 0; k < tiles[i][j].settles.size(); k++) {
                                    System.out.println("settlement at " + tiles[i][j].settles.get(k) + " " + tiles[i][j].settles.get(k).getOwner());
                                    Player currentOwner = tiles[i][j].settles.get(k).getOwner();
                                    if (currentOwner != currentPlayer && owners.contains(currentOwner.toString()) == false && currentOwner.getInventorySize() > 0) {
                                        owners.add(tiles[i][j].settles.get(k).getOwner().toString());
                                    }
                                }
                                System.out.println("could steal from" + owners);
                                if (owners.size() > 0) {
                                    String[] owns = new String[owners.size()];
                                    for (int n = 0; n < owners.size(); n++) {
                                        owns[n] = owners.get(n) + "(Inventory Size: " + pManage.toStringReverse(owners.get(n)).getInventorySize() + " Color: " + pManage.toStringReverse(owners.get(n)).color.toUpperCase() + ")";
                                    }
                                    System.out.println("choose player to steal");
                                    while (movedRobber == false) {
                                        String picked = (String) JOptionPane.showInputDialog(null, "What player do you want to rob?", "Rob Player", JOptionPane.QUESTION_MESSAGE, null, owns, owns[0]);
                                        if (picked != null) {
                                            Player toSteal = pManage.get(Integer.parseInt("" + picked.charAt(7)));
                                            System.out.println("stole " + pManage.steal(currentPlayer, toSteal));
                                            movedRobber = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("roadBuilding") && currentPlayer.devCards.get("roadBuilding") > 0 && usedDevCard == false) {
                    if (currentPlayer.roadsLeft() > 0) {
                        gs.setSubState("roadBuilding1");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "You do not have enough roads to build", "NOTICE", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (gs.getSubState().equals("roadBuilding1")) {
                    if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                        gs.setSubState("");
                    }
                    for (int i = 0; i < intersections.length; i++) {
                        for (int j = 0; j < intersections[i].length; j++) {
                            if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                                if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                    gs.setSubState("roadBuilding2");
                                    roadBuildI1 = intersections[i][j];
                                }
                            }
                        }
                    }
                }
                else if (gs.getSubState().equals("roadBuilding2")) {
                    if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                        gs.setSubState("");
                        roadBuildI1 = null;
                    }
                    for (int i = 0; i < intersections.length; i++) {
                        for (int j = 0; j < intersections[i].length; j++) {
                            if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                                if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                    new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                    currentPlayer.manageRoads();
                                    pManage.updateLongestRoad();
                                    if (currentPlayer.roadsLeft()>0) {
                                        gs.setSubState("roadBuilding3");
                                    }
                                    else {
                                        currentPlayer.devCards.put("roadBuilding", currentPlayer.devCards.get("roadBuilding") - 1);
                                        Cards.numDevCards.put("roadBuilding", Cards.numDevCards.get("roadBuilding") + 1);
                                        gs.setSubState("");
                                        JOptionPane.showMessageDialog(null, "You do not have enough roads to build");
                                    }
                                    roadBuildI1 = null;
                                }
                            }
                        }
                    }
                }
                else if (gs.getSubState().equals("roadBuilding3")) {
                    for (int i = 0; i < intersections.length; i++) {
                        for (int j = 0; j < intersections[i].length; j++) {
                            if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                                if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                    gs.setSubState("roadBuilding4");
                                    roadBuildI1 = intersections[i][j];
                                }
                            }
                        }
                    }
                }
                else if (gs.getSubState().equals("roadBuilding4")) {
                    for (int i = 0; i < intersections.length; i++) {
                        for (int j = 0; j < intersections[i].length; j++) {
                            if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                                if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                    new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                    currentPlayer.manageRoads();
                                    pManage.updateLongestRoad();
                                    gs.setSubState("");
                                    roadBuildI1 = null;
                                    currentPlayer.devCards.put("roadBuilding", currentPlayer.devCards.get("roadBuilding") - 1);
                                    usedDevCard  = true;
                                    Cards.numDevCards.put("roadBuilding", Cards.numDevCards.get("roadBuilding") + 1);
                                }
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("")) {
                if (x >= 1600 && x <= 1600 + 170 && y >= 520 && y <= 580) { //if next turn button (1500, 520, 170, 60)
                    pManage.nextPlayer();
                    rolledDice = false;
                    usedDevCard = false;
                    gs.setSubState("");
                    gs.setGameState(1);
                    repaint();
                }
                else if (x>=30 && y>=630 && x<= 30 + 120 && y<=50+630) { //bank button 30, 630, 120, 50
                    JOptionPane.showMessageDialog(null, "Remaining Resource Cards: " + Cards.numResourceCards, "BANK", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i<pManage.size(); i++) {
                        JOptionPane.showMessageDialog(null, "Settlements: " + pManage.get(i).settlementsLeft() + "\nCities: " + pManage.get(i).citiesLeft() + "\nRoads: " + pManage.get(i).roadsLeft(), pManage.get(i).toString(), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if (x >= 1600 && x <= 1600 + 170 && y >= 200 && y <= 200 + 60) { //if trade button
                    String[] tradeTypes = new String[2];
                    tradeTypes[0] = "Trade With Players";
                    tradeTypes[1] = "Trade With Bank or Ports";
                    String picked = (String) JOptionPane.showInputDialog(null, "Which type of trade do you want?", "Trade Type", JOptionPane.QUESTION_MESSAGE, null, tradeTypes, tradeTypes[0]);
                    if (picked != null) {
                        if (picked.equals("Trade With Players")) {
                            gs.setSubState("domesticWant"); //current player adds items he wants to trade
                            finalOffers = new ArrayList<HashMap<String, Integer>>();
                            finalOfferPlayers = new ArrayList<Player>();
                            tradeITOrder = new ArrayList<Player>();
                            offers = new ArrayList<HashMap<String, Integer>>();
                            tradeITOrder.add(currentPlayer);
                            HashMap<String, Integer> temp = new HashMap<String, Integer>();
                            temp.put("brick", 0);
                            temp.put("ore", 0);
                            temp.put("sheep", 0);
                            temp.put("wheat", 0);
                            temp.put("wood", 0);
                            offers.add(temp);
                            System.out.println("order " + tradeITOrder);
                            System.out.println("offers " + offers);
                        }
                        else if (picked.equals("Trade With Bank or Ports")) {
                            gs.setSubState("maritime");
                        }
                    }
                    repaint();
                }
                else if (x>=1600 && y>=440 && x<=1600+170 && y<=440+60)  { //inventories button 1600, 440, 170, 60
                    String[] toView = new String[pManage.size()-1];
                    int count = 0;
                    for (int i = 0; i< pManage.size(); i++) {
                        if (pManage.get(i) != currentPlayer) {
                            toView[count] = pManage.get(i).toString() + "(" + pManage.get(i).getColor() + ")";
                            count++;
                        }
                    }
                    String picked = (String) JOptionPane.showInputDialog(null, "Which Player's inventory do you want to view(only click if you are the corresponding player)", "Players", JOptionPane.QUESTION_MESSAGE, null, toView, toView[0]);
                    if (picked != null) {
                        gs.setSubState("showInventory");
                        int playerIndex = Integer.parseInt("" + picked.charAt(7));
                        toViewInven = pManage.get(playerIndex);
                    }
                    repaint();
                }
                else if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60)  { //build button 1600, 360, 170, 60
                    System.out.println("current inventory: " + currentPlayer.resources);
                    if (currentPlayer.enoughResourcesCard() || currentPlayer.enoughResourcesRoad() ||
                            currentPlayer.enoughResourcesSettlement() || currentPlayer.enoughResourcesCity()) {
                        gs.setGameState(2);
                        System.out.println("entered build");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "You do not have enough resources to build anything. You can either trade or end your turn.", "NOTICE", JOptionPane.ERROR_MESSAGE);
                        gs.setSubState("");
                    }
                }
                //development cards
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("roadBuilding") && currentPlayer.devCards.get("roadBuilding") > 0 && usedDevCard == false) {
                    if (currentPlayer.roadsLeft() > 0) {
                        gs.setSubState("roadBuilding1");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "You do not have enough roads to build", "NOTICE", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("monopoly") && currentPlayer.devCards.get("monopoly") > 0 && usedDevCard == false) {
                    String[]  options = new String[5];
                    options[0] = "Wheat";
                    options[1] = "Ore";
                    options[2] = "Sheep";
                    options[3] = "Wood";
                    options[4] = "Brick";

                    String picked = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked != null) {
                        currentPlayer.devCards.put("monopoly", currentPlayer.devCards.get("monopoly") - 1);
                        Cards.numDevCards.put("monopoly", Cards.numDevCards.get("monopoly") + 1);
                        pManage.monopoly(currentPlayer, picked.toLowerCase());
                        usedDevCard = true;
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("yearOfPlenty") && currentPlayer.devCards.get("yearOfPlenty") > 0 && usedDevCard == false) {
                    ArrayList<String> options1 = new ArrayList<>();
                    for (String s : Cards.numResourceCards.keySet()) {
                        if (Cards.numResourceCards.get(s) > 0) {
                            options1.add(s);
                        }
                    }
                    String[] options = new String[options1.size()];
                    for (int i = 0; i < options1.size(); i++) {
                        options[i] = options1.get(i);
                    }
                    String picked1 = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource 1", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked1 != null) {
                        ArrayList<String> options2 = new ArrayList<>();
                        for (String s : Cards.numResourceCards.keySet()) {
                            if (s.equals(picked1)) {
                                if (Cards.numResourceCards.get(s) > 1) {
                                    options2.add(s);
                                }
                            }
                            else {
                                if (Cards.numResourceCards.get(s) > 0) {
                                    options2.add(s);
                                }
                            }
                        }
                        String[] options22 = new String[options2.size()];
                        for (int i = 0; i < options2.size(); i++) {
                            options22[i] = options2.get(i);
                        }
                        String picked2 = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource 2", JOptionPane.QUESTION_MESSAGE, null, options22, options22[0]);
                        if (picked2 != null) {
                            currentPlayer.devCards.put("yearOfPlenty", currentPlayer.devCards.get("yearOfPlenty") - 1);
                            Cards.numDevCards.put("yearOfPlenty", Cards.numDevCards.get("yearOfPlenty") + 1);
                            pManage.yearOfPlenty(currentPlayer, picked1.toLowerCase(), picked2.toLowerCase());
                            usedDevCard = true;
                        }
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("knight") && currentPlayer.devCards.get("knight") > 0 && usedDevCard == false) {
                    gs.setSubState("knight");
                    currentPlayer.useKnight();
                    pManage.updateLargestArmy();
                    usedDevCard = true;
                }
            }
            else if (gs.getSubState().equals("knight")) {
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[i].length; j++) {
                        if (tiles[i][j] != null && x >= tiles[i][j].getX() + 52 && x <= tiles[i][j].getX() + 52 + 55 && y >= tiles[i][j].getY() + 50 && y <= tiles[i][j].getY() + 100 && tiles[i][j] != robber.getPosition() && tiles[i][j].getResource() != "desert") {
                            robber.setPosition(tiles[i][j]);
                            gs.setSubState("");
                            System.out.println("robber moved to " + i + " " + j + " " + tiles[i][j].getResource());
                            boolean movedRobber = false;
                            //stealing
                            ArrayList<String> owners = new ArrayList<String>();
                            System.out.println(tiles[i][j].settles.size());
                            for (int k = 0; k < tiles[i][j].settles.size(); k++) {
                                System.out.println("settlement at " + tiles[i][j].settles.get(k) + " " + tiles[i][j].settles.get(k).getOwner());
                                Player currentOwner = tiles[i][j].settles.get(k).getOwner();
                                if (currentOwner != currentPlayer && owners.contains(currentOwner.toString()) == false && currentOwner.getInventorySize() > 0) {
                                    owners.add(tiles[i][j].settles.get(k).getOwner().toString());
                                }
                            }
                            System.out.println("could steal from" + owners);
                            if (owners.size() > 0) {
                                String[] owns = new String[owners.size()];
                                for (int n = 0; n < owners.size(); n++) {
                                    owns[n] = owners.get(n) + "(Inventory Size: " + pManage.toStringReverse(owners.get(n)).getInventorySize() + " Color: " + pManage.toStringReverse(owners.get(n)).color.toUpperCase() + ")";
                                }
                                System.out.println("choose player to steal");
                                while (movedRobber == false) {
                                    String picked = (String) JOptionPane.showInputDialog(null, "What player do you want to rob?", "Rob Player", JOptionPane.QUESTION_MESSAGE, null, owns, owns[0]);
                                    if (picked != null) {
                                        Player toSteal = pManage.get(Integer.parseInt("" + picked.charAt(7)));
                                        System.out.println("stole " + pManage.steal(currentPlayer, toSteal));
                                        movedRobber = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("showInventory")) {
                if (x >= 300 && x <= 300 + 60 && y >= 900 && y <= 900 + 30) { //back button
                    gs.setSubState("");
                    toViewInven = null;
                }

            }
            else if (gs.getSubState().equals("domesticWant")) {
                System.out.println("order " + tradeITOrder);
                System.out.println("offers " + offers);
                if (x >= 300 && x <= 300 + 60 && y >= 900 && y <= 900 + 30) { //if done button 300, 900, 60, 30
                    tradeITOrder.remove(0);
                    currentPlayerWant = offers.remove(0);
                    int sum = 0;
                    for (String s : currentPlayerWant.keySet()) {
                        sum += currentPlayerWant.get(s);
                    }
                    if (sum == 0) {
                        gs.setSubState("");
                        currentPlayerWant = null;
                    } else if (sum > 0) {
                        gs.setSubState("domesticPlayers");
                        for (int i = 0; i < pManage.players.size(); i++) {
                            if (pManage.players.get(i) != currentPlayer) {
                                if (pManage.players.get(i).getResources().get("wood") >= currentPlayerWant.get("wood") &&
                                        pManage.players.get(i).getResources().get("brick") >= currentPlayerWant.get("brick") && pManage.players.get(i).getResources().get("sheep") >= currentPlayerWant.get("sheep") && pManage.players.get(i).getResources().get("wheat") >= currentPlayerWant.get("wheat") && pManage.players.get(i).getResources().get("ore") >= currentPlayerWant.get("ore")) {
                                    //if enough resources to trade
                                    tradeITOrder.add(pManage.players.get(i));
                                    HashMap<String, Integer> temp = new HashMap<String, Integer>();
                                    temp.put("brick", 0);
                                    temp.put("ore", 0);
                                    temp.put("sheep", 0);
                                    temp.put("wheat", 0);
                                    temp.put("wood", 0);
                                    offers.add(temp);
                                }
                            }
                        }
                        if (tradeITOrder.size() == 0) {
                            gs.setSubState("domesticFinal");
                        }
                    }
                } else if (offers.size() > 0 && tradeITOrder.size() > 0) {
                    //if MouseEvent m left click
                    if (m.getButton() == MouseEvent.BUTTON1) {
                        String resource = coordToResource(x, y);
                        System.out.println("adding " + resource + " to want");
                        HashMap<String, Integer> currentOffer = offers.get(0);
                        if (resource != null) {
                            currentOffer.put(resource, currentOffer.get(resource) + 1);
                        }
                        System.out.println("updated want " + currentOffer);
                    }
                   else if (m.getButton() == MouseEvent.BUTTON3) {
                        String resource = coordToResource(x, y);
                        System.out.println("removing " + resource + " from want");
                        HashMap<String, Integer> currentOffer = offers.get(0);
                        if (resource != null && currentOffer.get(resource) > 0) {
                            currentOffer.put(resource, currentOffer.get(resource) - 1);
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("domesticPlayers")) {
                if (tradeITOrder.size() == 0) {
                    gs.setSubState("domesticFinal");
                }
                if (x >= 300 && x <= 300 + 60 && y >= 900 && y <= 900 + 30) { //if done button
                    HashMap<String, Integer> currentOffer = offers.remove(0);
                    Player currentTradePlayer = tradeITOrder.remove(0);

                    int sum = 0;
                    for (String r : currentOffer.keySet()) {
                        sum += currentOffer.get(r);
                    }
                    if (sum > 0) {
                        //check if trade is valid
                        int diff1 = currentOffer.get("wood") - currentPlayerWant.get("wood");
                        int diff2 = currentOffer.get("brick") - currentPlayerWant.get("brick");
                        int diff3 = currentOffer.get("sheep") - currentPlayerWant.get("sheep");
                        int diff4 = currentOffer.get("wheat") - currentPlayerWant.get("wheat");
                        int diff5 = currentOffer.get("ore") - currentPlayerWant.get("ore");
                        if (!(diff1 >= 0 && diff2 >= 0 && diff3 >= 0 && diff4 >= 0 && diff5 >= 0)) {
                            if (!(diff1 <= 0 && diff2 <= 0 && diff3 <= 0 && diff4 <= 0 && diff5 <= 0)) {
                                if (currentPlayer.getResourceCount("wood") >= currentOffer.get("wood") &&
                                        currentPlayer.getResourceCount("brick") >= currentOffer.get("brick") &&
                                        currentPlayer.getResourceCount("sheep") >= currentOffer.get("sheep") &&
                                        currentPlayer.getResourceCount("wheat") >= currentOffer.get("wheat") &&
                                        currentPlayer.getResourceCount("ore") >= currentOffer.get("ore")) {
                                    finalOfferPlayers.add(currentTradePlayer);
                                    finalOffers.add(currentOffer);
                                }
                            }
                        }
                    }

                    if (tradeITOrder.size() == 0) {
                        gs.setSubState("domesticFinal");
                    }
                } else if (offers.size() > 0 && tradeITOrder.size() > 0) {
                    if (m.getButton() == MouseEvent.BUTTON1) {
                        String resource = coordToResource(x, y);
                        HashMap<String, Integer> currentOffer = offers.get(0);
                        if (resource != null) {
                            currentOffer.put(resource, currentOffer.get(resource) + 1);
                        }
                    }
                    else if (m.getButton() == MouseEvent.BUTTON3) {
                        String resource = coordToResource(x, y);
                        HashMap<String, Integer> currentOffer = offers.get(0);
                        if (resource != null) {
                            if (currentOffer.get(resource) > 0) {
                                currentOffer.put(resource, currentOffer.get(resource) - 1);
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("robber")) {
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[i].length; j++) {
                        if (tiles[i][j] != null && x >= tiles[i][j].getX() + 52 && x <= tiles[i][j].getX() + 52 + 55 && y >= tiles[i][j].getY() + 50 && y <= tiles[i][j].getY() + 100 && tiles[i][j] != robber.getPosition() && tiles[i][j].getResource() != "desert") {
                            robber.setPosition(tiles[i][j]);
                            gs.setSubState("");
                            System.out.println("robber moved to " + i + " " + j + " " + tiles[i][j].getResource());
                            boolean movedRobber = false;
                            //stealing
                            ArrayList<String> owners = new ArrayList<String>();
                            System.out.println(tiles[i][j].settles.size());
                            for (int k = 0; k < tiles[i][j].settles.size(); k++) {
                                System.out.println("settlement at " + tiles[i][j].settles.get(k) + " " + tiles[i][j].settles.get(k).getOwner());
                                Player currentOwner = tiles[i][j].settles.get(k).getOwner();
                                if (currentOwner != currentPlayer && owners.contains(currentOwner.toString()) == false && currentOwner.getInventorySize() > 0) {
                                    owners.add(tiles[i][j].settles.get(k).getOwner().toString());
                                }
                            }
                            System.out.println("could steal from" + owners);
                            if (owners.size() > 0) {
                                String[] owns = new String[owners.size()];
                                for (int n = 0; n < owners.size(); n++) {
                                    owns[n] = owners.get(n) + "(Inventory Size: " + pManage.toStringReverse(owners.get(n)).getInventorySize() + " Color: " + pManage.toStringReverse(owners.get(n)).color.toUpperCase() + ")";
                                }
                                System.out.println("choose player to steal");
                                while (movedRobber == false) {
                                    String picked = (String) JOptionPane.showInputDialog(null, "What player do you want to rob?", "Rob Player", JOptionPane.QUESTION_MESSAGE, null, owns, owns[0]);
                                    if (picked != null) {
                                        Player toSteal = pManage.get(Integer.parseInt("" + picked.charAt(7)));
                                        System.out.println("stole " + pManage.steal(currentPlayer, toSteal));;
                                        movedRobber = true;
                                    }
                                }


                            }
                            //discarding
                            toDiscard = new ArrayList<Player>();
                            numDiscard = new ArrayList<Integer>();
                            for (int b = 0; b < pManage.size(); b++) {
                                if (pManage.get(b).getInventorySize() > 7 && pManage.get(b) != currentPlayer) {
                                    toDiscard.add(pManage.get(b));
                                    numDiscard.add((int) Math.floor(pManage.get(b).getInventorySize() / 2.0));
                                }
                            }
                            if (toDiscard.size() > 0) {
                                gs.setSubState("discard");
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("discard")) {
                if (numDiscard.get(0) > 0) {
                    Player temp = toDiscard.get(0);
                    String resource = coordToResource(x, y);
                    System.out.println("discarding " + resource);
                    if (resource != null) {
                        if (temp.getResourceCount(resource) > 0) {
                            temp.removeResource(resource, 1);
                            numDiscard.set(0, numDiscard.get(0) - 1);
                            if (numDiscard.get(0) == 0) {
                                toDiscard.remove(0);
                                numDiscard.remove(0);
                                if (toDiscard.size() == 0) {
                                    gs.setSubState("");
                                }
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding1")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                gs.setSubState("roadBuilding2");
                                roadBuildI1 = intersections[i][j];
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding2")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                    roadBuildI1 = null;
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                currentPlayer.manageRoads();
                                pManage.updateLongestRoad();
                                if (currentPlayer.roadsLeft()>0) {
                                    gs.setSubState("roadBuilding3");
                                }
                                else {
                                    currentPlayer.devCards.put("roadBuilding", currentPlayer.devCards.get("roadBuilding") - 1);
                                    Cards.numDevCards.put("roadBuilding", Cards.numDevCards.get("roadBuilding") + 1);
                                    gs.setSubState("");
                                    JOptionPane.showMessageDialog(null, "You do not have enough roads to build");
                                }
                                roadBuildI1 = null;
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding3")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                gs.setSubState("roadBuilding4");
                                roadBuildI1 = intersections[i][j];
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding4")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                currentPlayer.manageRoads();
                                pManage.updateLongestRoad();
                                gs.setSubState("");
                                roadBuildI1 = null;
                                currentPlayer.devCards.put("roadBuilding", currentPlayer.devCards.get("roadBuilding") - 1);
                                usedDevCard  = true;
                                Cards.numDevCards.put("roadBuilding", Cards.numDevCards.get("roadBuilding") + 1);
                            }
                        }
                    }
                }
            }
            repaint();
        }
        if(gs.getGameState() == 2){
            if (x>=1600 && x<=1600+170 && y>=280 && y<=280+60) { //1600, 280, 170, 60
                if (showExpected) {
                    showExpected = false;
                }
                else {
                    showExpected = true;
                }
            }
            if (gs.getSubState().equals("")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60)  { //build button 1600, 360, 170, 60
                    ArrayList<String> possibleOptions = new ArrayList<>();
                    System.out.println("current inventory: " + currentPlayer.resources);
                    if (currentPlayer.enoughResourcesCard()) {
                        possibleOptions.add("Development Card");
                    } //devCard
                    if (currentPlayer.enoughResourcesSettlement()) {
                        possibleOptions.add("Settlement");
                    } //settlement
                    if (currentPlayer.enoughResourcesRoad()) {
                        possibleOptions.add("Road");
                    }
                    if (currentPlayer.enoughResourcesCity()) {
                        possibleOptions.add("City");
                    }
                    String[] options = new String[possibleOptions.size()];
                    for (int i = 0; i < possibleOptions.size(); i++) {
                        options[i] = possibleOptions.get(i);
                    }
                    if (options.length > 0) {
                        String picked = (String) JOptionPane.showInputDialog(null, "Choose what you want to build or buy", "Building and Buying", JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
                        if (picked != null) {
                            if (picked.equals("Settlement")) {
                                gs.setSubState("settlement");
                            }
                            else if (picked.equals("Road")) {
                                gs.setSubState("road");
                            }
                            else if (picked.equals("City")) {
                                gs.setSubState("city");
                            }
                            else if (picked.equals("Development Card")) {
                                Cards.giveDevCard(currentPlayer);
                                currentPlayer.removeResource("wheat", 1);
                                currentPlayer.removeResource("sheep", 1);
                                currentPlayer.removeResource("ore", 1);
                                gs.setSubState("");
                                System.out.println("giving dev card");
                                System.out.println("new dev cards " + currentPlayer.newDevCards);
                                System.out.println("old dev cards " + currentPlayer.devCards);
                                repaint();
                            }
                        }
                        else {
                            gs.setSubState("");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "You do not have enough resources to build anything. You can either trade or end your turn.", "NOTICE", JOptionPane.ERROR_MESSAGE);
                        gs.setSubState("");
                    }
                }
                else if (x>=1600 && y>=440 && x<=1600+170 && y<=440+60)  { //inventories button 1600, 440, 170, 60
                    String[] toView = new String[pManage.size()-1];
                    int count = 0;
                    for (int i = 0; i< pManage.size(); i++) {
                        if (pManage.get(i) != currentPlayer) {
                            toView[count] = pManage.get(i).toString() + "(" + pManage.get(i).getColor() + ")";
                            count++;
                        }
                    }
                    String picked = (String) JOptionPane.showInputDialog(null, "Which Player's inventory do you want to view(only click if you are the corresponding player)", "Players", JOptionPane.QUESTION_MESSAGE, null, toView, toView[0]);
                    if (picked != null) {
                        gs.setSubState("showInventory");
                        int playerIndex = Integer.parseInt("" + picked.charAt(7));
                        toViewInven = pManage.get(playerIndex);
                    }
                }
                else if (x >= 1600 && x <= 1600 + 170 && y >= 520 && y <= 580) { //if next turn button (1500, 520, 170, 60)
                    pManage.nextPlayer();
                    rolledDice = false;
                    usedDevCard = false;
                    gs.setSubState("");
                    gs.setGameState(1);
                }
                //development cards
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("roadBuilding") && currentPlayer.devCards.get("roadBuilding") > 0 && usedDevCard == false) {
                    if (currentPlayer.roadsLeft() > 0) {
                        gs.setSubState("roadBuilding1");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "You do not have enough roads to build", "NOTICE", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("monopoly") && currentPlayer.devCards.get("monopoly") > 0 && usedDevCard == false) {
                    String[]  options = new String[5];
                    options[0] = "Wheat";
                    options[1] = "Ore";
                    options[2] = "Sheep";
                    options[3] = "Wood";
                    options[4] = "Brick";

                    String picked = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked != null) {
                        currentPlayer.devCards.put("monopoly", currentPlayer.devCards.get("monopoly") - 1);
                        Cards.numDevCards.put("monopoly", Cards.numDevCards.get("monopoly") + 1);
                        pManage.monopoly(currentPlayer, picked.toLowerCase());
                        usedDevCard = true;
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("yearOfPlenty") && currentPlayer.devCards.get("yearOfPlenty") > 0 && usedDevCard == false) {
                    ArrayList<String> options1 = new ArrayList<>();
                    for (String s : Cards.numResourceCards.keySet()) {
                        if (Cards.numResourceCards.get(s) > 0) {
                            options1.add(s);
                        }
                    }
                    String[] options = new String[options1.size()];
                    for (int i = 0; i < options1.size(); i++) {
                        options[i] = options1.get(i);
                    }
                    String picked1 = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource 1", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (picked1 != null) {
                        ArrayList<String> options2 = new ArrayList<>();
                        for (String s : Cards.numResourceCards.keySet()) {
                            if (s.equals(picked1)) {
                                if (Cards.numResourceCards.get(s) > 1) {
                                    options2.add(s);
                                }
                            }
                            else {
                                if (Cards.numResourceCards.get(s) > 0) {
                                    options2.add(s);
                                }
                            }
                        }
                        String[] options22 = new String[options2.size()];
                        for (int i = 0; i < options2.size(); i++) {
                            options22[i] = options2.get(i);
                        }
                        String picked2 = (String) JOptionPane.showInputDialog(null, "Which resource do you want?", "Choose Resource 2", JOptionPane.QUESTION_MESSAGE, null, options22, options22[0]);
                        if (picked2 != null) {
                            currentPlayer.devCards.put("yearOfPlenty", currentPlayer.devCards.get("yearOfPlenty") - 1);
                            Cards.numDevCards.put("yearOfPlenty", Cards.numDevCards.get("yearOfPlenty") + 1);
                            pManage.yearOfPlenty(currentPlayer, picked1.toLowerCase(), picked2.toLowerCase());
                            usedDevCard = true;
                        }
                    }
                }
                else if (coordToDevCard(x, y) != null && coordToDevCard(x, y).equals("knight") && currentPlayer.devCards.get("knight") > 0 && usedDevCard == false) {
                    gs.setSubState("knight");
                    currentPlayer.useKnight();
                    pManage.updateLargestArmy();
                    usedDevCard = true;
                }
            }
            else if (gs.getSubState().equals("knight")) {
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[i].length; j++) {
                        if (tiles[i][j] != null && x >= tiles[i][j].getX() + 52 && x <= tiles[i][j].getX() + 52 + 55 && y >= tiles[i][j].getY() + 50 && y <= tiles[i][j].getY() + 100 && tiles[i][j] != robber.getPosition() && tiles[i][j].getResource() != "desert") {
                            robber.setPosition(tiles[i][j]);
                            gs.setSubState("");
                            System.out.println("robber moved to " + i + " " + j + " " + tiles[i][j].getResource());
                            boolean movedRobber = false;
                            //stealing
                            ArrayList<String> owners = new ArrayList<String>();
                            System.out.println(tiles[i][j].settles.size());
                            for (int k = 0; k < tiles[i][j].settles.size(); k++) {
                                System.out.println("settlement at " + tiles[i][j].settles.get(k) + " " + tiles[i][j].settles.get(k).getOwner());
                                Player currentOwner = tiles[i][j].settles.get(k).getOwner();
                                if (currentOwner != currentPlayer && owners.contains(currentOwner.toString()) == false && currentOwner.getInventorySize() > 0) {
                                    owners.add(tiles[i][j].settles.get(k).getOwner().toString());
                                }
                            }
                            System.out.println("could steal from" + owners);
                            if (owners.size() > 0) {
                                String[] owns = new String[owners.size()];
                                for (int n = 0; n < owners.size(); n++) {
                                    owns[n] = owners.get(n) + "(Inventory Size: " + pManage.toStringReverse(owners.get(n)).getInventorySize() + " Color: " + pManage.toStringReverse(owners.get(n)).color.toUpperCase() + ")";
                                }
                                System.out.println("choose player to steal");
                                while (movedRobber == false) {
                                    String picked = (String) JOptionPane.showInputDialog(null, "What player do you want to rob?", "Rob Player", JOptionPane.QUESTION_MESSAGE, null, owns, owns[0]);
                                    if (picked != null) {
                                        Player toSteal = pManage.get(Integer.parseInt("" + picked.charAt(7)));
                                        System.out.println("stole " + pManage.steal(currentPlayer, toSteal));
                                        movedRobber = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("showInventory")) {
                if (x >= 300 && x <= 300 + 60 && y >= 900 && y <= 900 + 30) { //back button
                    gs.setSubState("");
                    toViewInven = null;
                }
            }
            else if (gs.getSubState().equals("settlement")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                }

                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.settleAble().contains(intersections[i][j])) {
                                intersections[i][j].setSettlement(currentPlayer);
                                currentPlayer.removeResource("brick", 1);
                                currentPlayer.removeResource("wood", 1);
                                currentPlayer.removeResource("sheep", 1);
                                currentPlayer.removeResource("wheat", 1);
                                gs.setSubState("");
                            }
                        }
                    }
                }

            }
            else if (gs.getSubState().equals("road")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                gs.setSubState("road1");
                                roadBuildI1 = intersections[i][j];
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("road1")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                    roadBuildI1 = null;
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                currentPlayer.removeResource("brick", 1);
                                currentPlayer.removeResource("wood", 1);
                                currentPlayer.manageRoads();
                                pManage.updateLongestRoad();
                                gs.setSubState("");
                                roadBuildI1 = null;
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("city")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.upgradeAble().contains(intersections[i][j])) {
                                currentPlayer.removeResource("wheat", 2);
                                currentPlayer.removeResource("ore", 3);
                                intersections[i][j].getSettlement().upgrade();
                                gs.setSubState("");
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding1")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                gs.setSubState("roadBuilding2");
                                roadBuildI1 = intersections[i][j];
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding2")) {
                if (x>=1600 && y>=360 && x<=1600+170 && y<=360+60) {//cancel(build) button
                    gs.setSubState("");
                    roadBuildI1 = null;
                }
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                currentPlayer.manageRoads();
                                pManage.updateLongestRoad();
                                if (currentPlayer.roadsLeft()>0) {
                                    gs.setSubState("roadBuilding3");
                                }
                                else {
                                    currentPlayer.devCards.put("roadBuilding", currentPlayer.devCards.get("roadBuilding") - 1);
                                    Cards.numDevCards.put("roadBuilding", Cards.numDevCards.get("roadBuilding") + 1);
                                    gs.setSubState("");
                                    JOptionPane.showMessageDialog(null, "You do not have enough roads to build");
                                }
                                roadBuildI1 = null;
                                currentPlayer.manageRoads();
                                pManage.updateLongestRoad();
                                roadBuildI1 = null;
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding3")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                                gs.setSubState("roadBuilding4");
                                roadBuildI1 = intersections[i][j];
                            }
                        }
                    }
                }
            }
            else if (gs.getSubState().equals("roadBuilding4")) {
                for (int i = 0; i < intersections.length; i++) {
                    for (int j = 0; j < intersections[i].length; j++) {
                        if (intersections[i][j] != null && intersections[i][j].getX() - 14 <= x && x <= intersections[i][j].getX() + 14 && intersections[i][j].getY() - 14 <= y && y <= intersections[i][j].getY() + 14) {
                            if (currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                                new Road(roadBuildI1, intersections[i][j], currentPlayer);
                                currentPlayer.manageRoads();
                                pManage.updateLongestRoad();
                                gs.setSubState("");
                                roadBuildI1 = null;
                                currentPlayer.devCards.put("roadBuilding", currentPlayer.devCards.get("roadBuilding") - 1);
                                usedDevCard  = true;
                                Cards.numDevCards.put("roadBuilding", Cards.numDevCards.get("roadBuilding") + 1);
                            }
                        }
                    }
                }
            }
        }
        repaint();
    }

    public void highlightUpgradeAble(Graphics g) {
        g.setColor(Color.cyan);
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null && currentPlayer.upgradeAble().contains(intersections[i][j])) {
                    g.fillRect(intersections[i][j].getX()-10, intersections[i][j].getY()-10, 10, 10);
                }
            }
        }
    }
    public void highlightSettleAble(Graphics g) {
        g.setColor(Color.cyan);
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null && currentPlayer.settleAble().contains(intersections[i][j])) {
                    g.fillRect(intersections[i][j].getX()-10, intersections[i][j].getY()-10, 10, 10);
                }
            }
        }
    }
    public void highlightRoadAble1(Graphics g) {
        g.setColor(Color.cyan);
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null && currentPlayer.possibleRoadI1().contains(intersections[i][j])) {
                    g.fillRect(intersections[i][j].getX()-10, intersections[i][j].getY()-10, 10, 10);
                }
            }
        }
    }
    public void highlightRoadAble2(Graphics g) {
        g.setColor(Color.cyan);
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null && currentPlayer.possibleRoadI2(roadBuildI1).contains(intersections[i][j])) {
                    g.fillRect(intersections[i][j].getX()-10, intersections[i][j].getY()-10, 10, 10);
                }
            }
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
    public void drawTradeNums(Graphics g, HashMap<String, Integer> temp, Player p) {
        g.setFont(new Font("Helvetica", Font.PLAIN, 70));
        Set<String> keys = temp.keySet();
        if (keys != null) {
            System.out.println("found requested resources from " + temp);
            Iterator<String> iter = keys.iterator();
            int width = 100;
            int count = 0;
            int horDiff = width + 30;
            while (iter.hasNext()) {
                String resource = iter.next();
                System.out.println("drawing " + resource);
                int amount = temp.get(resource);
                g.setColor(Color.black);
                g.drawString("" + amount, 461 + horDiff * count, 852);
                count++;
            }
        }
    }
    public void drawTrade(Graphics g) {
        changeColor(g);
        g.fillRoundRect(1600, 200, 170, 60, 20, 20);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        g.drawString("Trade", 1645, 200+40);
    }
    public void drawTradeGray(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRoundRect(1600, 200, 170, 60, 20, 20);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        g.drawString("Trade", 1645, 200+40);
    }
    public void drawGameLog(Graphics g) {
        changeColor(g);
        g.fillRoundRect(1600, 280, 170, 60, 20, 20);
        g.setColor(Color.black);

        if (showExpected) {
            g.setFont(new Font("Helvetica", Font.PLAIN, 30));
            g.drawString("Hide Values", 1605, 280+40);
        }
        else {
            g.setFont(new Font("Helvetica", Font.PLAIN, 28));
            g.drawString("Show Values", 1605, 280+40);
        }

    }
    public void drawBuild(Graphics g) {
        changeColor(g);
        g.fillRoundRect(1600, 360, 170, 60, 20, 20);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        if (gs.getGameState() == 2 && ((gs.getSubState().equals("settlement"))||gs.getSubState().equals("road") || gs.getSubState().equals("city")) || gs.getSubState().equals("road1")) {
            g.drawString("Cancel Build", 1605, 360+40);
        }
        else {
            g.drawString("Build", 1650, 360+40);
        }

    }
    public void drawBuild1(Graphics g) {
        changeColor(g);
        g.fillRoundRect(1600, 360, 170, 60, 20, 20);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        g.drawString("Build", 1650, 360+40);
    }
    public void drawPlayerInfo(Graphics g) {
        changeColor(g);
        g.fillRoundRect(1600, 440, 170, 60, 20, 20);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        g.drawString("Inventories", 1615, 440+40);
    }
    public void drawNextTurnButton(Graphics g) {
        changeColor(g);
        g.fillRoundRect(1600, 520, 170, 60, 20, 20);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        g.drawString("Next Turn", 1620, 520+40);
    }

    public void drawRobber(Graphics g) {
        //g.drawImage(tiles[1][j].getNumImage(), (int)x+52, (int)y+50, 55, 55, null);
        g.drawImage(robberImg, robber.getPosition().getX()+53, robber.getPosition().getY()+30, 54, 111, null);
    }
    public void drawDevCards(Graphics g) {
        HashMap<String, Integer> tempDev = currentPlayer.devCards;
        HashMap<String, Integer> tempNew = currentPlayer.newDevCards;
        Set<String> keys = tempDev.keySet();
        double ratio = 482.0/326.0;
        if (keys != null) {
            Iterator<String> iter = keys.iterator();
            int width = 100;
            int height = (int) (width*ratio);
            int count = 0;
            int horDiff = width + 30;
            while (iter.hasNext()) {
                String card = iter.next();
                changeColor(g);
                int amount = tempDev.get(card) + tempNew.get(card);
                if (amount > 0) {
                    BufferedImage img = Cards.cardImages.get(card);
                    g.drawImage(img, 1050+horDiff*count, 800, width, height, null);
                    g.setFont(new Font("Helvetica", Font.PLAIN, 70));
                    g.drawString(""+amount, 1050+horDiff*count, 852);
                    count++;
                }
            }
        }
    }
    public String coordToDevCard(int x, int y) {
        HashMap<String, Integer> tempDev = currentPlayer.devCards;
        HashMap<String, Integer> tempNew = currentPlayer.newDevCards;
        Set<String> keys = tempDev.keySet();
        double ratio = 482.0/326.0;
        if (keys != null) {
            Iterator<String> iter = keys.iterator();
            int width = 100;
            int height = (int) (width*ratio);
            int count = 0;
            int horDiff = width + 30;
            while (iter.hasNext()) {
                String card = iter.next();
                int amount = tempDev.get(card) + tempNew.get(card);

                if (amount > 0) {
                    if (x>=1050+horDiff*count && x<=1050+horDiff*count+width && y>=800 && y<=800+height) {
                        return card;
                    }
                    count++;
                }
            }
        }
        return null;
    }
    public void drawCards(Graphics g, Player p) {
        g.setFont(new Font("Helvetica", Font.PLAIN, 70));
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
                g.drawString(""+p.getResourceCount(resource), 400+horDiff*count, 852);
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
                if (!pManage.get(i).getSettlements().get(j).isCity()) {
                    g.fillRect(pManage.get(i).getSettlements().get(j).getX()-10, pManage.get(i).getSettlements().get(j).getY()-10, 20, 20);
                }
                else {
                    g.fillRoundRect(pManage.get(i).getSettlements().get(j).getX()-10, pManage.get(i).getSettlements().get(j).getY()-10, 20, 20, 20, 20);
                }
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
        //276x370 longest road largest army
        g.drawImage(buildCosts, 1350, 500, 180, (int)(180.0/276.0*370.0), null);
        g.drawImage(armyImg, 1150, 500, 180, (int)(180.0/276.0*370.0), null);
        pManage.updateLargestArmy();
        if (pManage.largestArmy != null) {
            changeColor(g, pManage.largestArmy);
            g.setFont(new Font("Helvetica", Font.BOLD, 30));
            g.drawString(pManage.largestArmy.toString(), 1150, 530);
        }
        g.drawImage(roadImg, 950, 500, 180, (int)(180.0/276.0*370.0), null);
        pManage.updateLongestRoad();
        if (pManage.longestRoad != null) {
            changeColor(g, pManage.longestRoad);
            g.setFont(new Font("Helvetica", Font.BOLD, 30));
            g.drawString(pManage.longestRoad.toString(), 950, 530);
        }
        //bank
        changeColor(g);
        g.fillRoundRect(30, 630, 120, 50, 20, 20);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        g.setColor(Color.BLACK);
        g.drawString("Bank", 50, 663);
        //victory points
        if (currentPlayer.totalVP() >= 10 && gs.getGameState() != 3) {
            VictoryPanel vp = new VictoryPanel(currentPlayer.toString());
            gs.setGameState(3);

        }
        //player info
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        g.setColor(Color.GREEN);
        g.drawString("Player #", 1000, 230);
        g.drawImage(resourceFaceDown, 1130, 150, 100, 150, null);
        g.setFont(new Font("Helvetica", Font.BOLD, 10));
        g.drawString("# of Resources Cards",1125, 135);
        g.drawImage(devFaceDown, 1260, 150, 100, 150, null);
        g.drawString("# of Development Cards",1250, 135);
        g.drawImage(Cards.cardImages.get("knight"), 1380, 150, 100, 150, null);
        g.drawString("# of Knights Used",1387, 135);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        for (int i = 0; i<pManage.size(); i++) {
            changeColor(g, pManage.get(i));
            g.drawString(pManage.get(i).toString(), 1000, 330 + i*30);
            g.drawString(pManage.get(i).totalResources() + "", 1170, 330 + i*30);
            g.drawString(pManage.get(i).totalDevCards() + "", 1300, 330 + i*30);
            g.drawString(pManage.get(i).knightsUsed + "", 1420, 330 + i*30);
        }
    }
    public void drawIntersections(Graphics g) {
        g.setColor(Color.green);
        for (int i = 0; i < intersections.length; i++) {
            for (int j = 0; j < intersections[i].length; j++) {
                if (intersections[i][j] != null) {
                    g.fillRect(intersections[i][j].getX()-10, intersections[i][j].getY()-10, 10, 10);
                }
            }
        }
    }
    public void drawExpected(Graphics g) {
        if (showExpected) {
            for (int i = 0; i < intersections.length; i++) {
                for (int j = 0; j < intersections[i].length; j++) {
                    if (intersections[i][j] != null) {
                        g.setColor(Color.green);
                        g.setFont(new Font("Helvetica", Font.BOLD, 15));
                        double factor = 1e5; // = 1 * 10^5 = 100000.
                        double result = Math.round(intersections[i][j].getExpectedValue() * factor) / factor;
                        g.drawString( result + "", intersections[i][j].getX()-30, intersections[i][j].getY()-15);
                    }
                }
            }
        }
    }
    private void drawPorts(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int x = 246;
        int y = 47;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(103, 52, 1));
        g2.drawImage(portImages.get(0), x-50, y-50, 100, 100,  null);
        g2.drawLine(250, 120, 250, 80);
        g2.drawLine(320, 84, 277, 75);
        g2.drawImage(portImages.get(1), 84-30, 273-30, 100, 100,  null);
        g2.drawLine(163, 250, 133, 270);
        g2.drawLine(162, 325, 138, 315);
        g2.drawImage(portImages.get(2), 84-30, 534-50, 100, 100,  null);
        g2.drawLine(163, 490, 133, 510);
        g2.drawLine(162, 565, 138, 555);
        g2.drawImage(portImages.get(3), 240-10, 760-50, 100, 100,  null);
        g2.drawLine(250, 695, 255, 725);
        g2.drawLine(322, 728, 308, 735);
        g2.drawImage(portImages.get(4), 559-40, 760-50, 100, 100,  null);
        g2.drawLine(563, 695, 563, 725);
        g2.drawLine(490, 728, 540, 735);
        g2.drawImage(portImages.get(5), 795-50, 639-35, 100, 100,  null);
        g2.drawLine(727,610,770,620);
        g2.drawLine(800,573,800,622);
        g2.drawImage(portImages.get(6), 940-50, 404-50, 100, 100,  null);
        g2.drawLine(885, 370, 912, 370);
        g2.drawLine(885, 450, 912, 440);
        g2.drawImage(portImages.get(7), 788-50, 162-50, 100, 100,  null);
        g2.drawLine(730,207,760,197);
        g2.drawLine(800,198,800,242);
        g2.drawImage(portImages.get(8), 698-190, y-50, 100, 100,  null);
        g2.drawLine(564, 120, 564, 80);
        g2.drawLine(485, 83, 525, 75);

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
        g.setFont(new Font("Helvetica", Font.PLAIN, 70));
        g.drawString("Player " + p.playerIndex, 30, 850);
        int vp = currentPlayer.updateVP();
        int hvp = currentPlayer.updateHiddenVP();
        g.setFont(new Font("Helvetica", Font.PLAIN, 30));
        g.drawString("Points: " + vp + "(+"+hvp+") = " + (hvp+vp), 30, 920);
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
