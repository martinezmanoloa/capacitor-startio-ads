# @martinezmanoloa/capacitor-startio-ads

Capacitor plugin for SDK start.io (only android)

## Install

```bash
npm install @martinezmanoloa/capacitor-startio-ads
npx cap sync
```

...

## Methods

```typescript
// You can use AdViewOptions NativeAdData
import { StartioAds } from '@martinezmanoloa/capacitor-startio-ads';

initParams(); // Init sdk start.io with your appId from account, test with 205489527, in prod use enableTest: false
loadInterstitialAd(); // Loading your InterstitialAd before use showInterstitialAd()
showInterstitialAd(); // Show fullscreen ad
showBannerAd(); // Show banner ad in TOP|BOTTOM with param options: AdViewOptions = {}
hideBannerAd(); // Hide your banner ad
loadRewardedVideoAd(); // Loading video reward ad before use showRewardedVideoAd()
showRewardedVideoAd(); // Show video ad
showMrec(); // Show ad (Medium Rectangle 300px x 250px)
hideMrec(); // Hide your Mrec ad
// autoInterstitialAd(); // In Capacitor Single Activity Application not working
// interstitialTimeFrequencyAd(); // In Capacitor Single Activity Application not working
loadNativeAd(); // returns the raw data of the ad (Title, Description, Image URL, etc.) in a JSON object. You are responsible for rendering this data in your application's HTML/CSS.
```

**Important:** Currently, this plugin **does not automatically handle click registration or impression tracking** for Native Ads. The Start.io SDK requires a native Android View to bind interactions (`registerViewForInteraction`), which is not directly accessible from the HTML layer.

**Use Case:** Use this format if you need to display promoted content visually to match your app's design, but be aware that standard click-through attribution metrics may not be tracked by the SDK in this specific implementation.

...

## 📘 Documentation

The documentation for the underlying Start.io Android SDK integration (which this plugin uses) can be found here: **https://support.start.io/hc/en-us/articles/360014774799-Integration-via-Maven**

...

## ⚠️ Licensing & Attribution ⚠️

This plugin is licensed under the **MIT License** (Copyright 2025 Manolo Martínez).

The underlying Android SDK (Start.io) is distributed under the **Apache License, Version 2.0**. You must comply with the terms of the Apache License, Version 2.0, when using the plugin's native Android component.

A copy of the Apache License 2.0 is provided in the NOTICE file and/or LICENSE-APACHE.md.
