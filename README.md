# CSCoreSDK

# Features
- [x] **[Locker](./docs/locker.md)** - Secure authentication using CSAS oAuth servers
- [x] **[WEBAPI Client](./docs/webapi-howto.md)** - Simplifies access to CSAS APIs

# [CHANGELOG](CHANGELOG.md)

# Requirements
- Android 4.1+
- Gradle 2.8+
- Android Studio 1.5+

# CoreSDK Instalation
You would normally use CoreSDK through other CSAS SDKs. If you want to use Locker without the UI or develop your app against the bare bones, you can install CoreSDK directly.

## Install
You can install CoreSDK using the following gradle settings.

1. Check your project build.gradle file that it contains `JCenter` repository:
```gradle
    allprojects {
        repositories {
            ...
            jcenter()
            ...
        }
    }
```

2. Insert this line into your module build.gradle file to compile CoreSDK:
```gradle
    dependencies {
        ...
        compile 'cz.csas:cs-core-sdk:1.1.0@aar'
        ...
    }
```

# Usage
After you've installed the SDK you will be able to use CoreSDK in your project.

## Configuration
Before using CoreSDK in your application, you need to initialize it by providing it your WebApiKey.

```
    CoreSDK.getInstance().useWebApiKey( "YourApiKey" )
```
**See [configuration guide](docs/configuration.md)** for all the available configuration options.

## Locker
Locker simplifies authentication against CSAS servers. It allows developer to obtain access token for the user and store it in a secure manner.

Please see [locker guide](./docs/locker.md) to see how to configure & use locker.

## Webapi
Webapi simplifies building your own client that can talk to CSAS WebApi is easy thanks to basic building blocks that are included in the CoreSDK.

Please see [webapi guide](./docs/webapi-howto.md).

## Proguard
The CoreSDK Proguard additions are necessary to be specified besides the recommended Android Proguard configuration. Copy the CoreSDK proguard rules to your project.

Please see [proguard-rules.pro](./core/consumer-proguard-rules.pro).
# Contributing
Contributions are more than welcome!
Please read our [contribution guide](CONTRIBUTING.md) to learn how to contribute to this project.

# Terms and License
Please read our [terms & conditions in license](LICENSE.md)
