package forbiddenIsland;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

import java.awt.*;

//Represents a cell that is the Ocean
class OceanCell extends Cell {
    OceanCell(double h, int x, int y, Cell l, Cell t, Cell r, Cell b) {
        super(h, x, y, l, t, r, b, true);
    }

    //draws the ocean cell
    public WorldImage drawCell(int w) {
        Color sea = new Color(3, 12, 126);
        return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, sea);
    }

}
