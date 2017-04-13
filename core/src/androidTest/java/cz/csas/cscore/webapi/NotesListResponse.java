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
public class NotesListResponse extends ListOfPrimitivesResponse<String> {

    /**
     * The Items.
     */
    @CsExpose
    @CsSerializedName(value = "notes",alternate = "items")
    public List<String> notes;

    /**
     * Instantiates a new Notes list response.
     *
     * @param notes the items
     */
    public NotesListResponse(List<String> notes) {
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
