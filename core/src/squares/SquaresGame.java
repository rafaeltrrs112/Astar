package squares;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.character.EnemyCharacter;
import squares.character.PCPlayerCharacter;
import squares.utils.Enums.State;
import squares.utils.Enums.TileTypes;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.templates.generators.*;
import squares.ui.ChipButton;
import squares.stages.MainStage;
import squares.systems.GridRenderSystem;
import squares.systems.WeaponSystem;
import squares.touch.CharacterHandler;
import squares.touch.EnemyHandler;
import squares.touch.UserInputHandler;

import static squares.utils.Initializer.*;


/**
 * The main game class.
 */
public class SquaresGame extends Game {
    private Stage roundStage;
    private Stage menuStage;

    private Engine engine = new PooledEngine();
    private final Array<Array<Entity>> gridField = initializeGrid();

    private SpellMaker blasterThreeGenerator = new BlasterThreeGenerator(engine);
    private SpellMaker blasterOneGenerator = new BlasterOneGenerator(engine);
    private SpellMaker blasterTwoGenerator = new BlasterTwoGenerator(engine);

    private SpellMaker wideSwordGenerator = new SwordGenerator(engine);
    private SpellMaker shortSwordGenerator = new ShortsSwordGenerator(engine);
    private SpellMaker longSwordGenerator = new LongSwordGenerator(engine);

    private State currentState = State.REQUEUE;

    private float passedTime = 0;

    private ShapeRenderer shapeRenderer;


    /*
     * The game initialization phase.
     * 1. Create the entities that will be representing the grid and add them all to the ECS engine.
     * 2. Create the player controlled entity.
     */
    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        initGrid(engine, gridField);

        initSystems();

        initMainStage();
    }

    private void initMainStage() {
        Entity player = initPlayer(TileTypes.GreenPlayerOccupied, TileTypes.BlueTile, 2, 2);
        Entity enemy = initPlayer(TileTypes.CoralPlayerOccupied, TileTypes.RedTile, 8, 2);
        Entity otherEnemy = initPlayer(TileTypes.VioletPlayerOccupied, TileTypes.RedTile, 5, 3);

        CharacterEntity playerCharacter = new PCPlayerCharacter(player, TileTypes.GreenPlayerOccupied);
        CharacterEntity coralEnemy = new EnemyCharacter(enemy, TileTypes.CoralPlayerOccupied);
        CharacterEntity violetEnemy = new EnemyCharacter(otherEnemy, TileTypes.VioletPlayerOccupied);

        CharacterHandler playerHandler = new UserInputHandler(playerCharacter, gridField);
        CharacterHandler coralHandler = new EnemyHandler(coralEnemy, gridField);
        CharacterHandler violetHandler = new EnemyHandler(violetEnemy, gridField);

        Array<CharacterHandler> handlers = Array.with(coralHandler, playerHandler, violetHandler);

        roundStage = initUiView(new ScreenViewport(), handlers, playerCharacter);
        menuStage = menuStage(playerCharacter);

        Gdx.input.setInputProcessor(roundStage);
    }

    private Entity initPlayer(TileTypes occupyType, TileTypes allergicType, float x, float y) {
        Entity characterEntity = playerTemplate.makeEntity();

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
        return characterEntity;
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

    private Stage initUiView(Viewport viewport, Array<CharacterHandler> handlers, CharacterEntity character) {
        return new MainStage(handlers, viewport);
    }

    private Stage menuStage(CharacterEntity mainPlayer) {
        Stage menuStage = new Stage(new ScreenViewport());

        final TextButton blaster2Button = new ChipButton(makeButtonStyle("blastertwo.png"), mainPlayer, blasterTwoGenerator, 40);
        final TextButton blaster1Button = new ChipButton(makeButtonStyle("balsterone.png"), mainPlayer, blasterOneGenerator, 20);
        final TextButton blaster3Button = new ChipButton(makeButtonStyle("blaster.png"), mainPlayer, blasterThreeGenerator, 60);

        final TextButton wideSwordButton = new ChipButton(makeButtonStyle("widesword.png"), mainPlayer, wideSwordGenerator, 80);
        final TextButton longSwordButton = new ChipButton(makeButtonStyle("longsword.png"), mainPlayer, longSwordGenerator, 80);
        final TextButton shortSwordButton = new ChipButton(makeButtonStyle("shortsword.png"), mainPlayer, shortSwordGenerator, 80);

        final TextButton confirmButton = new ChipButton("CONFIRM", makeButtonStyle("confirmbutton.gif"), mainPlayer, shortSwordGenerator, 80);

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("State change!");
                currentState = State.ROUND;
            }
        });

        blaster1Button.setPosition(170, 840);
        blaster2Button.setPosition(280, 840);
        blaster3Button.setPosition(390, 840);

        wideSwordButton.setPosition(500, 840);
        longSwordButton.setPosition(610, 840);
        shortSwordButton.setPosition(720, 840);

        confirmButton.setPosition(840, 840);

        menuStage.addActor(blaster3Button);
        menuStage.addActor(blaster2Button);
        menuStage.addActor(blaster1Button);

        menuStage.addActor(wideSwordButton);
        menuStage.addActor(longSwordButton);
        menuStage.addActor(shortSwordButton);
        menuStage.addActor(confirmButton);

        return menuStage;
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        decideState(delta);

        switch (currentState) {
            case ROUND:
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                Gdx.input.setInputProcessor(roundStage);
                engine.update(delta);
                roundStage.act(delta);
                roundStage.draw();
                break;
            case REQUEUE:
                Gdx.gl.glClearColor(0, 0, 0, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                Gdx.input.setInputProcessor(menuStage);
                menuStage.act(delta);
                menuStage.draw();
                break;
        }
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.triangle(20, 0, 40, 0, 30, 100);
        shapeRenderer.end();
    }

    private void decideState(float delta) {
        passedTime += delta;
        System.out.println(passedTime);

        switch (currentState) {
            case ROUND: {
                if (passedTime >= 15) {
                    passedTime = 0;
                    currentState = State.REQUEUE;
                }
                break;
            }
            case REQUEUE: {
                if (passedTime >= 10) {
                    passedTime = 0;
//                    currentState = State.ROUND;
                }
                break;
            }
        }
    }


}
