#Configuration
##Basic configuration
Before you use any of the CSAS SDKs in your application, you need to initialize it by providing your WebApiKey into the CoreSDK.
```
CoreSDK.getInstance().useWebApiKey( "YourApiKey" )
```
This Api key will be then used for all communications with CSAS WebApi.

You can find example of full configuration below:
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

##Set environment
Environment can be set by `useEnvironment()` method. You can use one of the predefined environments or define your own. SDK ships with two predefined environments:

* `Environment.Sandbox` - Sandbox environment that is intended to be used in the development and testing phase.
* `Environment.Production` - Production environment that is intended to be used in the Production builds. **This environment is a gateway to real banking data, so be carefull!**

You can also specify your own environment. You can do so by creating the Environment object yourslef:
```
 Environment environment = Environment("https://www.example.com","https://www.example.com/widp/oauth2"))
```
##Set language
Language of communication can be set by the `.useLanguage()` method. Passed language will be sent in the `Accept-Language` header with each request to the WebApi. Default setting is `cs-CZ`

##Turn on Request signing 
If you have request signing enabled for your WebApiKey, you can pass your private signing key into `.useRequestSigning()` method. SDK will then sign your requests for every API that supports request signing.

##Turn on Locker
Please see [locker guide](locker.md) on how to configure & use locker.