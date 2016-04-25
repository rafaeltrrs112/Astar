package squares.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.components.Enums;

/**
 * The interface for a character entity. Used by the AI and the movable player to
 * move on the grid.
 */
public interface CharacterEntity {

    /**
     * The only information passed to a player character are all the entities on the grid. A manager
     * will be needed to inform this character of the state of each tile entity on the grid for decision making.
     *
     * @param currentGridState The entities that represent tiles on the grid.
     */
    boolean actOnGrid(Array<Array<Entity>> currentGridState);

    boolean tryVertical(Array<Array<Entity>> currentGrid, int increment);

    boolean tryHorizontal(Array<Array<Entity>> currentGrid, int increment);

    boolean canStep(Entity potentialTile);

    Enums.TileTypes occupyType();

    Entity getEntity();


}
