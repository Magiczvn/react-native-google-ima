
import { PropTypes } from 'react';
import { requireNativeComponent, View, WebView } from 'react-native';

var iface = {
  name: 'GoogleIMAView',
  propTypes: {
    IMATag: PropTypes.string,
    videoPosition: PropTypes.number,
    videoDuration: PropTypes.number,
    onAdError: PropTypes.func,
    onLoadAd: PropTypes.func,
    onPlayAd: PropTypes.func,
    onStopAd: PropTypes.func,
    onPauseAd: PropTypes.func,
    ...View.propTypes
  },
};

module.exports = requireNativeComponent('RNGoogleIMAView', iface);
