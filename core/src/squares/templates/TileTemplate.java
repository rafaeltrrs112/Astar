package squares.templates;

import com.badlogic.ashley.core.Entity;
import squares.components.Enums;
import squares.components.TagComponent;
import squares.components.TileComponent;
import squares.components.TransformComponent;

/**
 * Template for creating a tile.
 */
public class TileTemplate implements Template {

    @Override
    public Entity makeEntity() {
        Entity tile = new Entity();
        tile
                .add(new TransformComponent())
                .add(new TileComponent(Enums.TileTypes.RedPlayerTile));
        return tile;
    }

}
