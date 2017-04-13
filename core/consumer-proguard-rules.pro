# Core SDK (Embedded Retrofit)
# cscore
-keep class cz.csas.cscore.CoreRestService { *; }
-keep class cz.csas.cscore.RefreshTokenResponse { *; }
# client
-keep interface cz.csas.cscore.client.rest.* { *; }
-keep class cz.csas.cscore.client.rest.* { *; }
-keep class cz.csas.cscore.client.RestService { *; }
# utils
-keep class cz.csas.cscore.utils.csjson.* { *; }
# logger
-keep class cz.csas.cscore.logger.* { *; }
# webapi
-keep class cz.csas.cscore.webapi.WebApiService { *; }
-keep class cz.csas.cscore.webapi.ListResponse { *; }
-keep class cz.csas.cscore.webapi.PaginatedListResponse { *; }
-keep class cz.csas.cscore.webapi.WebApiEntity { *; }
-keep class cz.csas.cscore.webapi.WebApiRequest { *; }
-keep class cz.csas.cscore.webapi.WebApiStream { *; }
## signing
-keep class cz.csas.cscore.webapi.signing.SignInfo { *; }
-keep class cz.csas.cscore.webapi.signing.SigningRequest { *; }
-keep class cz.csas.cscore.webapi.signing.SigningResponse { *; }
-keep class cz.csas.cscore.webapi.signing.SigningState { *; }
-keep class cz.csas.cscore.webapi.signing.AuthorizationType { *; }
-keep class cz.csas.cscore.webapi.signing.Poll { *; }
# locker
-keep class cz.csas.cscore.locker.LockerRestService { *; }
-keep class cz.csas.cscore.locker.LockerRequest { *; }
-keep class cz.csas.cscore.locker.LockerRequestJson { *; }
-keep class cz.csas.cscore.locker.LockerResponse { *; }
-keep class cz.csas.cscore.locker.OneTimePasswordUnlockRequestJson { *; }
-keep class cz.csas.cscore.locker.PasswordRequestJson { *; }
-keep class cz.csas.cscore.locker.PasswordResponseJson { *; }
-keep class cz.csas.cscore.locker.RegistrationOrUnlockResponseJson { *; }
-keep class cz.csas.cscore.locker.RegistrationRequestJson { *; }
-keep class cz.csas.cscore.locker.UnlockRequestJson { *; }
-keep class cz.csas.cscore.locker.UnregisterRequestJson { *; }