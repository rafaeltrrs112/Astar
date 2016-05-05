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
        if (handlerDelegate().getTransformComponent().x != 5) {
            handlerDelegate().updatePrev();
            if ((handlerDelegate().getTransformComponent().x == 7 ||
                    handlerDelegate().getTransformComponent().x == 8)
                    && handlerDelegate().behindComrade()) {
                return Enums.UnitMovement.South;
            }
            return rushForward();
        } else if (!handlerDelegate().playerInLane()) {
            handlerDelegate().updatePrev();
            System.out.println("Not in lane!");
            System.out.println("Going " + super.behave());
            return super.behave();
        }
        handlerDelegate().updatePrev();

        return super.behave();
    }



    private Enums.UnitMovement rushForward() {
        return Enums.UnitMovement.West;
    }

}
