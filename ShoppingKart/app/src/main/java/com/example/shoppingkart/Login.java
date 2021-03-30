package com.example.shoppingkart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    Button b1, b2;
    EditText user, pass;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        b1 = findViewById(R.id.btn_login);
        user = findViewById(R.id.et_username);
        pass= findViewById(R.id.et_password);
        client= new AsyncHttpClient();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username= user.getText().toString();
                String str_password= pass.getText().toString();
                final SharedPreferences preferences=getSharedPreferences("MYPREFS",MODE_PRIVATE);
                if (str_username.isEmpty() || str_password.isEmpty()){
                    Toast.makeText(Login.this,"you will need to fill all the fields.", Toast.LENGTH_LONG).show();
                }
                else{
                    String url="http://dev.imagit.pl/wsg_zaliczenie/api/login/"+str_username+"/"+str_password;
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String response= new String(responseBody);
                            if (android.text.TextUtils.isDigitsOnly(response)){
                                if(android.text.TextUtils.isDigitsOnly(response)) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("id", response);
                                    editor.commit();
                                    Toast.makeText(Login.this, "Successfully logged in.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, Main.class);
                                    startActivity(intent);
                                }
                            }
                            else{
                                Toast.makeText(Login.this,"Username or password incorrect.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                }
                //Intent intent = new Intent(Login.this, Main.class);
                //startActivity(intent);
            }
        });

        b2 = findViewById(R.id.btn_register);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}



