package cz.csas.cscore.logger;

import android.util.Log;

import static android.util.Log.ASSERT;

/**
 * The type Log manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public class LogManagerImpl implements LogManager {

    private static final int LOG_CHUNK_SIZE = 4000;

    private final String tag;
    private final LogLevel logLevel;

    /**
     * Instantiates a new Log manager.
     *
     * @param tag      the tag
     * @param logLevel the log level
     */
    public LogManagerImpl(String tag, LogLevel logLevel) {
        this.tag = tag;
        this.logLevel = logLevel;
    }

    @Override
    public void log(String message, LogLevel logLevel) {
        for (int i = 0, len = message.length(); i < len; i += LOG_CHUNK_SIZE) {
            int end = Math.min(len, i + LOG_CHUNK_SIZE);
            logChunk(message.substring(i, end), logLevel);
        }
    }

    /**
     * Called one or more times for each call to {@link #log(String, LogLevel)}. The length of {@code chunk}
     * will be no more than 4000 characters to support Android's {@link Log} class.
     */
    private void logChunk(String chunk, LogLevel logLevel) {
        if (this.logLevel.getValue() <= logLevel.getValue()) {
            switch (logLevel) {
                case DETAILED_DEBUG:
                    Log.v(getTag(), chunk);
                    break;
                case DEBUG:
                    Log.d(getTag(), chunk);
                    break;
                case INFO:
                    Log.i(getTag(), chunk);
                    break;
                case WARNING:
                    Log.w(getTag(), chunk);
                    break;
                case ERROR:
                    Log.e(getTag(), chunk);
                    break;
                case FATAL:
                    Log.println(ASSERT, getTag(), chunk);
                    break;
            }
        }
    }

    @Override
    public String getTag() {
        return tag;
    }


}
