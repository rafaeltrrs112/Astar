package squares.templates;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.utils.Enums;
import squares.components.TileComponent;

/**
 * Template for creating a tile.
 */
public class TileTemplate implements Template {
    @Override
    public Entity makeEntity() {
        Entity tile = new Entity();
        tile.add(new TransformComponent());
        tile.add(new TileComponent(Enums.TileTypes.RedTile));
        return tile;
    }

}
