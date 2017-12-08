import React  from 'react';
import PropTypes from 'prop-types';
import { requireNativeComponent, View, WebView } from 'react-native';

var iface = {
  name: 'GoogleIMAView',
  propTypes: {
    IMATag: PropTypes.string.isRequired,
    videoPosition: PropTypes.number.isRequired,
    videoDuration: PropTypes.number.isRequired,
    onAdError: PropTypes.func.isRequired,
    onLoadAd: PropTypes.func.isRequired,
    onPlayAd: PropTypes.func.isRequired,
    onStopAd: PropTypes.func.isRequired,
    onPauseAd: PropTypes.func.isRequired,
    FBPlacementID: PropTypes.string.isRequired,
    ...View.propTypes
  },
};

module.exports = requireNativeComponent('RNGoogleIMAView', iface);