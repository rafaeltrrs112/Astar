package squares.systems.touch;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import squares.character.CharacterEntity;
import squares.character.PCPlayerCharacter;
import squares.components.Enums;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.components.spells.Spell;

/**
 *
 */
public class UserInputHandler implements InputProcessor {
    private PCPlayerCharacter mainPlayer;
    private Array<Array<Entity>> gridField;
    private final ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);
    private final TransformComponent transformComponent;

    public UserInputHandler(PCPlayerCharacter mainPlayer, Array<Array<Entity>> gridField) {
        this.mainPlayer = mainPlayer;
        this.gridField = gridField;
        this.transformComponent = mainPlayer.getEntity().getComponent(TransformComponent.class);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                mainPlayer.Move(Enums.UnitMovement.North, gridField);
                break;
            case Input.Keys.A:
                mainPlayer.Move(Enums.UnitMovement.West, gridField);
                break;
            case Input.Keys.S:
                mainPlayer.Move(Enums.UnitMovement.South, gridField);
                break;
            case Input.Keys.D:
                mainPlayer.Move(Enums.UnitMovement.East, gridField);
                break;
            case Input.Keys.SPACE:
                mainPlayer.castSpell();
                break;
            default:
                return false;
        }
        setTile();

        return false;
    }

    private void setTile() {
        TileComponent tile = tileMapper.get(gridField.get((int) transformComponent.y()).get((int) transformComponent.x()));
        tile.setCurrentType(mainPlayer.occupyType());
        for (Array<Entity> row : gridField) {
            for (Entity otherTile : row) {
                TileComponent tileComponent = tileMapper.get(otherTile);
                if (tileComponent != tile && tileComponent.getCurrentType() == mainPlayer.occupyType()) {
                    tileComponent.setCurrentType(tileComponent.getDefaultType());
                    return;
                }
            }
        }
    }


    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
