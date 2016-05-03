package squares.stages;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import squares.SquaresGame;
import squares.ui.ChipButton;

import static squares.SquaresGame.*;

/**
 * The stage that contains and controls the menu UI used by the player to choose their next chip.
 */
public class MenuStage extends Stage {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private Engine engine;
    private Stage roundStage;

    private static final float BUTTON_START_X_POSITION = 300;
    private static final float BUTTON_Y_HEIGHT = 840;

    private Array<Actor> chipButtons;
    private Actor confirmButton;

    public MenuStage(Viewport viewPort, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont bitmapFont,
                     Engine engine, Stage roundStage, Array<Actor> chipButtons, Actor confirmButton) {
        super(viewPort);
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.bitmapFont = bitmapFont;
        this.engine = engine;
        this.roundStage = roundStage;
        this.chipButtons = chipButtons;
        this.confirmButton = confirmButton;
        addActor(confirmButton);
        shuffleChips();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        hotKeyInput();
    }

    @Override
    public void draw() {
        drawPauseState();
        super.draw();
    }

    public void shuffleChips(){
        chipButtons.shuffle();

        getActors().removeAll(chipButtons, true);

        for(int i = 0; i < 5; i++){
            chipButtons.get(i).setPosition((i * 120) + BUTTON_START_X_POSITION, BUTTON_Y_HEIGHT);
            addActor(chipButtons.get(i));
        }
    }

    private void hotKeyInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            hotKeyPress(Input.Keys.NUM_1 - 8);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            hotKeyPress(Input.Keys.NUM_2 - 8);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            hotKeyPress(Input.Keys.NUM_3 - 8);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            hotKeyPress(Input.Keys.NUM_4 - 8);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
            hotKeyPress(Input.Keys.NUM_5 - 8);
        }
    }

    private void hotKeyPress(int targetButton){
        ChipButton button = (ChipButton) chipButtons.get(targetButton);
        button.apply();
    }

    private void drawPauseState() {
        engine.update(0);
        roundStage.draw();

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Color pauseColor = Color.BLACK;
        pauseColor.a = 0.5f;

        shapeRenderer.setColor(pauseColor);
        shapeRenderer.rect(0, 0, 1920f, 1080f);
        shapeRenderer.end();

        spriteBatch.begin();

        bitmapFont.draw(spriteBatch, "Re-Queue Up!", CENTER_X, CENTER_Y, 40f, Align.center, false);
        spriteBatch.end();
    }
}
