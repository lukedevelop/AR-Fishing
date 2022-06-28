package com.example.arfishing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CatchFish_GPS {
    MainActivity mainActivity;
    LocationManager lm;



    List<Address> citylist = null;
    String[] city;

    String add;


    CatchFish_GPS(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String get_gps() {
        /*this.lm = lm;
        this.geocoder = geocoder;*/
        final Geocoder geocoder = new Geocoder(mainActivity);
        GpsTracker gpsTracker = new GpsTracker(mainActivity);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {

            double latitude2 = gpsTracker.getLatitude();
            double longitude2 = gpsTracker.getLongitude();


            try {
                citylist = geocoder.getFromLocation(latitude2, longitude2, 10);
                if (citylist != null) {
                    city = citylist.get(0).toString().split(" ");
                    add = city[1] + " " + city[2] + " " +city[3] + " " + city[4].split("\"")[0];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return add;
    }
}
