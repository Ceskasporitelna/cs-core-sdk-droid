package cz.csas.cscore.locker;

import java.util.Map;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.rest.CsRestAdapter;
import cz.csas.cscore.client.rest.RequestInterceptor;
import cz.csas.cscore.client.rest.RequestSignerImpl;
import cz.csas.cscore.client.rest.converter.GsonConverter;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;

/**
 * The type Locker client.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 10 /11/15.
 */
class LockerClient implements RestClient {

    private RestService mLockerRestService;
    private CsRestAdapter mLockerAdapter;
    private Map<String, String> mGlobalHeaders;

    /**
     * Instantiates a new Locker client.
     *
     * @param webApiConfiguration the config manager
     */
    public LockerClient(final WebApiConfiguration webApiConfiguration) {

        CsJson csJson = new CsJsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        mLockerAdapter = new CsRestAdapter.Builder()
                .setLog(CoreSDK.getInstance().getLogger())
                .setRequestSigner(new RequestSignerImpl(webApiConfiguration))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader(WEB_API_KEY_HEADER_NAME, webApiConfiguration.getWebApiKey());
                        request.addHeader(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE_JSON);
                        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);
                        request.addHeader(ACCEPT_LANGUAGE_HEADER_NAME, webApiConfiguration.getLanguage());
                        if (mGlobalHeaders != null) {
                            for (Map.Entry<String, String> entry : mGlobalHeaders.entrySet()) {
                                request.addHeader(entry.getKey(), entry.getValue());
                            }
                        }
                        if (webApiConfiguration.getPrivateSigningKey() != null)
                            request.signRequest();
                    }
                })
                .setEndpoint(webApiConfiguration.getEnvironment().getApiContextBaseUrl())
                .setConverter(new GsonConverter(csJson))
                .build();
        mLockerRestService = mLockerAdapter.create(LockerRestService.class);
    }

    @Override
    public RestService getService() {
        return mLockerRestService;
    }

    @Override
    public void setGlobalHeaders(Map<String, String> headers) {
        mGlobalHeaders = headers;
    }

    @Override
    public CsRestAdapter getRestAdapter() {
        return mLockerAdapter;
    }
}
