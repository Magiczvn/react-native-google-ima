
package com.popsworldwide;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.Callback;

public class RNReactNativeGoogleImaPackage implements ReactPackage {
    Callback onAdsStartedCallback, onAdsCompletedCallback, onAdsErrorCallback;

    public RNReactNativeGoogleImaPackage(Callback onAdsStartedCallback, Callback onAdsCompletedCallback, Callback onAdsErrorCallback) {
        this.onAdsStartedCallback = onAdsStartedCallback;
        this.onAdsCompletedCallback = onAdsCompletedCallback;
        this.onAdsErrorCallback = onAdsErrorCallback;
    }

    public RNReactNativeGoogleImaPackage() {
      this(null, null, null);
    }


    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
      return Arrays.<NativeModule>asList(new RNReactNativeGoogleImaModule(reactContext, onAdsStartedCallback, onAdsCompletedCallback, onAdsErrorCallback));
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }
}
