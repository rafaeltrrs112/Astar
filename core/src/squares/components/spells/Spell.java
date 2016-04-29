package squares.components.spells;

import com.badlogic.ashley.core.Component;
import squares.utils.Enums.TileTypes;
import squares.utils.Enums.TravelDirection;

/**
 * A player-usable spell.
 */
public class Spell implements Component {
    public boolean isActive = false;
    public boolean exhausted;

    public TileTypes occupyEffect;
    private float activeTime = 0;
    public float damage = 0;
    public float coolDown = .25f;
    public TravelDirection direction = TravelDirection.RIGHT;

    public void deActivate(){
        activeTime = 0;
        isActive = false;
    }
    public float iterate(float delta){
        activeTime += delta;

        if (activeTime > coolDown) {
            isActive = false;
            activeTime = 0;
        }

        return activeTime;
    }
}
