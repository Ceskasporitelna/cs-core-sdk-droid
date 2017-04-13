package cz.csas.cscore.webapi;

import java.io.File;

/**
 * The type Create file request.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 29 /12/15.
 */
public class CreateFileRequest extends WebApiRequest {

    private File fileData;

    private String filename;

    /**
     * Instantiates a new Create file request.
     *
     * @param fileData the file data
     * @param filename the filename
     */
    public CreateFileRequest(File fileData, String filename) {
        this.fileData = fileData;
        this.filename = filename;
    }

    /**
     * Gets file data.
     *
     * @return the file data
     */
    public File getFileData() {
        return fileData;
    }

    /**
     * Gets filename.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
}
