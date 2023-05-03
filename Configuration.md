# Configuration

## SDKs to build project
1. Project SDK: Oracle OpenJDK 16.0.2 at Languagelevel: 17
2. Gradle JVM: Oracle OpenJDK 17.0.4
3. Android SDK: API 33

## To look into the Database use the Database Tool
1. Database -> Data Source -> H2
2. use URL `jdbc:h2:file:./db`

## Configure ApiClient
1. Go to [file](client/src/commonMain/kotlin/com/example/common/ApiClient.kt) client/src/commonMain/kotlin/com/example/common/ApiClient.kt
2. Edit line 24 `private val ip = "0.0.0.0:8080"` to match your private network url


## Add Android SDK
1. Tools -> Android -> SDK Manager
2. Make sure API Level 33 is installed
3. At "Android SDK Location" click Edit
    1. Follow steps to auto-generate Local.Propoerties

## Start Modules
### Server
Run [file](server/src/main/kotlin/de/hsflensburg/bookmanager/Application.kt) `server/src/main/kotlin/de/hsflensburg/bookmanager/Application.kt`

### Desktop
Run [file](ui-desktop/src/jvmMain/kotlin/Main.kt) `ui-desktop/src/jvmMain/kotlin/Main.kt`

### Android
Run [file](android/src/main/java/com/example/android/MainActivity.kt) `android/src/main/java/com/example/android/MainActivity.kt`

### Web 
Run Gradle Task `web -> Tasks -> kotlin browser -> jsBrowserRun`

## Admin Panel
1. Login using Username: Admin Password: Admin

