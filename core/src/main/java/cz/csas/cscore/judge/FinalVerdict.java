package cz.csas.cscore.judge;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Judge Final verdict.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class FinalVerdict {

    @CsExpose
    private Stats stats;

    @CsExpose
    private List<TestCase> cases;

    /**
     * Gets stats.
     *
     * @return the stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * Gets cases.
     *
     * @return the cases
     */
    public List<TestCase> getCases() {
        return cases;
    }
}
