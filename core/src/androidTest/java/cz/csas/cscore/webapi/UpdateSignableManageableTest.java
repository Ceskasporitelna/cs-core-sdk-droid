package cz.csas.cscore.webapi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.cscore.webapi.signing.TacSigningProcess;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * The type Update signable managable test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 04 /05/16.
 */
public class UpdateSignableManageableTest extends WebApiClientTest {

    private final String X_JUDGE_CASE_CONTACTS_CREATE_SIGNABLE = "webapi.contacts.update.signable.manageable";
    private CountDownLatch mUpdateSignal;
    private CountDownLatch mSigningSignalGET;
    private CountDownLatch mSigningSignalPOST;
    private CountDownLatch mSigningSignalPUT;
    private UpdateContactResponse mUpdateContactResponse;
    private SigningObject mSigningObject;
    private FilledSigningObject mFilledSigningObject;
    private TacSigningProcess mTacSigningProcess;

    @Override
    public void setUp() {
        super.setUp();
        mUpdateSignal = new CountDownLatch(1);
        mSigningSignalGET = new CountDownLatch(1);
        mSigningSignalPOST = new CountDownLatch(1);
        mSigningSignalPUT = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_CONTACTS_CREATE_SIGNABLE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mTestWebApiClient.setGlobalHeaders(headers);
    }

    /**
     * Test create.
     */
    @Test
    public void testUpdateSignableManageable() {
        UpdateContactRequest request = new UpdateContactRequest(1, "Peter", "733845987");
        mTestWebApiClient.getContactsResource().withId("1").update(request, new CallbackWebApi<UpdateContactResponse>() {
            @Override
            public void success(UpdateContactResponse updateContactResponse) {
                mUpdateContactResponse = updateContactResponse;
                mUpdateSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                CsSDKError error1 = error;
                mUpdateSignal.countDown();
            }
        });

        try {
            mUpdateSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mSigningObject = mUpdateContactResponse.signing();

        assertNotNull(mSigningObject);
        assertEquals("18917bfb2990b3982662e09eb2ae7545eeb99104ca5505ca9281f16a275b1968", mSigningObject.getSignId());
        assertEquals(SigningState.OPEN, mSigningObject.getSigningState());
        assertEquals(SigningState.OPEN, mUpdateContactResponse.signing().getSigningState());

        mSigningObject.getInfo(new CallbackWebApi<FilledSigningObject>() {
            @Override
            public void success(FilledSigningObject filledSigningObject) {
                mFilledSigningObject = filledSigningObject;
                mSigningSignalGET.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                CsSDKError error1 = error;
                mSigningSignalGET.countDown();
            }
        });

        try {
            mSigningSignalGET.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mFilledSigningObject);
        assertEquals("18917bfb2990b3982662e09eb2ae7545eeb99104ca5505ca9281f16a275b1968", mFilledSigningObject.getSignId());
        assertEquals(SigningState.OPEN, mFilledSigningObject.getSigningState());
        assertEquals(SigningState.OPEN, mUpdateContactResponse.signing().getSigningState());
        assertEquals(AuthorizationType.TAC, mFilledSigningObject.getAuthorizationType());
        assertTrue(mFilledSigningObject.getPossibleAuthorizationType().contains(AuthorizationType.TAC));
        assertTrue(mFilledSigningObject.canBeSignedWith(AuthorizationType.TAC));
        assertFalse(mFilledSigningObject.canBeSignedWith(AuthorizationType.MOBILE_CASE));
        assertEquals(AuthorizationType.TAC, mFilledSigningObject.getScenarios().get(0).get(0));


        mFilledSigningObject.startSigningWithTac(new CallbackWebApi<TacSigningProcess>() {
            @Override
            public void success(TacSigningProcess tacSigningProcess) {
                mTacSigningProcess = tacSigningProcess;
                mSigningSignalPOST.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mSigningSignalPOST.countDown();
            }
        });

        try {
            mSigningSignalPOST.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mTacSigningProcess);

        mTacSigningProcess.finishSigning("00000000", new CallbackWebApi<SigningObject>() {
            @Override
            public void success(SigningObject signingObject) {
                mSigningObject = signingObject;
                mSigningSignalPUT.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mSigningSignalPUT.countDown();
            }
        });

        try {
            mSigningSignalPUT.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(mSigningObject);
        assertEquals("18917bfb2990b3982662e09eb2ae7545eeb99104ca5505ca9281f16a275b1968", mSigningObject.getSignId());
        assertEquals(SigningState.DONE, mSigningObject.getSigningState());
        assertEquals(SigningState.DONE, mUpdateContactResponse.signing().getSigningState());

    }
}