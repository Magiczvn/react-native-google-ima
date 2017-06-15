package com.popsworldwide;

import android.support.annotation.Nullable;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;

/**
 * Created by TAMNGUYEN on 6/14/17.
 */

public class RNGoogleIMAViewGroupManager extends ViewGroupManager<RNGoogleIMAView> {
  @Override public String getName() {
    return "RNGoogleIMAView";
  }

  @Override protected RNGoogleIMAView createViewInstance(ThemedReactContext reactContext) {
    return new RNGoogleIMAView(reactContext);
  }

  @Override
  @Nullable
  public Map getExportedCustomDirectEventTypeConstants() {
    MapBuilder.Builder builder = MapBuilder.builder();
    for (RNGoogleIMAView.Events event : RNGoogleIMAView.Events.values()) {
      builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
    }
    return builder.build();
  }

  @ReactProp(name = "IMATag")
  public void setImaTag(final RNGoogleIMAView imaView, String tag) {
    imaView.setAdsTagURL(tag);
    if(tag!=null)
      imaView.requestAds();
  }

  @ReactProp(name = "videoPosition")
  public void setVideoPosition(final RNGoogleIMAView imaView, float position) {
    imaView.setVideoPosition(position);
  }

  @ReactProp(name = "videoDuration")
  public void setVideoDuration(final RNGoogleIMAView imaView, float duration) {
    imaView.setVideoDuration(duration);
  }

}
