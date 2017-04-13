package cz.csas.cscore.webapi;

import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.apiquery.CreateEnabled;

/**
 * The type File resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /12/15.
 */
public class FileResource extends Resource implements CreateEnabled<CreateFileRequest, UploadedFile> {
    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public FileResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public void create(CreateFileRequest request, CallbackWebApi<UploadedFile> callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Disposition", "attachment; filename=\"" + request.getFilename() + "\"");
        headers.put("Content-Type", "application/octet-stream");
        ResourceUtils.callUpload(this, request.getFileData(), headers, null, UploadedFile.class, callback);
    }

    /**
     * Upload.
     *
     * @param request
     * @param callback
     */
    public void upload(CreateFileRequest request, CallbackWebApi<UploadedFile> callback) {
        create(request, callback);
    }

    public void downloadPost(DownloadFileParameters parameters, DownloadFileRequest request, CallbackWebApi<WebApiStream> callback) {
        ResourceUtils.callDownload(Method.POST, this, "export", parameters, request, null, null, callback);
    }

    public void downloadGet(DownloadFileParameters parameters, DownloadFileRequest request, CallbackWebApi<WebApiStream> callback) {
        ResourceUtils.callDownload(Method.GET, this, "export", parameters, request, null, null, callback);
    }
}
