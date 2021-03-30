package com.example.shoppingkart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final SharedPreferences preferences=getSharedPreferences("MYPREFS",MODE_PRIVATE);
        final String user_id=preferences.getString("id","");
        final Button b1 = findViewById(R.id.btn_add);
        final Button b2 = findViewById(R.id.btn_set);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Add.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Settings.class);
                startActivity(intent);
            }
        });

        final ListView lv_lists=findViewById(R.id.lv_lists);
        final ArrayList<String> ItemsList=new ArrayList<>();
        final ArrayAdapter<String> listsAdopter=new ArrayAdapter<>(Main.this,android.R.layout.simple_list_item_1,ItemsList);
        lv_lists.setAdapter(listsAdopter);
        final ArrayAdapter<String> listsAdopter2=new ArrayAdapter<>(Main.this,android.R.layout.simple_list_item_multiple_choice,ItemsList);
        lv_lists.setAdapter(listsAdopter);

        final AsyncHttpClient client=new AsyncHttpClient();
        String url="http://dev.imagit.pl/wsg_zaliczenie/api/items/"+user_id;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String JSON=new String(responseBody);
                try {
                    JSONArray jArray=new JSONArray(JSON);
                    for(int i=0; i<jArray.length();i++){
                        JSONObject jObject=jArray.getJSONObject(i);
                        String item_id= jObject.getString("ITEM_ID");
                        String ListName=jObject.getString("ITEM_NAME");
                        String Desc=jObject.getString("ITEM_DESCRIPTION");
                        ItemsList.add(item_id+". "+ListName+"\n"+Desc);
                    }
                    lv_lists.setAdapter(listsAdopter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        lv_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                lv_lists.setItemChecked(position, true);
            }
        });

        lv_lists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(Main.this);
                builder.setTitle("Delete Item")
                        .setMessage("Do you want to delete this item ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String x=lv_lists.getItemAtPosition(position).toString();
                                String[] a=x.split(". ");
                                String item_id=a[0];
                                String url="http://dev.imagit.pl/wsg_zaliczenie/api/item/delete/"+user_id+"/"+item_id;
                                client.get(url, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                        Toast.makeText(Main.this,"item deleted",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(Main.this,Main.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            }
        });

    }

}

