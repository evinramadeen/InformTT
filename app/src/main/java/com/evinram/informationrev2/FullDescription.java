package com.evinram.informationrev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;


public class FullDescription extends AppCompatActivity
{

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    String userEmail;
    int subCategoryIndex; //used to determine what is the index of the subcategory so i can find the objectId
    String objectId; //Used for the remove favorite function


    ImageView ivFavorite;
    ImageView ivNotFav;

    TextView tvSubCategory, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_description);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ivNotFav = findViewById(R.id.ivNotFav);
        tvDescription = findViewById(R.id.tvDescription);
        tvSubCategory = findViewById(R.id.tvSubCategory);
        ivFavorite = findViewById(R.id.ivFav);
//holdfavorites is the list that will hold the sub categories that the user has already favorited.
        final List<String> holdfavorite = new ArrayList<String>();

        final String mainCategory = getIntent().getStringExtra("main_category");
        final String subCategory = getIntent().getStringExtra("sub_category");
        String fullDescrip = getIntent().getStringExtra("full_description");

        //set the information that i passed in from the subCategoryAdapter activity to the text views
        tvSubCategory.setText(subCategory);
        tvDescription.setText(fullDescrip);

        //Next I am going to try to set up something that determines which values are favorited already.
        userEmail=ApplicationClass.user.getEmail();
        String whereClause = "userEmail = '" +userEmail+"'";//determine which user is adding the favorites.
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("sub_category");
        //I may have to remove this or at least change the text because I am not really getting the user's favorites here,
        //I am just trying to see if he already favorited this category. the user doesnt need to know
        showProgress(true);
        tvLoad.setText("Fetching data. Please wait...");

        Backendless.Persistence.of(Favorites.class).find(queryBuilder, new AsyncCallback<List<Favorites>>()
        {
            @Override
            public void handleResponse(List<Favorites> response)
            {
                ApplicationClass.favorites=response;

                //Using a for loop to copy the obtained subCategories into an array list.
                for(int i=0; i < response.size();i++)
                {
                    holdfavorite.add(i,ApplicationClass.favorites.get(i).getSub_category());
                }
                //the contains function checks the array list against the string value that was passed in along with
                //the intent that started this activity.
                Boolean favTest = holdfavorite.contains(subCategory);
                showProgress(false);
                setDisplay(favTest);//so while I am displaying the full description that was passed in with the intent, and I am determining if the subcategory was already in the favorites
                                    //table, set display is called and displays the correct heart as required.

            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Toast.makeText(FullDescription.this, "Error "+fault.getMessage() , Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });



        //When the user presses the Heart Icon, it would either make the subcategory a favorite or remove it from
        //the list of favorites. the two functions below perform that job.
        ivNotFav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Favorites favorite = new Favorites();
                favorite.setMain_category(mainCategory);
                favorite.setSub_category(subCategory);
                favorite.setUserEmail(ApplicationClass.user.getEmail());

                showProgress(true);
                tvLoad.setText("Adding to your favorites.");
            //This is how I add the selected subcategory to favorites. I also added the main category but I actually have not used it yet
                //in the future I can use it as a second layer of testing in case multiple main categories have a subCategory with the same name
                //but i do not think that will happen.
                Backendless.Persistence.save(favorite, new AsyncCallback<Favorites>()
                {
                    @Override
                    public void handleResponse(Favorites response)
                    {
                        Toast.makeText(FullDescription.this, "Added to favorites successfully", Toast.LENGTH_SHORT).show();
                        ivFavorite.setVisibility(View.VISIBLE);
                        ivNotFav.setVisibility(View.GONE);
                        showProgress(false);
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        Toast.makeText(FullDescription.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);

                    }
                });

            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgress(true);
                tvLoad.setText("Removing from Favorites...");
                final int index = holdfavorite.indexOf(subCategory);
                Backendless.Persistence.of(Favorites.class).remove(ApplicationClass.favorites.get(index), new AsyncCallback<Long>()
                {
                    @Override
                    public void handleResponse(Long response)
                    {
                        Toast.makeText(FullDescription.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        ivFavorite.setVisibility(View.GONE);
                        ivNotFav.setVisibility(View.VISIBLE);
                        showProgress(false);
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        Toast.makeText(FullDescription.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);

                    }
                });
            }
        });

    }
    /*The setDisplay function is used to set the display based on if the subCategory was already a favorite of the user or not. it alters which Image view is subsequently
    displayed.
     */
    public void setDisplay(boolean favTest)
    {
        if(favTest ==Boolean.TRUE)
        {
            ivFavorite.setVisibility(View.VISIBLE);
            ivNotFav.setVisibility(View.GONE);

        }
        else if(favTest ==Boolean.FALSE)
        {
            ivNotFav.setVisibility(View.VISIBLE);
            ivFavorite.setVisibility(View.GONE);
        }


    }

    //Here is where I am going to put the action bar method. This action bar is being used to record if the user has favorited any data as well as
    //allows him to change the font. I have not determined if i will also record their choice of font. Should at least try to maintain it throughout
    //the different activities for that session.


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.sub_descript, menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.favorite:
                Toast.makeText(this, "Add favorite clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FullDescription.this,FavoritesActivity.class));

            break;

            case R.id.text_size:
                Toast.makeText(this, "Text Size clicked, Will work on this code", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
