package cz.csas.cscore.webapi;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;

/**
 * The type User list response.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 28 /12/15.
 */
public class UserQueueListResponse extends ListResponse<User> {

    /**
     * The Users.
     */
    @CsExpose
    @CsSerializedName(value = "users", alternate = "items")
    List<User> users;

    @Override
    protected List<User> getItems() {
        return users;
    }

    /**
     * Get users list.
     *
     * @return the list
     */
    public List<User> getUsers(){
        return getItems();
    }
}
