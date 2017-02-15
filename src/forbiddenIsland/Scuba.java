package forbiddenIsland;

import java.awt.Color;
import javalib.worldimages.*;


class Scuba extends Target {
    WorldImage img;

    Scuba(int x, int y) {
        super(x, y);
        this.img = new CircleImage(0, OutlineMode.SOLID, Color.red);
    }

    //produces the swimmingSuit's image
    WorldImage drawImage() {
        return this.img;
    }

    void updateImage() {
        this.img = new FromFileImage("src/Image/Scuba.png");
    }
}


