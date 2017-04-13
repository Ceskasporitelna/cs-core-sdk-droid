package cz.csas.cscore.webapi;

import java.io.InputStream;

/**
 * Streamed WebApi response. Used for representing file downloads and other binary data sent from server
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /04/16.
 */
public class WebApiStream extends WebApiEntity {

    private String filename;
    private String contentType;
    private Long contentLength;
    private String contentDisposition;
    private InputStream inputStream;

    /**
     * Instantiates a new Web api stream.
     *
     * @param filename           name of the downloaded file parsed from the Content-Disposition
     *                           header
     * @param contentType        the content type value from Content-Type header receiver
     * @param contentLength      the content length value from Content-Size header receiver
     * @param contentDisposition the content disposition value from Content-Disposition header
     * @param inputStream        the input stream containing the received file and dispatching it as
     *                           a stream
     */
    public WebApiStream(String filename, String contentType, Long contentLength, String contentDisposition, InputStream inputStream) {
        this.filename = filename;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentDisposition = contentDisposition;
        this.inputStream = inputStream;
    }

    /**
     * Get filename.
     *
     * @return the name of the file. May be null if the entity being streamed is not considered a file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get content size. May be null for streams of undetermined length
     *
     * @return the content size
     */
    public Long getContentLength() {
        return contentLength;
    }

    /**
     * Get content disposition. May be null.
     *
     * @return the content disposition
     */
    public String getContentDisposition() {
        return contentDisposition;
    }

    /**
     * Get input stream.
     *
     * @return the input stream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Get content type. May be null for streams without defined content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }
}
