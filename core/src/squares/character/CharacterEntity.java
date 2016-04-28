package squares.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.components.Enums;

/**
 * The interface for a character entity. Used by the AI and the movable player to
 * move on the grid.
 */
public interface CharacterEntity {

    public boolean Move(Enums.UnitMovement direction, Array<Array<Entity>> currentGrid);

    public void castSpell();
}