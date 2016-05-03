package squares.handler.enemy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.character.CharacterEntity;
import squares.handler.ai.Align;
import squares.handler.ai.Approach;
import squares.handler.ai.Oscillate;
import squares.handler.ai.Steering;
import squares.utils.Enums;

import java.awt.*;
import java.util.Random;

/**
 * The AI enemy handler.
 */
public class SimpleMind extends EnemyHandler {
    private Point comfort = new Point(9, 4);
    private Point previous = null;
    private Random random = new Random();
    protected Behavior currentBehavior = Behavior.AverageBehavior;

    protected Steering oscillateBehavior;
    protected Steering alignBehavior;
    protected Steering approachBehavior;

    protected enum Behavior {
        AverageBehavior,
        Align,
        Approach,
        Stand
    }

    public SimpleMind(CharacterEntity playerCharacter, Array<Array<Entity>> gridField) {
        super(playerCharacter, gridField);
        previous = new Point((int) transformComponent.x, (int) transformComponent.y);
        oscillateBehavior = new Oscillate(this, gridField);
        alignBehavior = new Align(this, gridField);
        approachBehavior = new Approach(this, gridField);
    }

    @Override
    public Enums.UnitMovement movementPattern() {
        switch (currentBehavior) {
            case AverageBehavior:
                return oscillateBehavior.behave();
            case Align:
                return alignBehavior.behave();
            case Approach:
                return approachBehavior.behave();
            default:
                throw new AssertionError("Invalid current behavior: " + currentBehavior);
        }

    }

    @Override
    public boolean attackPattern() {
        if (hasSpells() && !comradeInLane()) {
            if (blasterTypes.contains(nextSpell().occupyEffect, true)) {
                shootForward();
                return true;
            } else if (nextSpell().occupyEffect == Enums.TileTypes.WideSwordOccupied) {
                cutForward();
                return true;
            } else if (nextSpell().occupyEffect == Enums.TileTypes.BoomerangOccupied) {
                throwBoomerang();
                return true;
            }
        }

        return false;
    }

    public final void updatePrev() {
        previous = new Point((int) transformComponent.x, (int) transformComponent.y);
    }

    public void getComfortable() {
        if (previous.y == transformComponent.y) {
            comfort.x = random.nextInt(5) + 5;
        }
    }

    public Point getComfort() {
        return comfort;
    }


    private void throwBoomerang() {
        if (playerInLane() || playerInTopLane() || playerInBetweenLanes()) {
            cast();
        }
    }

    private void cutForward() {
        if (playerInSSwordRange()) System.out.println("Is player in column" + playerInSSwordRange());

        if (playerInLane() && playerInSSwordRange()) {
            cast();
        }
    }

    protected void shootForward() {
        if (playerInLane()) {
            cast();
        }
    }

    public TransformComponent getTransformComponent() {
        return transformComponent;
    }

}
