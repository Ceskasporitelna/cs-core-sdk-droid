package cz.csas.cscore.webapi;

import java.util.List;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;

/**
 * The type Notes paginated list response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 30 /03/16.
 */
public class NotesPaginatedListResponse extends PaginatedListOfPrimitivesResponse<String, NotesPaginatedListResponse> {

    /**
     * The Items.
     */
    @CsExpose
    @CsSerializedName(value = "notes",alternate = "items")
    public List<String> notes;

    /**
     * Instantiates a new Notes paginated list response.
     *
     * @param notes the items
     */
    public NotesPaginatedListResponse(List<String> notes) {
        this.notes = notes;
    }

    @Override
    protected List<String> getItems() {
        return notes;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public List<String> getNotes() {
        return getItems();
    }
}
