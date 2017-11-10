
package com.popsworldwide;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.facebook.ads.*;


public class RNGoogleIMAView extends FrameLayout implements LifecycleEventListener{

  public enum Events {
    EVENT_LOADAD("onLoadAd"),
    EVENT_PLAYAD("onPlayAd"),
    EVENT_ADERROR("onAdError"),
    EVENT_STOPAD("onStopAd"),
    EVENT_PAUSEAD("onPauseAd");

    private final String mName;

    Events(final String name) {
      mName = name;
    }

    @Override
    public String toString() {
      return mName;
    }
  }

  private final ThemedReactContext reactContext;
  private ImaSdkFactory mSdkFactory;
  private AdsLoader mAdsLoader;
  private AdsManager mAdsManager;

  private float videoPosition = 0;
  private float videoDuration = 0;
  private String adsTagURL;
  private String fbPlacementID;
  private RCTEventEmitter mEventEmitter;
  private VideoAdPlayer.VideoAdPlayerCallback videoAdPlayerCallback;
  private VideoAdPlayer mVideoAdPlayer;
  private InstreamVideoAdView adView;


  private boolean mIsAdDisplayed = false;


  private AdErrorEvent.AdErrorListener onAdsError =  new AdErrorEvent.AdErrorListener() {
    @Override public void onAdError(AdErrorEvent adErrorEvent) {
      WritableMap event = Arguments.createMap();
      event.putString("message", adErrorEvent.getError().getMessage());

      mEventEmitter.receiveEvent(getId(), Events.EVENT_ADERROR.toString(), event);
    }
  };

  private AdEvent.AdEventListener onAdsEvent = new AdEvent.AdEventListener() {
    @Override public void onAdEvent(AdEvent adEvent) {

      switch (adEvent.getType()) {
        case LOADED:
          mAdsManager.start();
          mIsAdDisplayed = true;
          break;
        case CONTENT_PAUSE_REQUESTED:
          mIsAdDisplayed = true;

          break;
        case CONTENT_RESUME_REQUESTED:
          mIsAdDisplayed = false;
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

  @Override public void onViewAdded(View child) {
    if(this.adsTagURL!=null){
      String childName = child.getClass().getSimpleName();
      if(childName.toLowerCase().contains("webview")){
        child.measure(getMeasuredWidth(), getMeasuredHeight());
        child.layout(0,0, getMeasuredWidth(), getMeasuredHeight());
      }
    }
    super.onViewAdded(child);
  }


  public RNGoogleIMAView(ThemedReactContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.reactContext.addLifecycleEventListener(this);
    this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));

    mEventEmitter = reactContext.getJSModule(RCTEventEmitter.class);
    initGoogleIMA();
  }


  public void initGoogleIMA(){
    mSdkFactory = ImaSdkFactory.getInstance();
    mAdsLoader = mSdkFactory.createAdsLoader(this.getContext());
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


  public void onVideoCompleted(){
    // Handle completed event for playing post-rolls.
    if (mIsAdDisplayed) {
      if(videoAdPlayerCallback !=null)
        videoAdPlayerCallback.onEnded();
    } else if (mAdsLoader != null){
        mAdsLoader.contentComplete();
    }
  }

  public void setAdsTagURL(String url){
    this.adsTagURL = url;    ;
  }
  public void setFBPlacementID(String url){
    this.fbPlacementID = url;
  }

  public int pxToDp(int px) {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return dp;
  }


  public void requestAds() {
    if(adsTagURL != null){
      mVideoAdPlayer = new VideoAdPlayer() {
        @Override public void playAd() {
          mEventEmitter.receiveEvent(getId(), Events.EVENT_PLAYAD.toString(), null);
        }

        @Override public void loadAd(String s) {
          WritableMap event = Arguments.createMap();
          event.putString("url", s);
          mEventEmitter.receiveEvent(getId(), Events.EVENT_LOADAD.toString(), event);
        }

        @Override public void stopAd() {
          mEventEmitter.receiveEvent(getId(), Events.EVENT_STOPAD.toString(), null);
        }

        @Override public void pauseAd() {
          mEventEmitter.receiveEvent(getId(), Events.EVENT_PAUSEAD.toString(), null);
        }

        @Override public void resumeAd() {
          playAd();
        }

        @Override public void addCallback(VideoAdPlayerCallback callback) {
          videoAdPlayerCallback = callback;
        }

        @Override public void removeCallback(VideoAdPlayerCallback callback) {
          videoAdPlayerCallback = null;
        }

        @Override public VideoProgressUpdate getAdProgress() {

          if (videoPosition <= 0) {
            return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
          }
          return new VideoProgressUpdate((long)videoPosition*1000L, (long)videoDuration*1000L);
        }
      };

      AdDisplayContainer adDisplayContainer = mSdkFactory.createAdDisplayContainer();
      adDisplayContainer.setAdContainer(this);
      adDisplayContainer.setPlayer(mVideoAdPlayer);
      // Create the ads request.
      AdsRequest request = mSdkFactory.createAdsRequest();

      request.setAdTagUrl(adsTagURL);
      request.setAdDisplayContainer(adDisplayContainer);
      request.setAdWillAutoPlay(true);


      // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
      mAdsLoader.requestAds(request);
    }
    else if (fbPlacementID != null){
      /*Facebook ads*/
      adView = new InstreamVideoAdView(
          this.getContext(),
          this.fbPlacementID,
          new AdSize(
              pxToDp(this.getMeasuredWidth()),
              pxToDp(this.getMeasuredHeight())
          )
      );



      adView.setAdListener(new InstreamVideoAdListener() {
        @Override
        public void onAdLoaded(Ad ad) {
          // we have an ad so let's show it
          RNGoogleIMAView.this.addView(adView);
          adView.show();
          mEventEmitter.receiveEvent(getId(), Events.EVENT_LOADAD.toString(), null);
        }


        @Override
        public void onAdVideoComplete(Ad ad) {
          if (adView != null) {
            RNGoogleIMAView.this.removeView(adView);
            adView.destroy();
          }
          mEventEmitter.receiveEvent(getId(), Events.EVENT_STOPAD.toString(), null);
        }

        @Override
        public void onError(Ad ad, AdError adError) {
          WritableMap event = Arguments.createMap();
          event.putString("message", adError.getErrorMessage());

          mEventEmitter.receiveEvent(getId(), Events.EVENT_ADERROR.toString(), event);
        }

        @Override
        public void onAdClicked(Ad ad) {

        }

        @Override public void onLoggingImpression(Ad ad) {

        }
      });
      adView.loadAd();
    }

  }

  public void setContentProgress(float position, float duration){
    videoPosition = position;
    videoDuration = duration;
  }

  public void setVideoPosition(float position){
    if(Math.abs(position - videoDuration) < 0.01 ){
      onVideoCompleted();
    }
    setContentProgress(position, this.videoDuration);
  }

  public void setVideoDuration(float videoDuration){
    setContentProgress(this.videoPosition, videoDuration);
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
    if (adView != null) {
      adView.destroy();
    }
  }
}
