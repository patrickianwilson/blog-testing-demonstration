# Induction Testing Companion Code

##Building Locally

The code is fully executable.  If you wish to run it locally you must first complete two steps:

1. Get a Google URL Shortener API Token from: https://console.developers.google.com/apis/credentials
2. Setup a local mysql database and update the hibernate.cfg.xml file with the appropriate crednetials.

To actually run the build:

```./gradlew clean test runIntegrationTests ```

This will compile, assemble and run all the tests.

