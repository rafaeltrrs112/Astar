package squares.components;

import com.badlogic.ashley.core.Component;

/**
 *
 */
public class TagComponent implements Component {
    public String name;

    public TagComponent() {
        this("NO_NAME");
    }

    public TagComponent(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

}
