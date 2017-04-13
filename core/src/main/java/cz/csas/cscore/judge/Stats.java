package cz.csas.cscore.judge;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Stats.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class Stats {

    @CsExpose
    private Integer numberOfCases;

    @CsExpose
    private Integer successfulCases;

    @CsExpose
    private Integer failedCases;

    @CsExpose
    private Integer notCalledCases;

    /**
     * Gets number of cases.
     *
     * @return the number of cases
     */
    public Integer getNumberOfCases() {
        return numberOfCases;
    }

    /**
     * Gets successful cases.
     *
     * @return the successful cases
     */
    public Integer getSuccessfulCases() {
        return successfulCases;
    }

    /**
     * Gets failed cases.
     *
     * @return the failed cases
     */
    public Integer getFailedCases() {
        return failedCases;
    }

    /**
     * Gets not called cases.
     *
     * @return the not called cases
     */
    public Integer getNotCalledCases() {
        return notCalledCases;
    }
}
