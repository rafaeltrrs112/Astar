package squares.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import squares.utils.Enums.TileTypes;

public class PlayerComponent implements Component {
    public Array<TileTypes> allergies = Array.with();
    public Queue<Entity> spells = new Queue<Entity>();
    public int health = 250;

    public boolean allergic(TileTypes type) {
        return !allergies.contains(type, true);
    }

    public TileTypes occupyType;
}
