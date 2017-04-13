package cz.csas.cscore.client.rest.android;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Executor;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04/01/16.
 */
public class BackgroundThreadExecutor implements Executor {
    private final HandlerThread handlerThread = new HandlerThread("BackgroundThread");
    private Handler handler;

    @Override public void execute(Runnable r) {
        if(!handlerThread.isAlive())
            handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(r);
    }
}
