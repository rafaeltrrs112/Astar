package squares.templates;

import com.badlogic.ashley.core.Entity;
import squares.components.PlayerComponent;
import squares.components.TransformComponent;

/**
 */
public class PlayerTemplate implements Template {
    @Override
    public Entity makeEntity() {
        Entity entity = new Entity();
        entity.add(new TransformComponent());
        entity.add(new PlayerComponent());
        return entity;
    }
}
