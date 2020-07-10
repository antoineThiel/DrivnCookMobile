package com.example.drivncook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences shp = getSharedPreferences("logged", MODE_PRIVATE);
        String id = shp.getString("id", null);
        if (id != null){
            Intent it = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(it);
        }

        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.text);
        final Button login = findViewById(R.id.login);
        Button signin = findViewById(R.id.signin);
        final EditText loginEntry = (EditText) findViewById(R.id.loginEntry);
        final EditText passwordEntry = (EditText) findViewById(R.id.passwordEntry);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String loginString = loginEntry.getText().toString();
                final String url ="http://51.210.7.226/check_cred/"+loginString+"/"+passwordEntry.getText().toString();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                     if (response.get("val").toString() == "true"){

                                         SharedPreferences.Editor edit = shp.edit();
                                         String id = response.get("id").toString();
                                         edit.putString("id", id);
                                         edit.apply();
                                         Intent it = new Intent(MainActivity.this, HomeActivity.class);
                                         startActivity(it);
                                     }else{
                                         loginEntry.setError("Wrong logs");
                                     }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                            }
                        });

                //Add Request to the Queue.
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it  = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(it);
            }
        });
    }
}
