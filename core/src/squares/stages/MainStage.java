package squares.stages;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.components.TileComponent;
import squares.components.spells.Spell;
import squares.handler.CharacterHandler;

/**
 */
public class MainStage extends Stage {
    private Array<CharacterHandler> handlers;
    private CharacterEntity mainCharacter;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private Array<Array<Entity>> gridField;

    public MainStage(CharacterEntity mainCharacter, Array<CharacterHandler> handlers, Viewport viewPort, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont bitmapFont, Array<Array<Entity>> gridField) {
        super(viewPort);
        this.handlers = handlers;
        this.mainCharacter = mainCharacter;
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.bitmapFont = bitmapFont;
        this.gridField = gridField;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (CharacterHandler handler : handlers) {
            if(handler.getCharacter().getPlayerComponent().health > 0) {
                handler.handle(delta);
            } else {
                TransformComponent transformComponent = handler.getCharacter().getEntity().getComponent(TransformComponent.class);
                TileComponent tileComponent = gridField.get((int) transformComponent.y).get((int) transformComponent.x).getComponent(TileComponent.class);
                tileComponent.occupier = null;
                tileComponent.setCurrentType(tileComponent.getDefaultType());
            }
        }
    }

    @Override
    public void draw() {
        super.draw();
        drawChips();
    }

    private void drawChips() {
        ImmutableArray<Entity> chips = mainCharacter.getPlayerComponent().getSpells();

        if (chips.size() != 0) {
            spriteBatch.begin();
            Spell nextSpell =  chips.get(0).getComponent(Spell.class);
            Sprite sprite = nextSpell.sprite;
            sprite.setSize(100, 100);
            sprite.setPosition(10, 900);
            sprite.draw(spriteBatch);
            sprite.setSize(65, 65);

            bitmapFont.setColor(Color.WHITE);
            bitmapFont.draw(spriteBatch, nextSpell.damage + "", 10, 1030);
            spriteBatch.end();

            spriteBatch.begin();
            for (int i = 1; i < chips.size(); i++) {
                Spell spell = chips.get(i).getComponent(Spell.class);
                int step = 70;
                Sprite spriteOther = spell.sprite;
                sprite.setSize(65, 65);
                spriteOther.setPosition(10, 890 - ((i * step)));
                spriteOther.draw(spriteBatch);
            }
            spriteBatch.end();
        }
    }

}