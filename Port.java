import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Port {
    Intersection i1;
    Intersection i2;
    Settlement s1;
    Settlement s2;
    Player p1;
    Player p2;
    int tradeRatio;
    ArrayList<String> ports = new ArrayList<String>();
    String portResource;

    public Port() {
        i1 = null;
        i2 = null;
        s1 = null;
        s2 = null;
        tradeRatio = 0;
        portResource = "";
        ports.add("Brick");
        ports.add("Sheep");
        ports.add("Wheat");
        ports.add("Wood");
        ports.add("Ore");

    }

    public void setI1(Intersection i1) {
        this.i1 = i1;
    }

    public void setI2(Intersection i2) {
        this.i2 = i2;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public void setPortResource(String portResource) {
        this.portResource = portResource;
    }

    public void setS1(Settlement s1) {
        this.s1 = s1;
    }

    public void setS2(Settlement s2) {
        this.s2 = s2;
    }

    public void setTradeRatio(int tradeRatio) {
        this.tradeRatio = tradeRatio;
    }



}
