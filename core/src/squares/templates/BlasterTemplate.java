package squares.templates;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import squares.components.spells.Blaster;
import squares.components.TransformComponent;

/**
 * Template for creating a blaster entity.
 */
public class BlasterTemplate implements Template {
    @Override
    public Entity makeEntity() {
        Entity entity = new Entity();
        Blaster blaster = new Blaster();
        blaster.isActive = false;
        entity
                .add(new TransformComponent(new Vector2(0, 0)))
                .add(blaster);
        return entity;
    }
}
