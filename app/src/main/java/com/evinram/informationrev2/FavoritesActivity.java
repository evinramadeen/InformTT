package com.evinram.informationrev2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity
{
    ListView lvList;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    String userEmail;


    FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvList=findViewById(R.id.lvList);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(FavoritesActivity.this,FullDescription.class);
                intent.putExtra("main_category",ApplicationClass.favorites.get(position).getMain_category());
                intent.putExtra("sub_category", ApplicationClass.favorites.get(position).getSub_category());
                intent.putExtra("full_description",ApplicationClass.favorites.get(position).getFull_description());
                startActivity(intent);
                finish();

            }
        });

        //Next I am going to try to set up something that determines which values are favorited already.
        userEmail=ApplicationClass.user.getEmail();
        String whereClause = "userEmail = '" +userEmail+"'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("sub_category");
        showProgress(true);
        tvLoad.setText(R.string.fetching_fav);


        //Make sure and include a check for when the user has no favorites, a text view can be displayed telling them
        //how to add to favorites.
        Backendless.Data.of(Favorites.class).find(queryBuilder, new AsyncCallback<List<Favorites>>()
        {
            @Override
            public void handleResponse(List<Favorites> response)
            {
                ApplicationClass.favorites=response;
                adapter=new FavoritesAdapter(FavoritesActivity.this,ApplicationClass.favorites);
                lvList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Toast.makeText(FavoritesActivity.this, "Error: " +fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(FavoritesActivity.this,MainCategories.class));
        finish();
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

