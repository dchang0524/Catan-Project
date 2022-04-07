import java.util.ArrayList;

public class Board {
    Tile[][] tiles;
    Intersection[][] intersections;

    public Board() {
        setUpTiles();
        setUpIntersections();
    }

    public void setUpTiles() {
        tiles = new Tile[5][5]; // [0][0] [0][4] [1][4] [3][4] [4][0] [4][4]

        //sets up resources for each tile in the board
        ArrayList<Tile> tileList = new ArrayList<Tile>();
        for (int i = 0; i<4; i++){
            tileList.add(new Tile("wood"));
            tileList.add(new Tile("sheep"));
            tileList.add(new Tile("wheat"));
        }
        for (int i = 0; i<3; i++){
            tileList.add(new Tile("brick"));
            tileList.add(new Tile("ore"));
        }
        tileList.add(new Tile("desert"));
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
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!((i == 0 && j == 0)|| (i == 0 && j == 4) || (i == 4 && j == 0) || (i == 4 && j == 4) || (i == 1 && j == 4) || (i == 3 && j == 4)) || tiles[i][j].getType().equals("desert")) {
                    tiles[i][j].setNumber(order.remove(0));
                }
            }
        }

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
                        if (i != 0) {
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
    } //TODO: check if order of numbers can be just left to right

    public void setUpIntersections() {
        Intersection[][] intersections = new Intersection[11][12];
    }
}
