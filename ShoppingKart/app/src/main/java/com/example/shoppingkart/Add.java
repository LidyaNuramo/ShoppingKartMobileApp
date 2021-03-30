package com.example.shoppingkart;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        final EditText etname = findViewById(R.id.et_name);
        final EditText etdesc = findViewById(R.id.et_desc);
        final Button home = findViewById(R.id.btn_home);
        final Button b1 =findViewById(R.id.btn_submit);
        final Button b2 = findViewById(R.id.btn_set);
        final AsyncHttpClient client=new AsyncHttpClient();
        final SharedPreferences preferences=getSharedPreferences("MYPREFS",MODE_PRIVATE);
        final String user_id=preferences.getString("id","");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etname.getText().toString();
                String desc=etdesc.getText().toString();

                if(name.isEmpty() || desc.isEmpty() ){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Add.this);
                    builder.setTitle("Error")
                            .setMessage("Please fill in the forms properly.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }

                else{
                    String url="http://dev.imagit.pl/wsg_zaliczenie/api/item/add";
                    RequestParams params=new RequestParams();
                    params.put("user",user_id);
                    params.put("name",name);
                    params.put("desc",desc);
                    client.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String Response =new String(responseBody);
                            Toast.makeText(Add.this, Response,
                                    Toast.LENGTH_SHORT).show();
                            if(Response.equals("OK")){
                                Toast.makeText(Add.this, "List added successfully.",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Add.this, Main.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Add.this, "Error adding the list.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(Add.this, "Error adding the list.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add.this, Main.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add.this, Settings.class);
                startActivity(intent);
            }
        });
    }
}
