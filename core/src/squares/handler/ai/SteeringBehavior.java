package squares.handler.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.handler.enemy.SimpleMind;

import java.util.Random;

/**
 */
abstract public class SteeringBehavior implements Steering {
    private final SimpleMind handlerDelegate;
    protected final Array<Array<Entity>> gridField;
    private final Random random = new Random();

    public SteeringBehavior(SimpleMind handler, Array<Array<Entity>> gridField) {
        this.handlerDelegate = handler;
        this.gridField = gridField;
    }

    public SimpleMind handlerDelegate(){
        return handlerDelegate;
    }

    public Random random(){
        return random;
    }

}
