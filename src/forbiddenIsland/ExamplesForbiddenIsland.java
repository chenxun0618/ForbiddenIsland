package forbiddenIsland;

import tester.Tester;
import java.util.ArrayList;

//examples of ForbiddenIsland
class ExamplesForbiddenIsland {
    ForbiddenIslandWorld w = new ForbiddenIslandWorld();

    Cell c = new Cell(20, 20, 20, null, null, null, null, false);
    Target t1 = new Target(10, 10);
    Target t2 = new Target(30, 30);
    IList<Target> empty = new MtList<Target>();
    IList<Target> ts = new ConsList<Target>(t1, new ConsList<Target>(t2, empty));
    ArrayList<ArrayList<Double>> a;

    void initialWorld() {
        w.makeBoard("m");
        a = w.generateHeights(64);
    }

    void testForbiddenIsland(Tester t) {
        initialWorld();
        t.checkExpect(this.ts.removeThis(t1), new ConsList<Target>(t2, empty));
        t.checkExpect(this.empty.removeThis(t1), this.empty);
        t.checkExpect(this.ts.isCons(), true);
        t.checkExpect(this.empty.isCons(), false);
        t.checkExpect(this.ts.asCons(), this.ts);
        t.checkException(new IllegalArgumentException("Cannot convert empty to cons"),
                this.empty, "asCons");
        t.checkExpect(this.w.cells.get(32).get(32).isFlooded, false);
        t.checkExpect(this.w.cells.get(5).get(5).isFlooded, true);
        t.checkExpect(c.checkRGB(300), 255);
        t.checkExpect(c.checkRGB(-100), 0);
        t.checkExpect(c.checkRGB(140), 140);
        t.checkExpect(w.makeHeights("m").size(), ForbiddenIslandWorld.ISLAND_SIZE + 1);
        t.checkExpect(w.makeHeights("r").size(), ForbiddenIslandWorld.ISLAND_SIZE + 1);
        t.checkExpect(w.makeHeights("t").size(), ForbiddenIslandWorld.ISLAND_SIZE + 1);
        t.checkExpect(w.highestCell().height, w.cells.get(ForbiddenIslandWorld.ISLAND_SIZE / 2)
                .get(ForbiddenIslandWorld.ISLAND_SIZE / 2).height);
        t.checkExpect(a.get(0).get(0), 0.0);
        t.checkExpect(a.get(32).get(0), 0.0);
        t.checkExpect(a.get(0).get(32), 0.0);
        t.checkExpect(a.get(64).get(32), 0.0);
        t.checkExpect(a.get(32).get(64), 0.0);
        t.checkExpect(a.get(32).get(32), 0.0);
        w.setEdge(a);
        t.checkExpect(a.get(32).get(0), 1.0);
        t.checkExpect(a.get(0).get(32), 1.0);
        t.checkExpect(a.get(64).get(32), 1.0);
        t.checkExpect(a.get(32).get(64), 1.0);
        w.setMax(a, 32);
        t.checkExpect(a.get(32).get(32), 32.0);
    }

    void testBigBang(Tester t) {
        initialWorld();

        this.w.bigBang((ForbiddenIslandWorld.ISLAND_SIZE + 1) * Cell.CELL_SIZE,
                (ForbiddenIslandWorld.ISLAND_SIZE + 1) * Cell.CELL_SIZE,
                ForbiddenIslandWorld.WORLD_RATE);
    }

    public static void main(String[] args) {
        ExamplesForbiddenIsland forbiddenIsland = new ExamplesForbiddenIsland();
        Tester t = new Tester();
        forbiddenIsland.testForbiddenIsland(t);
        forbiddenIsland.testBigBang(t);
    }
}
