package squares.templates.generators;

import com.badlogic.ashley.core.Entity;

/**
 */
public interface SpellMaker {
    Entity makeSpell(int damage);
}
