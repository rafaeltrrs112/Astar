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

import java.awt.*;

import static squares.generators.Initializer.initializeGrid;
import static squares.generators.Initializer.playerTemplate;

/**
 */
public class SquaresGame extends Game {

    private Engine engine = new PooledEngine();

    /*
     * The game initialization phase.
     * 1. Create the entities that will be representing the grid and add them all to the ECS engine.
     * 2. Create the player controlled entity.
     */
    @Override
    public void create() {
        Array<Array<Entity>> gridField = initializeGrid();
        addAll(engine, gridField);


        Entity player = playerTemplate.makeEntity();
        EntitySystem movementSystem = new PCMovementSystem(new MainCharacter(player, player.getComponent(TransformComponent.class)), gridField);

        engine.addEntity(player);
        engine.addSystem(new GridRenderSystem());
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
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());
    }

}
