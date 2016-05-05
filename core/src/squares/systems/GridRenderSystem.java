package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final BitmapFont bitmapFont;

    public GridRenderSystem(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, BitmapFont bitmapFont) {
        super(Family.all(TransformComponent.class, TileComponent.class).get());
        this.shapeRenderer = shapeRenderer;
        this.bitmapFont = bitmapFont;
        this.spriteBatch = spriteBatch;
    }

    private void processEntityComponents(TransformComponent transformComponent, TileComponent tileComponent) {
        /// behave stuff here.
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        switch (tileComponent.getCurrentType()) {
            case RedTile:
                Color firebrick = Color.FIREBRICK;
                firebrick.a = 0.5f;
                drawRect(firebrick, transformComponent);
                break;
            case BlueTile:
                Color navy = Color.TEAL;
                navy.a = 0.5f;
                drawRect(navy, transformComponent);
                break;
            case GreenPlayerOccupied:
                Color forest = Color.FOREST;
                forest.a = (((float) tileComponent.occupier.key.health) / 250f);
                drawRect(forest, transformComponent);
                drawHealth(tileComponent, transformComponent);
                break;
            case VioletPlayerOccupied:
                drawRect(Color.VIOLET, transformComponent);
                drawHealth(tileComponent, transformComponent);
                break;
            case CoralPlayerOccupied:
                drawRect(Color.CORAL, transformComponent);
                drawHealth(tileComponent, transformComponent);
                break;
            case PurplePlayerOccupied:
                drawRect(Color.PURPLE, transformComponent);
                drawHealth(tileComponent, transformComponent);
                break;
            case LongSwordOccupied:
            case ShortSwordOccupied:
            case WideSwordOccupied:
            case BlasterOneOccupied:
            case BlasterTwoOccupied:
            case BlasterThreeOccupied:
            case BombOneOccupied:
            case BombTwoOccupied:
            case GrappleOccupied:
            case BoomerangOccupied:
                drawRect(tileComponent.getCurrentType().getColor(), transformComponent);
                break;
            default:
                throw new IllegalArgumentException("Invalid tile type");
        }

    }

    private void drawRect(Color color, TransformComponent transformComponent) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(transformComponent.x, transformComponent.y, Initializer.TILE_SIZE - 5, Initializer.TILE_SIZE - 5);
        shapeRenderer.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final TransformComponent transformComponent = transformMapper.get(entity);
        final TileComponent tileComponent = tileMapper.get(entity);
        processEntityComponents(transformComponent, tileComponent);
    }

    private void drawHealth(TileComponent tileComponent, TransformComponent transformComponent) {
        if (tileComponent.isOccupied()) {
            spriteBatch.begin();
            bitmapFont.setColor(Color.DARK_GRAY);
            bitmapFont.draw(spriteBatch, tileComponent.occupier.key.health + "", transformComponent.x + 35f, transformComponent.y + 75f);
            spriteBatch.end();
        }
    }

}
