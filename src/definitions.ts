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
   * and ready to be shown with `showRewarded()`.
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
  initialize(options: { appId: string }): Promise<void>;

  // --- Interstitials ---
  /**
   * Shows a standard interstitial (full-screen) ad.
   */
  showInterstitial(): Promise<void>;

  /**
   * Enables the "Exit Ad".
   * The SDK will show an ad when the user presses "back"
   * to exit the application.
   */
  enableExitAd(): Promise<void>;

  /**
   * Disables the "Exit Ad".
   */
  disableExitAd(): Promise<void>;

  // --- Rewarded Video ---
  /**
   * Loads a rewarded video ad.
   * You must listen for the 'rewardedVideoLoaded' event
   * before calling 'showRewarded()'.
   */
  loadRewarded(): Promise<void>;

  /**
   * Shows a rewarded ad that has already been loaded.
   * You must listen for the 'rewardedVideoEarned' event
   * to know when to give the reward to the user.
   */
  showRewarded(): Promise<void>;

  // --- Banner Ads ---
  /**
   * Shows a Banner ad.
   */
  showBanner(options?: AdViewOptions): Promise<void>;

  /**
   * Hides and destroys the Banner ad.
   */
  hideBanner(): Promise<void>;

  // --- MRec Ads ---
  /**
   * Shows an MRec (Medium Rectangle) ad.
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
