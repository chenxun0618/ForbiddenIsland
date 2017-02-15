package forbiddenIsland;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.FontStyle;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

//to represent a ForbiddenIslandWorld
class ForbiddenIslandWorld extends World {
    // Defines an int constant
    static final int ISLAND_SIZE = 64;
    static final double MAX_HEIGHT = 32.0;
    static final int TARGETS_NUMBER = 10;
    static final double WORLD_RATE = 0.5;
    static final int PLAYERS = 1;
    static final int SCUBAS_NUMBER = 5;
    // All the cells of the game, including the ocean
    IList<Cell> board;
    // the current height of the ocean
    int waterHeight;
    // the current ticks of the world
    int tick;
    // the steps the players have taken
    int steps;
    // the heights of each cell on the board
    ArrayList<ArrayList<Double>> heights;
    // the cells of the board
    ArrayList<ArrayList<Cell>> cells;
    // all the land cells
    ArrayList<ArrayList<Cell>> allLandCells;
    // the list of targets in this game
    IList<Target> targets;
    // the list of players in this game
    IList<Pilot> pilots;
    // the list of players in this game
    IList<forbiddenIsland.Scuba> scubas;
    // helicopter
    HelicopterTarget h;
    Random rand;

    ForbiddenIslandWorld() {
        this.board = new MtList<Cell>();
        this.waterHeight = 0;
        this.tick = 0;
        this.steps = 0;
        this.heights = new ArrayList<ArrayList<Double>>();
        this.cells = new ArrayList<ArrayList<Cell>>();
        this.allLandCells = new ArrayList<ArrayList<Cell>>();
        this.targets = new MtList<Target>();
        this.pilots = new MtList<Pilot>();
        this.scubas = new MtList<Scuba>();
        this.h = new HelicopterTarget(0, 0);
        this.rand = new Random();
    }

////////////////////////////////////////Heights/////////////////////////////////////////////

    //produces mountain heights or random heights or terrain heights
    ArrayList<ArrayList<Double>> makeHeights(String s) {
        ArrayList<ArrayList<Double>> a = this.generateHeights(ISLAND_SIZE);
        if (s.equals("m")) {
            this.setMax(a, MAX_HEIGHT);
            this.setHeight(a);
        }
        else if (s.equals("r")) {
            this.setHeight(a);
            this.randomHeight(a);
        }
        else if (s.equals("t")) {
            this.setCorners(a);
            this.setMax(a, MAX_HEIGHT);
            this.setEdge(a);
            this.randomTerrain(a, new Posn(0, 0), new Posn(ISLAND_SIZE, ISLAND_SIZE));
        }
        this.heights = a;
        return a;
    }

    //generates a list of lists of zeros
    ArrayList<ArrayList<Double>> generateHeights(int size) {
        ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> col = new ArrayList<Double>();

        for (int i = 0; i < size + 1; i = i + 1) {
            for (int j = 0; j < size + 1; j = j + 1) {
                col.add(0.0);
            }
            result.add(col);
            col = new ArrayList<Double>();
        }
        return result;
    }

    //sets the corners of the canvas to 0
    void setCorners(ArrayList<ArrayList<Double>> a) {
        a.get(0).set(0, 0.0);
        a.get(a.size() - 1).set(0, 0.0);
        a.get(0).set(a.size() - 1, 0.0);
        a.get(a.size() - 1).set(a.size() - 1, 0.0);
    }

    //sets the maximum height
    void setMax(ArrayList<ArrayList<Double>> a, double maxHeight) {
        a.get((a.size() - 1) / 2).set((a.size() - 1) / 2, maxHeight);
    }

    //sets the middle of the edges to 1
    void setEdge(ArrayList<ArrayList<Double>> a) {
        a.get(0).set((a.size() - 1) / 2, 1.0);
        a.get((a.size() - 1) / 2).set(0, 1.0);
        a.get(a.size() - 1).set((a.size() - 1) / 2, 1.0);
        a.get((a.size() - 1) / 2).set(a.size() - 1, 1.0);
    }

