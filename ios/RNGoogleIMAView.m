@import FBAudienceNetwork;
#import "RNGoogleIMAView.h"
#import <React/RCTUtils.h>

@interface CTKBannerView () <FBAdViewDelegate>

@end

@interface RNGoogleIMAView : UIViewController <FBInstreamAdViewDelegate>
@property (nonatomic, strong) FBInstreamAdView *adView;

@implementation RNGoogleIMAView

-(void) setFBPlacementID:(NSString *)FBPlacementID{
    
}
