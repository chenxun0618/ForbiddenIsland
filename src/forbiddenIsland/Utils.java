package forbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.WorldImage;


//utility class
class Utils {
    //places an image on this world scene with the given coordinate
    void placeImage(WorldImage img, int x, int y, WorldScene s) {
        s.placeImageXY(img, x * Cell.CELL_SIZE + Cell.CELL_SIZE / 2,
                y * Cell.CELL_SIZE + Cell.CELL_SIZE / 2);
    }
}
