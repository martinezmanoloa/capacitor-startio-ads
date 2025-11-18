import { WebPlugin } from '@capacitor/core';
import type { StartioAdsPlugin, AdViewOptions, NativeAdData } from './definitions';

export class StartioAdsWeb extends WebPlugin implements StartioAdsPlugin {
  async initParams(options: { appId: string; returnAd?: boolean }): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }

  // --- Interstitials ---
  async loadInterstitialAd(): Promise<{ status: 'loaded' }> {
    console.warn('Start.io Ads is not available on the web');
    throw this.unavailable('Ads are not available on the web');
  }

  async showInterstitialAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  async enableExitAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  async disableExitAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  async autoInterstitialAd(options: { enabled: boolean }): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }

  async interstitialTimeFrequencyAd(options: {
    secondsBetweenAds?: number;
    activitiesBetweenAds?: number;
  }): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }

  // --- Rewarded Video ---
  async loadRewardedVideoAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
    this.notifyListeners('rewardedVideoFailed', {
      error: 'Not available on web',
    });
  }

  async showRewardedVideoAd(): Promise<void> {
    console.warn('Start.io Ads is not available on the web');
  }

  // --- Banner Ads ---
  async showBannerAd(options?: AdViewOptions): Promise<void> {
    console.warn('Start.io Ads is not available on the web', options);
  }

  async hideBannerAd(): Promise<void> {
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
