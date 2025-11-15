# @martinezmanoloa/capacitor-startio-ads

Capacitor plugin for SDK start.io

## Install

```bash
npm install @martinezmanoloa/capacitor-startio-ads
npx cap sync
```

## API

<docgen-index>

* [`initialize(...)`](#initialize)
* [`showInterstitial()`](#showinterstitial)
* [`enableExitAd()`](#enableexitad)
* [`disableExitAd()`](#disableexitad)
* [`loadRewarded()`](#loadrewarded)
* [`showRewarded()`](#showrewarded)
* [`showBanner(...)`](#showbanner)
* [`hideBanner()`](#hidebanner)
* [`showMrec(...)`](#showmrec)
* [`hideMrec()`](#hidemrec)
* [`loadNativeAd()`](#loadnativead)
* [`addListener(EventName, ...)`](#addlistenereventname-)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialize(...)

```typescript
initialize(options: { appId: string; }) => Promise<void>
```

Initializes the Start.io SDK.
Must be called once when the app starts.

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ appId: string; }</code> |

--------------------


### showInterstitial()

```typescript
showInterstitial() => Promise<void>
```

Shows a standard interstitial (full-screen) ad.

--------------------


### enableExitAd()

```typescript
enableExitAd() => Promise<void>
```

Enables the "Exit Ad".
The SDK will show an ad when the user presses "back"
to exit the application.

--------------------


### disableExitAd()

```typescript
disableExitAd() => Promise<void>
```

Disables the "Exit Ad".

--------------------


### loadRewarded()

```typescript
loadRewarded() => Promise<void>
```

Loads a rewarded video ad.
You must listen for the 'rewardedVideoLoaded' event
before calling 'showRewarded()'.

--------------------


### showRewarded()

```typescript
showRewarded() => Promise<void>
```

Shows a rewarded ad that has already been loaded.
You must listen for the 'rewardedVideoEarned' event
to know when to give the reward to the user.

--------------------


### showBanner(...)

```typescript
showBanner(options?: AdViewOptions | undefined) => Promise<void>
```

Shows a Banner ad.

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#adviewoptions">AdViewOptions</a></code> |

--------------------


### hideBanner()

```typescript
hideBanner() => Promise<void>
```

Hides and destroys the Banner ad.

--------------------


### showMrec(...)

```typescript
showMrec(options?: AdViewOptions | undefined) => Promise<void>
```

Shows an MRec (Medium Rectangle) ad.

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#adviewoptions">AdViewOptions</a></code> |

--------------------


### hideMrec()

```typescript
hideMrec() => Promise<void>
```

Hides and destroys the MRec ad.

--------------------


### loadNativeAd()

```typescript
loadNativeAd() => Promise<NativeAdData>
```

Loads the data for a Native ad.
Returns an object with the information (title, image, etc.)
for you to render in your UI.

**Returns:** <code>Promise&lt;<a href="#nativeaddata">NativeAdData</a>&gt;</code>

--------------------


### addListener(EventName, ...)

```typescript
addListener<EventName extends keyof StartioAdListeners>(eventName: EventName, listenerFunc: StartioAdListeners[EventName]) => Promise<PluginListenerHandle>
```

Adds a listener for ad events.

| Param              | Type                                       | Description           |
| ------------------ | ------------------------------------------ | --------------------- |
| **`eventName`**    | <code>EventName</code>                     | The name of the event |
| **`listenerFunc`** | <code>StartioAdListeners[EventName]</code> | The callback          |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### Interfaces


#### AdViewOptions

Position options for Banner and MRec ads.

| Prop           | Type                           | Description                       | Default               |
| -------------- | ------------------------------ | --------------------------------- | --------------------- |
| **`position`** | <code>'TOP' \| 'BOTTOM'</code> | Position of the ad on the screen. | <code>"BOTTOM"</code> |


#### NativeAdData

Data for a Native ad.
You are responsible for rendering this in your UI (HTML/CSS).

| Prop               | Type                |
| ------------------ | ------------------- |
| **`title`**        | <code>string</code> |
| **`description`**  | <code>string</code> |
| **`imageUrl`**     | <code>string</code> |
| **`iconUrl`**      | <code>string</code> |
| **`rating`**       | <code>number</code> |
| **`callToAction`** | <code>string</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### StartioAdListeners

Defines the events that the plugin can emit
(especially for Rewarded Ads).

| Prop                      | Type                                               | Description                                                                     |
| ------------------------- | -------------------------------------------------- | ------------------------------------------------------------------------------- |
| **`rewardedVideoEarned`** | <code>(info: { earned: true; }) =&gt; void</code>  | Fires when the rewarded video has finished and the user has earned the reward.  |
| **`rewardedVideoLoaded`** | <code>() =&gt; void</code>                         | Fires when a rewarded ad is loaded and ready to be shown with `showRewarded()`. |
| **`rewardedVideoFailed`** | <code>(info: { error: string; }) =&gt; void</code> | Fires if the rewarded ad fails to load.                                         |
| **`rewardedVideoClosed`** | <code>() =&gt; void</code>                         | Fires when the rewarded ad is closed.                                           |
| **`rewardedVideoOpened`** | <code>() =&gt; void</code>                         | Fires when the rewarded ad is opened.                                           |

</docgen-api>
