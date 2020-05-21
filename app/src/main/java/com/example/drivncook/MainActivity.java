package com.example.drivncook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button signin;
    private EditText loginEntry;
    private EditText passwordEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        signin = findViewById(R.id.signin);
        loginEntry = (EditText)findViewById(R.id.loginEntry);
        passwordEntry = (EditText)findViewById(R.id.passwordEntry);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(loginEntry.getText().toString(), passwordEntry.getText().toString());
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(it);
            }
        });
    }

    private void validate(String userName, String userPassowrd){
        new LoginActivity(this).execute(userName, userPassowrd);
    }
}
