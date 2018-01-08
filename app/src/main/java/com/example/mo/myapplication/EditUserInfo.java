package com.example.mo.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import DB.DBHelper;

public class EditUserInfo extends AppCompatActivity {
    private String name;
    private String email;
    private String password;
    private String phone;
    EditText updatedName;
    EditText updatedEmail;
    EditText updatedPassword;
    EditText updatedPhone;
    DBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        updatedName = (EditText) findViewById(R.id.updated_name);
        updatedEmail = (EditText) findViewById(R.id.updated_email);
        updatedPassword = (EditText) findViewById(R.id.updated_password);
        updatedPhone = (EditText) findViewById(R.id.updated_mobile_num);
        changeInfo();

    }

    public void changeInfo() {

        name = getIntent().getStringExtra("name");
        updatedName.setText(name);

        email = getIntent().getStringExtra("email");
        updatedEmail.setText(email);

        password = getIntent().getStringExtra("password");
        updatedPassword.setText(password);

        phone = getIntent().getStringExtra("phone");
        updatedPhone.setText(phone);
    }


    public void onClickSaveButton(View view) {

        SQLiteDatabase db= openOrCreateDatabase("TrueOwner",MODE_PRIVATE,null);
        helper=new DBHelper(this);
        name=updatedName.getText().toString();
        email=updatedEmail.getText().toString();
        password=updatedPassword.getText().toString();
        phone=updatedPhone.getText().toString();
        helper.updateUser(name,phone,email,password);
        Intent intent = new Intent(EditUserInfo.this, profile.class);
        startActivity(intent);
    }
}
