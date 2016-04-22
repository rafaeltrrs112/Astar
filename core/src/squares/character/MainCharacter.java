package squares.character;

import com.badlogic.ashley.core.Entity;
import squares.components.Enums;
import squares.components.Enums.UnitMovement;
import squares.components.TransformComponent;

/**
 * Main player character object wrapper.
 */
public class MainCharacter {
    private Entity playerEntity;
    private TransformComponent transformComponent;

    public TransformComponent getTransformComponent() {
        return transformComponent;
    }

    public MainCharacter(Entity entity, TransformComponent transformComponent) {
        playerEntity = entity;
        this.transformComponent = transformComponent;
    }

    public void Move(UnitMovement direction) {
        switch (direction) {
            case North:
                transformComponent.getPosition().y -= 1;
                break;
            case South:
                transformComponent.getPosition().y += 1;
                break;
            case East:
                transformComponent.getPosition().x += 1;
                break;
            case West:
                transformComponent.getPosition().x -= 1;
                break;
            default :
                throw new IllegalArgumentException("Invalid direction");
        }
    }
}
