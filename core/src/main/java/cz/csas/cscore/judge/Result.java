package cz.csas.cscore.judge;

/**
 * The enum Result.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public enum Result {

    /**
     * Ok result.
     */
    OK("OK"),

    /**
     * Error result.
     */
    ERROR("Error"),

    /**
     * Not called result.
     */
    NOT_CALLED("NotCalled");

    private String result;

    /**
     * Instantiates a new Result.
     *
     * @param result the result
     */
    Result(String result) {
        this.result = result;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }
}
