package com.example.mo.myapplication;


import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;

import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;


import java.util.Calendar;

import Actions.SendingEmail.CheckNetworkConnection;
import Actions.SendingEmail.Config;
import Actions.SendingEmail.SendMail;
import DB.DBHelper;

public class CustomKeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;
    static double startTime;
    static double endTime;
    static long charactersText = 0 ;
    static long errors = 0 ;
    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        if(charactersText == 0)
            return;
        endTime = Calendar.getInstance().getTimeInMillis();
        Double inputDauration = (endTime - startTime) / 1000 ;
        Double averageTypeSpeed = (charactersText / inputDauration) ;
        String averageString = Double.toString(averageTypeSpeed);
        averageString = String.format("%.2f",averageTypeSpeed);
        Double errorRate = errors / inputDauration ;

        Log.e("TAG","Input Duration = " + String.valueOf( (endTime-startTime)/1000 ));
        Log.e("TAG","Characters written = " + String.valueOf( charactersText));
        Log.e("TAG","Average speed = " + averageString);


        SharedPreferences learnPreference = getSharedPreferences("CurrentMode" , MODE_PRIVATE);
        int isActive = learnPreference.getInt("Mode",1);

        if(isActive == 1) {
            insertDB(Double.parseDouble(averageString),errorRate);
            Log.e("TAG","Inserted to DB " +averageString);
        } else if(isActive == 2) {
            if (queryDB(Double.parseDouble(averageString))) {
                Toast.makeText(this, "Authroized", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Query DB  " + averageString + "  " + queryDB(Double.parseDouble(averageString)));
            } else {
                Toast.makeText(this, "Not authroized", Toast.LENGTH_SHORT).show();
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (checkforFingerPrint()) {
                        Intent intent = new Intent(this, FingerPrint.class);
                        intent.putExtra("parentActivity", "another");
                        startActivity(intent);
                    }
                }
                    CheckNetworkConnection checkNetworkConnection = new CheckNetworkConnection();
                    if (checkNetworkConnection.checkNetConnection(getApplicationContext())) {
                        SharedPreferences preferences = getSharedPreferences("userInfo",MODE_PRIVATE);
                        String mail = preferences.getString("EMAIL","");
                        SendMail action = new SendMail(getApplicationContext(), mail, "True Owner", "Susspecious Activity Found");
                        action.execute();
                    }
                Log.e("TAG", "Query DB  " + averageString + "  " + queryDB(Double.parseDouble(averageString)));
            }
        } else {

        }
        charactersText = 0 ;
        errors = 0 ;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                errors ++ ;
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
    }


    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        charactersText++;
        if(charactersText == 1)
            startTime = Calendar.getInstance().getTimeInMillis();
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode){
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

                break;
            default:
                char code = (char) primaryCode;
                if(Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }



    void insertDB(double speed , double error){
        DBHelper helper = new DBHelper(this);
        helper.insertTypeSpeed(speed,error);


    }
    boolean queryDB(double speed){
        DBHelper helper = new DBHelper(this);
        return  helper.querySpeed(speed);
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
