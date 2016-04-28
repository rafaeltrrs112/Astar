package squares.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.components.TileComponent;
import squares.templates.*;

import static squares.components.Enums.TileTypes;

/**
 * Initializer utility methods.
 */
public class Initializer {
    public static Template tileTemplate = new TileTemplate();
    public static Template playerTemplate = new PlayerTemplate();
    public static Template spellTempalte = new SpellTemplate();
    public static final float WIDTH = 1280;
    public static final int HEIGHT = 720;
    private static final float ADJUST_X = (WIDTH / 4);
    private static final float ADJUST_Y = (HEIGHT / 4);
    public static final float TILE_SIZE = 150;

    public static Array<Array<Entity>> initializeGrid() {
        Array<Array<Entity>> gridList = new Array<Array<Entity>>();

        for (int j = 0; j < 5; j++) {
            Array<Entity> row = new Array<Entity>();
            for (int i = 0; i < 10; i++) {
                Entity entity = tileTemplate.makeEntity();
                TransformComponent transformComponent = entity.getComponent(TransformComponent.class);


                transformComponent.x = (i * TILE_SIZE) + ADJUST_X;
                transformComponent.y = (j * TILE_SIZE) + ADJUST_Y;

                TileTypes defaultType = i <= 4 ? TileTypes.RedPlayerTile : TileTypes.BluePlayerTile;

                TileComponent tileComp = entity.getComponent(TileComponent.class);

                tileComp.setCurrentType(defaultType);
                tileComp.setDefaultType(defaultType);

                row.add(entity);
            }
            gridList.add(row);
        }

        return gridList;
    }

    public static TextButton.TextButtonStyle makeButtonStyle(String chipPath) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 12;

        BitmapFont font12 = generator.generateFont(parameter);

        generator.dispose();

        textButtonStyle.font = font12;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(chipPath))));

        return textButtonStyle;
    }
}
