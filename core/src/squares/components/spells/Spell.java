package squares.components.spells;

import com.badlogic.ashley.core.Component;

/**
 * A player-usable spell.
 */
public abstract class Spell implements Component {
    public boolean isActive;
}
