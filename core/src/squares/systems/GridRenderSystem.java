package squares.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import squares.components.Enums;
import squares.components.TileComponent;
import squares.components.TransformComponent;

/**
 * Creates a grid render system.
 */
public class GridRenderSystem extends IteratingSystem {
    private Texture bluePlayerTexture;
    private Texture redPlayerTexture;
    private Texture playerCharacterTexture;
    private SpriteBatch spriteBatch;

    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);

    private final SpriteBatch batch;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public GridRenderSystem(SpriteBatch spriteBatch) {
        super(Family.all(TransformComponent.class, TileComponent.class).get());
        this.batch = spriteBatch;

    }

    private void processEntityComponents(Entity entity, TransformComponent transformComponent, TileComponent tileComponent, float delta) {
        /// Do stuff here.
        shapeRenderer.begin(ShapeType.Filled);
        switch (tileComponent.getCurrentType()) {
            case RedPlayerTile:
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(transformComponent.x(), transformComponent.y(), 50f, 50f);
                break;
            case BluePlayerTile:
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.rect(transformComponent.x(), transformComponent.y(), 50f, 50f);
                break;
            case Occupied:
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(transformComponent.x(), transformComponent.y(), 50f, 50f);
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
        processEntityComponents(entity, transformComponent, tileComponent, deltaTime);
    }
}
