package cz.csas.cscore.logger;

/**
 * The interface Log manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12 /11/15.
 */
public interface LogManager {

    /**
     * Log.
     *
     * @param message  the message
     * @param logLevel the log level
     */
    public void log(String message, LogLevel logLevel);

    /**
     * Gets tag.
     *
     * @return the tag
     */
    public String getTag();
}
