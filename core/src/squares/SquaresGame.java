package squares;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.character.EnemyCharacter;
import squares.character.PCPlayerCharacter;
import squares.components.Enums.TileTypes;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.templates.generators.BlasterGenerator;
import squares.templates.generators.SpellMaker;
import squares.templates.generators.SwordGenerator;
import squares.spells.ChipButton;
import squares.stages.MainStage;
import squares.systems.GridRenderSystem;
import squares.systems.WeaponSystem;
import squares.touch.CharacterHandler;
import squares.touch.EnemyHandler;
import squares.touch.UserInputHandler;

import static squares.utils.Initializer.*;


/**
 */
public class SquaresGame extends Game {
    private Stage stage;

    private Engine engine = new PooledEngine();
    private final Array<Array<Entity>> gridField = initializeGrid();
    private SpellMaker blasterGenerator = new BlasterGenerator(engine);
    private SpellMaker swordGenerator = new SwordGenerator(engine);

    /*
     * The game initialization phase.
     * 1. Create the entities that will be representing the grid and add them all to the ECS engine.
     * 2. Create the player controlled entity.
     */
    @Override
    public void create() {
        initGrid(engine, gridField);
        Entity player = playerTemplate.makeEntity();
        Entity enemy = playerTemplate.makeEntity();

        initPlayer(player, TileTypes.GreenPlayerOccupied, TileTypes.BluePlayerTile, 2, 2);
        initPlayer(enemy, TileTypes.CoralPlayerOccupied, TileTypes.RedPlayerTile, 8, 2);

        Entity blaster = blasterGenerator.makeSpell(30);
        Entity sword = swordGenerator.makeSpell(80);
        Entity blaster2 = blasterGenerator.makeSpell(100);
        Entity blaster3 = blasterGenerator.makeSpell(100);
        Entity blaster4 = blasterGenerator.makeSpell(100);

        initSystems();

        CharacterEntity mainPlayer = new PCPlayerCharacter(player, TileTypes.GreenPlayerOccupied);
        CharacterEntity enemyCharacter = new EnemyCharacter(enemy, TileTypes.CoralPlayerOccupied);

        mainPlayer.getPlayerComponent().spells.addLast(blaster);
        mainPlayer.getPlayerComponent().spells.addLast(sword);

        enemyCharacter.getPlayerComponent().spells.addLast(blaster2);
        enemyCharacter.getPlayerComponent().spells.addLast(blaster3);
        enemyCharacter.getPlayerComponent().spells.addLast(blaster4);

        CharacterHandler playerHandler = new UserInputHandler(mainPlayer, gridField);
        CharacterHandler enemyHandler = new EnemyHandler(enemyCharacter, gridField);

        Array<CharacterHandler> handlers = Array.with(enemyHandler, playerHandler);

        stage = initStage(new ScreenViewport(), handlers, mainPlayer);

        Gdx.input.setInputProcessor(stage);
    }

    private void initPlayer(Entity characterEntity, TileTypes occupyType, TileTypes allergicType, float x, float y) {
        TransformComponent transformComp = characterEntity.getComponent(TransformComponent.class);
        PlayerComponent playerComponent = characterEntity.getComponent(PlayerComponent.class);
        playerComponent.occupyType = occupyType;

        playerComponent.allergies.add(allergicType);

        transformComp.x = x;
        transformComp.y = y;

        TileComponent tileComp = gridField.get((int) transformComp.y).get((int) transformComp.x).getComponent(TileComponent.class);
        tileComp.setCurrentType(occupyType);
        tileComp.occupier = characterEntity.getComponent(PlayerComponent.class);
        engine.addEntity(characterEntity);
    }

    private void initSystems() {
        engine.addSystem(new WeaponSystem(gridField));
        engine.addSystem(new GridRenderSystem());
    }

    private static void initGrid(Engine engine, Array<Array<Entity>> gridField) {
        for (Array<Entity> gridTile : gridField) {
            for (Entity tile : gridTile) {
                engine.addEntity(tile);
            }
        }
    }

    private Stage initStage(Viewport viewport, Array<CharacterHandler> handlers, CharacterEntity character) {
        final TextButton blasterButton = new ChipButton(makeButtonStyle("blaster.png"), character, blasterGenerator, 30);
        final TextButton swordButton = new ChipButton(makeButtonStyle("widesword.png"), character, swordGenerator, 80);

        blasterButton.setPosition(100, 500);
        swordButton.setPosition(100, 390);

        Stage stage = new MainStage(handlers, viewport);

        stage.addActor(blasterButton);
        stage.addActor(swordButton);

        return stage;
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


}
