
# react-native-google-ima (Work In Progress)

## Getting started

`$ npm install https://github.com/Magiczvn/react-native-google-ima.git --save`

### Mostly automatic installation

`$ react-native link react-native-google-ima`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-react-native-google-ima` and add `RNReactNativeGoogleIma.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNReactNativeGoogleIma.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNReactNativeGoogleImaPackage;` to the imports at the top of the file
  - Add `new RNReactNativeGoogleImaPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-google-ima'
  	project(':react-native-google-ima').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-google-ima/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-google-ima')
  	```

## Usage
```javascript
import RNReactNativeGoogleIma from 'react-native-google-ima';

// TODO: What to do with the module?
RNReactNativeGoogleIma;
```
  
