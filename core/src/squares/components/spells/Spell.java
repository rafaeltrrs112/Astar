package squares.components.spells;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import squares.utils.Enums.TileTypes;
import squares.utils.Enums.TravelDirection;

/**
 * A player-usable spell.
 */
public class Spell implements Component {
    public boolean isActive = false;
    public boolean exhausted;
    public Sprite sprite;
    public TileTypes occupyEffect;
    private float activeTime = 0;
    public float damage = 0;
    public float coolDown = .25f;
    private TravelDirection currentDirection = TravelDirection.RIGHT;
    private TravelDirection originalDirection = null;

    public void deActivate() {
        activeTime = 0;
        isActive = false;
    }

    public float iterate(float delta) {
        activeTime += delta;

        if (activeTime > coolDown) {
            isActive = false;
            activeTime = 0;
        }

        return activeTime;
    }

    public boolean iterateTravel(float delta) {
        activeTime += delta;
        boolean canTravel = activeTime > coolDown;

        if (canTravel) {
            activeTime = 0;
        }

        return canTravel;
    }

    public void setOriginalDirection(TravelDirection direction){
        this.originalDirection = direction;
    }

    public TravelDirection getOriginalDirection(){
        return originalDirection;
    }

    public void setDirection(TravelDirection direction){
        this.currentDirection = direction;
    }

    public TravelDirection getDirection() {
        return currentDirection;
    }


}
