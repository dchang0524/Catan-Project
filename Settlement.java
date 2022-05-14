import java.util.HashMap;

public class Settlement {
    boolean isCity;
    Intersection position;
    Tile t1, t2, t3;
    Player owner;
    int xCoord;
    int yCoord;
    int tradeRatio;
    String tradeResource;

    public Settlement(Intersection pos, Player p) {
        position = pos;
        t1 = pos.t1;
        t2 = pos.t2;
        t3 = pos.t3;
        isCity = false;
        owner = p;
        xCoord = pos.getX();
        yCoord = pos.getY();
        p.getSettlements().add(this);
        if (t1 != null) {
            t1.settles.add(this);
        }
        if (t2 != null) {
            t2.settles.add(this);
        }
        if (t3 != null) {
            t3.settles.add(this);
        }
        setTradeResource(pos.portResource);
        setTradeRatio(pos.portTrade);
        if (pos.portTrade != 0) {
            updatePlayerShop();
        }
    }
    public boolean isCity() {
        return isCity;
    }
    public void upgrade() {
        isCity = true;
    }
    public Player getOwner() {
        return owner;
    }
    public void setX(int x) {
        xCoord = x;
    }
    public void setY(int y) {
        yCoord = y;
    }
    public int getX() {
        return xCoord;
    }
    public int getY() {
        return yCoord;
    }
    public String toString() {
        return "Settlement at " + position.toString();
    }
    public Intersection getPosition() {
        return position;
    }
    public void giveAllResource() {
        int amount = 1;
        if (t1 != null && !t1.resource.equals("desert")) {
            System.out.println("Giving " + amount + " of " + t1.getResource());
            owner.addResource(t1.getResource(),amount);
        }
        if (t2 != null && !t2.resource.equals("desert")) {
            System.out.println("Giving " + amount + " of " + t2.getResource());
            owner.addResource(t2.getResource(),amount);
        }
        if (t3 != null && !t3.resource.equals("desert")) {
            System.out.println("Giving " + amount + " of " + t3.getResource());
            owner.addResource(t3.getResource(),amount);
        }
    }
    public void giveResource(Tile t) {
        if (isCity == false) {
            System.out.println("Giving 1 of " + t.getResource() + " to " + owner.toString());
            owner.addResource(t.getResource(),1);
        }
        else {
            System.out.println("Giving 2 of " + t.getResource() + " to " + owner.toString());
            owner.addResource(t.getResource(),2);
        }
    }
    public void distributeAllResources() {
        if (t1 != null) {
            owner.addResource(t1.getResource(), 1);
        }
        else if (t2 != null) {
            owner.addResource(t2.getResource(), 1);
        }
        else if (t3 != null) {
            owner.addResource(t3.getResource(), 1);
        }
    }
    public void setTradeRatio(int ratio) {
        tradeRatio = ratio;
    }
    public void setTradeResource(String resource) {
        this.tradeResource = resource;
    }
    public void updatePlayerShop() {
        HashMap<String, Integer> sr = owner.shopRatio;
        if (tradeResource != null) {
            sr.put(tradeResource, tradeRatio);
        }
        else {
            for (String resource : sr.keySet()) {
                if (sr.get(resource) > 3) {
                    sr.put(resource, 3);
                }
            }
        }
    }
}
