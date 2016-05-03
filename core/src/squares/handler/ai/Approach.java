package squares.handler.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.handler.enemy.SimpleMind;
import squares.utils.Enums;

/**
 * Approach behavior approached the player.
 */
public class Approach extends Align {
    public Approach(SimpleMind handler, Array<Array<Entity>> gridField) {
        super(handler, gridField);
    }

    @Override
    public Enums.UnitMovement behave() {
        if (!handlerDelegate().playerInLane()) {
            return super.behave();
        } else {
            if (handlerDelegate().getComfort().x != 5) {
                handlerDelegate().getComfort().x -= 1;
            }
            return rushForward();
        }
    }

    private Enums.UnitMovement rushForward() {
        return Enums.UnitMovement.West;
    }

}
