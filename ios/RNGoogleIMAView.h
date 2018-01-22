#import <React/RCTView.h>
#import <React/RCTComponent.h>
@import UIKit;

@interface RNGoogleIMAView : UIView

@property (nonatomic, copy) RCTBubblingEventBlock onAdError;
@property (nonatomic, copy) RCTBubblingEventBlock onLoadAd;
@property (nonatomic, copy) RCTBubblingEventBlock onPlayAd;
@property (nonatomic, copy) RCTBubblingEventBlock onStopAd;
@property (nonatomic, copy) RCTBubblingEventBlock onPauseAd;


@property (nonatomic, strong) NSString *IMATag;
@property (nonatomic, strong) NSString *FBPlacementID;

@property (nonatomic, strong) NSNumber *videoPosition;
@property (nonatomic, strong) NSNumber *videoDuration;
@end


