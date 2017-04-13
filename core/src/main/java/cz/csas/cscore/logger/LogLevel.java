package cz.csas.cscore.logger;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/08/16.
 */
public enum LogLevel {

    /*
     * Log level DETAILED_DEBUG has all the messages with the detailed diagnostic information.
     * (f.e. the step by step outputs from the encryption algorithms)
     *
     * This value match the android log level {@link android.util.Log#VERBOSE}
     */
    DETAILED_DEBUG(1),
    /*
     * Log level DEBUG includes diagnostic information, payloads and responses of the HTTP
     * requests.
     *
     * This value match the android log level {@link android.util.Log#DEBUG}
     */
    DEBUG(2),
    /*
     * Log level INFO includes the informative messages. f.e. locker state changes
     * (unlock/lock/registration/unregistration), HTTP requests result, status and path.
     *
     * No sensitive data should be provided through messages with this log level.
     *
     * This value match the android log level {@link android.util.Log#INFO}
     */
    INFO(3),
    /*
     * Log level WARNING provides all the warning messages which are not critical for running
     * SDK/application.
     *
     * No sensitive data should be provided through messages with this log level.
     *
     * This value match the android log level {@link android.util.Log#WARN}
     */
    WARNING(4),
    /*
     * Log level ERROR includes all the exception messages which influence the proper working
     * of the SDK/application but are not fatal for running the SDK/application.
     *
     * No sensitive data should be provided through messages with this log level.
     *
     * This value match the android log level {@link android.util.Log#ERROR}
     */
    ERROR(5),

    /*
     * Log level FATAL includes the fatal error messages which don't allow the SDK/application
     * to run anymore. The result of message like this is usually the SDK/app crash.
     *
     * No sensitive data should be provided through messages with this log level.
     *
     * This value match the android log level {@link android.util.Log#ASSERT}
     */
    FATAL(6);

    public int value;

    LogLevel(int value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
