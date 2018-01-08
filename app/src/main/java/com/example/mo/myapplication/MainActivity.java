package com.example.mo.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import DB.DBHelper;


public class MainActivity extends AppCompatActivity {

    Dialog dialog;
    EditText editEmail;
    EditText editPassword;
    String mail,pass;
    DBHelper helper;
    DBHelper helper1;
    String dbMail,dbPassword;
    TextView signUp;
    Button loginBtn ;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });

        signUp=(TextView)findViewById(R.id.link_signup);
        SQLiteDatabase database = openOrCreateDatabase("TrueOwner",MODE_PRIVATE,null);
        helper1=new DBHelper(this);
        int numOfRows=helper1.numberOfRows("user");
        String x=Integer.toString(numOfRows);
        //Toast.makeText(getApplicationContext(),x,Toast.LENGTH_LONG).show();
        if(numOfRows==1){
            signUp.setVisibility(View.INVISIBLE);
        }
    }
    public void onClick(View v)
    {
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
        this.finish();
    }

    public void onClickButton()
    {
        editEmail=(EditText)findViewById(R.id.input_email);
        editPassword=(EditText)findViewById(R.id.input_password);
        mail=editEmail.getText().toString();
        pass=editPassword.getText().toString();

        //----------------------------------------
        SQLiteDatabase mydatabase = openOrCreateDatabase("TrueOwner",MODE_PRIVATE,null);
        helper=new DBHelper(this);
        helper.onCreate(mydatabase);
        int numOfRows=0;

        numOfRows=helper.numberOfRows("user");

       if(numOfRows==1){
        //helper.insertUser("Maged","01028107886","maged.taliawy@gmail.com","1234",50.5,50.5);
        Cursor rs = helper.getDataUser(1);
        rs.moveToFirst();
        dbMail = rs.getString(rs.getColumnIndex("email"));
        dbPassword = rs.getString(rs.getColumnIndex("password"));
        //Toast.makeText(getApplicationContext(),dbMail,Toast.LENGTH_LONG);
        if (dbMail.equals(mail)&&dbPassword.equals(pass)){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            this.finish();
        }

        else {
           // Toast.makeText(this, "Wrong Email or Password ", Toast.LENGTH_LONG).show();
            //------------------------------------------------------------------
            // Dialod message for wrong
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong");
            builder.setMessage("Wrong Email or Password try again");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(MainActivity.this, "Thanks...", Toast.LENGTH_SHORT).show();
                }
            });
            dialog = builder.create();
            dialog.show();
            //---------------------------------------------
            //to clear an edit text when foucus on it
            editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editEmail.setText("", TextView.BufferType.EDITABLE);
                    }
                }

            });
            editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editPassword.setText("", TextView.BufferType.EDITABLE);
                    }
                }
            });
        }
        }
        else{

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Warning");
           builder.setMessage("You have not an account yet \n Do you want to create an account ?");
           builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                  // Toast.makeText(MainActivity.this, "Thanks...", Toast.LENGTH_SHORT).show();
                   Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                   startActivity(intent);

               }
           });
           builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                  Toast.makeText(getApplicationContext(),"Please create new account first from link below the login button",Toast.LENGTH_LONG);
               }
           });
           dialog = builder.create();
           dialog.show();
       }
        //----------------------------------------------------------------------
    }





}
