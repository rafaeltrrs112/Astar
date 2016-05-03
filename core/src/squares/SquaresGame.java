package squares;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.character.EnemyCharacter;
import squares.character.PCPlayerCharacter;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.components.spells.Spell;
import squares.handler.CharacterHandler;
import squares.handler.KeyMap;
import squares.handler.enemy.GrappleTurret;
import squares.handler.enemy.MechanicEnemy;
import squares.handler.enemy.SimpleMind;
import squares.handler.UserInputHandler;
import squares.stages.MainStage;
import squares.stages.MenuStage;
import squares.systems.GridRenderSystem;
import squares.systems.WeaponSystem;
import squares.templates.generators.SpellGenerator;
import squares.templates.generators.SpellMaker;
import squares.ui.ChipButton;
import squares.utils.Enums.State;
import squares.utils.Enums.TileTypes;

import static squares.utils.Initializer.*;


/**
 * The main game class.
 */
public class SquaresGame extends Game {
    private static final String BLASTER_TWO_URI = "blastertwo.png";
    private static final String BLASTER_ONE_URI = "balsterone.png";
    private static final String BLASTER_THREE_URI = "blaster.png";
    private static final String WIDE_SWORD_URI = "widesword.png";
    private static final String LONG_SWORD_URI = "longsword.png";
    private static final String SHORT_SWORD_URI = "shortsword.png";
    private static final String CONFIRM_URI = "confirmbutton.gif";
    private static final String BACKGROUND_URI = "circuit.jpg";
    private static final String BOMB_ONE_URI = "bombone.png";
    private static final String BOMB_TWO_URI = "bombtwo.png";
    private static final String BOOMERANG_URI = "boomerang.png";
    private static final String GRAPPLE_URI = "grapple.png";

    private static final int LOADING_BAR_WIDTH_HEIGHT = 40;
    private static final int LOADING_BAR_START_POSIT = 450;

    private static final float LOADING_BAR_Y_POSIT = 980f;
    private static final int TEXT_SIZE = 35;

    private static final float RE_QUEUE_PHASE_TIMEOUT = 10f;
    private static final float ROUND_PHASE_TIME_OUT = 10f;
    private static final float COUNT_DOWN_PHASE_TIME_OUT = 3.5f;

    private static final float BUTTON_START_X_POSITION = 170;
    private static final float BUTTON_Y_HEIGHT = 840;
    public static final float CENTER_X = WIDTH - 220;
    public static final float CENTER_Y = HEIGHT - 100;


    private Stage roundStage;
    private MenuStage menuStage;

    private Engine engine = new PooledEngine();

    private final Array<Array<Entity>> gridField = initializeGrid();

    private SpellMaker blasterThreeGenerator;

    private SpellMaker blasterOneGenerator;

    private SpellMaker blasterTwoGenerator;

    private SpellMaker wideSwordGenerator;
    private SpellMaker shortSwordGenerator;
    private SpellMaker longSwordGenerator;

    private SpellGenerator bombOneGenerator;
    private SpellGenerator bombTwoGenerator;
    private SpellGenerator boomerangGenerator;
    private SpellMaker grappleGenerator;

    private State currentState = State.REQUEUE;

    private float passedTime = 0;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private BitmapFont countDownFont;

    private static Sprite backgroundSprite;

    private CharacterEntity mainCharacter;

    /*
     * The game initialization phase.
     * 1. Create the entities that will be representing the grid and add them all to the ECS engine.
     * 2. Create the player controlled entity.
     */
    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        backgroundSprite = new Sprite(new Texture(BACKGROUND_URI));

        initGenerators();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = TEXT_SIZE;

        bitmapFont = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter countDownParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        countDownParameter.size = TEXT_SIZE * 2;
        countDownFont = generator.generateFont(countDownParameter);

        initGrid(engine, gridField);

        initSystems();

        initMainStage();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        decideState(delta);

        switch (currentState) {
            case ROUND:
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                drawBackGround();

                Gdx.input.setInputProcessor(roundStage);
                engine.update(delta);

                roundStage.act(delta);
                roundStage.draw();

                drawLoadingBar(Color.WHITE, (int) ROUND_PHASE_TIME_OUT);
                break;
            case REQUEUE:
                Gdx.gl.glClearColor(204f / 255f, 204f / 255f, 204f / 255f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                drawBackGround();

                Gdx.input.setInputProcessor(menuStage);

                menuStage.act(delta);
                menuStage.draw();

                drawLoadingBar(Color.WHITE, (int) RE_QUEUE_PHASE_TIMEOUT);
                break;
            case COUNTDOWN:
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                drawBackGround();

                Gdx.input.setInputProcessor(roundStage);
                engine.update(delta);

                roundStage.draw();

                drawCountDown();
                break;
        }
    }

