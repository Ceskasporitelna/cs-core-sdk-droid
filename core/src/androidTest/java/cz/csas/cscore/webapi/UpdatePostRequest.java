package cz.csas.cscore.webapi;

/**
 * The type Update post request.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 01 /09/16.
 */
public class UpdatePostRequest extends CreatePostRequest {
    /**
     * Instantiates a new Create post request.
     *
     * @param userId the user id
     * @param id     the id
     * @param title  the title
     * @param body   the body
     */
    public UpdatePostRequest(Integer userId, Integer id, String title, String body) {
        super(userId, id, title, body);
    }
}
