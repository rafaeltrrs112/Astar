package squares.touch;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.components.Enums;
import squares.components.TileComponent;

/**
 * Input handler for manipulating the main player character.
 */
public class UserInputHandler implements CharacterHandler {
    protected CharacterEntity playerCharacter;
    protected Array<Array<Entity>> gridField;
    protected final ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);
    protected final TransformComponent transformComponent;

    public UserInputHandler(CharacterEntity mainPlayer, Array<Array<Entity>> gridField) {
        this.playerCharacter = mainPlayer;
        this.gridField = gridField;
        this.transformComponent = mainPlayer.getEntity().getComponent(TransformComponent.class);
    }

    @Override
    public void handle(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            playerCharacter.Move(Enums.UnitMovement.North, gridField);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            playerCharacter.Move(Enums.UnitMovement.West, gridField);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            playerCharacter.Move(Enums.UnitMovement.South, gridField);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            playerCharacter.Move(Enums.UnitMovement.East, gridField);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playerCharacter.castSpell(Enums.TravelDirection.RIGHT);
        }
        setTile();
    }

    void setTile() {
        TileComponent tile = tileMapper.get(gridField.get((int) transformComponent.y).get((int) transformComponent.x));
        tile.setCurrentType(playerCharacter.occupyType());
        for (Array<Entity> row : gridField) {
            for (Entity otherTile : row) {
                TileComponent tileComponent = tileMapper.get(otherTile);
                if (tileComponent != tile && tileComponent.getCurrentType() == playerCharacter.occupyType()) {
                    tileComponent.occupier = null;
                    tileComponent.setCurrentType(tileComponent.getDefaultType());
                    return;
                }
            }
        }
    }
}
