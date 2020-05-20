package com.evinram.informationrev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;


public class FullDescription extends AppCompatActivity implements SingleChoiceDialogFragment.SingleChoiceListener
{

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    String userEmail;
    String textSize;
    String mainCategory;


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

        mainCategory = getIntent().getStringExtra("main_category");
        final String subCategory = getIntent().getStringExtra("sub_category");
        final String fullDescrip = getIntent().getStringExtra("full_description");
        //Since I am changing the tables to the Text format, I am going to try and pull the full description from the sub_categories table instead of passing it using intents



        textSize = ApplicationClass.user.getProperty("text_size").toString();
        switch (textSize)
        {
            case "Small":
                tvSubCategory.setTextSize(20);
                tvDescription.setTextSize(14);
                break;
            case "Medium":
                tvSubCategory.setTextSize(28);
                tvDescription.setTextSize(20);
                break;
            case "Large":
                tvSubCategory.setTextSize(36);
                tvDescription.setTextSize(28);
                break;
            default:
                tvSubCategory.setTextSize(24);
                tvDescription.setTextSize(16);
                break;
        }
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
                favorite.setFull_description(fullDescrip);
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
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
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


    //This is for the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {//to change the action bar items for each diff page, make a different layout in the menu folder.
        getMenuInflater().inflate(R.menu.sub_descript, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.favorite:
                Toast.makeText(this, "Showing your current favorites.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FullDescription.this,FavoritesActivity.class));
                finish();

                break;

            case R.id.text_size:
                DialogFragment singleChoiceDialog = new SingleChoiceDialogFragment();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(),"Single Choice Dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //the buttons that go along with the change text size alert dialog.
    @Override
    public void onPositiveButtonClicked(String[] list, int position)
    {

        String newTextSize=""; //just initializing it to make an error go away.

//This switch case is used in conjunction with the alert dialog that comes up when the Text edit button is pressed in the action view.
        switch (list[position])
        {
            case "Small Text":
                newTextSize="Small";
                break;

            case "Medium Text":
                newTextSize="Medium";
                break;

            case "Large Text":
                newTextSize="Large";
                break;


        }
//If the user is trying to change it to a text size he already uses, it makes no sense.
        if (ApplicationClass.user.getProperty("text_size").equals(newTextSize))
        {
            Toast.makeText(this, "Your preferred text size is already "+newTextSize, Toast.LENGTH_LONG).show();
        }
        else
        {
            //Updating the user preferred text size

            showProgress(true);
            tvLoad.setText("Changing your text preference throughout the app...");

            ApplicationClass.user.setProperty("text_size",newTextSize);
            Backendless.UserService.update(ApplicationClass.user, new AsyncCallback<BackendlessUser>()
            {
                @Override
                public void handleResponse(BackendlessUser response)
                {
                    Toast.makeText(FullDescription.this, "New Text size is: "+ApplicationClass.user.getProperty("text_size"), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                    overridePendingTransition(0,0);
                }

                @Override
                public void handleFault(BackendlessFault fault)
                {
                    Toast.makeText(FullDescription.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        }


    }

    @Override
    public void onNegativeButtonClicked()
    {
        Toast.makeText(this, "Font Size not changed.", Toast.LENGTH_SHORT).show();

    }

    //I am adding an OnBackPressed option because Main Category would not refresh and change the font size if user presses back from this activity.

    @Override
    public void onBackPressed()
    {
        Intent intentFull = new Intent(FullDescription.this,SubCategories.class);
        intentFull.putExtra("main_category",mainCategory);
        startActivity(intentFull);
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
