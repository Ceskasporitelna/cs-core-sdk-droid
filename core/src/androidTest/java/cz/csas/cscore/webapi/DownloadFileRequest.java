package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Download file request.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 28 /04/16.
 */
public class DownloadFileRequest extends WebApiRequest {

    @CsExpose
    private String receiverName;

    @CsExpose
    private String senderName;

    /**
     * Instantiates a new Download file request.
     *
     * @param receiverName the receiver name
     * @param senderName   the sender name
     */
    public DownloadFileRequest(String receiverName, String senderName) {
        this.receiverName = receiverName;
        this.senderName = senderName;
    }

    /**
     * Gets receiver name.
     *
     * @return the receiver name
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * Gets sender name.
     *
     * @return the sender name
     */
    public String getSenderName() {
        return senderName;
    }
}
