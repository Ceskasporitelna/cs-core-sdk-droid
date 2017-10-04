# Locker
Locker simplifies authentication against CSAS servers. It allows developer to obtain access token for the user and store it in a secure manner.

## Fetures
- [x] **Registration** - Obtaining access token from oAuth2 serves for the user and securing it by password.
- [x] **Unlock (Login)** - Obtaining access token after registration in exchange for the password.
- [x] **Lock (Logout)** - Securing the access token when the app is not in use
- [x] **Unregister** - Purging the access token from the device
- [x] **Change password** - Change the password that secures the access token
- [x] **Token Refresh** - Refreshes the access token
- [x] **OneTimePassword** - Obtaining the access token without user intervention (Work in Progress)


## Configuring locker
You need to configure the locker before you can use it.

You can configure locker by calling `.useLocker()` on the CoreSDK. Example configuration is below:
```
    LockerConfig lockerConfig = new LockerConfig.Builder()
        .setClientId("YourClientID")
        .setClientSecret("YourClientSecret")
        .setPublicKey("YourPublicKey")
        .setRedirectUrl("yourscheme://your-host")
        .setScope("/v1/netbanking")
        .setOfflineAuthEnabled()
        .create();

    CoreSDK.getInstance()
        .useContext(Context)
        .useWebApiKey("YourApiKey")
        .useEnvironment(Environment.Sandbox)
        .useLanguage("en-US")
        .useRequestSigning("YourPrivateSigningKey")
        .useLocker(lockerConfig)
```
### Configuration parameters
* `clientId` - ID of your application assigned by WebApi
* `clientSecret` - Client secret provided to you by WebApi
* `publicKey` - Public key used to encrypt the session key between you and the Locker server. **IMPORTANT**: Be sure to copy it exactly from the WebApi console as-is. Pay special attention to not include any spaces or carriage returns in the string.
* `redirectUrlPath` - oAuth2 redirection url that your application has to be registered to handle. **IMPORTANT**: You have to register your URL scheme with the APP.
* `scope` - Scope for the retrieved access token. If in doubt, use `/v1/netbanking`
* `offlineAuthEnabled` - Locker offline authentication (verification) flag. Default value is `false`.

### OAuth redireciton handling
In order to successfully handle redirections from the Webview to your app during the Locker registration, you have to configure your app to properly handle it.

1. Define the url scheme part *(that's the thing before the `://`)* and host *(that's the thing after the `://`)* of your `redirectUrl` via LockerConfig file.

2. Catch activity result by adding these lines to your own activity, which called `Locker.register()` method. You need to customize following behaviour.

```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.OAUTH_REQUEST_CODE){
            if(resultCode == RESULT_OK)
                CoreSDK.getInstance().getLocker().processUrl(data.getStringExtra(cz.csas.cscore.locker.Constants.CODE_EXTRA));
        }
    }
```
You can take `OAUTH_REQUEST_CODE` from Locker `Constants` or even rewrite it. (`OAUTH_REQUEST_CODE = 100`)


### OAuth styling
`OAuthLoginActivity` allows some styling optimizations via `Locker` interface. Only navigation bar is now stylable. It is possible to set `navBarColor` and to set if the logo should be shown using `showLogo` option.

```

    /*
     * Select `CsNavBarColor` and show logo
     */
    OAuthLoginActivityOptions oAuthLoginActivityOptions = new OAuthLoginActivityOptions.Builder().setNavBarColor(CsNavBarColor.WHITE).setShowLogo(true).create();

    /*
     * Or select custom navigation bar color and do not show logo
     */
    OAuthLoginActivityOptions oAuthLoginActivityOptions = new OAuthLoginActivityOptions.Builder().setNavBarColor(Color.parseColor("#3F51B5")).setShowLogo(false).create();
 
    /*
     * OAuthLoginActivity styling
     */
    CoreSDK.getInstance().getLocker().setOAuthLoginActivityOptions(oAuthLoginActivityOptions);
```

### Javascript injection
['OAuthLoginActivity'](../core/src/main/java/cz/csas/cscore/locker/OAuthLoginActivity.java) also allows you to inject javascript, but only for limited number of OAuth2 base urls. See the code snippet below:

```

    /*
     ****************************
     * ALLOWED OAUTH2 BASE URLS:
     ****************************
     * https://bezpecnost.csast.csas.cz/mep/fs/fl/oauth2
     * https://www.csast.csas.cz/widp/oauth2
     ****************************
     *
     * Javascript injection
     */
    CoreSDK.getInstance().getLocker().injectTestingJSForRegistration("your_testing_javascript);

```

This code is then in `OAuthLoginActivity` executed. (This is already implemented, it is just info about used methods.)

```

    /*
     * Android OAuthLoginActivity javascript execution implementation
     */
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        mLoginWebView.evaluateJavascript(mJavascript, null);
    } else {
        mLoginWebView.loadUrl("javascript:" + mJavascript);
    }

```

### Offline authentication
Locker allows you to use also offline authentication to verify user. If `LockerConfig.setOfflineAuthEnabled()` is set, each user of your app has two attempts to verify himself in offline mode. In a case of two unsuccessful verification attempts, online authentication is required. Also after successful online authentication, offline verification attempts are renewed. There is no impact to actual Locker state, only `LockerStatus.isVerifiedOffline()` changes. 

Now you are all set to use the locker!

## Using locker
Please see [public API of the locker](../core/src/main/java/cz/csas/cscore/locker/Locker.java) for interface documentation.