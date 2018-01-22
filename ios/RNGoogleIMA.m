
#import "RNGoogleIMA.h"

@implementation RNGoogleIMA

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

@end

#import "CTKBannerViewManager.h"
#import "CTKBannerView.h"

@implementation CTKBannerViewManager

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (UIView *)view {
    return [CTKBannerView new];
}

RCT_EXPORT_VIEW_PROPERTY(size, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(placementId, NSString)
RCT_EXPORT_VIEW_PROPERTY(onAdPress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdError, RCTBubblingEventBlock)

@end
  
