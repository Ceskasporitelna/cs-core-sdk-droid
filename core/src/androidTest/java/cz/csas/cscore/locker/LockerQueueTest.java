package cz.csas.cscore.locker;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.LockerTest;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

/**
 * The type Locker queue test
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 25 /02/16.
 */
public class LockerQueueTest extends LockerTest {

    private final int NUM_OF_CALLS = 10;
    private final String X_JUDGE_SESSION_HEADER_QUEUE = "core.locker.queue.session";
    private final String X_JUDGE_CASE_HEADER_QUEUE = "core.locker.unlock.queue";
    private CountDownLatch mQueueSignal;

    @Override
    public void setUp() {
        mXJudgeSessionHeader = X_JUDGE_SESSION_HEADER_QUEUE;
        super.setUp();
        mQueueSignal = new CountDownLatch(NUM_OF_CALLS);
        LockerUtils.setLocker(mLocker, mXJudgeSessionHeader, mCryptoManager);
        JudgeUtils.setJudge(mJudgeClient, Constants.X_JUDGE_CASE_HEADER_REGISTER, mXJudgeSessionHeader);
        LockerUtils.lockerRegister(mLocker);
    }

    @Test
    public void testQueue() {
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE_HEADER_QUEUE, mXJudgeSessionHeader);
        for (int i = 1; i <= NUM_OF_CALLS; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    mLocker.unlock(Constants.PASSWORD_TEST, new Callback<RegistrationOrUnlockResponse>() {
                        @Override
                        public void success(RegistrationOrUnlockResponse registrationOrUnlockResponse, Response response) {
                            mQueueSignal.countDown();
                        }

                        @Override
                        public void failure(CsRestError error) {
                            mQueueSignal.countDown();
                        }
                    });
                }
            });
            t.start();
        }

        try {
            mQueueSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
