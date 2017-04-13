package cz.csas.cscore.judge;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CsRestError;

/**
 * The type Judge testing utils. It sets the judge session.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 08 /12/15.
 */
public class JudgeUtils {

    /**
     * Set judge.
     *
     * @param judgeClient        the judge client
     * @param judgeCaseHeader    the judge case header
     * @param judgeSessionHeader the judge session header
     */
    public static void setJudge(JudgeClient judgeClient, String judgeCaseHeader, String judgeSessionHeader){

        final CountDownLatch judgeSignal = new CountDownLatch(1);

        ((JudgeRestService) judgeClient.getService()).nextCase(judgeCaseHeader, judgeSessionHeader, new Callback<cz.csas.cscore.client.rest.client.Response>() {
            @Override
            public void success(cz.csas.cscore.client.rest.client.Response response, cz.csas.cscore.client.rest.client.Response response2) {
                judgeSignal.countDown();
            }

            @Override
            public void failure(CsRestError error) {
                judgeSignal.countDown();
            }
        });

        try {
            judgeSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
