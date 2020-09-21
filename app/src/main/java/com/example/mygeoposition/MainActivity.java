package com.example.mygeoposition;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String nameProvider;

    TextView titleField;
    TextView addrField;
    TextView accField;
    TextView latField;
    TextView longField;
    TextView altField;
    TextView spdField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        nameProvider = locationManager.getBestProvider(new Criteria(), false);

        Toast.makeText(MainActivity.this, "onCreate", Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(MainActivity.this, R.string.msg, Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(nameProvider);

        if(location != null){
            onLocationChanged(location);
        }else {
            locationManager.requestLocationUpdates(nameProvider, 1000, 0, this);
        }



    }

    private void initUI() {
        titleField = (TextView) findViewById(R.id.titleApp);
        titleField.setTypeface(Typeface.createFromAsset(getAssets(),"comfortaa-BOLD.ttf"));
        addrField = (TextView)findViewById(R.id.address);
        addrField.setTypeface(Typeface.createFromAsset(getAssets(), "comrortaa_regular.ttf"));
        accField = (TextView)findViewById(R.id.accuracy);
        accField.setTypeface(Typeface.createFromAsset(getAssets(), "comrortaa_regular.ttf"));
        latField = (TextView)findViewById(R.id.latitude);
        latField.setTypeface(Typeface.createFromAsset(getAssets(), "comrortaa_regular.ttf"));
        longField = (TextView)findViewById(R.id.longitude);
        longField.setTypeface(Typeface.createFromAsset(getAssets(), "comrortaa_regular.ttf"));
        spdField= (TextView)findViewById(R.id.speed);
        spdField.setTypeface(Typeface.createFromAsset(getAssets(), "comrortaa_regular.ttf"));
        altField= (TextView)findViewById(R.id.altitude);
        altField.setTypeface(Typeface.createFromAsset(getAssets(), "comrortaa_regular.ttf"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(MainActivity.this, "Enable LOCATION ACCESS in settings", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates(nameProvider, 1000, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(MainActivity.this, "Enable LOCATION ACCESS in settings", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.removeUpdates(MainActivity.this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        String provider = location.getProvider();

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Double altitude = location.getAltitude();
        Float speed = location.getSpeed();
        Float accuracy = location.getAccuracy();

        Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            String address = "";

            if (addressList != null && addressList.size() > 0){
                Log.i("address", addressList.get(0).toString());
                for(int i =0; i < addressList.get(0).getMaxAddressLineIndex(); i++){
                    address += addressList.get(0).getAddressLine(i);

                }
                addrField.setText(getString(R.string.addr) + address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        accField.setText(getString(R.string.acc) + accuracy.toString() +" m");
        latField.setText(getString(R.string.lat) + latitude.toString());
        longField.setText(getString(R.string.lng) + longitude.toString());
        altField.setText(getString(R.string.alt) + altitude.toString() + " m");
        spdField.setText(getString(R.string.spd) + speed.toString() + " m/s");

        Log.i("provider", String.valueOf(provider));
        Log.i("latitude", String.valueOf(latitude));
        Log.i("longitude", String.valueOf(longitude));
        Log.i("altitude", String.valueOf(altitude));
        Log.i("speed", String.valueOf(speed));
        Log.i("accuracy", String.valueOf(accuracy));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}