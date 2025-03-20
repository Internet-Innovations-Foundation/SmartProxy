## Smart Proxy

[![](https://jitpack.io/v/Internet-Innovations-Foundation/SmartProxy.svg)](https://jitpack.io/#Internet-Innovations-Foundation/SmartProxy)

The ***Smart Proxy*** library enables quick and efficient integration of media streaming capabilities into your application using a proxy to bypass content restrictions in your country.

Currently, the library utilizes [Outline SDK](https://github.com/Jigsaw-Code/outline-sdk) as its proxy solution. In future updates, we plan to add alternative proxy services for even greater flexibility. To facilitate seamless player implementation, the library provides extensions for [ExoPlayer](https://developer.android.com/media/media3/exoplayer), allowing you to stream media content effortlessly. The library is independent of the architecture or UI framework you are using.

## Installation
To integrate the library into your application, add the following repository:

***build.gradle***

```gradle
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		//......
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```

or ***settings.gradle.kts***

```kotlin
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		//....
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}
```

Include the dependency in your project or module:

***build.gradle***

```gradle
implementation 'com.github.Internet-Innovations-Foundation:SmartProxy:X.Y.Z'
```

or ***build.gradle.kts***

```kotlin
implementation("com.github.Internet-Innovations-Foundation:SmartProxy:X.Y.Z")
```

You can find the latest version of the library on [JitPack](https://jitpack.io/#Internet-Innovations-Foundation/SmartProxy)

## Configuration

To enable proxy support in your application, you can either use the default *OutlineProxy* settings or configure it with custom parameters. 
You can view the settings for the default connection strategy [here](https://github.com/Internet-Innovations-Foundation/SmartProxy/blob/main/smart_proxy/src/main/java/org/iif/smart_proxy/utils/OutlideDefaultConfig.kt).
You can learn more about configuring Outline SDK [here](https://github.com/Jigsaw-Code/outline-sdk/tree/main/x/mobileproxy#configure-and-run-the-local-proxy-forwarder).


The following example demonstrates how to initialize and start the proxy using OutlineProxy with its default configuration:

```kotlin
fun getDefaultProxy(): AppProxy {
	val defaultMediaHost = "https://dwamdstream102.akamaized.net/"
	val defaultConfig = OutlineConfigImpl.default(defaultMediaHost)  
	return OutlineProxyImpl(defaultConfig)
}
```
*defaultMediaHost* is used for the correct selection of a strategy in [Outline SDK](https://github.com/Jigsaw-Code/outline-sdk/tree/main/x/mobileproxy#using-the-smart-proxy).

Next, create a proxy manager: 

```kotlin
fun provideProxyManager(proxy: AppProxy): ProxyManager {  
	return ProxyManager(proxy)  
}
```

Once set up, you can interact with the ***ProxyManager***, which provides the following methods:

```kotlin
fun start() // Starts the proxy with the predefined configuration
fun stop() // Stops the proxy
fun updateConfig(config: ProxyConfig) // Updates the configuration and restarts the proxy
fun getHost(): String // Returns the proxy host
fun getPort(): Int  // Returns the proxy port
fun getProxy(): Proxy // Returns a net.Proxy object for use in networking layers
```

Once the ***ProxyManager*** is configured, simply call *start()*, and the *Outline Proxy* will be ready to use.

To enable media playback through *ExoPlayer* using a proxy, apply the extension function to *ExoPlayer*:

```kotlin
ExoPlayer.Builder(context).build().playStream(streamData, proxy)
```

The ***playStream(...)*** function accepts three parameters:

``` 
1.  An implementation of StreamData, which contains the stream URL and stream type.
2.  An implementation of net.Proxy from ProxyManager.
3.  A Boolean flag indicating whether the media should start playing immediately upon player readiness.
```

Once configured, media streaming will commence.

## Updating configuration

If you wish to apply custom settings to Outline Sdk, simply update the configuration using the *updateConfig(config: ProxyConfig)* function within ***ProxyManager***. The new configuration will be automatically applied, and the proxy will restart with the updated settings.

## Custom Proxies

The library allows you to implement custom proxies for your project and integrate them with ***ProxyManager***. To do this, implement the *AppProxy* interface and define the necessary methods.

Example:

```kotlin
class MyCustomProxyImpl() : AppProxy {
	suspend fun start() { ... }
	suspend fun stop()  { ... }
	suspend fun updateConfig(config: ProxyConfig<*>)  { ... }
	fun getHost(): String?  { ... }
	fun getPort(): Int?  { ... }
	fun getConnectionStatus(): ConnectionStatus { ... }
}
```

## Demo Application

The demo application showcases how to utilize the library for implementing a streaming service that plays DW English content.

Upon launch, the app applies the default settings and then retrieves necessary data from a server. The retrieved data includes user information, the stream URL, and the program schedule for the current day.

You can use the demo application as a foundation for building your own streaming app. Simply configure the app to connect to your server and adjust the data models as needed.
