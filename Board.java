import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;

public class Board{
    private Tile[][] tiles;
    private Intersection[][] intersections;
    private Port[] ports;
    private BufferedImage[] numImages;

    public Board() {
        setUpTiles();
        setUpIntersections();
        //setPorts();
    }
    public Intersection[][] getIntersections() {
        return intersections;
    }
    public void setUpTiles() {
        tiles = new Tile[5][5]; // [0][0] [0][4] [1][4] [3][4] [4][0] [4][4]
        numImages = new BufferedImage[13];
        //sets up resources for each tile in the board
        BufferedImage wood = null;
        BufferedImage sheep = null;
        BufferedImage wheat = null;
        BufferedImage brick = null;
        BufferedImage ore = null;
        BufferedImage desert = null;
        try{
            wood = ImageIO.read(Board.class.getResource("/TileImages/WoodTile.png"));
            sheep = ImageIO.read(Board.class.getResource("/TileImages/SheepTile.png"));
            wheat = ImageIO.read(Board.class.getResource("/TileImages/WheatTile.png"));
            brick = ImageIO.read(Board.class.getResource("/TileImages/BrickTile.png"));
            ore = ImageIO.read(Board.class.getResource("/TileImages/MountainTile.png"));
            desert = ImageIO.read(Board.class.getResource("/TileImages/DesertTile.png"));
            numImages[2] = ImageIO.read(Board.class.getResource("/Numbers/two.png"));
            numImages[3] = ImageIO.read(Board.class.getResource("/Numbers/three.png"));
            numImages[4] = ImageIO.read(Board.class.getResource("/Numbers/four.png"));
            numImages[5] = ImageIO.read(Board.class.getResource("/Numbers/five.png"));
            numImages[6] = ImageIO.read(Board.class.getResource("/Numbers/six.png"));
            numImages[8] = ImageIO.read(Board.class.getResource("/Numbers/eight.png"));
            numImages[9] = ImageIO.read(Board.class.getResource("/Numbers/nine.png"));
            numImages[10] = ImageIO.read(Board.class.getResource("/Numbers/ten.png"));
            numImages[11] = ImageIO.read(Board.class.getResource("/Numbers/eleven.png"));
            numImages[12] = ImageIO.read(Board.class.getResource("/Numbers/twelve.png"));
        }
        catch(Exception e){
            System.out.println("Error loading image");
        }
        ArrayList<Tile> tileList = new ArrayList<Tile>();
        for (int i = 0; i<4; i++){
            tileList.add(new Tile("wood", wood));
            tileList.add(new Tile("sheep", sheep));
            tileList.add(new Tile("wheat", wheat));
        }
        for (int i = 0; i<3; i++){
            tileList.add(new Tile("brick", brick));
            tileList.add(new Tile("ore", ore));
        }

        tileList.add(new Tile("desert", desert));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!((i == 0 && j == 0)|| (i == 0 && j == 4) || (i == 4 && j == 0) || (i == 4 && j == 4) || (i == 1 && j == 4) || (i == 3 && j == 4))) {
                    //randomize tiles from tileList
                    int random = (int) (Math.random() * tileList.size());
                    tiles[i][j] = tileList.remove(random);
                }
            }
        }

        //set up numbers for the tiles,
        // TODO: check if numbers can be just from left from right, and if numbers are accurate

        /*
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ((tiles[i][j] != null && !tiles[i][j].getResource().equals("desert"))) {
                    tiles[i][j].setNumber(order.remove(0));
                }
            }
        }
        */
        //connect tiles
        //TODO: assumed that if we didnt put a tile in a certain spot(in tiles[][]), it will be null
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!((i == 0 && j == 0)|| (i == 0 && j == 4) || (i == 4 && j == 0) || (i == 4 && j == 4) || (i == 1 && j == 4) || (i == 3 && j == 4))) {
                    Tile temp = tiles[i][j];
                    if (i == 0) {
                        temp.setT5(tiles[i+1][j-1]);
                        temp.setT4(tiles[i+1][j]);
                        temp.setT6(tiles[i][j-1]);
                        temp.setT3(tiles[i][j+1]);
                    }
                    else if (i == 1) {
                        temp.setT1(tiles[i - 1][j]);
                        temp.setT5(tiles[i + 1][j]);
                        temp.setT2(tiles[i-1][j + 1]);
                        temp.setT4(tiles[i + 1][j + 1]);
                        if (j != 0) {
                            temp.setT6(tiles[i][j - 1]);
                        }
                        temp.setT3(tiles[i][j + 1]);
                    }
                    else if (i == 2) {
                        if (j>0) {
                            temp.setT1(tiles[i - 1][j-1]);
                            temp.setT6(tiles[i][j-1]);
                            temp.setT5(tiles[i + 1][j-1]);
                        }
                        temp.setT4(tiles[i + 1][j]);
                        temp.setT2(tiles[i-1][j]);
                        if (j != 4) {
                            temp.setT3(tiles[i][j + 1]);
                        }
                    }
                    else if (i == 3) {
                        temp.setT1(tiles[i - 1][j]);
                        temp.setT5(tiles[i + 1][j]);
                        temp.setT2(tiles[i-1][j + 1]);
                        temp.setT4(tiles[i + 1][j + 1]);
                        if (j != 0) {
                            temp.setT6(tiles[i][j - 1]);
                        }
                        temp.setT3(tiles[i][j + 1]);
                    }
                    else if (i == 4) {
                        temp.setT1(tiles[i - 1][j-1]);
                        temp.setT6(tiles[i][j-1]);
                        temp.setT3(tiles[i][j+1]);
                        temp.setT2(tiles[i-1][j]);
                    }
                }
            }
        }
        ArrayList<Integer> order = new ArrayList<>();
        order.add(5);
        order.add(2);
        order.add(6);
        order.add(3);
        order.add(8);
        order.add(10);
        order.add(9);
        order.add(12);
        order.add(11);
        order.add(4);
        order.add(8);
        order.add(10);
        order.add(9);
        order.add(4);
        order.add(5);
        order.add(6);
        order.add(3);
        order.add(11);
        int start = (int)(Math.random()*4);
            ArrayList<Tile> visited = new ArrayList<Tile>();
            if(start == 0){
                //System.out.println("Start " + start);
                Tile currentTile = tiles[0][1];
                if(!currentTile.resource.equals("desert")){
                    currentTile.setNumber(order.remove(0));
                    currentTile.setNumImage(numImages[currentTile.getNumber()]);
                }
                int count = 1;
                while(!order.isEmpty()){
                    visited.add(currentTile);
                    if(count < 8){
                        if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                    }
                    else if(count < 15){
                        if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                    }
                    else{
                        if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                    }
                    if(!currentTile.resource.equals("desert")){
                        currentTile.setNumber(order.remove(0));
                        currentTile.setNumImage(numImages[currentTile.getNumber()]);
                    }
                    count++;
                    //System.out.println(currentTile.getNumber());
                }
            }
            else if(start == 1){
                //System.out.println("Start " + start);
                Tile currentTile = tiles[4][1];
                if(!currentTile.resource.equals("desert")){
                    currentTile.setNumber(order.remove(0));
                    currentTile.setNumImage(numImages[currentTile.getNumber()]);
                }
                int count = 1;
                while(!order.isEmpty()){
                    visited.add(currentTile);
                    if(count < 10){
                        if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                    }
                    else if(count < 15){
                        if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                    }
                    else{
                        if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                    }
                    if(!currentTile.resource.equals("desert")){
                        currentTile.setNumber(order.remove(0));
                        currentTile.setNumImage(numImages[currentTile.getNumber()]);
                    }
                    count++;
                }
            }
            else if(start == 2){
                //System.out.println("Start " + start);
                Tile currentTile = tiles[4][3];
                if(!currentTile.resource.equals("desert")){
                    currentTile.setNumber(order.remove(0));
                    currentTile.setNumImage(numImages[currentTile.getNumber()]);
                }
                int count = 1;
                while(!order.isEmpty()){
                    visited.add(currentTile);
                    if(count < 9){
                        if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                    }
                    else if(count < 15){
                        if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                    }
                    else{
                        if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                    }
                    if(!currentTile.resource.equals("desert")){
                        currentTile.setNumber(order.remove(0));
                        currentTile.setNumImage(numImages[currentTile.getNumber()]);
                    }
                    count++;
                }
            }
            else if(start == 3){
                //System.out.println("Start " + start);
                Tile currentTile = tiles[0][3];
                if(!currentTile.resource.equals("desert")){
                    currentTile.setNumber(order.remove(0));
                    currentTile.setNumImage(numImages[currentTile.getNumber()]);
                }
                int count = 1;
                while(!order.isEmpty()){
                    visited.add(currentTile);
                    if(count < 10){
                        if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                    }
                    else if(count < 15){
                        if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                        else if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                    }
                    else{
                        if(currentTile.t5 != null && !visited.contains(currentTile.t5)){
                            currentTile = currentTile.t5;
                        }
                        else if(currentTile.t4 != null && !visited.contains(currentTile.t4)){
                            currentTile = currentTile.t4;
                        }
                        else if(currentTile.t3 != null && !visited.contains(currentTile.t3)){
                            currentTile = currentTile.t3;
                        }
                        else if(currentTile.t2 != null && !visited.contains(currentTile.t2)){
                            currentTile = currentTile.t2;
                        }
                        else if(currentTile.t1 != null && !visited.contains(currentTile.t1)) {
                            currentTile = currentTile.t1;
                        }
                        else if(currentTile.t6 != null && !visited.contains(currentTile.t6)){
                            currentTile = currentTile.t6;
                        }
                    }
                    if(!currentTile.resource.equals("desert")){
                        currentTile.setNumber(order.remove(0));
                        currentTile.setNumImage(numImages[currentTile.getNumber()]);
                    }
                    count++;
                }
            }
    }
    public void setUpIntersections() {
        this.intersections = new Intersection[12][6];
        //fill up intersections that will be used
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0 || i == 11) {
                    if (j < 3) {
                        intersections[i][j] = new Intersection();
                    }
                } else if (i == 1 || i == 2 || i == 9 || i == 10) {
                    if (j < 4) {
                        intersections[i][j] = new Intersection();
                    }
                } else if (i == 3 || i == 4 || i == 7 || i == 8) {
                    if (j < 5) {
                        intersections[i][j] = new Intersection();
                    }
                } else if (i == 6 || i == 5) {
                    if (j < 6) {
                        intersections[i][j] = new Intersection();
                    }
                }
            }
        }
        for (int i = 0; i < intersections[i].length; i++) {
            //System.out.println(intersections[5][i] + " ");
        }
        //connect intersections
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i % 2 == 0 && intersections[i][j] != null) {
                    intersections[i][j].setI1(intersections[i + 1][j]);
                    intersections[i][j].setI2(intersections[i + 1][j + 1]);
                } else if (i % 2 == 1 && intersections[i][j] != null) {
                    intersections[i][j].setI3(intersections[i + 1][j]);
                }
            }
        }
        for (int i = 11; i > 6; i--) {
            for (int j = 0; j < 6; j++) {
                if (i % 2 == 0) {
                    if (intersections[i][j] != null) {
                        intersections[i][j].setI3(intersections[i - 1][j]);
                    }
                } else if (i % 2 == 1) {
                    if (intersections[i][j] != null) {
                        intersections[i][j].setI1(intersections[i - 1][j]);
                        intersections[i][j].setI2(intersections[i - 1][j + 1]);
                    }
                }
            }
        }
        //set intersections' tiles
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 0) {
                    if (j < 3) {
                        intersections[i][j].setT3(tiles[i][j + 1]);
                    }
                } else if (i == 1) {
                    if (j < 4) {
                        intersections[i][j].setT2(tiles[i - 1][j + 1]);
                        intersections[i][j].setT3(tiles[i - 1][j]);
                    }
                } else if (i == 2) {
                    if (j < 4) {
                        intersections[i][j].setT2(tiles[i - 2][j + 1]);
                        intersections[i][j].setT1(tiles[i - 2][j]);
                        intersections[i][j].setT3(tiles[i - 1][j]);
                    }
                } else if (i == 3) {
                    if (j < 5) {
                        intersections[i][j].setT1(tiles[i - 3][j]);
                        intersections[i][j].setT2(tiles[i - 2][j]);
                        if (j > 0) {
                            intersections[i][j].setT3(tiles[i - 2][j - 1]);
                        }
                    }
                } else if (i == 4) {
                    if (j < 5) {
                        intersections[i][j].setT2(tiles[i - 3][j]);
                        if (j > 0) {
                            intersections[i][j].setT1(tiles[i - 3][j - 1]);
                        }
                        intersections[i][j].setT3(tiles[i - 2][j]);
                    }
                } else if (i == 5) {
                    if (j < 5) {
                        intersections[i][j].setT2(tiles[2][j]);
                    }
                    if (j > 0) {
                        //System.out.println("i: " + i + " j: " + j);
                        intersections[i][j].setT1(tiles[1][j - 1]);
                        intersections[i][j].setT3(tiles[2][j - 1]);
                    }

                } else if (i == 6) {
                    if (j < 5) {
                        intersections[i][j].setT2(tiles[2][j]);
                    }
                    if (j > 0) {
                        intersections[i][j].setT1(tiles[2][j - 1]);
                        intersections[i][j].setT3(tiles[3][j - 1]);
                    }
                } else if (i == 7) {
                    if (j < 5) {
                        intersections[i][j].setT2(tiles[3][j]);
                        intersections[i][j].setT1(tiles[2][j]);
                        if (j > 0) {
                            intersections[i][j].setT3(tiles[3][j - 1]);
                        }
                    }
                } else if (i == 8) {
                    if (j < 5) {
                        if (j < 4) {
                            intersections[i][j].setT2(tiles[3][j + 1]);
                        }
                        if (j > 0) {
                            intersections[i][j].setT1(tiles[3][j - 1]);
                        }
                        intersections[i][j].setT3(tiles[4][j]);
                    }
                } else if (i == 9) {
                    if (j < 4) {
                        intersections[i][j].setT2(tiles[4][j + 1]);
                        intersections[i][j].setT1(tiles[3][j]);
                        if (j > 0) {
                            intersections[i][j].setT3(tiles[4][j - 1]);
                        }
                    }
                } else if (i == 10) {
                    if (j < 4) {
                        intersections[i][j].setT2(tiles[4][j + 1]);
                        intersections[i][j].setT1(tiles[4][j]);
                    }
                } else if (i == 11) {
                    if (j < 3) {
                        intersections[i][j].setT1(tiles[4][j]);
                    }
                }
            }
        }
        //set intersections' coordinates
        for (int i = 0; i < intersections[0].length; i++) {
            if (intersections[0][i] != null) {
                intersections[0][i].setX(332+157*i);
                intersections[0][i].setY(96);
            }
             if (intersections[1][i] != null) {
                intersections[1][i].setX(255+157*i);
                intersections[1][i].setY(133);
            }
             if (intersections[2][i] != null) {
                intersections[2][i].setX(255+157*i);
                intersections[2][i].setY(214);
            }
             if (intersections[3][i] != null) {
                intersections[3][i].setX(176+157*i);
                intersections[3][i].setY(255);
            }
             if (intersections[4][i] != null) {
                intersections[4][i].setX(176+157*i);
                intersections[4][i].setY(333);
            }
             if (intersections[5][i] != null) {
                intersections[5][i].setX(98+157*i);
                intersections[5][i].setY(378);
            }
             if (intersections[6][i] != null) {
                intersections[6][i].setX(98+157*i);
                intersections[6][i].setY(454);
            }
             if (intersections[7][i] != null) {
                intersections[7][i].setX(175+157*i);
                intersections[7][i].setY(490);
            }
             if (intersections[8][i] != null) {
                intersections[8][i].setX(175+157*i);
                intersections[8][i].setY(572);
            }
             if (intersections[9][i] != null) {
                intersections[9][i].setX(255+157*i);
                intersections[9][i].setY(612);
            }
             if (intersections[10][i] != null) {
                intersections[10][i].setX(255+157*i);
                intersections[10][i].setY(693);
            }
             if (intersections[11][i] != null) {
                intersections[11][i].setX(332+157*i);
                intersections[11][i].setY(730);
            }
        }
    }
    public void setPorts(){
        this.ports = new Port[9];
        //fill ports
        for(int i=0;i<9;i++){
            ports[i] = new Port();
        }
        //connect with intersections
        ports[0].setI1(intersections[1][0]);
        ports[0].setI2(intersections[0][0]);
        ports[1].setI1(intersections[0][1]);
        ports[1].setI2(intersections[1][2]);
        ports[2].setI1(intersections[2][3]);
        ports[2].setI2(intersections[3][4]);
        ports[3].setI1(intersections[5][5]);
        ports[3].setI2(intersections[6][5]);
        ports[4].setI1(intersections[8][4]);
        ports[4].setI2(intersections[9][3]);
        ports[5].setI1(intersections[10][2]);
        ports[5].setI2(intersections[11][1]);
        ports[6].setI1(intersections[11][0]);
        ports[6].setI2(intersections[10][1]);
        ports[7].setI1(intersections[8][0]);
        ports[7].setI2(intersections[7][0]);
        ports[8].setI1(intersections[4][0]);
        ports[8].setI2(intersections[3][0]);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void distributeResources(int k) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tiles[i][j] != null && tiles[i][j].getNumber() == k) {
                    tiles[i][j].giveResources();
                }
            }
        }
    } //interactions with robber will be covered in main method
}
