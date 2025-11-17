package com.manolo.capacitor.startioads;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.Mrec;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.adsbase.AutoInterstitialPreferences;

import android.view.ViewGroup;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.Gravity;

import java.util.List;
import java.util.ArrayList;

@CapacitorPlugin(name = "StartioAds")
public class StartioAdsPlugin extends Plugin {

    private StartioAds implementation = new StartioAds();
    private RelativeLayout adViewContainer;
    private Banner bannerView;
    private Mrec mrecView;
    private StartAppAd rewardedAd;
    private StartAppAd interstitialAd;

    /**
     * 
     * Methods init Start.io Ad
     */
    @PluginMethod
    public void initParams(PluginCall call) {
        String appId = call.getString("appId");

        if (appId == null || appId.isEmpty()) {
            call.reject("Code 1: Missed 'appId' from Start.io");
            return;
        }
        boolean returnAd = call.getBoolean("returnAd", Boolean.FALSE);

        StartAppSDK.init(getContext().getApplicationContext(), appId, returnAd);
        call.resolve();
    }

    /**
     * 
     * Methods Interstitial Ad
     */
    // --- Load Interstitial Ad
    @PluginMethod
    public void loadInterstitialAd(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (interstitialAd == null) {
                interstitialAd = new StartAppAd(getActivity());
            }

            interstitialAd.loadAd(new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    call.resolve(new JSObject().put("status", "loaded"));
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    String error = ad != null ? ad.getErrorMessage() : "Unknown error";
                    call.reject("Code 2: Error loading Interstitial Ad: " + error);
                }
            });
        });
    }

    // --- Show Interstitial Ad
    @PluginMethod
    public void showInterstitialAd(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (interstitialAd != null && interstitialAd.isReady()) {
                interstitialAd.showAd(new AdDisplayListener() {
                    @Override
                    public void adHidden(Ad ad) {
                        // Closed ad
                        interstitialAd = null;
                    }

                    @Override
                    public void adDisplayed(Ad ad) {
                        // Ad show
                    }

                    @Override
                    public void adClicked(Ad ad) {
                        // Ad click
                    }

                    @Override
                    public void adNotDisplayed(Ad ad) {
                        call.reject("Code 3: The Interstitial Ad could not be displayed " + ad.getErrorMessage());
                    }
                });
                call.resolve();
            } else {
                call.reject("Code 4: The interstitial ad is not ready. Please first call 'loadInterstitialAd'.");
            }
        });
    }

    // --- Enable Exit Ad
    @PluginMethod
    public void enableExitAd(PluginCall call) {
        // Enable the flag so that handleOnBackPressed() can display the Exit Ad
        exitAdEnabled = true;
        call.resolve();
    }

    // --- Disable Exit Ad
    @PluginMethod
    public void disableExitAd(PluginCall call) {
        // Disable the flag so that handleOnBackPressed() ignores the Exit Ad logic
        exitAdEnabled = false;
        call.resolve();
    }

    // --- Overwrites Capacitor's back button handling
    @Override
    public void handleOnBackPressed() {
        if (exitAdEnabled) {
            if (StartAppAd.onBackPressed(getActivity())) {
                return;
            }
        }

        super.handleOnBackPressed();
    }

    // --- Autostitials Ad
    @PluginMethod
    public void autoInterstitial(PluginCall call) {
        boolean enabled = call.getBoolean("enabled", Boolean.FALSE);

        getActivity().runOnUiThread(() -> {
            if (enabled) {
                StartAppAd.enableAutoInterstitial();
            }
            call.resolve();
        });
    }

    // --- Time Frequency Ad
    // Note: Hybric frameworks "Activity change" is limited (since there is only one
    // native activity)
    @PluginMethod
    public void interstitialTimeFrequency(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            AutoInterstitialPreferences preferences = new AutoInterstitialPreferences();
            boolean settingApplied = false;

            Integer seconds = call.getInt("secondsBetweenAds");
            if (seconds != null && seconds > 0) {
                preferences.setSecondsBetweenAds(seconds);
                settingApplied = true;
            }

            Integer activities = call.getInt("activitiesBetweenAds");
            if (activities != null && activities > 0) {
                preferences.setActivitiesBetweenAds(activities);
                settingApplied = true;
            }

            if (settingApplied) {
                StartAppAd.setAutoInterstitialPreferences(preferences);
                call.resolve();
            } else {
                call.reject(
                        "Code 5: You must provide 'secondsBetweenAds' or 'activitiesBetweenAds' with a positive value");
            }
        });
    }

    /**
     * 
     * Methods Banner Ad
     */
    @PluginMethod
    public void showBannerAd(PluginCall call) {
        String position = call.getString("position", "BOTTOM");

        getActivity().runOnUiThread(() -> {
            getAdViewContainer();

            if (bannerView != null) {
                adViewContainer.removeView(bannerView);
                bannerView = null;
            }

            bannerView = new Banner(getActivity());

            RelativeLayout.LayoutParams bannerParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            bannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            if ("TOP".equals(position)) {
                bannerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                bannerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }

            adViewContainer.addView(bannerView, bannerParams);
            call.resolve();
        });
    }

    @PluginMethod
    public void hideBannerAd(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (bannerView != null) {
                adViewContainer.removeView(bannerView);
                bannerView = null;
            }
            call.resolve();
        });
    }

    /**
     * 
     * Methods Rewarded Video Ad
     */
    @PluginMethod
    public void loadRewardedVideoAd(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            rewardedAd = new StartAppAd(getActivity());

            rewardedAd.setVideoListener(new VideoListener() {
                @Override
                public void onVideoCompleted() {
                    JSObject ret = new JSObject();
                    ret.put("earned", true);
                    notifyListeners("rewardedVideoEarned", ret, true);
                }
            });

            AdEventListener loadListener = new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    notifyListeners("rewardedVideoLoaded", new JSObject(), true);
                    call.resolve();
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    String error = ad != null ? ad.getErrorMessage() : "Unknown error";
                    JSObject ret = new JSObject();
                    ret.put("error", error);
                    notifyListeners("rewardedVideoFailed", ret, true);
                    call.reject("Code 6: Error loading rewarded ad: " + error);
                }
            };

            rewardedAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, loadListener);
        });
    }

    @PluginMethod
    public void showRewardedVideoAd(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (rewardedAd != null && rewardedAd.isReady()) {
                AdDisplayListener displayListener = new AdDisplayListener() {
                    @Override
                    public void adHidden(Ad ad) {
                        notifyListeners("rewardedVideoClosed", new JSObject(), true);
                        rewardedAd = null;
                    }

                    @Override
                    public void adDisplayed(Ad ad) {
                        notifyListeners("rewardedVideoOpened", new JSObject(), true);
                    }

                    @Override
                    public void adClicked(Ad ad) {
                        // The user clicked (optional)
                    }

                    @Override
                    public void adNotDisplayed(Ad ad) {
                        String error = ad != null ? ad.getErrorMessage() : "Unknown error";
                        call.reject("Code 7: The ad could not be displayed: " + error);
                    }
                };

                rewardedAd.showAd(displayListener);
                call.resolve();

            } else {
                call.reject("Code 8: The rewarded ad isn't ready. Did you call 'loadRewarded' first?");
            }
        });
    }

    /**
     * 
     * Methods MRec Ad
     */
    @PluginMethod
    public void showMrec(PluginCall call) {
        String position = call.getString("position", "BOTTOM");

        getActivity().runOnUiThread(() -> {
            getAdViewContainer();
            if (mrecView != null) {
                adViewContainer.removeView(mrecView);
                mrecView = null;
            }

            mrecView = new Mrec(getActivity());

            RelativeLayout.LayoutParams mrecParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            mrecParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            if ("TOP".equals(position)) {
                mrecParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                mrecParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }

            adViewContainer.addView(mrecView, mrecParams);
            call.resolve();
        });
    }

    @PluginMethod
    public void hideMrec(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (mrecView != null) {
                adViewContainer.removeView(mrecView);
                mrecView = null;
            }
            call.resolve();
        });
    }

    /**
     * 
     * Methods Native Ad
     */
    // NOTE: This only LOADS the ad data.
    // You are responsible for RENDERING them in your Ionic UI.
    @PluginMethod
    public void loadNativeAd(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            StartAppNativeAd nativeAd = new StartAppNativeAd(getActivity());

            AdEventListener adNativeListener = new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    try {
                        // StartAppNativeAd loadedAd = (StartAppNativeAd) ad;
                        ArrayList<NativeAdDetails> nativeAds = nativeAd.getNativeAds();
                        NativeAdDetails nativeAdDetails = nativeAds.get(0);

                        JSObject adData = new JSObject();
                        adData.put("title", nativeAdDetails.getTitle());
                        adData.put("description", nativeAdDetails.getDescription());
                        adData.put("imageUrl", nativeAdDetails.getImageUrl());
                        adData.put("iconUrl", nativeAdDetails.getSecondaryImageUrl());
                        adData.put("rating", nativeAdDetails.getRating());
                        adData.put("callToAction", nativeAdDetails.getCallToAction());

                        call.resolve(adData);

                    } catch (Exception e) {
                        call.reject("Code 9: Error processing native ad: " + e.getMessage());
                    }
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    String error = ad != null ? ad.getErrorMessage() : "Unknown error";
                    call.reject("Code 10: Error loading Native Ad: " + error);
                }
            };

            nativeAd.loadAd(new NativeAdPreferences(), adNativeListener);
        });
    }

    /**
     * Functions Aux
     * This is the "magic" for Banners/MRec.
     * Creates a RelativeLayout that floats *above* the Capacitor WebView,
     * allowing us to anchor native views (like banners) to it.
     */
    private void getAdViewContainer() {
        if (adViewContainer != null) {
            adViewContainer.bringToFront();
            return;
        }

        adViewContainer = new RelativeLayout(getActivity());

        RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        adViewContainer.setLayoutParams(containerParams);

        adViewContainer.setClickable(false);
        adViewContainer.setFocusable(false);

        ((ViewGroup) getBridge().getWebView().getParent()).addView(adViewContainer);

        adViewContainer.bringToFront();
    }
}
