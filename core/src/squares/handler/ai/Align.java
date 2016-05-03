package squares.handler.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.components.TileComponent;
import squares.handler.enemy.SimpleMind;
import squares.utils.Enums;

/**
 * The pursue behavior: aims to reach the same lane as the player.
 */
public class Align extends SteeringBehavior {

    public Align(SimpleMind handler, Array<Array<Entity>> gridField) {
        super(handler, gridField);
    }

    @Override
    public Enums.UnitMovement behave() {
        if (handlerDelegate().getTransformComponent().x != handlerDelegate().getComfort().x) {
            return handlerDelegate().getTransformComponent().x < handlerDelegate().getComfort().x ? Enums.UnitMovement.East : Enums.UnitMovement.West;
        }
        for (Array<Entity> lane : gridField) {
            for (Entity e : lane) {
                TileComponent tileComponent = e.getComponent(TileComponent.class);
                if (tileComponent.isOccupied() && tileComponent.occupier.key.occupyType == Enums.TileTypes.GreenPlayerOccupied) {
                    if (gridField.indexOf(lane, true) < handlerDelegate().getTransformComponent().y) {
                        handlerDelegate().updatePrev();

                        return Enums.UnitMovement.South;
                    } else if (gridField.indexOf(lane, true) > handlerDelegate().getTransformComponent().y) {
                        handlerDelegate().updatePrev();
                        return Enums.UnitMovement.North;
                    } else return Enums.UnitMovement.Stay;
                }
            }
        }
        return Enums.UnitMovement.Stay;
    }
}
