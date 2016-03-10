package com.mygdx.game;

import android.os.Handler;
import android.os.Looper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class DefaultExecutors {

    static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Submits things on the UI Thread.
     */
    static Executor UiExecutor = new Executor() {
        @Override
        public void execute(@NotNull Runnable command) {
            handler.post(command);
        }
    };

}

