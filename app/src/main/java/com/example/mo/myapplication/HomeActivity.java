package com.example.mo.myapplication;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import DB.DBHelper;

public class HomeActivity extends AppCompatActivity {


  static public Switch learnMode , activeMode , deactiveMode , fingerprint , typeSpeedFeature , fingerPrintFeature ;
  static public RelativeLayout features ;


    @Override
    protected void onResume() {
        super.onResume();
        final String s= Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        initScreen();




        fingerPrintFeature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preference = getSharedPreferences("Features",MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit() ;
                editor.putBoolean("FingerPrint",b);
                editor.commit(); editor.apply();
            }
        });



        typeSpeedFeature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Log.e("TAG", s + "      " + getPackageName() + "/.CustomKeyboard ");
                    if(!s.equals(getPackageName() + "/.CustomKeyboard")){
                        typeSpeedFeature.setChecked(false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Type speed");
                        builder.setMessage("You must make True owner as default keyboard to enable the feature");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                    }



                    if(new DBHelper(HomeActivity.this).numberOfRows("TYPE_SPEED") > 0){
                        SharedPreferences preference = getSharedPreferences("Features",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preference.edit() ;
                        editor.putBoolean("TypeSpeed",true);
                        editor.commit(); editor.apply();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Type speed Activation");
                        builder.setMessage("To active Type speed learn mode must be on and disable the application");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activeMode.setChecked(false);
                                features.setVisibility(View.GONE);
                                learnMode.setChecked(true);
                                typeSpeedFeature.setChecked(false);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                    }
                } else {
                    SharedPreferences preference = getSharedPreferences("Features",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preference.edit() ;
                    editor.putBoolean("TypeSpeed",false);
                    editor.commit(); editor.apply();
                }
            }
        });






        learnMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                //b == true switch on else off
                if(b)
                {
                    activeMode.setChecked(false);
                    deactiveMode.setChecked(false);

                    //-----------------------------------------------------------------------------
                    writePreference(1);
                    Log.e("TAG","Learning Mode is " + b);
                    //-----------------------------------------------------------------------------
                    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                        if(!checkforFingerPrint())
                            startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                    } else{
                        fingerprint.setVisibility(View.INVISIBLE);
                    }

                }

            }
        });
        activeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {

                if(b) {
                    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (new DBHelper(HomeActivity.this).numberOfRows("TYPE_SPEED") > 0 || checkforFingerPrint()) {

                        } else {
                            Toast.makeText(HomeActivity.this, "Application Must Train First", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if(b)
                {
                    learnMode.setChecked(false);
                    deactiveMode.setChecked(false);
                    features.setVisibility(RelativeLayout.VISIBLE);

                    //-----------------------------------------------------------------------------
                    writePreference(2);
                    Log.e("TAG","Active Mode is " + b);
                    //-----------------------------------------------------------------------------
                    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                        if(!checkforFingerPrint()){
                            startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                        }
                    } else{
                        fingerprint.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    features.setVisibility(LinearLayout.GONE);
                }
            }
        });
        deactiveMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                //b == true switch on else off
                if(b)
                {
                    learnMode.setChecked(false);
                    activeMode.setChecked(false);

                    //-----------------------------------------------------------------------------
                    writePreference(3);
                    Log.e("TAG","Deactivate Mode is " + b);
                    //-----------------------------------------------------------------------------

                }

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        learnMode = (Switch)  findViewById(R.id.switch1);
        activeMode = (Switch)  findViewById(R.id.switch2);
        deactiveMode = (Switch)  findViewById(R.id.switch3);
        fingerprint = (Switch)  findViewById(R.id.switch13);

        typeSpeedFeature = (Switch)  findViewById(R.id.switch12);
        fingerPrintFeature = (Switch) findViewById(R.id.switch13);
        features=(RelativeLayout) findViewById(R.id.layout1);

        /*
        Toolbar tb = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Forget password");
        */
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==  android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    private void initScreen() {


        final String s= Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        Log.e("TAG","  " + (new DBHelper(this).numberOfRows("TYPE_SPEED")  == 0) + "  " +s);

        if(new DBHelper(this).numberOfRows("TYPE_SPEED")  == 0 && s.equals(getPackageName() + "/.CustomKeyboard") == false ){
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Type speed Learning");
            builder.setMessage("To active Type speed learn mode must set True owner as default Keyboard");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }


        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            if(new DBHelper(this).numberOfRows("TYPE_SPEED") > 0   || checkforFingerPrint()){
                activeMode.setEnabled(true);
            } else {
                Log.e("TAG","  " + new DBHelper(this).numberOfRows("TYPE_SPEED") + "  " + checkforFingerPrint());
                activeMode.setEnabled(false);
                Toast.makeText(this,"Application Must train First",Toast.LENGTH_LONG).show();
            }
        } else{
            fingerprint.setVisibility(View.INVISIBLE);
        }

        SharedPreferences preference = getSharedPreferences("Features",MODE_PRIVATE);
        typeSpeedFeature.setChecked(preference.getBoolean("TypeSpeed",false));
        fingerPrintFeature.setChecked(preference.getBoolean("FingerPrint",false));

        SharedPreferences learnPreference = getSharedPreferences("CurrentMode" , MODE_PRIVATE);
        int Mode = learnPreference.getInt("Mode",1);

        if(Mode == 1)
            learnMode.setChecked(true);
        else if(Mode == 2) {
            activeMode.setChecked(true);
            features.setVisibility(RelativeLayout.VISIBLE);
        }
        else if(Mode == 3)
            deactiveMode.setChecked(true);
    }

    public void onClickButtonlogout(View v){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public void onClickButtonProfile(View v){
        Intent intent=new Intent(getApplicationContext(),profile.class);
        startActivity(intent);
    }

    private void writePreference(int mode) {
        SharedPreferences learnPreference = getSharedPreferences("CurrentMode" , MODE_PRIVATE);
        SharedPreferences.Editor learnEditor  = learnPreference.edit();
        learnEditor.putInt("Mode",mode);
        learnEditor.commit(); learnEditor.apply();
    }

    private boolean checkforFingerPrint(){
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected())
                Toast.makeText(this, "Fingerprint authentication permission not enable", Toast.LENGTH_SHORT).show();
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!fingerprintManager.hasEnrolledFingerprints())
                    {   Toast.makeText(this, "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        return  false ;
                    } else {
                        return  true ;
                    }
                }
            }
        }
        return false ;
    }
}