    //produces mountain heights
    void setHeight(ArrayList<ArrayList<Double>> a) {
        int center = (a.size() - 1) / 2;
        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                a.get(i).set(j, MAX_HEIGHT - Math.abs(center - i) - Math.abs(center - j));
            }
        }
    }

    //produces random heights
    void randomHeight(ArrayList<ArrayList<Double>> a) {
        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                if (a.get(i).get(j) > 0) {
                    a.get(i).set(j, Math.random() * (MAX_HEIGHT - 1) + 1);
                }
            }
        }
    }

    //random range for randomTerrain
    double random(double h1, double h2) {
        return Math.random() * (h1 + h2) / 3 - Math.random() * h2;
    }

    //creates random terrain heights
    void randomTerrain(ArrayList<ArrayList<Double>> a, Posn topLeft, Posn rightBottom) {
        if ((topLeft.x + 2 < rightBottom.x) &&
                (topLeft.y + 2 < rightBottom.y)) {
            this.randomTerrainHelp(a.get(topLeft.x).get(topLeft.y),
                    a.get((topLeft.x + rightBottom.x) / 2).get(topLeft.y),
                    a.get(topLeft.x).get((topLeft.y + rightBottom.y) / 2),
                    a.get((topLeft.x + rightBottom.x) / 2).get((topLeft.y + rightBottom.y) / 2), a,
                    topLeft, new Posn((topLeft.x + rightBottom.x) / 2,
                            (topLeft.y + rightBottom.y) / 2));
            this.randomTerrainHelp(a.get((topLeft.x + rightBottom.x) / 2).get(topLeft.y),
                    a.get(rightBottom.x).get(topLeft.y),
                    a.get((topLeft.x + rightBottom.x) / 2).get((topLeft.y + rightBottom.y) / 2),
                    a.get(rightBottom.x).get((topLeft.y + rightBottom.y) / 2), a,
                    new Posn((topLeft.x + rightBottom.x) / 2, topLeft.y),
                    new Posn(rightBottom.x, (topLeft.y + rightBottom.y) / 2));
            this.randomTerrainHelp(a.get(topLeft.x).get((topLeft.y + rightBottom.y) / 2),
                    a.get((topLeft.x + rightBottom.x) / 2).get((topLeft.y + rightBottom.y) / 2),
                    a.get(topLeft.x).get(rightBottom.y),
                    a.get((topLeft.x + rightBottom.x) / 2).get(rightBottom.y), a,
                    new Posn(topLeft.x, (topLeft.y + rightBottom.y) / 2),
                    new Posn((topLeft.x + rightBottom.x) / 2, rightBottom.y));
            this.randomTerrainHelp(
                    a.get((topLeft.x + rightBottom.x) / 2).get((topLeft.y + rightBottom.y) / 2),
                    a.get(rightBottom.x).get((topLeft.y + rightBottom.y) / 2),
                    a.get((topLeft.x + rightBottom.x) / 2).get(rightBottom.y),
                    a.get(rightBottom.x).get(rightBottom.y), a,
                    new Posn((topLeft.x + rightBottom.x) / 2, (topLeft.y + rightBottom.y) / 2),
                    rightBottom);
        }


    }

    //helper method for randomTerrain
    void randomTerrainHelp(double tl, double tr, double bl, double br,
                           ArrayList<ArrayList<Double>> a, Posn topLeft, Posn rightBottom) {

        double t = random(tl, tr) + (tl + tr) / 2;
        double b = random(bl, br) + (bl + br) / 2;
        double l = random(tl, bl) + (tl + bl) / 2;
        double r = random(tr, br) + (tr + br) / 2;
        double m = (random(t, b) + random(l, r)) / 2 + (tl + tr + bl + br) / 4;

        if (a.get(topLeft.x).get((topLeft.y + rightBottom.y) / 2) == 0) {
            a.get(topLeft.x).set((topLeft.y + rightBottom.y) / 2, l);
        }
        if (a.get((topLeft.x + rightBottom.x) / 2).get(topLeft.y) == 0) {
            a.get((topLeft.x + rightBottom.x) / 2).set(topLeft.y, t);
        }
        if (a.get(rightBottom.x).get((topLeft.y + rightBottom.y) / 2) == 0) {
            a.get(rightBottom.x).set((topLeft.y + rightBottom.y) / 2, r);
        }
        if (a.get((topLeft.x + rightBottom.x) / 2).get(rightBottom.y) == 0) {
            a.get((topLeft.x + rightBottom.x) / 2).set(rightBottom.y, b);
        }
        if (a.get((topLeft.x + rightBottom.x) / 2).get((topLeft.y + rightBottom.y) / 2) == 0) {
            a.get((topLeft.x + rightBottom.x) / 2).set((topLeft.y + rightBottom.y) / 2, m);
        }
        randomTerrain(a, topLeft, rightBottom);
    }



