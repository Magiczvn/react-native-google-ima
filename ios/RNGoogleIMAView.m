#import "RNGoogleIMAView.h"
#import <React/RCTUtils.h>
#import <FBAudienceNetwork/FBAudienceNetwork.h>
#import <React/RCTEventDispatcher.h>
@implementation RNGoogleIMAView {
    RCTEventDispatcher *_eventDispatcher;
    UIViewController *_adViewController;
    NSString *_placementID;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher
{
    if ((self = [super init])) {
        _eventDispatcher = eventDispatcher;
       
    }
    return self;
}

- (void)setIMATag:(NSString *)IMATag {

}

- (void)setFBPlacementID:(NSString *)FBPlacementID{
    _placementID = FBPlacementID;
}

- (void)setVideoDuration:(NSNumber *)videoDuration{
    if([videoDuration integerValue] > 0 & videoDuration != self.videoDuration){
        if(_placementID != NULL){
            [self loadInstreamAd];
            _videoDuration = videoDuration;
        }
    }    
}

- (void)loadInstreamAd
{
    _adViewController = [[UIViewController alloc] init];
    [_adViewController.view setFrame:self.bounds];
    [self addSubview: _adViewController.view];
    
    [FBAdSettings setLogLevel:FBAdLogLevelLog];
    [FBAdSettings addTestDevice:@"5377da984d4daad958301f92914c6a2d4b18da4d"];
    self.adView = [[FBInstreamAdView alloc] initWithPlacementID:_placementID];
    self.adView.delegate = self;
    [self.adView loadAd];
}

- (void)adViewDidLoad:(FBInstreamAdView *)adView
{
    self.onLoadAd([[NSDictionary alloc] initWithObjectsAndKeys:@"placementID", adView.placementID, nil]);
    NSLog(@"Ad is loaded and ready to be displayed");
    
    // The ad can now be added to the layout and shown
    self.adView.frame = self.bounds;
    self.adView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [_adViewController.view addSubview:self.adView];
    [self.adView showAdFromRootViewController:_adViewController];
    [self bringSubviewToFront:_adViewController.view];
}

- (void)adViewDidEnd:(FBInstreamAdView *)adView
{
    NSLog(@"Ad ended"); 
    [self.adView removeFromSuperview];
    self.adView = nil;
    [_adViewController.view removeFromSuperview];
    // The app should now proceed to content
    self.onStopAd(@{});
}

- (void)adView:(FBInstreamAdView *)adView didFailWithError:(NSError *)error
{
    NSLog(@"Ad failed: %@", error.localizedDescription);
    [self.adView removeFromSuperview];
    self.adView = nil;
    [_adViewController.view removeFromSuperview];
    // The app should now proceed to content
    self.onAdError(@{});
}

@end
