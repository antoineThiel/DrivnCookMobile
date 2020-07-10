package com.example.drivncook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("Registered")
public class SigninActivity extends AppCompatActivity {

    private EditText email;
    private EditText lastName;
    private EditText firstName;
    private EditText password;
    private Button signin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signin = findViewById(R.id.signin);
        lastName = findViewById(R.id.clast);
        firstName = findViewById(R.id.cfirst);
        email = findViewById(R.id.cemail);
        password = findViewById(R.id.cpassword);

        SharedPreferences shp = getSharedPreferences("logged", MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.clear();
        edit.apply();

        SharedPreferences shp2 = getSharedPreferences("order", MODE_PRIVATE);

        SharedPreferences.Editor edit2 = shp2.edit();
        edit2.clear();
        edit2.apply();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().matches("")){
                    email.setError("Veuillez remplir le champ");
                }
                else if (firstName.getText().toString().matches("")){
                    firstName.setError("Veuillez remplir le champ");
                }
                else if (lastName.getText().toString().matches("")){
                    lastName.setError("Veuillez remplir le champ");
                }
                else if (password.getText().toString().matches("")){
                    password.setError("Veuillez remplir le champ");
                }
                else{
                    JSONObject account = new JSONObject();
                    try {
                        account.put("email", email.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        account.put("lastName", lastName.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        account.put("firstName", firstName.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }try {
                        account.put("password", password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = "http://51.210.7.226/signinCustomer";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, account,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.get("status").toString().equals("ok")) {
                                            Intent it = new Intent(SigninActivity.this, MainActivity.class);
                                            startActivity(it);
                                        }
                                        if (response.get("status").toString().equals("exist")){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
                                            builder.setTitle("Erreur lors de l'inscription")
                                                .setMessage("Un utilisateur utilisant cette adresse email existe déjà")
                                                .setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                        if (response.get("status").toString().equals("500")){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
                                            builder.setTitle("Erreur lors de l'inscription")
                                                    .setMessage("Une erreur due au serveur, veuillez réessayer")
                                                    .setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error){

                                }
                            });
                    MySingleton.getInstance(SigninActivity.this).addToRequestQueue(jsonObjectRequest);
                }
            }
        });
    }
}
