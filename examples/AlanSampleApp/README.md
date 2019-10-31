# Alan Android SDK example

Alan sample app. One can see how to install SDK and use it methods. 


### 1. Connecting SDK

Add these lines to your *module* level build.gradle file

```java
def AlanMavenRepo = "https://mymavenrepo.com/repo/fSCXIHAoBMWBdlZGqq6n/"


repositories {
    maven { url AlanMavenRepo } // <-- add this line!
    ...
}

dependencies {

    ...
    implementation "app.alan:sdk:3.0.7" // <-- And this
}

``` 

### 2. Place AlanButton view on the layout

```xml

 <com.alan.alansdk.button.AlanButton
       app:layout_constraintBottom_toBottomOf="parent"
       app:layout_constraintEnd_toEndOf="parent"
       android:id="@+id/alanBtn"
       app:button_horizontal_align="right"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"/>
```

### 3. Connect to the Alan studio backend

```java
alanButton = findViewById(R.id.alanBtn)
//        Insert your project key here from "Embed code" button on the tutor.alan.app page
//        Link Alan button with sdk so it can listen to the dialog state and control voice interaction
alanButton!!.initSDK("8e0b083e795c924d64635bba9c3571f42e956eca572e1d8b807a3e2338fdd0dc/stage")
Toast.makeText(this, "Sdk inited successfully", Toast.LENGTH_SHORT).show()

``` 

Please use applied script (*AlanSDK_ExampleScript.txt*) on the Alan Studio side