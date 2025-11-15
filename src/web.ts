import { WebPlugin } from '@capacitor/core';
import type { StartioAdsPlugin, AdViewOptions, NativeAdData } from './definitions';

export class StartioAdsWeb extends WebPlugin implements StartioAdsPlugin {
  async initialize(options: { appId: string }): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }

  // --- Interstitials ---
  async showInterstitial(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }
  async enableExitAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }
  async disableExitAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  // --- Rewarded Video ---
  async loadRewarded(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
    this.notifyListeners('rewardedVideoFailed', {
      error: 'Not available on web',
    });
  }
  async showRewarded(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  // --- Banner Ads ---
  async showBanner(options?: AdViewOptions): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }
  async hideBanner(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  // --- MRec Ads ---
  async showMrec(options?: AdViewOptions): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }
  async hideMrec(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  // --- Native Ads ---
  async loadNativeAd(): Promise<NativeAdData> {
    console.warn('Start.io Ads is not available on the web');
    throw this.unavailable('Native Ads are not available on the web');
  }
}
