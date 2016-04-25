package squares;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import squares.character.PCPlayerCharacter;
import squares.components.spells.Blaster;
import squares.components.Enums;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.systems.GridRenderSystem;
import squares.systems.WeaponSystem;
import squares.systems.touch.UserInputHandler;

import static squares.generators.Initializer.initializeGrid;
import static squares.generators.Initializer.playerTemplate;
import static squares.generators.Initializer.blasterTemplate;


/**
 */
public class SquaresGame extends Game {

    private Engine engine = new PooledEngine();
    private final Array<Array<Entity>> gridField = initializeGrid();
    private final ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);

    /*
     * The game initialization phase.
     * 1. Create the entities that will be representing the grid and add them all to the ECS engine.
     * 2. Create the player controlled entity.
     */
    @Override
    public void create() {
        addAll(engine, gridField);

        Entity player = playerTemplate.makeEntity();

        initPlayer(player, 2, 2);

        Entity blaster = blasterTemplate.makeEntity();

        engine.addEntity(blaster);
        initSystems();
        PCPlayerCharacter mainPlayer = new PCPlayerCharacter(player, Enums.TileTypes.GreenPlayerOccupied);
        mainPlayer.getPlayerComponent().spells.addLast(blaster);
        Gdx.input.setInputProcessor(new UserInputHandler(mainPlayer, gridField));
    }

    private void initPlayer(Entity player, int x, int y) {
        player.getComponent(TransformComponent.class).setPosition(x, y);
        gridField.get(y).get(x).getComponent(TileComponent.class).setCurrentType(Enums.TileTypes.GreenPlayerOccupied);
        engine.addEntity(player);

    }

    private void initSystems() {
        engine.addSystem(new WeaponSystem(gridField));
        engine.addSystem(new GridRenderSystem());
    }

    private static void addAll(Engine engine, Array<Array<Entity>> gridField) {
        for (Array<Entity> gridTile : gridField) {
            for (Entity tile : gridTile) {
                engine.addEntity(tile);
            }
        }
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());
    }


}
