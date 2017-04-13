package cz.csas.cscore.webapi;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import cz.csas.cscore.Environment;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeClient;

/**
 * The type Web api client test.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
@RunWith(AndroidJUnit4.class)
public abstract class WebApiClientTest {

    private final String WEBAPI_SESSION_HEADER = "webapi-session-header";
    /**
     * The M x judge session header.
     */
    protected String mXJudgeSessionHeader;
    /**
     * The M cs configuration.
     */
    protected WebApiConfiguration mWebApiConfiguration;
    /**
     * The M judge client.
     */
    protected JudgeClient mJudgeClient;
    /**
     * The M test web api client.
     */
    protected TestWebApiClient mTestWebApiClient;


    /**
     * Set up.
     */
    @Before
    public void setUp() {
        mWebApiConfiguration = new WebApiConfigurationImpl("TEST_API_KEY", new Environment(Constants.TEST_BASE_URL, Constants.TEST_BASE_URL_OAUTH, false), "cs-CZ", null);
        mXJudgeSessionHeader = WEBAPI_SESSION_HEADER;//UUID.randomUUID().toString();
        mJudgeClient = new JudgeClient(Constants.TEST_BASE_URL);
        mTestWebApiClient = new TestWebApiClient(mWebApiConfiguration);
    }
}
