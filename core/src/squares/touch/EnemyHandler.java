package squares.touch;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.components.Enums;
import squares.components.TileComponent;

/**
 * The AI enemy handler.
 */
public class EnemyHandler extends UserInputHandler {
    private float timePassed = 0f;
    private Enums.UnitMovement movementDirection = Enums.UnitMovement.North;

    public EnemyHandler(CharacterEntity playerCharacter, Array<Array<Entity>> gridField) {
        super(playerCharacter, gridField);
    }

    @Override
    public void handle(float delta) {
        timePassed += delta;

        if (timePassed >= 1) {
            timePassed = 0;

            if (transformComponent.y == 4) {
                movementDirection = Enums.UnitMovement.South;
            }

            if (transformComponent.y == 0) {
                movementDirection = Enums.UnitMovement.North;
            }

            if (playerInLane()){
                playerCharacter.castSpell(Enums.TravelDirection.LEFT);
            }

            playerCharacter.Move(movementDirection, gridField);

            setTile();
        }
    }

    private boolean playerInLane() {
        Array<Entity> lane = gridField.get((int) transformComponent.y);
        for (Entity tile : lane) {
            TileComponent tileComp = tile.getComponent(TileComponent.class);
            if (tileComp.isOccupied() && tileComp.occupier.occupyType == Enums.TileTypes.GreenPlayerOccupied) {
                return true;
            }
        }
        return false;
    }

}
