package com.example.mo.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import DB.DBHelper;

public class profile extends AppCompatActivity {

    TextView viewName;
    TextView viewEmail;
    TextView viewPassword;
    TextView viewMobile;
    DBHelper helper;
    String dbName,dbEmail,dbPassword,dbMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewName=(TextView)findViewById(R.id.username);
        viewEmail=(TextView)findViewById(R.id.usermail);
        viewPassword=(TextView)findViewById(R.id.userpassword);
        viewMobile=(TextView)findViewById(R.id.userphone);
        //-----------------------------------------------------------
        SQLiteDatabase database=openOrCreateDatabase("TrueOwner",MODE_PRIVATE,null);
        helper=new DBHelper(this);
        Cursor rs= helper.getDataUser(1);
        rs.moveToFirst();
        dbName=rs.getString(rs.getColumnIndex("name"));
        dbEmail=rs.getString(rs.getColumnIndex("email"));
        dbPassword=rs.getString(rs.getColumnIndex("password"));
        dbMobile=rs.getString(rs.getColumnIndex("phone"));
        viewName.setText(dbName);
        viewEmail.setText(dbEmail);
        viewPassword.setText(dbPassword);
        viewMobile.setText(dbMobile);

    }

    public void onClickEditButton(View view) {
        Intent i = new Intent(profile.this, EditUserInfo.class);
        i.putExtra("name",dbName);
        i.putExtra("email", dbEmail);
        i.putExtra("password", dbPassword);
        i.putExtra("phone", dbMobile);
        startActivity(i);
    }
}
