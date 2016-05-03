package squares.handler;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.utils.Enums;
import squares.components.TileComponent;

/**
 * Input handler for manipulating the main player character.
 */
public class UserInputHandler implements CharacterHandler {
    private final CharacterEntity playerCharacter;
    protected final Array<Array<Entity>> gridField;
    protected final ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);
    protected final TransformComponent transformComponent;
    private final KeyMap keyMap;

    public UserInputHandler(CharacterEntity mainPlayer, Array<Array<Entity>> gridField, KeyMap keyMap) {
        this.playerCharacter = mainPlayer;
        this.gridField = gridField;
        this.transformComponent = mainPlayer.getEntity().getComponent(TransformComponent.class);
        this.keyMap = keyMap;
    }

    @Override
    public void handle(float delta) {
        playerCharacter.getPlayerComponent().cont(delta);
        if (!playerCharacter.getPlayerComponent().stunned()) {
            if (Gdx.input.isKeyJustPressed(keyMap.UP)) {
                playerCharacter.Move(Enums.UnitMovement.North, gridField);
            } else if (Gdx.input.isKeyJustPressed(keyMap.LEFT)) {
                playerCharacter.Move(Enums.UnitMovement.West, gridField);
            } else if (Gdx.input.isKeyJustPressed(keyMap.DOWN)) {
                playerCharacter.Move(Enums.UnitMovement.South, gridField);
            } else if (Gdx.input.isKeyJustPressed(keyMap.RIGHT)) {
                playerCharacter.Move(Enums.UnitMovement.East, gridField);
            } else if (Gdx.input.isKeyJustPressed(keyMap.FIRE)) {
                playerCharacter.castSpell(keyMap.DIRECTION);
            }
        }
        setTile();
    }

    @Override
    public CharacterEntity getCharacter() {
        return playerCharacter;
    }

    protected final void setTile() {
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

    public final void moveCharacter(Enums.UnitMovement direction) {
        playerCharacter.Move(direction, gridField);
    }

    public final boolean hasSpells() {
        return playerCharacter.getPlayerComponent().hasSpells();
    }

    public void shuffleSpells() {
        playerCharacter.getPlayerComponent().shuffleSpells();
    }

}
