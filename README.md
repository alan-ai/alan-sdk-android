# Alan SDK for Android

[Alan Platform](https://alan.app/) • [Alan Studio](https://studio.alan.app/register) • [Docs](https://alan.app/docs/intro.html) • [FAQ](https://alan.app/docs/additional/faq.html) •
[Blog](https://alan.app/blog/) • [Twitter](https://twitter.com/alanvoiceai)

[![GitHub release (latest by date)](https://img.shields.io/github/v/release/alan-ai/alan-sdk-android)](https://github.com/alan-ai/alan-sdk-android/releases)

Create a voice script for your application in [Alan Studio](https://studio.alan.app/register) and then add it to your app.

### SDK installation
Config **module-level** build.gradle file.

### Option 1. Add it as a Maven dependency
```java
def AlanMavenRepo = "https://mymavenrepo.com/repo/fSCXIHAoBMWBdlZGqq6n/"

repositories {
	...
	//Add the following line to the repositories section
    maven { url AlanMavenRepo }
}

...

dependencies {
	...
	//Adding the Alan SDK dependency
    implementation "app.alan:sdk:<latest.version>"
}
```

### Option 2. Download aar package and include it manually

Download sdk from releases section [here](https://github.com/alan-ai/alan-android-sdk/releases)
Put in your <project>/app/libs folder (create one if needed) then modify your build.gradle file

```java
repositories {
		...
		//Add the following line to the repositories section
	    flatDir {
	        dirs 'libs'
	    }
}

dependencies {
	...
	//Alan SDK dependency
 	implementation (name: 'AlanSdk-<version>', ext: 'aar')
}
```

## Add the Alan Button to your layout

AlanButton class provides a view with voice button and instance methods to communicate with Alan Studio
Create new AlanButton instance using xml layout:

__activity_main.xml:__


```xml
 <com.alan.alansdk.button.AlanButton
       app:layout_constraintBottom_toBottomOf="parent"
       app:layout_constraintEnd_toEndOf="parent"
       android:id="@+id/alanBtn"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"/>
```

Note that `layout_width`param should be set to `match_parent`

## AlanConfig

Object that describes parameters which will be provided for AlanButton.

Use AlanConfig.Builder to create config with particular parameters

|**Name**  | **Type** | **Description** |
|--|--|--|
| projectId  | String | Project key from Alan Studio |
| dialogId  | String | (Optional) Dialog id for connection to a particular dialog |
| dataObject  | String\|JSONObject | (Optional) Valid JSON string or JSONObject which will be passed to Alan Studio project |

## Connect to your Project in Alan Studio

__MainActivity.java:__

```java

private AlanButton alanButton;

protected void onCreate(Bundle savedInstanceState) {
	...
	alanButton = findViewById(R.id.alanBtn);
    AlanConfig config = AlanConfig.builder()
        .setProjectId("<YOUR_PROJECT_KEY>")
        .build();
	alanButton.initWithConfig(config);
}
```

Now your Alan connection is ready and running. Try run your app and speak with Alan. 

### Handle events from AlanSDK.
There are multiple callbacks coming from Alan backend. 
In order to handle some of them user should extend AlanCallback class and register listener to AlanButton object.

Callbacks definition:

```java
/**
     * Invokes when connection state with backend changed
     * @param connectState
     */
    public void onConnectStateChanged(@NonNull ConnectionState connectState) {

    }

    /**
     * Invokes on button interaction state changed
     * @param dialogState
     */
    public void onDialogStateChanged(@NonNull DialogState dialogState) {

    }

    /**
     * Invokes when user speech is recognized on backend
     * @param eventRecognised
     */
    public void onRecognizedEvent(EventRecognised eventRecognised) {

    }

    /**
     * Invokes when some command from the script side is received
     * @param eventCommand
     */
    public void onCommandReceived(EventCommand eventCommand) {

    }

    /**
     * Invokes on answer from the script
     * @param eventText
     */
    public void onTextEvent(EventText eventText) {

    }

    /**
     * Invokes on any other event
     * @param event
     * @param payload
     */
    public void onEvent(String event, String payload) {

    }

    /**
    * Invokes on Alan errors
    */
    public void onError(String error) {

    }
```

#### Example

```java
AlanCallback myCallback = new AlanCallback() {
            @Override
            public void onCommandReceived(EventCommand eventCommand) {
                super.onCommandReceived(eventCommand);
                //Handle command here
            }
        };
alanButton.registerCallback(myCallback);
```

## Logging

One can set extended logging with static method of the Alan object:

`Alan.enableLogging(true);`

## Sample: 
See our example of Alan Integration [here](https://github.com/alan-ai/alan-sdk-android/tree/master/examples/AlanSampleApp)

### Other platforms:
* [Native iOS](https://github.com/alan-ai/alan-sdk-ios)
* [Web](https://github.com/alan-ai/alan-sdk-web)
* [Cordova](https://github.com/alan-ai/alan-sdk-cordova)
* [ReactNative](https://github.com/alan-ai/alan-sdk-reactnative)
* [Flutter](https://pub.dev/packages/alan_voice)

## Have questions?
If you have any questions or if something is missing in the documentation, please [contact us](mailto:support@alan.app), or tweet us [@alanvoiceai](https://twitter.com/alanvoiceai). We love hearing from you!).
