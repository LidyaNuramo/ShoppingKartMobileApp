package com.example.shoppingkart;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;


public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);

        final Button b1 = findViewById(R.id.btn_register);

        final EditText user=findViewById(R.id.et_username);
        final EditText pass=findViewById(R.id.et_password);
        final EditText emailaddr=findViewById(R.id.et_email);

        final AsyncHttpClient client=new AsyncHttpClient();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=user.getText().toString();
                String password=pass.getText().toString();
                String email=emailaddr.getText().toString();

                if(username.isEmpty() || password.isEmpty() ||email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("Error")
                            .setMessage("Please fill in the forms properly")
                            .setNeutralButton("OK", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                else{
                    String url="http://dev.imagit.pl/wsg_zaliczenie/api/register";
                    RequestParams params=new RequestParams();
                    params.put("login",username);
                    params.put("pass",password);
                    params.put("email",email);
                    client.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String Response =new String(responseBody);
                            Toast.makeText(Register.this, Response,
                                    Toast.LENGTH_SHORT).show();
                            if(Response.equals("OK")){
                                Toast.makeText(Register.this, "Registration successful",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Register.this, "Account already exists.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }

                    });
                }
            }
        });

        final Button b2 = findViewById(R.id.btn_login);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
