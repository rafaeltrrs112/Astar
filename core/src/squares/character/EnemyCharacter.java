package squares.character;

import com.badlogic.ashley.core.Entity;
import squares.utils.Enums;

/**
 * Enemy character that implements the movable character interface.
 */
public class EnemyCharacter extends PCPlayerCharacter {

    public EnemyCharacter(Entity entity, Enums.TileTypes occupyType) {
        super(entity, occupyType);
    }

}
