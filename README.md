
# react-native-qr-gallery

## Getting started

`$ npm install react-native-qr-gallery --save`

### Mostly automatic installation

`$ react-native link react-native-qr-gallery`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-qr-gallery` and add `RNQrGallery.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNQrGallery.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNQrGalleryPackage;` to the imports at the top of the file
  - Add `new RNQrGalleryPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-qr-gallery'
  	project(':react-native-qr-gallery').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-qr-gallery/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-qr-gallery')
  	```

## Usage
```javascript
import RNQrGallery from 'react-native-qr-gallery';

try {
    const data = await RNQrGallery.readQRCode();
    if (data === 'CANCEL') {
        return;
    }
    alert(data);
} catch (e) {
    console.log('onError', e);
}

```
  
