package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import squares.SquaresGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        runGame();
    }

    public static void runGame() {
        LwjglApplicationConfiguration appConfig = new LwjglApplicationConfiguration();

        appConfig.height = 1080;
        appConfig.width = 1920;

        new LwjglApplication(new SquaresGame(), appConfig);

    }
}
