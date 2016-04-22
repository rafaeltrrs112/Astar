package squares.components;

import com.badlogic.ashley.core.Component;

/**
 * A tile component representation.
 */
public class TileComponent implements Component {
    private final Enums.TileTypes defaultType;
    private Enums.TileTypes currentType;

    public TileComponent(Enums.TileTypes defaultType) {
        this.defaultType = defaultType;
    }

    public Enums.TileTypes getDefaultType() {
        return defaultType;
    }

    public Enums.TileTypes getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Enums.TileTypes currentType) {
        this.currentType = currentType;
    }

}
