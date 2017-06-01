package cz.csas.cscore.webapi.signing;

import java.net.URI;
import java.net.URISyntaxException;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.error.CsSigningError;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.utils.StringUtils;
import cz.csas.cscore.utils.UrlUtils;
import cz.csas.cscore.webapi.CallApiType;
import cz.csas.cscore.webapi.Method;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * The type Request signer provides immutable and manageable orders signing.
 * <p/>
 * Most of create/update/delete active calls done by the user/application need to be signed by using
 * particular authorization method, to confirm user intention to execute active operation and
 * authorize user with some security tools in the same time.
 * <p/>
 * Following list enumerates some of the possible authorization methods used in FE:
 * *NO AUTHORIZATION* - validation of user response as confirmation (e.g. confirm button click)
 * *TAC* - validation of user response as randomly generated number/code sent to user personal device
 * via SMS
 * *MOBILE CASE* - validation of the user response using mobile application, this method have three
 * forms (user can choose which he'll use)ONLINE - mobile application receives PUSH notification
 * with relevant data for authorization and user just clicks confirmation button in mobile
 * application (data are sent over internet to bank)QR - mobile application retrieves relevant data
 * for authorization by reading QR code displayed in frontend application, generates onetime
 * password and user enters this OTP into frontend application to authorize operation Depending on
 * the business defined characteristic of an entity/resource, creating/updating/deleting action
 * requires an optional signing process. There are two different types of action orders:
 * <p/>
 * *Immutable orders* - active calls which create/change resource (stored in local BE system) only
 * after succesful signing by user
 * *Manageable orders* - active calls which allow to create/update/deleate particular order
 * (temporarily stored in local system) and only after user signing this order is sent to BE
 * processing
 * <p/>
 * See also: http://docs.netbankingv3.apiary.io/#introduction/authorization
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class RequestSigner {

    static final String SIGNING_MODULE = "Signing";
    private String url;
    private WebApiClient webApiClient;
    private SigningObject signingObject;
    private LogManager logManager;

    /**
     * Instantiates a new Request signer.
     *
     * @param webApiEntity the web api entity
     * @throws CsSigningError the cs signing error
     */
    public RequestSigner(WebApiEntity webApiEntity) throws CsSigningError {
        this.url = webApiEntity.getResource().getBasePath() + UrlUtils.getSignUrl(webApiEntity);
        this.webApiClient = webApiEntity.getResource().getClient();
        this.logManager = CoreSDK.getInstance().getLogger();
    }

    /**
     * Instantiates a new Request signer.
     *
     * @param url          the url
     * @param webApiClient the web api client
     * @param object       the object
     */
    public RequestSigner(String url, WebApiClient webApiClient, Object object) {
        this.url = url + UrlUtils.getSignUrl(object);
        this.webApiClient = webApiClient;
        this.logManager = CoreSDK.getInstance().getLogger();
    }

    /**
     * Set signing object.
     *
     * @param signingObject the signing object
     */
    public void setSigningObject(SigningObject signingObject) {
        this.signingObject = signingObject;
    }

    /**
     * Get signing url.
     *
     * @return the sign url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get web api client.
     *
     * @return the signing related web api client
     */
    public WebApiClient getWebApiClient() {
        return webApiClient;
    }

    /**
     * Get signing object.
     *
     * @return the signing related signing object
     */
    public SigningObject getSigningObject() {
        return signingObject;
    }

    /**
     * Get signing info. Returns callback with FilledSigningObject containing all the necessary
     * signing info.
     *
     * @param signId   the signing related sign id
     * @param callback the callback
     */
    public void getSigningInfo(String signId, final CallbackWebApi<FilledSigningObject> callback) {
        getWebApiClient().callApi(CallApiType.NORMAL, appendUrlWithSign(signId), Method.GET, null, null, null, null, new CsCallback<Object>() {
            @Override
            public void success(Object o, Response response) {
                o = response.getBodyObject(SigningResponse.class);
                if (o != null) {
                    callback.success(getFilledSigningObject((SigningResponse) o));
                    logManager.log(StringUtils.logLine(SIGNING_MODULE, "GetInfo", "Getting signing info successful."), LogLevel.DEBUG);
                } else {
                    CsSigningError error = new CsSigningError(CsSigningError.Kind.UNSIGNABLE_ENTITY);
                    callback.failure(error);
                    logManager.log(StringUtils.logLine(SIGNING_MODULE, "GetInfoError", "Getting signing info error:" + error.getLocalizedMessage()), LogLevel.DEBUG);
                }
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(processError(error));
                logManager.log(StringUtils.logLine(SIGNING_MODULE, "GetInfoError", "Getting signing info error:" + error.getLocalizedMessage()), LogLevel.DEBUG);
            }
        }, null);
    }

    /**
     * Start signing with tac authorization type. Convenience method for private start signing.
     *
     * @param signId   the signing related sign id
     * @param callback the callback
     */
    public void startSigningWithTac(String signId, final CallbackWebApi<TacSigningProcess> callback) {
        startSigning(signId, AuthorizationType.TAC, new CallbackWebApi<SigningProcess>() {
            @Override
            public void success(SigningProcess signingProcess) {
                callback.success((TacSigningProcess) signingProcess);
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(processError(error));
            }
        });
    }

    /**
     * Start signing with mobile case authorization type. Convenience method for private
     * start signing.
     *
     * @param signId   the signing related sign id
     * @param callback the callback
     */
    public void startSigningWithMobileCase(String signId, final CallbackWebApi<MobileCaseSigningProcess> callback) {
        startSigning(signId, AuthorizationType.MOBILE_CASE, new CallbackWebApi<SigningProcess>() {
            @Override
            public void success(SigningProcess signingProcess) {
                callback.success((MobileCaseSigningProcess) signingProcess);
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(processError(error));
            }
        });
    }

    /**
     * Start signing with no authorization type. Convenience method for private start signing.
     * method.
     *
     * @param signId   the signing related sign id
     * @param callback the callback
     */
    public void startSigningWithNoAuthorization(String signId, final CallbackWebApi<NoAuthorizationSigningProcess> callback) {
        startSigning(signId, AuthorizationType.NONE, new CallbackWebApi<SigningProcess>() {
            @Override
            public void success(SigningProcess signingProcess) {
                callback.success((NoAuthorizationSigningProcess) signingProcess);
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(processError(error));
            }
        });
    }

    private void startSigning(String signId, final AuthorizationType authorizationType, final CallbackWebApi<SigningProcess> callback) {
        SigningRequest request = new SigningRequest(authorizationType);
        getWebApiClient().callApi(CallApiType.NORMAL, appendUrlWithSign(signId), Method.POST, request, null, null, null, new CsCallback<Object>() {
            @Override
            public void success(Object o, Response response) {
                o = response.getBodyObject(SigningResponse.class);
                if (o != null) {
                    SigningObject signingObject = getSigningObject((SigningResponse) o);
                    switch (authorizationType) {
                        case TAC:
                            callback.success(new TacSigningProcess(signingObject));
                            break;
                        case MOBILE_CASE:
                            callback.success(new MobileCaseSigningProcess(signingObject));
                            break;
                        case NONE:
                            callback.success(new NoAuthorizationSigningProcess(signingObject));
                            break;
                        default:
                            callback.failure(new CsSigningError(CsSigningError.Kind.OTHER));
                            break;
                    }
                    logManager.log(StringUtils.logLine(SIGNING_MODULE, "Start", "Start signing with " + authorizationType + " successful."), LogLevel.DEBUG);
                } else {
                    CsSigningError error = new CsSigningError(CsSigningError.Kind.UNSIGNABLE_ENTITY);
                    callback.failure(error);
                    logManager.log(StringUtils.logLine(SIGNING_MODULE, "StartError", "Start signing with " + authorizationType + "error:" + error.getLocalizedMessage()), LogLevel.DEBUG);
                }
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(error);
                logManager.log(StringUtils.logLine(SIGNING_MODULE, "StartError", "Start signing with " + authorizationType + "error:" + error.getLocalizedMessage()), LogLevel.DEBUG);
            }

        }, null);
    }

    /**
     * Finish signing with tac authorization type. Convenience method for private finish signing
     * method.
     *
     * @param signId          the signing related sign id
     * @param oneTimePassword the one time password
     * @param callback        the callback
     */
    public void finishSigningWithTac(String signId, String oneTimePassword, final CallbackWebApi<SigningObject> callback) {
        finishSigning(signId, AuthorizationType.TAC, oneTimePassword, callback);
    }

    /**
     * Finish signing with mobile case authorization type. Convenience method for private finish
     * signing method.
     *
     * @param signId          the signing related sign id
     * @param oneTimePassword the one time password
     * @param callback        the callback
     */
    public void finishSigningWithMobileCase(String signId, String oneTimePassword, final CallbackWebApi<SigningObject> callback) {
        finishSigning(signId, AuthorizationType.MOBILE_CASE, oneTimePassword, callback);
    }

    /**
     * Finish signing with no authorization type. Convenience method for private finish signing
     * method.
     *
     * @param signId   the signing related sign id
     * @param callback the callback
     */
    public void finishSigningNoAuthorization(String signId, final CallbackWebApi<SigningObject> callback) {
        finishSigning(signId, AuthorizationType.NONE, null, callback);
    }

    private void finishSigning(String signId, final AuthorizationType authorizationType, String oneTimePassword, final CallbackWebApi<SigningObject> callback) {
        FinishSigningRequest request = new FinishSigningRequest(authorizationType, oneTimePassword);
        getWebApiClient().callApi(CallApiType.NORMAL, appendUrlWithSign(signId), Method.PUT, request, null, null, null, new CsCallback<Object>() {
            @Override
            public void success(Object o, Response response) {
                o = response.getBodyObject(SigningResponse.class);
                if (o != null) {
                    callback.success(getSigningObject((SigningResponse) o));
                    logManager.log(StringUtils.logLine(SIGNING_MODULE, "Finished", "Finish signing with " + authorizationType + " successful."), LogLevel.DEBUG);
                } else {
                    CsSigningError error = new CsSigningError(CsSigningError.Kind.UNSIGNABLE_ENTITY);
                    callback.failure(error);
                    logManager.log(StringUtils.logLine(SIGNING_MODULE, "FinishedError", "Finish signing with " + authorizationType + " error: " + error.getLocalizedMessage()), LogLevel.DEBUG);
                }
            }

            @Override
            public void failure(CsSDKError error) {
                callback.failure(processError(error));
                logManager.log(StringUtils.logLine(SIGNING_MODULE, "FinishedError", "Finish signing with " + authorizationType + " error: " + error.getLocalizedMessage()), LogLevel.DEBUG);
            }
        }, null);
    }

    /**
     * Cancel signing.
     *
     * @param signId   the signing related sign id
     * @param callback the callback
     */
    public void cancelSigning(String signId, final CallbackWebApi<FilledSigningObject> callback) {
        //TODO
    }

    private String appendUrlWithSign(String signId) {
        try {
            return new URI(url + "/sign/" + signId).normalize().getPath();
        } catch (URISyntaxException e) {
            throw new CsSigningError(CsSigningError.Kind.OTHER);
        }
    }

    private FilledSigningObject getFilledSigningObject(SigningResponse signingResponse) {
        SignInfo signInfo = signingResponse.getSignInfo();
        signingObject.setSignId(signInfo.getSignId());
        signingObject.setSigningState(signInfo.getState());
        ((FilledSigningObject) signingObject).setAuthorizationType(signingResponse.getAuthorizationType());
        ((FilledSigningObject) signingObject).setScenarios(signingResponse.getScenarios());
        return (FilledSigningObject) signingObject;
    }

    private SigningObject getSigningObject(SigningResponse signingResponse) {
        SignInfo signInfo = signingResponse.getSignInfo();
        signingObject.setSignId(signInfo.getSignId());
        signingObject.setSigningState(signInfo.getState());
        return signingObject;
    }

    private CsSigningError processError(CsSDKError error) {
        if (error instanceof CsRestError) {
            int status = ((CsRestError) error).getResponse().getStatus();
            if (status == 400 || status == 404) {
                String errorBody = ((CsRestError) error).getResponse().getBodyString();
                if (errorBody != null) {
                    if (errorBody.contains("ID_NOT_FOUND")
                            || errorBody.contains("HASH_MISMATCH"))
                        return new CsSigningError(CsSigningError.Kind.INVALID_SIGN_ID);
                    else if (errorBody.contains("NOT_SIGNABLE"))
                        return new CsSigningError(CsSigningError.Kind.UNSIGNABLE_ENTITY);
                    else if (errorBody.contains("AUTH_LIMIT_EXCEEDED"))
                        return new CsSigningError(CsSigningError.Kind.AUTH_LIMIT_EXCEEDED);
                    else if (errorBody.contains("NO_AUTH_AVAILABLE"))
                        return new CsSigningError(CsSigningError.Kind.NO_AUTH_AVAILABLE);
                    else if (errorBody.contains("CZ-SIGN_IN_PROGRESS"))
                        return new CsSigningError(CsSigningError.Kind.SIGNING_IN_PROGRESS);
                    else if (errorBody.contains("OTP_REQUEST_NOT_ALLOWED")
                            || errorBody.contains("OTP_INVALID")
                            || errorBody.contains("OTP_EXPIRED"))
                        return new CsSigningError(CsSigningError.Kind.INVALID_OTP);
                    else if (errorBody.contains("AUTH_METHOD_LOCKED"))
                        return new CsSigningError(CsSigningError.Kind.AUTH_METHOD_LOCKED);
                    else if (errorBody.contains("ONE_ATTEMPT_LEFT"))
                        return new CsSigningError(CsSigningError.Kind.ONE_ATTEMPT_LEFT);
                    else if (errorBody.contains("USER_LOCKED"))
                        return new CsSigningError(CsSigningError.Kind.USER_LOCKED);
                    else if (errorBody.contains("CZ-ORDER_DELIVERATION_UNCERTAIN"))
                        return new CsSigningError(CsSigningError.Kind.ORDER_DELIVERY_UNCERTAIN);
                    else if (errorBody.contains("CZ-INSUFFICIENT_BALANCE"))
                        return new CsSigningError(CsSigningError.Kind.INSUFFICIENT_BALANCE);
                    else if (errorBody.contains("CZ-PHONE_NUMBER_BLOCKED"))
                        return new CsSigningError(CsSigningError.Kind.PHONE_NUMBER_BLOCKED);
                    else if (errorBody.contains("LIMIT_EXCEEDED")
                            || errorBody.contains("CZ-MONTHLY_LIMIT_EXCEEDED"))
                        return new CsSigningError(CsSigningError.Kind.LIMIT_EXCEEDED);
                    else if (errorBody.contains("FIELD_INVALID"))
                        return new CsSigningError(CsSigningError.Kind.INVALID_AUTHORIZATION_TYPE);
                }
                return new CsSigningError(CsSigningError.Kind.SIGNING_FAILED);
            }
        }
        return new CsSigningError(CsSigningError.Kind.OTHER, error.getLocalizedMessage());
    }
}
