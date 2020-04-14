package com.evinram.informationrev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class RandomActivity extends AppCompatActivity
{
    Button btnNothing;
    TextView tvSeeMore;
    Boolean spanning=Boolean.FALSE;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        btnNothing = findViewById(R.id.btnNothing);
        //TextView tvTest = findViewById(R.id.tvTest);
        final TextView tvTest = findViewById(R.id.tvTest);
        //tvSeeMore = findViewById(R.id.tvSeeMore);

        btnNothing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(RandomActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RandomActivity.this,MainCategories.class));
            }
        });

        //final String text ="Not many characters";
        final String text = "I tend to shy away from restaurant chains, but wherever I go, PF Chang&apos;s has solidly good food and, like Starbucks, they&apos;re reliable. We were staying in Boston for a week and after a long day and blah blah blah blah...";
        String dispText ="";
        int textLength=0;

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE && text.length()>100)
        {
            dispText=text.substring(0,100) + "... see More";
            spanning=Boolean.TRUE;

        }
        else if(orientation == Configuration.ORIENTATION_PORTRAIT && text.length()>50)
        {

            dispText=text.substring(0,50) + "... see More";;
            spanning=Boolean.TRUE;
        }

        else
        {
            dispText=text;
            spanning=Boolean.FALSE;

        }

        if(spanning=Boolean.TRUE)
        {
            SpannableString ss = new SpannableString(dispText);

            ClickableSpan clickableSpan = new ClickableSpan()
            {
                @Override
                public void onClick(@NonNull View widget)
                {
                    Toast.makeText(RandomActivity.this, "Click Registered. Start New Activity", Toast.LENGTH_SHORT).show();
                }
            };

            if(orientation==Configuration.ORIENTATION_PORTRAIT)
            {
                ss.setSpan(clickableSpan,50,62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(orientation==Configuration.ORIENTATION_LANDSCAPE)
            {
                ss.setSpan(clickableSpan,100,112, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            tvTest.setText(ss);
            tvTest.setMovementMethod(LinkMovementMethod.getInstance());

        }
        else
        {
            tvTest.setText(text);
        }



        //Below is a second method which requires a second text view but hard to place for text of different lengths
        /*int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE && text.length()>100)
        {
            tvSeeMore.setVisibility(View.VISIBLE);
            dispText=text.substring(0,100);
        }
        else if(orientation == Configuration.ORIENTATION_PORTRAIT && text.length()>100)
        {
            tvSeeMore.setVisibility(View.VISIBLE);
            dispText=text.substring(0,100);
        }

        else
        {
            dispText=text;
            tvSeeMore.setVisibility(View.GONE);

        }
        tvTest.setText(dispText);
        tvSeeMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tvSeeMore.setVisibility(View.GONE);
                tvTest.setText(text);

            }
        });*/
    }
}
