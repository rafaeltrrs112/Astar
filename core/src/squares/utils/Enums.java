package squares.utils;

import com.badlogic.gdx.graphics.Color;
import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Container constants class for the enums.
 */
public class Enums {
    public enum TileTypes {

        RedTile(Color.FIREBRICK),
        BlueTile(Color.NAVY),
        GreenPlayerOccupied(Color.FOREST),
        CoralPlayerOccupied(Color.CORAL),
        BlasterOneOccupied(Color.YELLOW),
        WideSwordOccupied(Color.LIGHT_GRAY),
        VioletPlayerOccupied(Color.VIOLET),
        LongSwordOccupied(Color.SLATE),
        ShortSwordOccupied(Color.PURPLE),
        BlasterTwoOccupied(Color.TEAL),
        BlasterThreeOccupied(Color.BLACK),
        BombOneOccupied(Color.ORANGE),
        BombTwoOccupied(Color.PINK),
        BoomerangOccupied(Color.MAROON),
        GrappleOccupied(Color.BROWN);

        private final Color color;

        public Color getColor() {
            return color;
        }

        TileTypes(Color color){
            this.color = color;
        }
    }

    public enum UnitMovement {
        North,
        South,
        East,
        West,
        Stay
    }

    public enum TravelDirection {
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP(0, 1),
        DOWN(0, -1);

        public final int xIncrement;
        public final int yIncrement;

        TravelDirection(int xIncrement, int yIncrement) {
            this.xIncrement = xIncrement;
            this.yIncrement = yIncrement;
        }

        public int getxIncrement() {
            return xIncrement;
        }

        public int getyIncrement() {
            return yIncrement;
        }

        public TravelDirection flip(){
            switch(this){
                case LEFT: return RIGHT;
                case RIGHT: return LEFT;
                case UP: return DOWN;
                case DOWN: return UP;
                default: throw new AssertionError("Invalid Direction type " + this.getClass());
            }
        }
    }

    public enum State {
        ROUND,
        COUNTDOWN, REQUEUE
    }
}
