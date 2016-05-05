package squares.handler.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.handler.enemy.SimpleMind;
import squares.utils.Enums;

/**
 */
public class Oscillate extends SteeringBehavior {

    public Oscillate(SimpleMind handler, Array<Array<Entity>> gridField) {
        super(handler, gridField);
    }

    @Override
    public Enums.UnitMovement behave() {
        if (random().nextInt(1) == 0 && handlerDelegate().getTransformComponent().x != handlerDelegate().getComfort().x) handlerDelegate().getComfortable();
        if (handlerDelegate().getTransformComponent().x != handlerDelegate().getComfort().x) {
            if(handlerDelegate().inFrontOfComrade()) return Enums.UnitMovement.North;
            return handlerDelegate().getTransformComponent().x < handlerDelegate().getComfort().x ? Enums.UnitMovement.East : Enums.UnitMovement.West;
        }
        if (handlerDelegate().currentDirection() == Enums.UnitMovement.East || handlerDelegate().currentDirection() == Enums.UnitMovement.West) {
            handlerDelegate().updatePrev();
            return random().nextInt(1) == 0 ? Enums.UnitMovement.North : Enums.UnitMovement.South;
        }
        if (handlerDelegate().getTransformComponent().y == 4) {
            handlerDelegate().updatePrev();
            return Enums.UnitMovement.South;
        } else if (handlerDelegate().getTransformComponent().y == 0) {
            handlerDelegate().updatePrev();
            return Enums.UnitMovement.North;
        } else {
            return handlerDelegate().currentDirection();
        }
    }
}
