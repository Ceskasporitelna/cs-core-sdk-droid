package cz.csas.cscore;

import java.util.Map;

import cz.csas.cscore.client.RestClient;
import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.rest.CsRestAdapter;
import cz.csas.cscore.client.rest.RequestInterceptor;
import cz.csas.cscore.client.rest.converter.GsonConverter;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;

/**
 * The type Core client.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /11/15.
 */
class CoreClient implements RestClient {

    private RestService coreRestService;
    private CsRestAdapter mCoreAdapter;
    private Map<String, String> mGlobalHeaders;

    /**
     * Instantiates a new Core client.
     *
     * @param webApiConfiguration the web api configuration
     */
    public CoreClient(final WebApiConfiguration webApiConfiguration) {

        CsJson csJson = new CsJsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        mCoreAdapter = new CsRestAdapter.Builder()
                .setLog(CoreSDK.getInstance().getLogger())
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader(WEB_API_KEY_HEADER_NAME, webApiConfiguration.getWebApiKey());
                        request.addHeader(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_HEADER_VALUE_WWW_URL_ENCODED);
                        request.addHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);
                        request.addHeader(ACCEPT_LANGUAGE_HEADER_NAME, webApiConfiguration.getLanguage());
                        if (mGlobalHeaders != null) {
                            for (Map.Entry<String, String> entry : mGlobalHeaders.entrySet()) {
                                request.addHeader(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                })
                .setEndpoint(webApiConfiguration.getEnvironment().getOAuth2ContextBaseUrl())
                .setConverter(new GsonConverter(csJson))
                .build();
        coreRestService = mCoreAdapter.create(CoreRestService.class);
    }

    @Override
    public RestService getService() {
        return coreRestService;
    }

    @Override
    public void setGlobalHeaders(Map<String, String> headers) {
        mGlobalHeaders = headers;
    }

    @Override
    public CsRestAdapter getRestAdapter() {
        return mCoreAdapter;
    }
}
