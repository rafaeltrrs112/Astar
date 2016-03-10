package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.AStarGame;
import com.mygdx.game.parse.ConfigParser;
import com.mygdx.game.systems.GridConfig;

public class DesktopLauncher {
    public static void main(String[] arg) {
        runGame();
    }

    public static void runGame() {
        ConfigParser.INSTANCE.parseSettings();
//        IndexedAStarPathFinderMain.searchNodePath_WhenSearchCanHitDeadEnds_ExpectedOuputPathFound();
        GridConfig config = new GridConfig(80f, 80f, 2f, 1440f, 1440f, null);
        LwjglApplicationConfiguration appConfig = new LwjglApplicationConfiguration();
        appConfig.width = (int) config.getGridWidth();
        appConfig.height = (int) config.getGridHeight();
        new LwjglApplication(new AStarGame(config), appConfig);

    }
}
