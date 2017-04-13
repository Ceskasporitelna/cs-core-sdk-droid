package cz.csas.cscore.judge;

import junit.framework.TestCase;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Judge Verdict.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class Verdict {

    @CsExpose
    private String callId;

    @CsExpose
    private String testCaseId;

    @CsExpose
    private String result;

    @CsExpose
    private TestCase testCase;

    @CsExpose
    private Request actualRequest;

    @CsExpose
    private List<Error> errors;

    /**
     * Gets call id.
     *
     * @return the call id
     */
    public String getCallId() {
        return callId;
    }

    /**
     * Gets test case id.
     *
     * @return the test case id
     */
    public String getTestCaseId() {
        return testCaseId;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * Gets test case.
     *
     * @return the test case
     */
    public TestCase getTestCase() {
        return testCase;
    }

    /**
     * Gets actual request.
     *
     * @return the actual request
     */
    public Request getActualRequest() {
        return actualRequest;
    }

    /**
     * Gets errors.
     *
     * @return the errors
     */
    public List<Error> getErrors() {
        return errors;
    }
}