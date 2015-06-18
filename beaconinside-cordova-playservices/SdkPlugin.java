package com.beaconinside.androidsdk;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beaconinside.androidsdk.BeaconService;

public class SdkPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        BeaconService.init(this.cordova.getActivity(), "YOUR_TOKEN");
    }

}