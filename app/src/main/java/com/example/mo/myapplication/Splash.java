package com.example.mo.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import DB.DBHelper;

public class Splash extends AppCompatActivity {

    Dialog dialog;
    DBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler mHandler = new Handler();
        mHandler.postDelayed(r , 3000);

    }
    private void checkforFingerPrint(){
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected())
                Toast.makeText(this, "Fingerprint authentication permission not enable", Toast.LENGTH_SHORT).show();
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!fingerprintManager.hasEnrolledFingerprints())
                    { Toast.makeText(this, "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        //finish();
                      }
                }
            }
        }
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            SQLiteDatabase mydatabase = openOrCreateDatabase("TrueOwner",MODE_PRIVATE,null);
            helper=new DBHelper(getApplicationContext());
            helper.onCreate(mydatabase);
            int numOfRows=0;
            numOfRows=helper.numberOfRows("user");

            if(numOfRows==1){
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                    checkforFingerPrint();
                    Intent intent=new Intent(Splash.this,FingerPrint.class);
                    intent.putExtra("parentActivity","splash");
                    startActivity(intent);
                    finish();
                } else{
                    Intent intent=new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            else {
                startActivity(new Intent(Splash.this , MainActivity.class));
                finish();
                 }

        }
    };
}
