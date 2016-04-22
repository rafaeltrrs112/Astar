package squares.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

//Apache commons pool for pooling components.
public class TransformComponent implements Component {
    private Vector2 position;

    public TransformComponent() {
        this(Vector2.Zero);
    }

    public TransformComponent(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float x() {
        return position.x;
    }

    public float y() {
        return position.y;
    }
}
