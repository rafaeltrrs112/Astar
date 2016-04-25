package squares.utils;

import com.badlogic.ashley.core.Entity;

/**
 * Tagged Entity class, may be used later
 */
public class TaggedEntity extends Entity {
    private final String tag;

    public TaggedEntity(String _tag) {
        tag = _tag;
    }
}