////////////////////////////////////////////Cells////////////////////////////////////////////////

    //makes cells with the according heights
    ArrayList<ArrayList<Cell>> makeCells(String s) {
        ArrayList<ArrayList<Cell>> a = this.generateCells(s);
        this.connectCells(a);
        this.cells = a;
        this.makeAllLandCells(a);
        return a;
    }

    //generates Cells/OceanCells with only heights assigned
    ArrayList<ArrayList<Cell>> generateCells(String s) {
        ArrayList<ArrayList<Double>> a = this.makeHeights(s);
        ArrayList<ArrayList<Cell>> result = new ArrayList<ArrayList<Cell>>();
        ArrayList<Cell> col = new ArrayList<Cell>();

        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                if (a.get(i).get(j) > 0) {
                    col.add(new Cell(a.get(i).get(j), i, j, null, null, null, null, false));
                }
                else {
                    col.add(new OceanCell(a.get(i).get(j), i, j, null, null, null, null));
                }
            }
            result.add(col);
            col = new ArrayList<Cell>();
        }
        return result;
    }

    //connects the cells with the adjacent cells
    void connectCells(ArrayList<ArrayList<Cell>> a) {
        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                Cell c = a.get(i).get(j);

                if (i == 0 && j == 0) {
                    c.left = c;
                    c.top = c;
                    c.right = a.get(i + 1).get(j);
                    c.bottom = a.get(i).get(j + 1);
                }
                else if (i == a.size() - 1 && j == 0) {
                    c.right = c;
                    c.top = c;
                    c.left = a.get(i - 1).get(j);
                    c.bottom = a.get(i).get(j + 1);
                }
                else if (i == 0 && j == a.size() - 1) {
                    c.left = c;
                    c.bottom = c;
                    c.top = a.get(i).get(j - 1);
                    c.right = a.get(i + 1).get(j);
                }
                else if (i == a.size() - 1 && j == a.size() - 1) {
                    c.right = c;
                    c.bottom = c;
                    c.left = a.get(i - 1).get(j);
                    c.top = a.get(i).get(j - 1);
                }
                else if (i == 0) {
                    c.left = c;
                    c.top = a.get(i).get(j - 1);
                    c.right = a.get(i + 1).get(j);
                    c.bottom = a.get(i).get(j + 1);
                }
                else if (i == a.size() - 1) {
                    c.right = c;
                    c.top = a.get(i).get(j - 1);
                    c.left = a.get(i - 1).get(j);
                    c.bottom = a.get(i).get(j + 1);
                }
                else if (j == 0) {
                    c.top = c;
                    c.left = a.get(i - 1).get(j);
                    c.right = a.get(i + 1).get(j);
                    c.bottom = a.get(i).get(j + 1);
                }
                else if (j == a.size() - 1) {
                    c.bottom = c;
                    c.left = a.get(i - 1).get(j);
                    c.top = a.get(i).get(j - 1);
                    c.right = a.get(i + 1).get(j);
                }
                else {
                    c.left = a.get(i - 1).get(j);
                    c.top = a.get(i).get(j - 1);
                    c.right = a.get(i + 1).get(j);
                    c.bottom = a.get(i).get(j + 1);
                }
            }
        }
    }

    //records all cells(not OceanCells)
    void makeAllLandCells(ArrayList<ArrayList<Cell>> a) {
        ArrayList<ArrayList<Cell>> b = new ArrayList<ArrayList<Cell>>();
        ArrayList<Cell> col = new ArrayList<Cell>();
        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                if (a.get(i).get(j).height > 0) {
                    col.add(a.get(i).get(j));
                }
            }
            if (col.size() > 0) {
                b.add(col);
            }

            col = new ArrayList<Cell>();
        }
        this.allLandCells = b;
    }

    //picks a random cell from the land cells
    Cell randomCell() {
        ArrayList<ArrayList<Cell>> a = this.allLandCells;

        for (int i = 0; i < a.size(); i = i + 1) {
            if (a.get(i).size() == 0) {
                a.remove(i);
            }
        }
        int i = rand.nextInt(a.size());
        int j = rand.nextInt(a.get(i).size());
        return a.get(i).remove(j);

    }


    //produces the highest cell
    Cell highestCell() {
        ArrayList<ArrayList<Cell>> a = this.cells;
        double acc = 0.0;
        Posn p = new Posn(0, 0);
        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                if (a.get(i).get(j).height > acc) {
                    acc = a.get(i).get(j).height;
                    p.x = i;
                    p.y = j;
                }
            }
        }
        return a.get(p.x).get(p.y);
    }

