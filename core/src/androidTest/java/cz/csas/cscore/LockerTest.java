package cz.csas.cscore;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.crypto.CryptoManagerImpl;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeClient;
import cz.csas.cscore.locker.Locker;
import cz.csas.cscore.locker.LockerConfig;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManagerImpl;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 05/12/15.
 */
@RunWith(AndroidJUnit4.class)
public abstract class LockerTest {

    protected String mXJudgeSessionHeader;
    protected JudgeClient mJudgeClient;
    protected CryptoManager mCryptoManager;
    protected CoreSDK mCoreSDK;
    protected Locker mLocker;

    @Before
    public void setUp() {
        mJudgeClient = new JudgeClient(Constants.TEST_BASE_URL);
        mCryptoManager = new CryptoManagerImpl();

        LockerConfig lockerConfig = new LockerConfig.Builder().setClientId(Constants.CLIENT_ID_TEST).setClientSecret(Constants.CLIENT_SECRET_TEST).setPublicKey(Constants.PUBLIC_KEY_TEST).setRedirectUrl(Constants.REDIRECT_URL_TEST).setScope(Constants.SCOPE_TEST).create();
        mCoreSDK = CoreSDK.getInstance();
        mCoreSDK.useContext(InstrumentationRegistry.getTargetContext());
        ((CoreSDKImpl) mCoreSDK).wipeKeychain().useWebApiKey(Constants.WEB_API_KEY_TEST).useEnvironment(new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true)).useLogger(new LogManagerImpl("TEST", LogLevel.DETAILED_DEBUG)).useLocker(lockerConfig);
        mLocker = mCoreSDK.getLocker();

        Map<String, String> headers = new HashMap<>();
        headers.put(cz.csas.cscore.judge.Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        ((CoreSDKImpl) mCoreSDK).setTestHeaders(headers);
        ((CoreSDKImpl) mCoreSDK).setTestRedirectUri(Constants.REDIRECT_URL_TEST_REFRESH_TOKEN);
        ((CoreSDKImpl) mCoreSDK).setTestCurrentTime(Constants.TIME_TEST);
    }
}
