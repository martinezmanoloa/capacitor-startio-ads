import type { PluginListenerHandle } from '@capacitor/core';

/**
 * Data for a Native ad.
 * You are responsible for rendering this in your UI (HTML/CSS).
 */
export interface NativeAdData {
  title: string;
  description: string;
  imageUrl: string;
  iconUrl: string;
  rating: number;
  callToAction: string;
}

/**
 * Position options for Banner and MRec ads.
 */
export interface AdViewOptions {
  /**
   * Position of the ad on the screen.
   * @default "BOTTOM"
   */
  position?: 'TOP' | 'BOTTOM';

  /**
   * Margin from the left in pixels.
   * Useful to lift the banner above the TabBar.
   * @default 0
   */
  leftMargin?: number;

  /**
   * Margin from the top in pixels.
   * Useful to lift the banner above the TabBar.
   * @default 0
   */
  topMargin?: number;

  /**
   * Margin from the right in pixels.
   * Useful to lift the banner above the TabBar.
   * @default 0
   */
  rightMargin?: number;

  /**
   * Margin from the bottom in pixels.
   * Useful to lift the banner above the TabBar.
   * @default 0
   */
  bottomMargin?: number;
}

/**
 * Defines the events that the plugin can emit
 * (especially for Rewarded Ads).
 */
export interface StartioAdListeners {
  /**
   * Fires when the rewarded video has finished
   * and the user has earned the reward.
   */
  rewardedVideoEarned: (info: { earned: true }) => void;

  /**
   * Fires when a rewarded ad is loaded
   * and ready to be shown with `showRewardedVideoAd()`.
   */
  rewardedVideoLoaded: () => void;

  /**
   * Fires if the rewarded ad fails to load.
   */
  rewardedVideoFailed: (info: { error: string }) => void;

  /**
   * Fires when the rewarded ad is closed.
   */
  rewardedVideoClosed: () => void;

  /**
   * Fires when the rewarded ad is opened.
   */
  rewardedVideoOpened: () => void;
}

export interface StartioAdsPlugin {
  /**
   * Initializes the Start.io SDK.
   * Must be called once when the app starts.
   */
  initParams(options: {
    appId: string;

    /**
     * @default false
     */
    returnAd?: boolean;

    /**
     * @default true
     */
    enableTest?: boolean;
  }): Promise<void>;

  // --- Interstitials ---
  /**
   * Loads an interstitial ad.
   * Resolves with { status: 'loaded' } when the ad is ready.
   */
  loadInterstitialAd(): Promise<{ status: 'loaded' }>;

  /**
   * Shows a pre-loaded interstitial ad.
   * Call 'loadInterstitialAd' first.
   */
  showInterstitialAd(): Promise<void>;

  /**
   * Enables the "Exit Ad".
   * The SDK will try to show an ad when the user presses "back"
   * to exit the application.
   */
  enableExitAd(): Promise<void>;

  /**
   * Disables the "Exit Ad".
   */
  disableExitAd(): Promise<void>;

  /**
   * Enables or disables Autostitial Ads.
   */
  autoInterstitialAd(options: { enabled: boolean }): Promise<void>;

  /**
   * Sets the time or activity frequency for Autostitial Ads.
   */
  interstitialTimeFrequencyAd(options: {
    /**
     * Minimum time interval between ads (in seconds).
     */
    secondsBetweenAds?: number;

    /**
     * Minimum number of activities between ads.
     */
    activitiesBetweenAds?: number;
  }): Promise<void>;

  // --- Rewarded Video ---
  /**
   * Loads a rewarded video ad.
   * You must listen for the 'rewardedVideoLoaded' event
   * before calling 'showRewardedVideoAd()'.
   */
  loadRewardedVideoAd(): Promise<void>;

  /**
   * Shows a rewarded ad that has already been loaded.
   * You must listen for the 'rewardedVideoEarned' event
   * to know when to give the reward to the user.
   */
  showRewardedVideoAd(): Promise<void>;

  // --- Banner Ads ---
  /**
   * Shows a Banner ad.
   */
  showBannerAd(options?: AdViewOptions): Promise<void>;

  /**
   * Hides and destroys the Banner ad.
   */
  hideBannerAd(): Promise<void>;

  // --- MRec Ads ---
  /**
   * Shows an MRec (Medium Rectangle 300px x 250px) ad.
   */
  showMrec(options?: AdViewOptions): Promise<void>;

  /**
   * Hides and destroys the MRec ad.
   */
  hideMrec(): Promise<void>;

  // --- Native Ads ---
  /**
   * Loads the data for a Native ad.
   * Returns an object with the information (title, image, etc.)
   * for you to render in your UI.
   */
  loadNativeAd(): Promise<NativeAdData>;

  /**
   * Adds a listener for ad events.
   *
   * @param eventName The name of the event
   * @param listenerFunc The callback
   */
  addListener<EventName extends keyof StartioAdListeners>(
    eventName: EventName,
    listenerFunc: StartioAdListeners[EventName],
  ): Promise<PluginListenerHandle>;
}
