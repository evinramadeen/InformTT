package com.evinram.informationrev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import org.w3c.dom.Text;

import java.util.List;

public class RandomActivity extends AppCompatActivity
{
    Button btnNothing;
    TextView tvSeeMore;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    Boolean spanning=Boolean.FALSE;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        btnNothing = findViewById(R.id.btnNothing);
        //TextView tvTest = findViewById(R.id.tvTest);
        final TextView tvTest = findViewById(R.id.tvTest);
        //tvSeeMore = findViewById(R.id.tvSeeMore);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        radioGroup = findViewById(R.id.radioText);

        btnNothing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                Toast.makeText(RandomActivity.this, "button id: "+selectedId+"button text "+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });




        //Below is the original spanning string text for the implementation of the see more function
        /*//final String text ="Not many characters";
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

            dispText=text.substring(0,50) + "... see More";
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
                    startActivity(new Intent(RandomActivity.this, FullDescription.class));

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
        }*/






    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
