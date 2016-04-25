package squares.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import squares.components.Enums;
import squares.components.TagComponent;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.generators.Initializer;

/**
 * Creates a grid render system.
 */
public class GridRenderSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);
    private ComponentMapper<TagComponent> tagMapper = ComponentMapper.getFor(TagComponent.class);

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public GridRenderSystem() {
        super(Family.all(TransformComponent.class, TileComponent.class).get());
    }

    private void processEntityComponents(Entity entity, TransformComponent transformComponent, TileComponent tileComponent, TagComponent tagComponent, float delta) {
        /// Do stuff here.
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND); // Or GL20
        shapeRenderer.begin(ShapeType.Filled);

        switch (tileComponent.getCurrentType()) {
            case RedPlayerTile:
                shapeRenderer.setColor(Color.FIREBRICK);
                shapeRenderer.rect(transformComponent.x(), transformComponent.y(), Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case BluePlayerTile:
                shapeRenderer.setColor(Color.NAVY);
                shapeRenderer.rect(transformComponent.x(), transformComponent.y(), Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
                break;
            case Occupied:
                shapeRenderer.setColor(Color.FOREST);
                shapeRenderer.rect(transformComponent.x(), transformComponent.y(), Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
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
        final TagComponent tagComponent = tagMapper.get(entity);
        processEntityComponents(entity, transformComponent, tileComponent, tagComponent, deltaTime);
    }
}
