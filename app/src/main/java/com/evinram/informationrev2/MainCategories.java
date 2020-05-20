package com.evinram.informationrev2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
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
import com.backendless.servercode.annotation.Async;

import org.w3c.dom.Text;

import java.util.List;

public class MainCategories extends AppCompatActivity implements SingleChoiceDialogFragment.SingleChoiceListener
{
    ListView lvList;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    TextView tvMainCat,tvSubCat;
    String textSize;

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
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(MainCategories.this,SubCategories.class);
                //intent.putExtra("index",position);
                intent.putExtra("main_category",ApplicationClass.categories.get(position).getMain_category());
                startActivity(intent);
                finish();
            }
        });

//This is the call to backendless to my main category table for information.
        DataQueryBuilder queryBuilder = DataQueryBuilder.create(); //creates the query builder
        queryBuilder.setGroupBy("main_category");// how i want to sort the data basically.

        showProgress(true);
        tvLoad.setText(R.string.retrieving_data_please_wait);

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
    {//This is used to see if the user just wants to exit or if he wants to logout also.
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
                        //MainCategories.this.finish();
                        finishAffinity();

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
                startActivity(new Intent(MainCategories.this,FavoritesActivity.class));

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
                    Toast.makeText(MainCategories.this, "New Text size is: "+ApplicationClass.user.getProperty("text_size"), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                    overridePendingTransition(0,0);
                }

                @Override
                public void handleFault(BackendlessFault fault)
                {
                    Toast.makeText(MainCategories.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
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