///////////////////////////////pilot/target/helicopter/////////////////////////////////////////

    //makes a pilot with a random coordinate
    Pilot makePilot() {
        Pilot p = new Pilot(0, 0);
        Cell c = this.randomCell();
        p.x = c.x;
        p.y = c.y;
        return p;
    }

    //makes a list of random pilots 
    IList<Pilot> makePilots(int n) {
        IList<Pilot> result = new MtList<Pilot>();
        while (n > 0) {
            result = new ConsList<Pilot>(this.makePilot(), result);
            n = n - 1;
        }
        this.pilots = result;
        return result;
    }


    //makes a target with a random coordinate
    Target makeTarget() {
        Target t = new Target(0, 0);
        Cell c = this.randomCell();
        t.x = c.x;
        t.y = c.y;
        return t;
    }

    //makes a list of random targets 
    IList<Target> makeTargets(int n) {
        IList<Target> result = new MtList<Target>();
        while (n > 0) {
            result = new ConsList<Target>(this.makeTarget(), result);
            n = n - 1;
        }
        this.targets = result;
        return result;
    }

    //determines if all the targets are picked up
    boolean targetPickedUp() {
        for (Target t : this.targets) {
            for (Pilot p : this.pilots) {
                if (t.x == p.x && t.y == p.y) {
                    this.targets = this.targets.removeThis(t);
                }
            }
        }
        return !this.targets.isCons();
    }

    //makes a helicopter
    void makeHelicopter(WorldScene s) {
        Cell c = this.highestCell();
        this.h.x = c.x;
        this.h.y = c.y;
        new Utils().placeImage(this.h.drawImage(), this.h.x, this.h.y, s);
    }

    //makes the board
    void makeBoard(String s) {
        ArrayList<ArrayList<Cell>> a = this.makeCells(s);
        this.makePilots(PLAYERS);
        this.makeTargets(TARGETS_NUMBER);
        this.makeScubas(SCUBAS_NUMBER);
        IList<Cell> result = new MtList<Cell>();
        for (int i = 0; i < a.size(); i = i + 1) {
            for (int j = 0; j < a.size(); j = j + 1) {
                result = new ConsList<Cell>(a.get(i).get(j), result);
            }
        }
        this.board = result;
    }

    //////////////////////////////////////////Whistles//////////////////////////////////////////

    //calculates the remaining time until the island floods completely
    int calculateTime() {
        return (int) Math.ceil((this.highestCell().height + 1) * 10 * WORLD_RATE -
                this.tick * WORLD_RATE);
    }

    //draws the time image
    void time(WorldScene s) {
        new Utils().placeImage(
                new TextImage("Time Remaining: " + Integer.toString(this.calculateTime())
                        + " s", 18, new Color(159, 100, 159)), 11, 2, s);
    }

    //draws the steps image
    void steps(WorldScene s) {
        new Utils().placeImage(
                new TextImage("Steps Taken: " + Integer.toString(this.steps),
                        18, new Color(159, 100, 159)), 9, 4, s);
    }

    //////////////////////////////////////////Bell//////////////////////////////////////////

    //makes a scuba with a random coordinate
    Scuba makeScuba() {
        Scuba s = new Scuba(0, 0);
        Cell c = this.randomCell();
        s.x = c.x;
        s.y = c.y;
        return s;
    }

    //makes a list of random scubas 
    IList<Scuba> makeScubas(int n) {
        IList<Scuba> result = new MtList<Scuba>();
        while (n > 0) {
            result = new ConsList<Scuba>(this.makeScuba(), result);
            n = n - 1;
        }
        this.scubas = result;
        return result;
    }

    //removes the scuba from the list
    void removeScuba() {
        for (Scuba s : this.scubas) {
            for (Pilot p : this.pilots) {
                if (s.x == p.x && s.y == p.y) {
                    this.scubas.removeThis(s);
                }
            }
        }
    }

    //updates the scubas' images
    void updateScubas() {
        for (Scuba s : this.scubas) {
            for (Pilot p : this.pilots) {
                if (s.x == p.x && s.y == p.y) {
                    s.updateImage();
                }
            }
        }
    }


    //////////////////////////////////////////World//////////////////////////////////////////

    public void onTick() {
        this.tick = this.tick + 1;
        if (this.tick % 10 == 0) {
            this.waterHeight = this.waterHeight + 1;
        }

    }

    public void onKeyEvent(String k) {
        Pilot p1 = this.pilots.asCons().first;
        int players = PLAYERS; //to fix dead code alert when PLAYERS is 1
        if (players > 1) {
            Pilot p2 = this.pilots.asCons().rest.asCons().first;
            if ((k.equals("left") && p1.x == 0) ||
                    (k.equals("right") && p1.x == ISLAND_SIZE) ||
                    (k.equals("up") && p1.y == 0) ||
                    (k.equals("down") && p1.y == ISLAND_SIZE) ||
                    (k.equals("a") && p2.x == 0) ||
                    (k.equals("d") && p2.x == ISLAND_SIZE) ||
                    (k.equals("w") && p2.y == 0) ||
                    (k.equals("s") && p2.y == ISLAND_SIZE)) {
                //do nothing
            }
            else if ((k.equals("left") && this.cells.get(p1.x - 1).get(p1.y).isFlooded) ||
                    (k.equals("right") && this.cells.get(p1.x + 1).get(p1.y).isFlooded) ||
                    (k.equals("up") && this.cells.get(p1.x).get(p1.y - 1).isFlooded) ||
                    (k.equals("down") && this.cells.get(p1.x).get(p1.y + 1).isFlooded) ||
                    (k.equals("a") && this.cells.get(p2.x - 1).get(p2.y).isFlooded) ||
                    (k.equals("d") && this.cells.get(p2.x + 1).get(p2.y).isFlooded) ||
                    (k.equals("w") && this.cells.get(p2.x).get(p2.y - 1).isFlooded) ||
                    (k.equals("s") && this.cells.get(p2.x).get(p2.y + 1).isFlooded)) {
                //do nothing
            }
            else if (k.equals("left")) {
                p1.x = p1.x - 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("a")) {
                p2.x = p2.x - 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("right")) {
                p1.x = p1.x + 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("d")) {
                p2.x = p2.x + 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("up")) {
                p1.y = p1.y - 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("w")) {
                p2.y = p2.y - 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("down")) {
                p1.y = p1.y + 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("s")) {
                p2.y = p2.y + 1;
                this.steps = this.steps + 1;
            }
        }
        else {
            if ((k.equals("left") && p1.x == 0) ||
                    (k.equals("right") && p1.x == ISLAND_SIZE) ||
                    (k.equals("up") && p1.y == 0) ||
                    (k.equals("down") && p1.y == ISLAND_SIZE)) {
                //do nothing
            }
            else if ((k.equals("left") && this.cells.get(p1.x - 1).get(p1.y).isFlooded) ||
                    (k.equals("right") && this.cells.get(p1.x + 1).get(p1.y).isFlooded) ||
                    (k.equals("up") && this.cells.get(p1.x).get(p1.y - 1).isFlooded) ||
                    (k.equals("down") && this.cells.get(p1.x).get(p1.y + 1).isFlooded)) {
                //do nothing
            }
            else if (k.equals("left")) {
                p1.x = p1.x - 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("right")) {
                p1.x = p1.x + 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("up")) {
                p1.y = p1.y - 1;
                this.steps = this.steps + 1;
            }
            else if (k.equals("down")) {
                p1.y = p1.y + 1;
                this.steps = this.steps + 1;
            }
        }
        if (k.equals("m")) {
            this.board = new MtList<Cell>();
            this.makeBoard("m");
            this.waterHeight = 0;
            this.tick = 0;
            this.steps = 0;
        }
        else if (k.equals("r")) {
            this.board = new MtList<Cell>();
            this.makeBoard("r");
            this.waterHeight = 0;
            this.tick = 0;
            this.steps = 0;
        }
        else if (k.equals("t")) {
            this.makeBoard("t");
            this.waterHeight = 0;
            this.tick = 0;
            this.steps = 0;
        }
        else if (k.equals("s")) {
            this.removeScuba();
        }
        else {
            //do nothing
        }
    }


    public WorldScene makeScene() {
        WorldScene s = new WorldScene((ISLAND_SIZE + 1) * Cell.CELL_SIZE,
                (ISLAND_SIZE + 1) * Cell.CELL_SIZE);
        for (Cell c : this.board) {
            c.render(s, this.waterHeight);
        }
        for (Target t : this.targets) {
            t.render(s);
        }
        if (this.targetPickedUp()) {
            this.makeHelicopter(s);
        }
        for (Scuba scuba : this.scubas) {
            this.updateScubas();
            scuba.render(s);
        }
        for (Pilot p : this.pilots) {
            p.render(s);
        }
        this.time(s);
        this.steps(s);

        return s;
    }

    public WorldScene lastScene(String s) {
        WorldScene ws = this.makeScene();
        new Utils().placeImage(new TextImage(s, 24, FontStyle.BOLD, Color.red),
                ISLAND_SIZE / 2, ISLAND_SIZE / 2, ws);
        return ws;
    }

    public WorldEnd worldEnds() {
        int players = 0;
        for (Pilot p : this.pilots) {
            if (this.cells.get(p.x).get(p.y).isFlooded) {
                return new WorldEnd(true, this.lastScene("GAME OVER"));
            }
            if (this.h.x == p.x && this.h.y == p.y) {
                players = players + 1;
            }
            if (players == PLAYERS) {
                return new WorldEnd(true, this.lastScene("YOU WON!"));
            }
        }
        return new WorldEnd(false, this.makeScene());
    }
}