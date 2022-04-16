package com.example.walkmyandroid1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    //buat textview dan button nya
    private TextView mLocationTextView;
    private Button mLocationButton;

    private final static int REQUEST_LOCATION_PERMISSION = 1;

    //buat variable FusedLocationProviderClient untuk mendapat location saat ini
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mLocationTextView = findViewById(R.id.textview_location);
        mLocationButton = findViewById(R.id.button_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //button mLocation klo dipencet mau ngapain?
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //cek permission yang diberikan respon oleh user yg mana?
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();

                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void getLocation(){
        //sebelum pake views location provider client, cek permission udah granted belum
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
                //klo tidak granted, kita request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
        }
        else {
            //klo udah granted, ngambil location dengan mFusedLocationClient
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                //location nya dicek dulu null atau bukan ?
                    if (location != null){
                        String result = "Latitude" + String.valueOf(location.getLatitude())
                        +"\n Longitude" + String.valueOf(location.getLongitude());

                        mLocationTextView.setText(result);
                    }
                    else{
                        mLocationTextView.setText("Location Not Found");
                    }
                }
            });
        }

    }
}