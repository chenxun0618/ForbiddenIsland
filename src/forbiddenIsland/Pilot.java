package forbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.WorldImage;

//to represent a pilot
class Pilot {
    int x;
    int y;

    Pilot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //produces the pilot's image
    WorldImage pilotImage() {
        return new FromFileImage("src/Image/pilot-icon.png");
    }

    //draws the pilot on to the world scene
    public void render(WorldScene s) {
        new Utils().placeImage(this.pilotImage(), this.x, this.y, s);
    }
}
