package squares.handler.enemy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.character.CharacterEntity;
import squares.utils.Enums;

/**
 * TODO: Make this mechanic shoot the player from afar and if the
 * player gets shocked the mechanic approaches the player and tries to stab the player.
 */
public class Mechanic extends SimpleMind {

    public Mechanic(CharacterEntity playerCharacter, Array<Array<Entity>> gridField) {
        super(playerCharacter, gridField);
        currentBehavior = Behavior.Approach;
    }

    @Override
    public Enums.UnitMovement movementPattern() {
        if (playerStunned()) {
            currentBehavior = Behavior.Approach;
        } else {
            getComfort().x = 9;
            currentBehavior = Behavior.AverageBehavior;
        }
        return super.movementPattern();
    }

}
