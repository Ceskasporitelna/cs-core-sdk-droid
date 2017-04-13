package cz.csas.cscore.locker;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.Environment;
import cz.csas.cscore.LockerTest;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06/12/15.
 */
public class InitializationEnvironmentHashTest extends LockerTest {

    private static final String X_JUDGE_CASE_HEADER_INIT_ENVIRONMENT_HASH = "core.locker.init.env.hash.session";


    @Before
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_CASE_HEADER_INIT_ENVIRONMENT_HASH;
        super.setUp();
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
    }

    @Test
    public void testInitializationNewUser() {
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientId(), Constants.CLIENT_ID_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientSecret(), Constants.CLIENT_SECRET_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getEnvironment(), new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getLanguage(), Constants.LANGUAGE_TEST);
        assertEquals(mCryptoManager.generatePublicKey(((LockerImpl) mLocker).getConfigManager().getPublicKey()), mCryptoManager.generatePublicKey(Constants.PUBLIC_KEY_TEST));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getWebApiKey(), Constants.WEB_API_KEY_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getRedirectUrl(), Constants.REDIRECT_URL_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getScope(), Constants.SCOPE_TEST);

        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);

        assertEquals(mLocker.getStatus().getState(), State.USER_UNLOCKED);

        // reinit locker and coreSDK
        LockerConfig lockerConfig = new LockerConfig.Builder().setClientId(Constants.SECOND_CLIENT_ID_TEST).setClientSecret(Constants.SECOND_CLIENT_SECRET_TEST).setPublicKey(Constants.PUBLIC_KEY_TEST).setRedirectUrl(Constants.REDIRECT_URL_TEST).setScope(Constants.SCOPE_TEST).create();
        CoreSDK.getInstance().useContext(InstrumentationRegistry.getTargetContext());
        CoreSDK.getInstance().useWebApiKey(Constants.WEB_API_KEY_TEST).useEnvironment(new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true)).useLocker(lockerConfig);
        mLocker = CoreSDK.getInstance().getLocker();

        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientId(), Constants.SECOND_CLIENT_ID_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientSecret(), Constants.SECOND_CLIENT_SECRET_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getEnvironment(), new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getLanguage(), Constants.LANGUAGE_TEST);
        assertEquals(mCryptoManager.generatePublicKey(((LockerImpl) mLocker).getConfigManager().getPublicKey()), mCryptoManager.generatePublicKey(Constants.PUBLIC_KEY_TEST));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getWebApiKey(), Constants.WEB_API_KEY_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getRedirectUrl(), Constants.REDIRECT_URL_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getScope(), Constants.SCOPE_TEST);
        assertEquals(mLocker.getStatus().getState(), State.USER_UNREGISTERED);
    }

    @Test
    public void testInitializationSameUser() {
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientId(), Constants.CLIENT_ID_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientSecret(), Constants.CLIENT_SECRET_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getEnvironment(), new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getLanguage(), Constants.LANGUAGE_TEST);
        assertEquals(mCryptoManager.generatePublicKey(((LockerImpl) mLocker).getConfigManager().getPublicKey()), mCryptoManager.generatePublicKey(Constants.PUBLIC_KEY_TEST));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getWebApiKey(), Constants.WEB_API_KEY_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getRedirectUrl(), Constants.REDIRECT_URL_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getScope(), Constants.SCOPE_TEST);

        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);

        assertEquals(mLocker.getStatus().getState(), State.USER_UNLOCKED);

        // reinit locker and coreSDK
        LockerConfig lockerConfig = new LockerConfig.Builder().setClientId(Constants.CLIENT_ID_TEST).setClientSecret(Constants.CLIENT_SECRET_TEST).setPublicKey(Constants.PUBLIC_KEY_TEST).setRedirectUrl(Constants.REDIRECT_URL_TEST).setScope(Constants.SCOPE_TEST).create();
        CoreSDK.getInstance().useContext(InstrumentationRegistry.getTargetContext());
        CoreSDK.getInstance().useWebApiKey(Constants.WEB_API_KEY_TEST).useEnvironment(new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true)).useLocker(lockerConfig);
        mLocker = CoreSDK.getInstance().getLocker();

        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientId(), Constants.CLIENT_ID_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientSecret(), Constants.CLIENT_SECRET_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getEnvironment(), new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getLanguage(), Constants.LANGUAGE_TEST);
        assertEquals(mCryptoManager.generatePublicKey(((LockerImpl) mLocker).getConfigManager().getPublicKey()), mCryptoManager.generatePublicKey(Constants.PUBLIC_KEY_TEST));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getWebApiKey(), Constants.WEB_API_KEY_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getRedirectUrl(), Constants.REDIRECT_URL_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getScope(), Constants.SCOPE_TEST);
        assertEquals(mLocker.getStatus().getState(), State.USER_LOCKED);
    }
}
