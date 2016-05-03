package squares.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.utils.Enums;
import squares.utils.Enums.TravelDirection;
import squares.components.PlayerComponent;

/**
 * The interface for a character entity. Used by the AI and the movable player to
 * move on the grid.
 */
public interface CharacterEntity {

    boolean Move(Enums.UnitMovement direction, Array<Array<Entity>> currentGrid);

    void castSpell(TravelDirection direction);

    Entity getEntity();

    Enums.TileTypes occupyType();

    PlayerComponent getPlayerComponent();

    Array<Enums.TileTypes> getAllergies();

}