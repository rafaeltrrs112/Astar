package squares.handler;

import com.badlogic.gdx.Input;
import squares.utils.Enums;

/**
 */
public class KeyMap {
    public final int UP;
    public final int DOWN;
    public final int LEFT;
    public final int RIGHT;
    public final int FIRE;
    public final Enums.TravelDirection DIRECTION;

    public KeyMap(int UP, int DOWN, int LEFT, int RIGHT, int FIRE, Enums.TravelDirection direction) {
        this.UP = UP;
        this.DOWN = DOWN;
        this.LEFT = LEFT;
        this.RIGHT = RIGHT;
        this.FIRE = FIRE;
        this.DIRECTION = direction;
    }

    public static KeyMap RED_DEFAULT() {
        return new KeyMap(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE, Enums.TravelDirection.RIGHT);
    }

    public static KeyMap BLUE_DEFAULT() {
        return new KeyMap(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.N, Enums.TravelDirection.LEFT);
    }
}
