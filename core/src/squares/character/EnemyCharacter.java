package squares.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.components.Enums;

/**
 * Enemy character that implements the movable character interface.
 */
public class EnemyCharacter implements CharacterEntity {
    @Override
    public boolean Move(Enums.UnitMovement direction, Array<Array<Entity>> currentGrid) {
        return false;
    }

    @Override
    public void castSpell() {

    }
}
