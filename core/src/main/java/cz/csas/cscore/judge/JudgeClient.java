package cz.csas.cscore.judge;

import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.rest.CsRestAdapter;
import cz.csas.cscore.client.rest.converter.GsonConverter;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.CsJsonBuilder;

/**
 * The type Judge client. The testing client to communicate with judge util!
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public class JudgeClient {

    private RestService judgeRestService;

    /**
     * Instantiates a new Judge client.
     *
     * @param baseUrl the base url
     */
    public JudgeClient(String baseUrl, LogManager logManager) {

        CsJson csJson = new CsJsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        CsRestAdapter judgeAdapter = new CsRestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setLog(logManager)
                .setConverter(new GsonConverter(csJson))
                .build();

        judgeRestService = judgeAdapter.create(JudgeRestService.class);
    }

    public RestService getService() {
        return judgeRestService;
    }
}
