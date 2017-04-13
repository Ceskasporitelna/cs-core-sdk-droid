package cz.csas.cscore.locker;

import cz.csas.cscore.webapi.apiquery.CsSignUrl;

/**
 * The type Sign url example.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 30 /03/16.
 */
@CsSignUrl(signUrl = "/../resourceSpec/")
public class SignUrlExample {

    private final String ID = "id";
    private final String SIGN_ID = "signId";
    private String basePath;

    /**
     * Instantiates a new Sign url example.
     *
     * @param basePath the base path
     */
    public SignUrlExample(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Gets base path.
     *
     * @return the base path
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets base path.
     *
     * @param basePath the base path
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Get sign url path string.
     *
     * @return the string
     */
    public String getSignUrlPath(){
        return ID+"/sign/"+SIGN_ID;
    }

}
