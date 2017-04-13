package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * The type Poll provides endpoint to retrieve state of the sign process (typically if is sign done
 * by MOBILE_CASE).
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
public class Poll extends WebApiEntity {

    @CsExpose
    private String url;

    @CsExpose
    private String id;

    @CsExpose
    private Long interval;

    /**
     * Get poll url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get poll id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Get poll interval.
     *
     * @return the interval
     */
    public Long getInterval() {
        return interval;
    }
}
