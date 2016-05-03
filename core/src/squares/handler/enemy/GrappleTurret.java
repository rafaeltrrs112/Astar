package squares.handler.enemy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.character.CharacterEntity;
import squares.utils.Enums;

/**
 * The grapple turret simple "AI" with a lower case i o.0.
 */
public class GrappleTurret extends SimpleMind {
    private Array<Enums.TileTypes> grappleTypes = Array.with(Enums.TileTypes.GrappleOccupied);

    public GrappleTurret(CharacterEntity playerCharacter, Array<Array<Entity>> gridField) {
        super(playerCharacter, gridField);
    }

    @Override
    public Enums.UnitMovement movementPattern() {
        return Enums.UnitMovement.Stay;
    }

    @Override
    public boolean attackPattern() {
        if (hasSpells() && !comradeInLane()) {
            if (grappleTypes.contains(nextSpell().occupyEffect, true)) {
                shootForward();
                return true;
            }
        }

        return false;
    }
}
