package cz.csas.cscore.judge;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Judge Error.
 */
/*
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class Error {

    @CsExpose
    private String type;

    @CsExpose
    private String message;

    @CsExpose
    private String expected;

    @CsExpose
    private String actual;

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets expected.
     *
     * @return the expected
     */
    public String getExpected() {
        return expected;
    }

    /**
     * Gets actual.
     *
     * @return the actual
     */
    public String getActual() {
        return actual;
    }
}