package forbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

import java.awt.*;

//Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the scren
    int x;
    int y;
    // the four adjacent cells to this one
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    Cell(double h, int x, int y, Cell l, Cell t, Cell r, Cell b, boolean isFlooded) {
        this.height = h;
        this.x = x;
        this.y = y;
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
        this.isFlooded = isFlooded;
    }

    // Defines an int constant
    static final int CELL_SIZE = 10;

    //determines if this cell is flooded
    public boolean isFlooded(int w) {
        return this.height < w &&
                ((this.left.height < w && this.left.isFlooded) ||
                (this.top.height < w && this.top.isFlooded) ||
                (this.right.height < w && this.right.isFlooded) ||
                (this.bottom.height < w && this.bottom.isFlooded));
    }

    //sets the RGB value to default value if the value over the range
    int checkRGB(int i) {
        int j = i;
        if (i > 255) {
            j = 255;
        }
        if (i < 0) {
            j = 0;
        }
        return j;
    }

    //makes the color for the cell according to the water height
    public Color makeColor(int w) {
        int layer = (int) (ForbiddenIslandWorld.MAX_HEIGHT - this.height + w + 1) / 2;
        int gap = layer * 7;
        int layer2 = (int) (w - this.height + 1) / 2;
        int r = 3 - layer2;
        int g = 12 - layer2;
        int b = 126 - layer2 * 8;
        int r2 = 31 + layer2 * 12;
        int g2 = 143 - layer2 * 12;
        int b2 = 31 - layer;
        int r3 = 255 - gap * 2;
        int g3 = 255 - gap;

        r = this.checkRGB(r);
        g = this.checkRGB(g);
        b = this.checkRGB(b);
        r2 = this.checkRGB(r2);
        g2 = this.checkRGB(g2);
        b2 = this.checkRGB(b2);
        r3 = this.checkRGB(r3);
        g3 = this.checkRGB(g3);

        if (this.isFlooded(w)) {
            this.isFlooded = true;
            return new Color(r, g, b);
        }
        else if (this.height < w) {
            return new Color(r2, g2, b2);
        }
        else {
            return new Color(r3, g3, r3);
        }

    }

    //draws the cell
    public WorldImage drawCell(int w) {
        return new RectangleImage(CELL_SIZE, CELL_SIZE,
                OutlineMode.SOLID, this.makeColor(w));
    }

    //draws the cell on to the world scene
    public void render(WorldScene s, int w) {
        new Utils().placeImage(this.drawCell(w), this.x, this.y, s);
    }

}
