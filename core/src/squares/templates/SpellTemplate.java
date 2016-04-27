package squares.templates;

import com.badlogic.ashley.core.Entity;
import squares.components.TransformComponent;
import squares.components.spells.Spell;

/**
 */
public class SpellTemplate implements Template {

    @Override
    public Entity makeEntity() {
        Entity entity = new Entity();
        entity
                .add(new TransformComponent())
                .add(new Spell());
        return entity;
    }
}
