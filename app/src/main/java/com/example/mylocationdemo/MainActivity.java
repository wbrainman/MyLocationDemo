package com.example.mylocationdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLocation";
    private Location mLocation;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        checkPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        Log.d(TAG, "checkPermissions: ");
        boolean b1 = false;
        boolean b2 = false;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: 111");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            b1 = true;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: 222");
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        } else {
            b2 = true;
        }

        if (b1 && b2) {
            onPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: code " + requestCode
                + " permissions " + permissions + " grantResults " + grantResults);
        onPermissionGranted();
    }

    private void onPermissionGranted() {
        String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
        Log.d(TAG, "bestProvider " + bestProvider);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(bestProvider);
        Log.d(TAG, "onStart: getLastKnownLocation " + location);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            mLocation = location;
            Log.d(TAG, "onLocationChanged: ?????? = " + location.getTime());
            Log.d(TAG, "onLocationChanged: ?????? = " + location.getLongitude());
            Log.d(TAG, "onLocationChanged: ?????? = " + location.getLatitude());
            Log.d(TAG, "onLocationChanged: ?????? = " + location.getAltitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: status " +status);
        }
    };

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // ????????????????????????ACCURACY_COARSE???????????????ACCURACY_FINE????????????
        criteria.setSpeedAccuracy(0); // ????????????????????????
        criteria.setCostAllowed(false); // ?????????????????????????????????
        criteria.setBearingAccuracy(0); // ??????????????????????????????
        criteria.setAltitudeRequired(false); // ??????????????????????????????
        criteria.setPowerRequirement(Criteria.POWER_LOW); // ????????????????????????
        return criteria;
    }

}