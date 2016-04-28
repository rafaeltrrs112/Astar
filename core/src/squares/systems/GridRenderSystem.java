package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.components.TileComponent;
import squares.utils.Initializer;

/**
 * Creates a grid render system.
 */
public class GridRenderSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public GridRenderSystem() {
        super(Family.all(TransformComponent.class, TileComponent.class).get());
    }

    private void processEntityComponents(TransformComponent transformComponent, TileComponent tileComponent) {
        /// Do stuff here.
        shapeRenderer.begin(ShapeType.Filled);
        switch (tileComponent.getCurrentType()) {
            case RedTile:
                shapeRenderer.setColor(Color.FIREBRICK);
                shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case BlueTile:
                shapeRenderer.setColor(Color.NAVY);
                shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case GreenPlayerOccupied:
                shapeRenderer.setColor(Color.FOREST);
                shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case BlasterOccupied:
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case SwordOccupied:
                shapeRenderer.setColor(Color.LIGHT_GRAY);
                shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case CoralPlayerOccupied:
                shapeRenderer.setColor(Color.CORAL);
                shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            default:
                throw new IllegalArgumentException("Invalid tile type");
        }

        shapeRenderer.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final TransformComponent transformComponent = transformMapper.get(entity);
        final TileComponent tileComponent = tileMapper.get(entity);
        processEntityComponents(transformComponent, tileComponent);
    }

}
