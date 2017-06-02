
package com.popsworldwide;

import android.view.ViewGroup;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;


public class RNReactNativeGoogleImaModule extends ReactContextBaseJavaModule implements
    LifecycleEventListener{

  private final ReactApplicationContext reactContext;
  private ImaSdkFactory mSdkFactory;
  private AdsLoader mAdsLoader;
  private AdsManager mAdsManager;

  private Integer videoPosition = 0;
  private Integer videoDuration = 0;

  private Callback onAdsStartedCallback;
  private Callback onAdsCompletedCallback;
  private Callback onAdsErrorCallback;

  private boolean mIsAdDisplayed = false;


  private AdErrorEvent.AdErrorListener onAdsError =  new AdErrorEvent.AdErrorListener() {
    @Override public void onAdError(AdErrorEvent adErrorEvent) {
      onAdsErrorCallback.invoke(adErrorEvent.getError().getMessage());
    }
  };

  private AdEvent.AdEventListener onAdsEvent = new AdEvent.AdEventListener() {
    @Override public void onAdEvent(AdEvent adEvent) {

      switch (adEvent.getType()) {
        case LOADED:
          mAdsManager.start();
          break;
        case CONTENT_PAUSE_REQUESTED:
          mIsAdDisplayed = true;
          onAdsStartedCallback.invoke();
          break;
        case CONTENT_RESUME_REQUESTED:
          mIsAdDisplayed = false;
          onAdsCompletedCallback.invoke();
          break;
        case ALL_ADS_COMPLETED:
          if (mAdsManager != null) {
            mAdsManager.destroy();
            mAdsManager = null;
          }
          break;
        default:
          break;
      }
    }
  };



  public RNReactNativeGoogleImaModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.reactContext.addLifecycleEventListener(this);
  }

  @ReactMethod
  public void init(){
    mSdkFactory = ImaSdkFactory.getInstance();
    mAdsLoader = mSdkFactory.createAdsLoader(reactContext.getApplicationContext());
    // Add listeners for when ads are loaded and for errors.
    mAdsLoader.addAdErrorListener(onAdsError);

    mAdsLoader.addAdsLoadedListener(new AdsLoader.AdsLoadedListener() {
      @Override
      public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
        // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
        // events for ad playback and errors.
        mAdsManager = adsManagerLoadedEvent.getAdsManager();

        // Attach event and error event listeners.
        mAdsManager.addAdErrorListener(onAdsError);
        mAdsManager.addAdEventListener(onAdsEvent);
        mAdsManager.init();
      }
    });
  }

  @ReactMethod
  public void onVideoCompleted(){
    // Handle completed event for playing post-rolls.
    if (mAdsLoader != null) {
      mAdsLoader.contentComplete();
    }
  }

  @ReactMethod
  public void requestAds(String  adTagUrl, ReadableMap viewgroup, Callback onAdsStartedCallback, Callback onAdsCompletedCallback, Callback onAdsErrorCallback) {
    final ViewGroup container = (ViewGroup) viewgroup;

    this.onAdsStartedCallback = onAdsStartedCallback;
    this.onAdsCompletedCallback = onAdsCompletedCallback;
    this.onAdsErrorCallback = onAdsErrorCallback;

    AdDisplayContainer adDisplayContainer = mSdkFactory.createAdDisplayContainer();
    adDisplayContainer.setAdContainer(container);

    // Create the ads request.
    AdsRequest request = mSdkFactory.createAdsRequest();
    request.setAdTagUrl(adTagUrl);
    request.setAdDisplayContainer(adDisplayContainer);
    request.setContentProgressProvider(new ContentProgressProvider() {
      @Override
      public VideoProgressUpdate getContentProgress() {
        if (mIsAdDisplayed || videoPosition <= 0) {
          return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
        }
        return new VideoProgressUpdate(videoPosition, videoDuration);
      }
    });

    // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
    mAdsLoader.requestAds(request);
  }

  @ReactMethod
  public void setContentProgress(Integer position, Integer duration){
    videoPosition = position;
    videoDuration = duration;
  }

  @Override
  public void onHostResume() {
    if (mAdsManager != null && mIsAdDisplayed) {
      mAdsManager.resume();
    }

  }

  @Override
  public void onHostPause() {
    if (mAdsManager != null && mIsAdDisplayed) {
      mAdsManager.pause();
    }
  }

  @Override
  public void onHostDestroy() {

  }

  @Override
  public String getName() {
    return "RNReactNativeGoogleIma";
  }
}
