package cz.csas.cscore.locker;

import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.Environment;
import cz.csas.cscore.LockerTest;
import cz.csas.cscore.judge.Constants;

import static junit.framework.Assert.assertEquals;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06/12/15.
 */
public class CoreSDKAndLockerInitializationTest extends LockerTest {

    private final String X_JUDGE_SESSION_HEADER_LOCKER_INIT = "core.locker.locker.init.session";

    @Before
    public void setUp(){
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_LOCKER_INIT;
        super.setUp();
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
    }

    @Test
    public void testInitialization(){
        Assert.assertEquals(((LockerImpl) mLocker).getConfigManager().getClientId(), Constants.CLIENT_ID_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientSecret(), Constants.CLIENT_SECRET_TEST);
        Assert.assertEquals(((LockerImpl) mLocker).getConfigManager().getEnvironment(), new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getLanguage(),Constants.LANGUAGE_TEST);
        assertEquals(mCryptoManager.generatePublicKey(((LockerImpl) mLocker).getConfigManager().getPublicKey()), mCryptoManager.generatePublicKey(Constants.PUBLIC_KEY_TEST));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getWebApiKey(),Constants.WEB_API_KEY_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getRedirectUrl(), Constants.REDIRECT_URL_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getScope(),Constants.SCOPE_TEST);

        // reinit locker and coreSDK
        LockerConfig lockerConfig = new LockerConfig.Builder().setClientId(Constants.CLIENT_ID_TEST).setClientSecret(Constants.CLIENT_SECRET_TEST).setPublicKey(Constants.PUBLIC_KEY_TEST).setRedirectUrl(Constants.REDIRECT_URL_TEST).setScope(Constants.SCOPE_TEST).create();
        CoreSDK.getInstance().useContext(InstrumentationRegistry.getTargetContext());
        CoreSDK.getInstance().useWebApiKey(Constants.WEB_API_KEY_TEST).useEnvironment(new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH,true)).useLocker(lockerConfig);
        mLocker = CoreSDK.getInstance().getLocker();

        Assert.assertEquals(((LockerImpl) mLocker).getConfigManager().getClientId(), Constants.CLIENT_ID_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getClientSecret(), Constants.CLIENT_SECRET_TEST);
        Assert.assertEquals(((LockerImpl) mLocker).getConfigManager().getEnvironment(), new Environment(Constants.TEST_WEBAPI_BASE_URL, Constants.TEST_BASE_URL_OAUTH, true));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getLanguage(),Constants.LANGUAGE_TEST);
        assertEquals(mCryptoManager.generatePublicKey(((LockerImpl) mLocker).getConfigManager().getPublicKey()), mCryptoManager.generatePublicKey(Constants.PUBLIC_KEY_TEST));
        assertEquals(CoreSDK.getInstance().getWebApiConfiguration().getWebApiKey(), Constants.WEB_API_KEY_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getRedirectUrl(), Constants.REDIRECT_URL_TEST);
        assertEquals(((LockerImpl) mLocker).getConfigManager().getScope(),Constants.SCOPE_TEST);
    }
}
