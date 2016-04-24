package squares;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import squares.character.MainCharacter;
import squares.components.TransformComponent;
import squares.systems.GridRenderSystem;
import squares.systems.PCMovementSystem;

import static squares.generators.Initializer.initializeGrid;
import static squares.generators.Initializer.playerTemplate;

/**
 */
public class SquaresGame extends Game {
    private static Viewport viewport;
    private static SpriteBatch spriteBatch;
    private static OrthographicCamera camera;

    private Engine engine = new PooledEngine();

    @Override
    public void create() {
        viewport = new FitViewport(1280, 720);

        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);
        camera = new OrthographicCamera();

        Array<Array<Entity>> gridField = initializeGrid();

        addAll(engine, gridField);

        Entity player = playerTemplate.makeEntity();
        EntitySystem movementSystem = new PCMovementSystem(new MainCharacter(player, player.getComponent(TransformComponent.class)), gridField);

        engine.addSystem(new GridRenderSystem(spriteBatch));
        engine.addSystem(movementSystem);
    }

    private static void addAll(Engine engine, Array<Array<Entity>> gridField) {
        for (Array<Entity> gridTile : gridField) {
            for (Entity tile : gridTile) {
                engine.addEntity(tile);
            }
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());
    }

}