    private void initGenerators() {
        blasterThreeGenerator = new SpellGenerator(engine, TileTypes.BlasterThreeOccupied, makeSprite(BLASTER_THREE_URI), .1f);
        blasterOneGenerator = new SpellGenerator(engine, TileTypes.BlasterOneOccupied, makeSprite(BLASTER_ONE_URI), .1f);
        blasterTwoGenerator = new SpellGenerator(engine, TileTypes.BlasterTwoOccupied, makeSprite(BLASTER_TWO_URI), .1f);

        wideSwordGenerator = new SpellGenerator(engine, TileTypes.WideSwordOccupied, makeSprite(WIDE_SWORD_URI));
        shortSwordGenerator = new SpellGenerator(engine, TileTypes.ShortSwordOccupied, makeSprite(SHORT_SWORD_URI));
        longSwordGenerator = new SpellGenerator(engine, TileTypes.LongSwordOccupied, makeSprite(LONG_SWORD_URI));

        bombOneGenerator = new SpellGenerator(engine, TileTypes.BombOneOccupied, makeSprite(BOMB_ONE_URI), 1f);
        bombTwoGenerator = new SpellGenerator(engine, TileTypes.BombTwoOccupied, makeSprite(BOMB_TWO_URI), 1f);
        boomerangGenerator = new SpellGenerator(engine, TileTypes.BoomerangOccupied, makeSprite(BOOMERANG_URI), .1f);

        grappleGenerator = new SpellGenerator(engine, TileTypes.GrappleOccupied, makeSprite(GRAPPLE_URI), .05f);
    }

    private Entity initPlayer(TileTypes occupyType, TileTypes allergicType, float x, float y) {
        Entity characterEntity = playerTemplate.makeEntity();

        TransformComponent transformComp = characterEntity.getComponent(TransformComponent.class);
        PlayerComponent playerComponent = characterEntity.getComponent(PlayerComponent.class);

        playerComponent.occupyType = occupyType;

        playerComponent.allergies.add(allergicType);

        transformComp.x = x;
        transformComp.y = y;

        TileComponent tileComp = gridField
                .get((int) transformComp.y)
                .get((int) transformComp.x)
                .getComponent(TileComponent.class);

        tileComp.setCurrentType(occupyType);
        tileComp.setOccupier(playerComponent, transformComp);

        return characterEntity;
    }

    private void initMainStage() {
        Entity playerOne = initPlayer(TileTypes.GreenPlayerOccupied, TileTypes.BlueTile, 2, 2);
        Entity turretOne = initPlayer(TileTypes.VioletPlayerOccupied, TileTypes.RedTile, 5, 3);
        Entity turretTwo = initPlayer(TileTypes.CoralPlayerOccupied, TileTypes.RedTile, 7, 1);

        CharacterEntity playerCharacter = new PCPlayerCharacter(playerOne, TileTypes.GreenPlayerOccupied);
        CharacterEntity coralEnemy = new EnemyCharacter(turretTwo, TileTypes.CoralPlayerOccupied);
        CharacterEntity violetEnemy = new EnemyCharacter(turretOne, TileTypes.VioletPlayerOccupied);

        violetEnemy.getAllergies().addAll(TileTypes.GreenPlayerOccupied);
        coralEnemy.getAllergies().addAll(TileTypes.GreenPlayerOccupied);

        armTurret(violetEnemy);
        armEnemies(coralEnemy);

        CharacterHandler playerHandler = new UserInputHandler(playerCharacter, gridField, KeyMap.RED_DEFAULT());
        CharacterHandler coralHandler = new MechanicEnemy(coralEnemy, gridField);
        CharacterHandler violetHandler = new GrappleTurret(violetEnemy, gridField);

        Array<CharacterHandler> handlers = Array.with(violetHandler, coralHandler, playerHandler);

        mainCharacter = playerCharacter;
        roundStage = initUiView(new ScreenViewport(), handlers, playerCharacter);
        menuStage = menuStage(playerCharacter);

        Gdx.input.setInputProcessor(roundStage);
    }

    private void armEnemies(CharacterEntity character) {
        for (int i = 0; i < 5; i++) {
            Entity blasterSpell = boomerangGenerator.makeSpell(60);
            blasterSpell.getComponent(Spell.class).coolDown = .1f;
            character.getPlayerComponent().queueSpell(blasterSpell);
        }

        for (int i = 0; i < 5; i++) {
            Entity blasterSpell = blasterThreeGenerator.makeSpell(60);
            blasterSpell.getComponent(Spell.class).coolDown = .1f;
            character.getPlayerComponent().queueSpell(blasterSpell);
        }

        for (int i = 0; i < 5; i++) {
            character.getPlayerComponent().queueSpell(wideSwordGenerator.makeSpell(80));
        }

        System.out.println(character.getPlayerComponent().getSpells().size());
    }

    private void armTurret(CharacterEntity character) {
        for (int i = 0; i < 50; i++) {
            Entity blasterSpell = grappleGenerator.makeSpell(5);
            blasterSpell.getComponent(Spell.class).coolDown = .05f;
            character.getPlayerComponent().queueSpell(blasterSpell);
        }
    }

    private void initSystems() {
        engine.addSystem(new WeaponSystem(gridField));
        engine.addSystem(new GridRenderSystem(spriteBatch, shapeRenderer, bitmapFont));
    }

