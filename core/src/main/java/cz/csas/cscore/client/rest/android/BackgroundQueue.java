package cz.csas.cscore.client.rest.android;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The type Background queue.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 16 /03/2017.
 */
public class BackgroundQueue extends HandlerThread {

    private final Object lock = new Object();
    private Queue<Runnable> mQueue = new LinkedList<>();
    private Handler mHandler;
    private boolean mIsAvailable = true;


    /**
     * Instantiates a new Background mQueue.
     *
     * @param name the name
     */
    public BackgroundQueue(String name) {
        super(name);
        start();
        mHandler = new Handler(this.getLooper());
    }

    /**
     * Add runnable to queue.
     *
     * @param runnable the runnable
     */
    public void addToQueue(Runnable runnable) {
        mQueue.add(runnable);
        runTopElement();
    }

    /**
     * On queue available let the queue know to start another operation.
     */
    public void onQueueAvailable() {
        mIsAvailable = true;
        runTopElement();
    }

    private void runTopElement() {
        synchronized (lock) {
            Runnable runnable = mQueue.peek();
            if (runnable != null && mIsAvailable) {
                mIsAvailable = false;
                mHandler.post(runnable);
                mQueue.remove();
            }
        }
    }
}