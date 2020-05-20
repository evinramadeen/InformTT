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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class SubCategories extends AppCompatActivity implements SingleChoiceDialogFragment.SingleChoiceListener
{
    ListView lvList;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    SubCategoryAdapter subAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        lvList = findViewById(R.id.lvList);


        //final int index = getIntent().getIntExtra("index",0);
        final String mainCategory = getIntent().getStringExtra("main_category");

        String whereClause ="main_category = '"+ mainCategory+"'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("sub_category");

        showProgress(true);
        tvLoad.setText("Getting " + mainCategory+" subcategories, please wait...");
        //Get and display the sub categories.
        Backendless.Persistence.of(SubCategory.class).find(queryBuilder, new AsyncCallback<List<SubCategory>>()
        {
            @Override
            public void handleResponse(List<SubCategory> response)
            {
                ApplicationClass.subCategories=response;
                subAdapter=new SubCategoryAdapter(SubCategories.this,ApplicationClass.subCategories);
                lvList.setAdapter(subAdapter);
                showProgress(false);

            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Toast.makeText(SubCategories.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });

    }

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
                startActivity(new Intent(SubCategories.this,FavoritesActivity.class));
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
                    Toast.makeText(SubCategories.this, "New Text size is: "+ApplicationClass.user.getProperty("text_size"), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                    overridePendingTransition(0,0);
                }

                @Override
                public void handleFault(BackendlessFault fault)
                {
                    Toast.makeText(SubCategories.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(SubCategories.this,MainCategories.class));
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
