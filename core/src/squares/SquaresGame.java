package squares;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import squares.character.PCPlayerCharacter;
import squares.components.Enums;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.generators.BlasterGenerator;
import squares.generators.SpellMaker;
import squares.generators.SwordGenertor;
import squares.spells.ChipButton;
import squares.stages.MainStage;
import squares.systems.GridRenderSystem;
import squares.systems.WeaponSystem;
import squares.systems.touch.UserInputHandler;

import static squares.generators.Initializer.initializeGrid;
import static squares.generators.Initializer.playerTemplate;


/**
 */
public class SquaresGame extends Game {
    private Stage stage;
    private Skin skin;

    private Engine engine = new PooledEngine();
    private final Array<Array<Entity>> gridField = initializeGrid();
    private final ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);
    private SpellMaker blasterGenerator;
    private SpellMaker swordGenerator;

    /*
     * The game initialization phase.
     * 1. Create the entities that will be representing the grid and add them all to the ECS engine.
     * 2. Create the player controlled entity.
     */
    @Override
    public void create() {
        addAll(engine, gridField);
        blasterGenerator = new BlasterGenerator(engine);
        swordGenerator = new SwordGenertor(engine);

        Entity player = playerTemplate.makeEntity();

        initPlayer(player, 2, 2);

        Entity blaster = blasterGenerator.makeSpell();
        Entity sword = swordGenerator.makeSpell();
        Entity sword2 = swordGenerator.makeSpell();

        initSystems();

        PCPlayerCharacter mainPlayer = new PCPlayerCharacter(player, Enums.TileTypes.GreenPlayerOccupied);

        mainPlayer.getPlayerComponent().spells.addLast(blaster);

        mainPlayer.getPlayerComponent().spells.addLast(sword);
        mainPlayer.getPlayerComponent().spells.addLast(sword2);

        UserInputHandler handler = new UserInputHandler(mainPlayer, gridField);
        createStage(new ScreenViewport(), handler, mainPlayer, blaster, sword);

        Gdx.input.setInputProcessor(stage);
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

    private void createStage(Viewport viewport, UserInputHandler handler, PCPlayerCharacter character, Entity blaster, Entity sword) {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        final TextButton blasterButton = new ChipButton("", makeButtonStyle("blaster.png"), character, blasterGenerator);
        final TextButton swordButton = new ChipButton("", makeButtonStyle("widesword.png"), character, swordGenerator);

        blasterButton.setWidth(100);
        blasterButton.setHeight(100);

        blasterButton.setPosition(100, 500);
        swordButton.setPosition(100, 390);

        swordButton.setWidth(100);
        swordButton.setHeight(100);

        stage = new MainStage(handler, viewport);
        stage.addActor(blasterButton);
        stage.addActor(swordButton);
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private TextButtonStyle makeButtonStyle(String chipPath) {
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        BitmapFont font12 = generator.generateFont(parameter);
        generator.dispose();
        textButtonStyle.font = font12;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(chipPath))));
        return textButtonStyle;
    }


}
