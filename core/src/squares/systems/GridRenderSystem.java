package squares.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public GridRenderSystem() {
        super(Family.all(TransformComponent.class, TileComponent.class).get());
    }

    private void processEntityComponents(Entity entity, TransformComponent transformComponent, TileComponent tileComponent, float delta) {
        /// Do stuff here.
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final TransformComponent transformComponent = transformMapper.get(entity);
        final TileComponent tileComponent = tileMapper.get(entity);
        processEntityComponents(entity, transformComponent, tileComponent, deltaTime);
    }
}