    private static void initGrid(Engine engine, Array<Array<Entity>> gridField) {
        for (Array<Entity> gridTile : gridField) {
            for (Entity tile : gridTile) {
                engine.addEntity(tile);
            }
        }
    }

    private Stage initUiView(Viewport viewport, Array<CharacterHandler> handlers, CharacterEntity character) {
        return new MainStage(character, handlers, viewport, shapeRenderer, spriteBatch, bitmapFont, gridField);
    }

    private Sprite makeSprite(String uri) {
        Texture texture = new Texture(uri);
        return new Sprite(texture);
    }

    private MenuStage menuStage(CharacterEntity mainPlayer) {
        final TextButton blaster2Button = new ChipButton(makeButtonStyle(BLASTER_TWO_URI), mainPlayer, blasterTwoGenerator, 40);
        final TextButton blaster1Button = new ChipButton(makeButtonStyle(BLASTER_ONE_URI), mainPlayer, blasterOneGenerator, 20);
        final TextButton blaster3Button = new ChipButton(makeButtonStyle(BLASTER_THREE_URI), mainPlayer, blasterThreeGenerator, 60);

        final TextButton wideSwordButton = new ChipButton(makeButtonStyle(WIDE_SWORD_URI), mainPlayer, wideSwordGenerator, 80);
        final TextButton longSwordButton = new ChipButton(makeButtonStyle(LONG_SWORD_URI), mainPlayer, longSwordGenerator, 80);
        final TextButton shortSwordButton = new ChipButton(makeButtonStyle(SHORT_SWORD_URI), mainPlayer, shortSwordGenerator, 80);

        final TextButton bombOneButton = new ChipButton(makeButtonStyle(BOMB_ONE_URI), mainPlayer, bombOneGenerator, 25);
        final TextButton boomerangButton = new ChipButton(makeButtonStyle(BOOMERANG_URI), mainPlayer, boomerangGenerator, 50);
        final TextButton bombTwoButton = new ChipButton(makeButtonStyle(BOMB_TWO_URI), mainPlayer, bombTwoGenerator, 0);

        final TextButton confirmButton = new ChipButton(makeButtonStyle(CONFIRM_URI), mainPlayer, shortSwordGenerator, 0);
        final TextButton grappleButton = new ChipButton(makeButtonStyle(GRAPPLE_URI), mainPlayer, grappleGenerator, 10);

        confirmButton.addListener(new ClickListener(Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentState = State.COUNTDOWN;
                passedTime = 0;
            }
        });
        confirmButton.setSize(100, 100);
        confirmButton.setPosition(BUTTON_START_X_POSITION, BUTTON_Y_HEIGHT);

        Array<Actor> buttons = Array.with(blaster1Button, grappleButton, blaster2Button, blaster3Button, wideSwordButton, longSwordButton, shortSwordButton, bombOneButton, boomerangButton, bombTwoButton);

        return new MenuStage(
                new ScreenViewport(), shapeRenderer, spriteBatch,
                countDownFont, engine, roundStage, buttons, confirmButton);
    }

    private void drawBackGround() {
        spriteBatch.begin();
        backgroundSprite.setScale(2);
        backgroundSprite.draw(spriteBatch);
        spriteBatch.end();
    }

    private void decideState(float delta) {
        passedTime += delta;
        switch (currentState) {
            case ROUND: {
                if (passedTime >= ROUND_PHASE_TIME_OUT) {
                    passedTime = 0;
                    currentState = State.REQUEUE;
                    menuStage.shuffleChips();
                    mainCharacter.getPlayerComponent().clearAllSpells();
                }
                break;
            }
            case REQUEUE: {
                if (passedTime >= RE_QUEUE_PHASE_TIMEOUT) {
                    passedTime = 0;
                    currentState = State.COUNTDOWN;
                }
                break;
            }
            case COUNTDOWN: {
                if (passedTime > COUNT_DOWN_PHASE_TIME_OUT) {
                    passedTime = 0;
                    currentState = State.ROUND;
                }
            }
        }
    }

    private void drawLoadingBar(Color fontColor, int fullGauge) {
        int start = LOADING_BAR_START_POSIT;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < (int) passedTime; i++) {
            shapeRenderer.setColor(135f / 255f, 255f / 255f, 255f / 255f, 1);
            shapeRenderer.rect((i * 40) + start, LOADING_BAR_Y_POSIT, LOADING_BAR_WIDTH_HEIGHT, LOADING_BAR_WIDTH_HEIGHT);
        }

        shapeRenderer.end();

        spriteBatch.begin();
        bitmapFont.setColor(fontColor);
        bitmapFont.draw(spriteBatch, Integer.toString((int) ((passedTime / fullGauge) * 100)) + "%", start - 68, 1015);
        spriteBatch.end();
    }

    private void drawCountDown() {
        spriteBatch.begin();
        countDownFont.setColor(Color.WHITE);

        countDownFont.draw(spriteBatch, ((int) passedTime) + "", CENTER_X, CENTER_Y, 40f, Align.center, false);
        spriteBatch.end();
    }
}
