package squares.generators;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import squares.components.TagComponent;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.templates.BlasterTemplate;
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
    public static Template blasterTemplate = new BlasterTemplate();

    private static final float ADJUST_X = (WIDTH / 4);
    private static final float ADJUST_Y = (HEIGHT / 4);
    public static final float TILE_SIZE = 150;

    public static Array<Array<Entity>> initializeGrid() {
        Array<Array<Entity>> gridList = new Array<Array<Entity>>();

        for (int j = 0; j < 5; j++) {
            Array<Entity> row = new Array<Entity>();
            for (int i = 0; i < 10; i++) {
                Entity entity = tileTemplate.makeEntity();
                TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
                TagComponent tagComponent = entity.getComponent(TagComponent.class);

                tagComponent.name = "X + " + i + ", Y + " + j;

                transformComponent.setPosition(new Vector2((i * TILE_SIZE) + ADJUST_X, (j * TILE_SIZE) + ADJUST_Y));

                System.out.println(transformComponent.getPosition());
                TileTypes defaultType = i <= 4 ? TileTypes.RedPlayerTile : TileTypes.BluePlayerTile;

                TileComponent tileComp = entity.getComponent(TileComponent.class);

                tileComp.setCurrentType(defaultType);
                tileComp.setDefaultType(defaultType);

                row.add(entity);
            }
            gridList.add(row);
        }

        return gridList;
    }
}
