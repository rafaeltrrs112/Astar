package squares.components;

/**
 * Container constants class for the enums.
 */
public class Enums {
    public enum TileTypes {
        RedTile,
        BlueTile,
        GreenPlayerOccupied,
        CoralPlayerOccupied,
        BlasterOccupied,
        SwordOccupied
    }

    public enum UnitMovement {
        North,
        South,
        East,
        West
    }

    public enum TravelDirection {
        LEFT(-1),
        RIGHT(1);

        public final int increment;

        TravelDirection(int increment){
            this.increment = increment;
        }

        public int getIncrement(){
            return increment;
        }
    }
}
