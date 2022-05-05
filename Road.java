import java.util.HashSet;

public class Road {
    private Intersection i1;
    private Intersection i2;
    private Player owner;
    private Road r1;
    private Road r2;
    private Road r3;
    private Road r4;


    //constructor
    public Road(Intersection i1, Intersection i2, Player owner) {
        this.i1 = i1;
        this.i2 = i2;
        this.owner = owner;
        owner.getRoads().add(this);
        if (i1.getI1() == i2) {
            i1.setR1(this);
            i2.setR1(this);
        }
        else if (i1.getI2() == i2) {
            i1.setR2(this);
            i2.setR2(this);
        }
        else if (i1.getI3() == i2) {
            i1.setR3(this);
            i2.setR3(this);
        }
    }

    public void setOwner(Road road, Player player) {
        if (owner == null) {
            owner = player;
        }
        //road.addRoad(player);
    }

    /*public void setOwner(Player player) {
        if (owner == null) {
            owner = player;
        }
    }*/
    public Player getOwner() {
        return owner;
    }

    public Intersection getI1() {
        return i1;
    }

    public Intersection getI2() {
        return i2;
    }

    //WIP
    public void setR1(Road r1) {
        this.r1 = r1;
    }

    public void setR2(Road r2) {
        this.r2 = r2;
    }

    public void setR3(Road r3) {
        this.r3 = r3;
    }

    public void setR4(Road r4) {
        this.r4 = r4;
    }

    public Road getR1() {
        return r1;
    }

    public Road getR2() {
        return r2;
    }

    public Road getR3() {
        return r3;
    }

    public Road getR4() {
        return r4;
    }

    public void connect(Road r) {
        Intersection commonPoint = this.commonPoint(r);
        if (commonPoint != null) {
            if (commonPoint.hasSettlement() && commonPoint.getSettlement().getOwner() == owner) {
                if (r.getR1() != r && r.getR2() != r && r.getR3() != r && r.getR4() != r) {
                    if (r1 == null) {
                        r1 = r;
                        r.connectHelper(this);
                    } else if (r2 == null) {
                        r2 = r;
                        r.connectHelper(this);
                    } else if (r3 == null) {
                        r3 = r;
                        r.connectHelper(this);
                    } else if (r4 == null) {
                        r4 = r;
                        r.connectHelper(this);
                    }
                }
            }
        }
    } //connects two roads if they should be connected
    public Intersection commonPoint(Road r) {
        if (this.i1 == r.i1 || this.i1 == r.i2) {
            return this.i1;
        }
        else if (this.i2 == r.i1 || this.i2 == r.i2) {
            return this.i2;
        }
        return null;
    }
    public void connectHelper(Road r) {
        if (r.getR1() != r && r.getR2() != r && r.getR3() != r && r.getR4() != r) {
            if (r1 == null) {
                r1 = r;
            } else if (r2 == null) {
                r2 = r;
            } else if (r3 == null) {
                r3 = r;
            } else if (r4 == null) {
                r4 = r;


            }
        }
    }

    public void disconnect(Road r) {
        Intersection commonPoint = this.commonPoint(r);
        if (commonPoint != null) {
            if (commonPoint.hasSettlement() && commonPoint.getSettlement().getOwner() != owner) {
                if (r1 == r) {
                    r1 = null;
                    r.disconnectHelper(this);
                } else if (r2 == r) {
                    r2 = null;
                    r.disconnectHelper(this);
                } else if (r3 == r) {
                    r3 = null;
                    r.disconnectHelper(this);
                } else if (r4 == r) {
                    r4 = null;
                    r.disconnectHelper(this);
                }
            }
        }
    }

    public void disconnectHelper(Road r) {
        if (r1 == r) {
            r1 = null;
        } else if (r2 == r) {
            r2 = null;
        } else if (r3 == r) {
            r3 = null;
        } else if (r4 == r) {
            r4 = null;
        }
    }

    public int getHeight() {
        HashSet<Road> visited = new HashSet<>();
        visited.add(this);
        return 1+Math.max(Math.max(r1.getHeightHelper(visited), r2.getHeightHelper(visited)), Math.max(r4.getHeightHelper(visited), r4.getHeightHelper(visited)));
    }
    public int getHeightHelper(HashSet<Road> visited) {
        if (this == null) {
            return 0;
        }
        if (visited.contains(this)) {
            return 0;
        }
        visited.add(this);
        return 1+Math.max(Math.max(r1.getHeightHelper(visited), r2.getHeightHelper(visited)), Math.max(r4.getHeightHelper(visited), r4.getHeightHelper(visited)));
    }

}



