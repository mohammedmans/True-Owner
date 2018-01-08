package com.example.mo.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Actions.SendingEmail.Config;
import DB.DBHelper;

public class SignUpActivity extends AppCompatActivity {


    EditText editName;
    EditText editEmail;
    EditText editMopile;
    EditText editPassword;
    EditText editReEnterPassword;
    EditText gmailPassword ;
    String name,email,mopile,password,reEnterPassword;
    String dbName,dbEmail,dbMobile,dbPassword,dbReEnterPassword;
    DBHelper helper;
    Dialog dialog;
    String message;
    Button signUp ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp = (Button) findViewById(R.id.btn_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });

    }

    public void onClick(View v)
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        this.finish();
    }

    public void onClickButton()
    {
        editName=(EditText)findViewById(R.id.input_name);
        editEmail=(EditText)findViewById(R.id.input_email);
        editMopile=(EditText)findViewById(R.id.input_mobile);
        editPassword=(EditText)findViewById(R.id.input_password);
        editReEnterPassword=(EditText)findViewById(R.id.input_reEnterPassword);
       // gmailPassword = (EditText) findViewById(R.id.input_GmailPassword);
        name=editName.getText().toString();
        email=editEmail.getText().toString();
        mopile=editMopile.getText().toString();
        password=editPassword.getText().toString();
        reEnterPassword=editReEnterPassword.getText().toString();

        //--------------------------------------------------------
        if (name.equals("")||email.equals("")||mopile.equals("")||password.equals("")||reEnterPassword.equals("")){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Wrong");
            builder.setMessage("You need to fill all blanks to be able to sign Up please try again");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Thanks...",Toast.LENGTH_SHORT).show();
                }
            });
            dialog=builder.create();
            dialog.show();
        }


        //--------------------------------------------------------
        //check the password confirmarion
        else if(!password.equals(reEnterPassword)){
            editReEnterPassword.setText("");
            // Dialod message for wrong
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Wrong");
            builder.setMessage("Wrong Password confirmation please try again");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Thanks...",Toast.LENGTH_SHORT).show();
                }
            });
            dialog=builder.create();
            dialog.show();
        }
        //--------------------------------------------------------
        //DB
        else{


            SharedPreferences preferences = getSharedPreferences("userInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("EMAIL",email);
            editor.commit();
            editor.apply();


            SQLiteDatabase mydatabase = openOrCreateDatabase("TrueOwner",MODE_PRIVATE,null);
            helper=new DBHelper(this);
            helper.onCreate(mydatabase);
            helper.insertUser(name,mopile,email,password);
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            this.finish();
        }

        //--------------------------------------------------------

    }
}
