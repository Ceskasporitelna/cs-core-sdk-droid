package cz.csas.cscore.webapi;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;

/**
 * The type Post list response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 27 /12/15.
 */
public class PostListResponse extends ListResponse<Post> {

    @CsExpose
    @CsSerializedName(value = "posts", alternate = "items")
    public List<Post> posts;

    /**
     * Instantiates a new Post list response.
     *
     * @param posts the data
     */
    public PostListResponse(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    protected List<Post> getItems() {
        return posts;
    }

    /**
     * Get posts list.
     *
     * @return the list
     */
    public List<Post> getPosts(){
        return getItems();
    }
}
