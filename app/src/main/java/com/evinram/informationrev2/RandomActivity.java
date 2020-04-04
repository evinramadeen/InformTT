package com.evinram.informationrev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RandomActivity extends AppCompatActivity
{
    Button btnNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        btnNothing = findViewById(R.id.btnNothing);

        btnNothing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(RandomActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RandomActivity.this,MainCategories.class));
            }
        });
    }
}
