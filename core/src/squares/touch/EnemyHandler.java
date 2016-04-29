package squares.touch;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.character.CharacterEntity;
import squares.utils.Enums;
import squares.components.TileComponent;
import squares.components.spells.Spell;

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

            playerCharacter.Move(movementDirection, gridField);

            setTile();
        }

        if (timePassed >= .1f) {
            if (playerCharacter.getPlayerComponent().spells.size != 0) {
                if (playerCharacter.getPlayerComponent().spells.first().getComponent(Spell.class).occupyEffect == Enums.TileTypes.BlasterOneOccupied) {
                    shootForward();
                }

                if (playerCharacter.getPlayerComponent().spells.first().getComponent(Spell.class).occupyEffect == Enums.TileTypes.SwordOccupied) {
                    cutForward();
                }
            }
        }
    }


    private void cutForward() {
//        if(playerInLane()) System.out.println("Is player in lane" + playerInLane());
        if(playerInSwordRange()) System.out.println("Is player in column" + playerInSwordRange());

        if (playerInLane() && playerInSwordRange()) {
            playerCharacter.castSpell(Enums.TravelDirection.LEFT);
        }
    }

    private void shootForward() {
        if (playerInLane()) {
            playerCharacter.castSpell(Enums.TravelDirection.LEFT);
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

    private boolean playerInSwordRange() {
        int forwardColumn = (int) transformComponent.x - 1;
        for (Array<Entity> entityArray : gridField) {
            if (tileMapper.get(entityArray.get(forwardColumn)).isOccupied()) return true;
        }
        return false;
    }

}
