package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.webapi.apiquery.GetEnabled;

/**
 * The type User.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class User extends WebApiEntity implements GetEnabled<UserDetail> {

    /**
     * The User id.
     */
    @CsExpose
    int userId;

    /**
     * The Name.
     */
    @CsExpose
    String name;

    /**
     * Instantiates a new User.
     *
     * @param userId the user id
     * @param name   the name
     */
    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void get(CallbackWebApi<UserDetail> callback) {
        ((UsersResource)resource).withId(getUserId()).get(callback);
    }
}
