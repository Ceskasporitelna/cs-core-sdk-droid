package cz.csas.cscore.judge;

import cz.csas.cscore.client.RestService;
import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.http.CsGet;
import cz.csas.cscore.client.rest.http.CsHeader;
import cz.csas.cscore.client.rest.http.CsPost;

/**
 * The interface Judge client.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 20 /11/15.
 */
public interface JudgeRestService extends RestService {

    /**
     * Next case.
     *
     * @param xJudgeCase    the x judge case
     * @param xJudgeSession the x judge session
     */
    @CsPost("/judge/nextCase")
    public void nextCase(@CsHeader("x-judge-case") String xJudgeCase, @CsHeader("x-judge-session") String xJudgeSession, Callback<Response> callback);

    /**
     * Verdict.
     *
     * @param xJudgeSession the x judge session
     * @param callback      the callback
     */
    @CsGet("/judge/lastCae/verdict")
    public void verdict(@CsHeader("x-judge-session") String xJudgeSession, Callback<Verdict> callback);

    /**
     * Final verdict.
     *
     * @param xJudgeSession        the x judge session
     * @param finalVerdictCallback the final verdict callback
     */
    @CsGet("/judge/finalVerdict")
    public void finalVerdict(@CsHeader("x-judge-session") String xJudgeSession, Callback<FinalVerdict> finalVerdictCallback);
}
