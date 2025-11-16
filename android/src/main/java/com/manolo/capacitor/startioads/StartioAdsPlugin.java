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

    // @PluginMethod
    // public void echo(PluginCall call) {
    //     String value = call.getString("value");

    //     JSObject ret = new JSObject();
    //     ret.put("value", implementation.echo(value));
    //     call.resolve(ret);
    // }

    // --- Methods Interstitial Ads ---
    @PluginMethod
    public void initialize(PluginCall call) {
        String appId = call.getString("appId");

        if (appId == null || appId.isEmpty()) {
            call.reject("Missed 'appId' from Start.io.");
            return;
        }

        StartAppSDK.init(getContext().getApplicationContext(), appId, false);
        call.resolve();
    }

    // Displays a standard full-screen ad
    @PluginMethod
    public void showInterstitial(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            StartAppAd.showAd(getActivity());
            call.resolve();
        });
    }

    // Enables ads that appear when the user
    // presses "back" to exit the app
    @PluginMethod
    public void enableExitAd(PluginCall call) {
        StartAppSDK.enableReturnAds(true);
        call.resolve();
    }

    @PluginMethod
    public void disableExitAd(PluginCall call) {
        StartAppSDK.enableReturnAds(false);
        call.resolve();
    }

    // --- Methods Rewarded Video Ads ---
    @PluginMethod
    public void loadRewarded(PluginCall call) {
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
                    call.reject("Error al cargar rewarded ad: " + error);
                }
            };

            rewardedAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, loadListener);
        });
    }

    @PluginMethod
    public void showRewarded(PluginCall call) {
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
                        call.reject("The ad could not be displayed: " + error);
                    }
                };

                rewardedAd.showAd(displayListener);
                call.resolve();

            } else {
                call.reject("El rewarded ad no está listo. ¿Llamaste a 'loadRewarded' primero?");
            }
        });
    }
    
    // --- Method Banner Ads ---
    @PluginMethod
    public void showBanner(PluginCall call) {
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
    public void hideBanner(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (bannerView != null) {
                adViewContainer.removeView(bannerView);
                bannerView = null;
            }
            call.resolve();
        });
    }

    // --- Method MRec Ads ---
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

    // --- Method de Native Ads ---
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
                        call.reject("Error al procesar el native ad: " + e.getMessage());
                    }
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    String error = ad != null ? ad.getErrorMessage() : "Unknown error";
                    call.reject("Error al cargar Native Ad: " + error);
                }
            };

            nativeAd.loadAd(new NativeAdPreferences(), adNativeListener);
        });
    }


    // --- Funciones Auxiliares ---
    /**
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
