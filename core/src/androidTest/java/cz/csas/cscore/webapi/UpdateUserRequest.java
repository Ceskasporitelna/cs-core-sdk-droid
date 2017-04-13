package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Update user request.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class UpdateUserRequest extends CreateUserRequest {

    /**
     * The User id.
     */
    @CsExpose
    int userId;

    /**
     * Instantiates a new Update user request.
     *
     * @param name           the name
     * @param position       the position
     * @param fullProfileUrl the full profile url
     */
    public UpdateUserRequest(String name, String position, String fullProfileUrl) {
        super(name, position, fullProfileUrl);
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
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
