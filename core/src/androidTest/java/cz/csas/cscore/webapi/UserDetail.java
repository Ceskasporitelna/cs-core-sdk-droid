package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;

/**
 * The type User detail.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class UserDetail extends WebApiEntity{

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
     * The Position.
     */
    @CsExpose
    String position;

    /**
     * The Full profile url.
     */
    @CsExpose
    @CsSerializedName("full_profile_url")
    String fullProfileUrl;


    /**
     * Instantiates a new User detail.
     *
     * @param userId         the user id
     * @param name           the name
     * @param position       the position
     * @param fullProfileUrl the full profile url
     */
    public UserDetail(int userId, String name, String position, String fullProfileUrl) {
        this.userId = userId;
        this.name = name;
        this.position = position;
        this.fullProfileUrl = fullProfileUrl;
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
     * Gets position.
     *
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Gets full profile url.
     *
     * @return the full profile url
     */
    public String getFullProfileUrl() {
        return fullProfileUrl;
    }
}
