
#import "RNGoogleIMAViewManager.h"
#import "RNGoogleIMAView.h"
#import <React/RCTBridge.h>

@implementation RNGoogleIMAViewManager

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (UIView *)view {
    
    RNGoogleIMAView *templeView = [[RNGoogleIMAView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
    return templeView;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}


RCT_EXPORT_VIEW_PROPERTY(IMATag, NSString);
RCT_EXPORT_VIEW_PROPERTY(FBPlacementID, NSString);
RCT_EXPORT_VIEW_PROPERTY(videoDuration, NSNumber);

RCT_EXPORT_VIEW_PROPERTY(onAdError, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onLoadAd, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onPlayAd, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onStopAd, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onPauseAd, RCTBubblingEventBlock);
@end

  
