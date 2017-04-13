package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Create post request.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class CreatePostRequest extends WebApiRequest {

    /**
     * The User id.
     */
    @CsExpose
    Integer userId;

    /**
     * The Id.
     */
    @CsExpose
    Integer id;

    /**
     * The Title.
     */
    @CsExpose
    String title;

    /**
     * The Body.
     */
    @CsExpose
    String body;

    /**
     * Instantiates a new Create post request.
     *
     * @param userId the user id
     * @param id     the id
     * @param title  the title
     * @param body   the body
     */
    public CreatePostRequest(Integer userId, Integer id, String title, String body) {
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
    public Integer getUserId() {
        return userId;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
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
}
