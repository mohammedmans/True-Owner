package FingerPrint;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.mo.myapplication.*;
import Actions.SendingEmail.CheckNetworkConnection;
import Actions.SendingEmail.SendMail;
import Actions.SendingSMS.sendingSMS;
import Actions.openMopileData.MopileData;
import DB.DBHelper;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by reale on 25/11/2016.
 */
@TargetApi(23)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private String parent;
    public FingerprintHandler(Context context,String parent) {
        this.context = context;
        this.parent=parent;
        Log.e("TAG","Hello from handler");
    }

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject,cenCancellationSignal,0,this,null);

    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        DBHelper helper;
        helper=new DBHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor rs= helper.getDataUser(1);
        rs.moveToFirst();
        String dbMobile=rs.getString(rs.getColumnIndex("phone"));
        Toast.makeText(context, "Fingerprint Authentication failed!", Toast.LENGTH_SHORT).show();
        sendingSMS sms=new sendingSMS();
        sms.SendSMS(dbMobile,"There is propability of unauthorized usage , please check your email for more details.");

        //-------------------------------------------------------
        //send Email
        CheckNetworkConnection check=new CheckNetworkConnection();
        if(!check.checkNetConnection(context)){
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            MopileData mopileData=new MopileData();
            try {
                mopileData.setMobileDataEnabled(context,true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }}//--------------------------------------------------------
        //check net connection

        if(check.checkNetConnection(context)){
             sendEmail(context);
        }

    }
    private void sendEmail(Context context) {
        DBHelper helper;
        helper=new DBHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor rs= helper.getDataUser(1);
        rs.moveToFirst();
        String dbEmail=rs.getString(rs.getColumnIndex("email"));
        //Getting content for email
        String email =dbEmail;
        String subject = "True Owner";
        String message = "There is propability of unauthorized usage ";

        //Creating SendMail object
        SendMail sm = new SendMail(context, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        if(parent.equals("splash")){
            context.startActivity(new Intent(context,HomeActivity.class));
        }
        else if(parent.equals( "another")){

        }

    }
}
