package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.webapi.apiquery.CsSignUrl;
import cz.csas.cscore.webapi.signing.Signable;
import cz.csas.cscore.webapi.signing.SigningObject;

/**
 * The type Post.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
@CsSignUrl()
public class Post extends WebApiEntity implements Signable {

    @CsExpose
    private int userId;

    @CsExpose
    private int id;

    @CsExpose
    private String title;

    @CsExpose
    private String body;

    private SigningObject signingObject;

    /**
     * Instantiates a new Post.
     *
     * @param userId the user id
     * @param id     the id
     * @param title  the title
     * @param body   the body
     */
    public Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    @Override
    public SigningObject signing() {
        return signingObject;
    }

    @Override
    public void setSigningObject(SigningObject signingObject) {
        this.signingObject = signingObject;
    }
}
