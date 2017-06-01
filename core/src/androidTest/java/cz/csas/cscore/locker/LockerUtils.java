package cz.csas.cscore.locker;

import android.support.test.InstrumentationRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.rest.CallbackBasic;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;

/**
 * The type Locker utils.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /12/15.
 */
public class LockerUtils {

    /**
     * Set locker.
     *
     * @param locker              the locker
     * @param xJudgesessionheader the x judgesessionheader
     * @param cryptoManager       the crypto manager
     */
    public static void setLocker(Locker locker, String xJudgesessionheader, CryptoManager cryptoManager){
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, xJudgesessionheader);
        ((LockerImpl) locker).setTestNonce(Constants.NONCE_TEST);
        ((LockerImpl) locker).setTestRandom(Constants.RANDOM_TEST);
        ((LockerImpl) locker).setTestNewRandom(Constants.NEW_RANDOM_TEST);
        ((LockerImpl) locker).setTestCode(Constants.CODE_TEST);
        ((LockerImpl) locker).setTestTime(Constants.ONE_TIME_PASSWORD_TIME_TEST);
        ((LockerImpl) locker).setTestDFP(Constants.DFP_TEST);
        ((LockerImpl) locker).setTestSEK(cryptoManager.decodeBase64(Constants.SEK_BASE64_TEST));
        ((LockerImpl) locker).getLockerClient().setGlobalHeaders(headers);
        ((LockerImpl) locker).setTestFlag();
    }
    /**
     * Locker register.
     *
     * @param locker the locker
     */
    public static void lockerRegister(Locker locker){

        final CountDownLatch registerSignal = new CountDownLatch(1);

        locker.register(InstrumentationRegistry.getTargetContext(), new CallbackBasic<LockerRegistrationProcess>() {
            @Override
            public void success(LockerRegistrationProcess lockerRegistrationProcess) {
                lockerRegistrationProcess.finishRegistration(new Password(LockType.FINGERPRINT, Constants.PASSWORD_TEST));
            }

            @Override
            public void failure() {
                registerSignal.countDown();
            }
        }, new CsCallback<RegistrationOrUnlockResponse>() {
            @Override
            public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                registerSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                registerSignal.countDown();
            }
        });

        locker.processUrl(Constants.CODE_URL_TEST);

        try {
            registerSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
