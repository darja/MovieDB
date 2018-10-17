# MovieDB

Demo application for displaying movies from [The Movie Database](https://www.themoviedb.org/)

## Architecture

### Code Structure

Main package contain following packages

* `activity` – project activity
* `api` – API configuration for **Retrofit**
* `db` – **Room** database configuration, including tables and Dao definitions
* `di` – **Dagger** dependency injection configuration
* `ui` – fragments, UI events and utils
* `util` – misc utilities

### Android Components Organization

Every activity and fragment is split into following parts:

* Activity/fragment class itself, that contains lifecycle callbacks and communicate to View and ViewModel
* View – contains all UI components and methods for updating it. Activity/fragment classes are often grow too big because of UI logic, so it makes sense to place UI code in separate class.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) is responsible for preparing and updating data that is displayed in the UI. ViewModel is a part of [Google Architecture Components](https://developer.android.com/topic/libraries/architecture/) and can survive its owner recreation, so it helps to organize business objects storage.

### Fetching and Caching Data

Business objects are cached in SQLite database with following structure:

![DB Scheme][doc/db_scheme.png]

Genres are requested and updated on every application start.

Movies lists are cached for 1 hour.

If an application needs to show movies list (popular movies or search result), it checks database first. If up-to-date movies list is found, cached data is displayed. Otherwise application deletes outdated objects, requests new ones from API and shows updated data from database. 

## Third-Party Libraries

Following libraries are used to make development more efficient and to write less boilerplate code:

* [Retrofit](https://square.github.io/retrofit/), [OkHttp](https://square.github.io/okhttp/), [Gson](https://github.com/google/gson) – for requesting API and parsing responses
* [Room](https://developer.android.com/topic/libraries/architecture/room) – ORM for SQLite
* [Butterknife](https://github.com/JakeWharton/butterknife) – for injecting UI components from XML
* [Dagger](https://google.github.io/dagger/) – dependency injection tool
* [Fresco](https://frescolib.org/) – for loading web images
* [EventBus](https://github.com/greenrobot/EventBus) – for communication between fragments and activities without callbacks

## Building and Running the Application

There are two build variants of the application: debug and release. Release version doesn't write logs to Logcat and doesn't support debugger attach.

### Prerequisites

* Android SDK, API level 28 (Pie)
* Gradle 4.6
* Android build tools 3.2.1
* Kotlin 1.2.71
* Android device or emulator with API level > 18 (JellyBean)

### Building

There are two ways to build the application.

#### From Android Studio

* Open the project
* Open **Build Variants** window and select proper variant (debug or release)
* Click **Build > Rebuild Project** in top menu.
* Wait till the end of operation

#### From Command Line

For debug version:

```
./gradlew clean assembleDebug
```

For release version:

```
./gradlew clean assembleRelease
```

For both:
```
./gradlew clean assemble
```

#### Output

In both cases apk files will appear in `{APP_ROOT}/app/build/outputs/` in `debug` or `release` folder respectively.

### Running 

#### From Android Studio

* Open the project if it is not opened
* Click **Run > Run 'app'**
* Choose your device or emulator 

#### From Command Line

For debug:

```
cd /{APP_ROOT}/app/build/outputs/debug
adb install -r app-debug.apk
adb shell am start -n com.darja.moviedb/com.darja.moviedb.activity.main.MainActivity
```

For release:

```
cd /{APP_ROOT}/app/build/outputs/release
adb install -r app-release.apk
adb shell am start -n com.darja.moviedb/com.darja.moviedb.activity.main.MainActivity
```
