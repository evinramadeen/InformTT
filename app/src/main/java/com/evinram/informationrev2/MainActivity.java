package com.evinram.informationrev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    Button btnLogin, btnRegister, btnRandom, btnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGuest = findViewById(R.id.btnGuest);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnRandom = findViewById(R.id.btnRandom);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,Login.class));
                MainActivity.this.finish();


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,Register.class));
                MainActivity.this.finish();

            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RandomActivity.class));
                MainActivity.this.finish();
            }
        });
    }
}
