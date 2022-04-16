package com.example.walkmyandroid1;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {


    //buat variable context
    private Context mContext;

    //buat variable
    public OnTaskCompleted mListener;

    //buat interface biar mainActivity tau
    interface OnTaskCompleted{
        void OnTaskCompleted(String result);
    }

    //buat constructor
    public FetchAddressTask (Context context, OnTaskCompleted listener){
        mContext = context;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Location location = locations[0];
        //alamat nya udah dapet dari location yg diparse, kemudian diubah jadi alamat pake geocoder
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        //dari geocoder memberikan respon list of address, buat variable nya
        List<Address> addresses = null;
        //variable untuk nampung alamatnya
        String resultMessage = "";

        //mencari alamat latitude n longitude location
        try {
            //karena kembalian dari geocodernya list,
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            //diatas bentuknya array, cek dulu ada ga dapet respon ga
            if (addresses == null || addresses.size() == 0){
                resultMessage = "Address Not Found";
            }
            //klo ada, ambil yg pertama
            else {
                Address address = addresses.get(0);
                //dari address ini terdiri beberapa line, harus dipisahkan
                ArrayList<String> parts = new ArrayList<>();
                for(int i=0; i<= address.getMaxAddressLineIndex(); i++){
                    //masukkan ke array
                    parts.add(address.getAddressLine(0));
                }
                //dari array ditampilkan di resultMessage
                resultMessage = TextUtils.join("\n", parts);

            }

        }
        catch (IOException ex){
            Log.d("Exception", "unable to get address");
        }

        return resultMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        //trigger supaya main Activity tau, result alamatnya ini biar ditampilin didepan
        mListener.OnTaskCompleted(s);
        super.onPostExecute(s);
    }
}
