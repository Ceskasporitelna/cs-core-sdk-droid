# Change Log

## [Unreleased]

## [1.4.0] - 2017-10-23

## Added 
- Add non-symmetric gesture grid compatibility

## Fixed
- Safe check for offline password storing
- Empty check for random string generation
- Safe throw callback error for locker registration

## [1.3.3] - 2017-10-04

## Fixed
- Fix theme colors

## [1.3.2] - 2017-10-04

## Fixed 
- OAuth2 activity settings and theme

## [1.3.1] - 2017-09-20

## Fixed
- Migration check for `UNREGISTERED` state
- Encryption key necessary in migration data

## [1.3.0] - 2017-09-20

## Added
- Migration from custom Locker implementation to Locker v1

## [1.2.5] - 2017-09-06

## Fixed
- Locker request signing

## [1.2.4] - 2017-08-11

## Fixed
- Disable saving password in webview

## [1.2.3] - 2017-07-19

## Fixed
- Offline password hash encryption edge case error prevention
- OAuthActivity webview reload on orientation change

## Added
- Offline authentication and locker config documentation

## [1.2.2] - 2017-06-27

## Added
- Progress bar to OAuthLoginActivity

## [1.2.1] - 2017-06-22

## Fixed
- LockerUI OAuth webview closed after back button pressed for the first time  

## Added
- Custom navigation bar color for OAuth login activity

## [1.2.0] - 2017-06-01

## Changed

- Runtime errors in locker interface returned in callbacks due to bg queue

## [1.1.1] - 2017-05-10

## Fixed

- Clean styles resource file
- Fix travis bintray descriptor file

## [1.1.0] - 2017-05-10

## Changed

- CoreSDK released to JCenter

## [1.0.0] - 2017-04-23

- Public release
