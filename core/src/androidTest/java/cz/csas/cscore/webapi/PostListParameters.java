package cz.csas.cscore.webapi;

import java.util.Map;

/**
 * The type Post list parameters.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 24 /03/16.
 */
public class PostListParameters extends Parameters {

    private final String PARAM_USER_UD = "userId";

    private Integer userId;

    /**
     * Instantiates a new Post list parameters.
     *
     * @param userId the user id
     */
    public PostListParameters(int userId) {
        this.userId = userId;
    }

    @Override
    public Map<String, String> toDictionary() {
        Map<String, String> dictionary = super.toDictionary();
        if(userId != null)
            dictionary.put(PARAM_USER_UD,userId.toString());
        return dictionary;
    }

}
