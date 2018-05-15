#import <React/RCTView.h>
#import <React/RCTComponent.h>
@import UIKit;
@import FBAudienceNetwork;
@class RCTEventDispatcher;

@interface RNGoogleIMAView : UIView<FBInstreamAdViewDelegate>
@property (nonatomic, strong) FBInstreamAdView *adView;
@property (nonatomic, copy) RCTBubblingEventBlock onAdError;
@property (nonatomic, copy) RCTBubblingEventBlock onLoadAd;
@property (nonatomic, copy) RCTBubblingEventBlock onPlayAd;
@property (nonatomic, copy) RCTBubblingEventBlock onStopAd;
@property (nonatomic, copy) RCTBubblingEventBlock onPauseAd;
@property (nonatomic, strong) NSNumber *videoDuration;
- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher;
@end


