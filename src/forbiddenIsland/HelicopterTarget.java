package forbiddenIsland;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.WorldImage;

//to represent a helicopter target
class HelicopterTarget extends Target {
    HelicopterTarget(int x, int y) {
        super(x, y);
    }

    //produces the helicopter's image
    WorldImage drawImage() {
        return new FromFileImage("src/Image/helicopter.png");
    }
}
