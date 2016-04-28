package squares.templates;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.components.PlayerComponent;

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
