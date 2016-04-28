package squares.components.spells;

import com.badlogic.ashley.core.Component;
import squares.components.Enums;

/**
 * A player-usable spell.
 */
public class Spell implements Component {
    public boolean isActive = false;
    public Enums.TileTypes occupyEffect;
    private float activeTime = 0;
    public float damage = 0;
    public float coolDown = .5f;

    public float iterate(float delta){
        activeTime += delta;

        if (activeTime > coolDown) {
            isActive = false;
            activeTime = 0;
        }

        return activeTime;
    }
}
