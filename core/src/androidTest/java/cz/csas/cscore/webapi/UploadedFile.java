package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;

/**
 * The type Uploaded file.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /12/15.
 */
public class UploadedFile extends WebApiEntity{

    /**
     * The File name.
     */
    @CsExpose
    @CsSerializedName("file_name")
    String fileName;

    /**
     * The Id.
     */
    @CsExpose
    String id;

    /**
     * The Size.
     */
    @CsExpose
    int size;

    /**
     * The Content type.
     */
    @CsExpose
    @CsSerializedName("content_type")
    String contentType;

    /**
     * The Status.
     */
    @CsExpose
    String status;

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    public boolean isOk(){
        return getStatus().equals("OK");
    }
}
