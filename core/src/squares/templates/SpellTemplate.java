package squares.templates;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.components.spells.Spell;

/**
 */
public class SpellTemplate implements Template {

    @Override
    public Entity makeEntity() {
        Entity entity = new Entity();
        entity.add(new TransformComponent());
        entity.add(new Spell());
        return entity;
    }
}
