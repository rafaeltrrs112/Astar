package squares.stages;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import jdk.internal.util.xml.impl.Input;

/**
 */
public class MainStage extends Stage {
    private InputProcessor userInputHandler;

    public MainStage(InputProcessor userInputHandler, Viewport viewPort){
        super(viewPort);
        this.userInputHandler = userInputHandler;
    }

    public boolean keyDown(int keyCode) {
        boolean res = super.keyDown(keyCode);
        userInputHandler.keyDown(keyCode);
        return res;
    }
}
