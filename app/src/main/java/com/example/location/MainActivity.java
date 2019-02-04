package com.example.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    GPSTracker gpsTracker;
    Double lat,longi;
    Location yourLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        gpsTracker = new GPSTracker(this);
        yourLocation = new Location("A");
        yourLocation.setLatitude(gpsTracker.getLatitude());
        yourLocation.setLongitude(gpsTracker.getLongitude());


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                lat = (Double)dataSnapshot.child("sub/user/Xjpxl1gzfFfpoAVUOjNdvdNKtCH3/location/latitude").getValue();
                longi = (Double)dataSnapshot.child("sub/user/Xjpxl1gzfFfpoAVUOjNdvdNKtCH3/location/longitude").getValue();
                Toast.makeText(MainActivity.this, "lat: "+lat+"\n long:"+longi,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    public void showDistance(View view) {
        TextView textView = findViewById(R.id.distance);
        Double distance = kilometerDistanceBetweenPoints(yourLocation.getLatitude(),yourLocation.getLongitude(),lat,longi);
        textView.setText(String.format("%.2d",Double.toString(distance)));
    }

    private double kilometerDistanceBetweenPoints(Double lat_a, Double lng_a, Double lat_b, Double lng_b) {
        Double pk = (Double) (180.f/Math.PI);

        Double a1 = lat_a / pk;
        Double a2 = lng_a / pk;
        Double b1 = lat_b / pk;
        Double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366 * tt;
    }
//
//    void getLocation() {
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onLocationChanged(Location location) {
//        //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
////            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
////                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//        }catch(Exception e)
//        {
//
//        }
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }

}
