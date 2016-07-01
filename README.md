Beaconinside Android SDK
==========

Information
--

The minimum Android version for the SDK is Android 2.3 Gingerbread (API level 9).
However beacon detection will only work on devices which have at least Android 4.3 Jelly Bean (API level 18)
and support Bluetooth Low Energy (Bluetooth 4.0).

The SDK has two different AAR Files.

*   beaconinside-sdk-playservices.aar

    Recommended: Needs Google Play Services for better beacon detection with lower power consumption

*   beaconinside-sdk-basic.aar

    Normal SDK without dependencies. Beacon detection will be started when the screen turns on and every 5 minutes to save battery. If a faster detection is needed you have to use the Google Play Services version.


Permissions
--
For geofences and beacon scanning (starting with Android 6.0) you need to add the location permission.

    android.permission.ACCESS_FINE_LOCATION

Importing the library automatically adds all the needed permissions to your app if not already used.

    android.permission.RECEIVE_BOOT_COMPLETED
    android.permission.BLUETOOTH
    android.permission.BLUETOOTH_ADMIN
    android.permission.INTERNET

The Play Services version uses additional permissions

    com.google.android.gms.permission.ACTIVITY_RECOGNITION

Install
--

#### Android Studio

1. Add module to project

    File -> New Module -> More Modules -> Import .JAR or .AAR Package -> Select beaconinside-androidsdk-*version*.aar -> Finish

2. Select module

    File -> Project Structure -> Modules - {Your app module} -> Dependencies -> Add (+) -> Module dependency -> Select beaconinside-androidsdk -> OK

3. Make sure that you have the play services as dependency if you are using the play service version of the Beaconinside SDK. You need the following lines in your gradle file.

```java
compile 'com.google.android.gms:play-services-location:8.4.0'
compile 'com.google.android.gms:play-services-ads:8.4.0'
compile 'com.google.protobuf.nano:protobuf-javanano:3.0.0-alpha-5'
```

Using
--

1. Get your SDK token in the developer zone at https://cms.beaconinside.com

2. Import the BeaconService in your Main Activity:
```java
    import com.beaconinside.androidsdk.BeaconService;
```

3. Copy this code to your Main Activity *onCreate()*, replace YOUR_TOKEN with the token from step 1
```java
    BeaconService.init(this, "YOUR_TOKEN");
```

Beacon Broadcast Intents with Meta Data
--

The Beacon Service library broadcasts by default entry, exit and update events for beacon zones via LocalBroadcastManager.

To get the Broadcasts you have to register a BroadcastReceiver with an IntentFilter for the events you want to receive:

    BeaconService.INTENT_BEACON_REGION_ENTER
    BeaconService.INTENT_BEACON_REGION_UPDATE
    BeaconService.INTENT_BEACON_REGION_EXIT

Additionally you get meta information about the beacon e.g. uuid, major, minor rssi and proximity.
You will also get the beacon meta data you entered on [Beaconinside MANAGE](https://manage.beaconinside.com)

```java

private BroadcastReceiver mReceiver;
private IntentFilter mFilter;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BeaconService.init(this, API_TOKEN);

    mFilter = new IntentFilter();
    mFilter.addAction(BeaconService.INTENT_BEACON_REGION_ENTER);
    mFilter.addAction(BeaconService.INTENT_BEACON_REGION_EXIT);
    mFilter.addAction(BeaconService.INTENT_BEACON_REGION_UPDATE);
    mFilter.addAction(BeaconService.INTENT_CAMPAIGN_NOTIFICATION);
    mFilter.addAction(BeaconService.INTENT_CAMPAIGN_CONVERSION);

    mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BeaconService.INTENT_BEACON_REGION_ENTER:
                    Log.d(TAG, "REGION ENTER " + intent.getExtras().toString());
                    break;
                case BeaconService.INTENT_BEACON_REGION_EXIT:
                    Log.d(TAG, "REGION EXIT " + intent.getStringExtra(BeaconService.INTENT_EXTRA_BEACON_ID));
                    break;
                case BeaconService.INTENT_BEACON_REGION_UPDATE:
                    Log.d(TAG, "REGION UPDATE rssi: " + intent.getIntExtra(BeaconService.INTENT_EXTRA_RSSI, 0)
                            + " proximity: " + intent.getStringExtra(BeaconService.INTENT_EXTRA_PROXIMITY));
                    break;
                case BeaconService.INTENT_CAMPAIGN_NOTIFICATION:
                    Log.d(TAG, "CAMPAIGN NOTIFICATION: " + intent.getExtras());
                    break;
                case BeaconService.INTENT_CAMPAIGN_CONVERSION:
                    Log.d(TAG, "CAMPAIGN CONVERSION: " + intent.getStringExtra(BeaconService.INTENT_EXTRA_CAMPAIGN_ID));
            }
        }
    };
}

@Override
protected void onStart() {
    super.onStart();
    LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mFilter);
}

@Override
protected void onStop() {
    super.onStop();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
}

```

Behaviour
--
The service for beacon scanning starts when the BeaconService.init() is called and runs in the
background till BeaconService.terminate() is called.

The SDK has multiple triggers to start the scanning of beacons in the background.

Play Services version
- Once when the device screen is turned on for 2,5 seconds
- Once every 30 minutes for 2,5 seconds
- Continuously if the device is moving

Basic version
- Once when the device screen is turned on for 2,5 seconds
- Once every 5 minutes for 2,5 seconds


Copyright (c) 2015 BEACONinside GmbH. All rights reserved.
