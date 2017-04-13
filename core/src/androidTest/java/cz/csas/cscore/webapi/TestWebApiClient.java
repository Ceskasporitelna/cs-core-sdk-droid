package cz.csas.cscore.webapi;

import cz.csas.cscore.client.WebApiConfiguration;

/**
 * The type Web api client test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public class TestWebApiClient extends WebApiClient {

    private PostsResource postsResource;
    private ContactsResource contactsResource;
    private UsersResource usersResource;
    private UsersQueueResource usersQueueResource;
    private NotesResource notesResource;
    private FileResource fileResource;

    /**
     * Instantiates a new Web api client test.
     *
     * @param webApiConfiguration the cs configuration
     */
    public TestWebApiClient(WebApiConfiguration webApiConfiguration) {
        super(webApiConfiguration);
        postsResource = new PostsResource("posts", this);
        contactsResource = new ContactsResource("contacts/personal", this);
        usersResource = new UsersResource("users", this);
        usersQueueResource = new UsersQueueResource("users/queue", this);
        notesResource = new NotesResource("notes", this);
        fileResource = new FileResource("file", this);
    }

    @Override
    protected String getClientPath() {
        return "";
    }

    /**
     * Gets posts resource.
     *
     * @return the posts resource
     */
    public PostsResource getPostsResource() {
        return postsResource;
    }

    /**
     * Gets contacts resource.
     *
     * @return the contacts resource
     */
    public ContactsResource getContactsResource() {
        return contactsResource;
    }

    /**
     * Gets users resource.
     *
     * @return the users resource
     */
    public UsersResource getUsersResource() {
        return usersResource;
    }

    /**
     * Get users queue resource users queue resource.
     *
     * @return the users queue resource
     */
    public UsersQueueResource getUsersQueueResource() {
        return usersQueueResource;
    }

    /**
     * Gets notes resource.
     *
     * @return the notes resource
     */
    public NotesResource getNotesResource() {
        return notesResource;
    }

    /**
     * Gets file resource.
     *
     * @return the file resource
     */
    public FileResource getFileResource() {
        return fileResource;
    }
}
