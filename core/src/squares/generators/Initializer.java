package squares.generators;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.templates.PlayerTemplate;
import squares.templates.Template;
import squares.templates.TileTemplate;

import static squares.components.Enums.TileTypes;
import static squares.generators.Constants.*;

/**
 * Initializer utility methods.
 */
public class Initializer {
    public static Template tileTemplate = new TileTemplate();
    public static Template playerTemplate = new PlayerTemplate();

    public static Array<Array<Entity>> initializeGrid() {
        Array<Array<Entity>> gridList = new Array<Array<Entity>>();

        for (int j = 0; j < 5; j++) {
            Array<Entity> row = new Array<Entity>();
            for (int i = 0; i < 10; i++) {
                Entity entity = tileTemplate.makeEntity();
                TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

                transformComponent.getPosition().x = i * TILE_GAP_STEP + WIDTH / 4;
                transformComponent.getPosition().y = j * TILE_GAP_STEP + HEIGHT / 4;


                TileTypes defaultType = i <= 4 ? TileTypes.RedPlayerTile : TileTypes.BluePlayerTile;

                TileComponent tileComp = entity.getComponent(TileComponent.class);

                tileComp.setCurrentType(defaultType);
                tileComp.setDefaultType(tileComp.getCurrentType());

                row.add(entity);
            }
            gridList.add(row);
        }

        return gridList;
    }
}
