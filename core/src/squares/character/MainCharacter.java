package squares.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.components.Enums;
import squares.components.Enums.UnitMovement;
import squares.components.TileComponent;
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

    public boolean Move(UnitMovement direction, Array<Array<Entity>> currentGrid) {
        switch (direction) {
            case North:
                return transformComponent.getPosition().y < 4 && tryVertical(currentGrid, 1);
            case South:
                return transformComponent.getPosition().y > 0 && tryVertical(currentGrid, -1);
            case East:
                return transformComponent.getPosition().x < 9 && tryHorizontal(currentGrid, 1);
            case West:
                return transformComponent.getPosition().x > 0 && tryHorizontal(currentGrid, -1);
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }

    private boolean tryVertical(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.getPosition().y + increment;
        Entity potentialTile = currentGrid.get((int) nextPosit).get((int) transformComponent.x());
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.getPosition().y = nextPosit;
        return canMove;
    }

    private boolean tryHorizontal(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.getPosition().x + increment;
        Entity potentialTile = currentGrid.get((int) transformComponent.y()).get((int) nextPosit);
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.getPosition().x = nextPosit;
        return canMove;
    }

    private boolean canStep(Entity potentialTile) {
        return potentialTile
                .getComponent(TileComponent.class)
                .getCurrentType() != Enums.TileTypes.BluePlayerTile;
    }


}
