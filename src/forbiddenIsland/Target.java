package forbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.CircleImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.WorldImage;

import java.awt.*;

//to represent a target
class Target {
    int x;
    int y;

    Target(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //produces the target's image
    WorldImage drawImage() {
        return new CircleImage(Cell.CELL_SIZE / 2, OutlineMode.SOLID, Color.orange);
    }

    //draws the target on to the world scene
    public void render(WorldScene s) {
        new Utils().placeImage(this.drawImage(), this.x, this.y, s);
    }
}
