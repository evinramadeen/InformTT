package com.evinram.informationrev2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class MainCategories extends AppCompatActivity
{
    ListView lvList;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categories);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvList = findViewById(R.id.lvList);

        //for now I am not using a where clause.
        //String whereClause = "main_category= 'Financial'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create(); //creates the query builder
        queryBuilder.setGroupBy("main_category");// how i want to sort the data basically.

        showProgress(true);
        tvLoad.setText("Retrieving data, please wait!");

        Backendless.Data.of(Category.class).find(queryBuilder, new AsyncCallback<List<Category>>()
        {
            @Override
            public void handleResponse(List<Category> response)
            {
                ApplicationClass.categories=response;
                adapter = new CategoryAdapter(MainCategories.this, ApplicationClass.categories);
                lvList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Toast.makeText(MainCategories.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        final AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainCategories.this);

        dialog2.setMessage("Do you want to Exit or Logout and Exit?");
        dialog2.setPositiveButton("Logout and Exit.", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog2, int which)
            {
                Backendless.UserService.logout(new AsyncCallback<Void>()
                {
                    @Override
                    public void handleResponse(Void response)
                    {
                        Toast.makeText(MainCategories.this, "User Successfully logged out.", Toast.LENGTH_SHORT).show();
                        MainCategories.this.finish();

                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        Toast.makeText(MainCategories.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        dialog2.setNegativeButton("Exit Only", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which)
            {
                MainCategories.this.finish();

            }
        });
        dialog2.show();



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
