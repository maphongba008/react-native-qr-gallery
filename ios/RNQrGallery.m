
#import "RNQrGallery.h"
#import <React/RCTConvert.h>
#import <AssetsLibrary/AssetsLibrary.h>
#import <AVFoundation/AVFoundation.h>
#import <React/RCTUtils.h>

@import MobileCoreServices;
@interface RNQrGallery ()

@property (nonatomic, strong) UIImagePickerController *picker;
@property (nonatomic, strong) RCTPromiseResolveBlock callback;
@property (nonatomic, strong) RCTResponseErrorBlock failureCallback;


@end

@implementation RNQrGallery

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(readQRCode:(RCTPromiseResolveBlock)success failure:(RCTResponseErrorBlock)failure){
    self.callback = success;
    self.failureCallback = failure;
    
    self.picker = [[UIImagePickerController alloc] init];
    self.picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    self.picker.delegate = self;
    dispatch_sync(dispatch_get_main_queue(), ^{
        UIViewController *root = RCTPresentedViewController();
        [root presentViewController:self.picker animated:YES completion:nil];
    });
    
}

-(NSString*)readQRCode:(CIImage*)ciImage {
    CIDetector *detector = [CIDetector detectorOfType:CIDetectorTypeQRCode context:nil options:@{CIDetectorAccuracy:CIDetectorAccuracyHigh}];
    NSArray *features = [detector featuresInImage:ciImage];
    NSLog(@"FEATURES SIZE: %ld", features.count);
    if (!features || features.count == 0) {
        return nil;
    }
    CIQRCodeFeature *feature = [features objectAtIndex:0];
    NSString *scannedResult = feature.messageString;
    return scannedResult;
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:nil];
    self.callback(@"CANCEL");
}

- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<UIImagePickerControllerInfoKey,id> *)info {
    
    [picker dismissViewControllerAnimated:YES completion:nil];
    UIImage *image = info[UIImagePickerControllerOriginalImage];
    CIImage *ciImage = [CIImage imageWithCGImage:image.CGImage];
    NSString *result = [self readQRCode:ciImage];
    self.callback(result);
}


@end
  
