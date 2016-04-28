package squares.stages;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import squares.touch.CharacterHandler;
import squares.touch.EnemyHandler;

/**
 */
public class MainStage extends Stage {
    private Array<CharacterHandler> handlers;

    public MainStage(Array<CharacterHandler> handlers, Viewport viewPort) {
        super(viewPort);
        this.handlers = handlers;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (CharacterHandler handler : handlers) {
            handler.handle(delta);
        }
    }

}
