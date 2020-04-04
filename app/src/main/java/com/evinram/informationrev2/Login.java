package com.evinram.informationrev2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity
{
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    TextView tvReset;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Login.this, MainActivity.class));
        Login.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvReset = findViewById(R.id.tvReset);





        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty())
                {

                    Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    showProgress(true); // shows the progress bar while we access the database
                    tvLoad.setText("Logging in, Please wait!");

                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response)
                        {
                            ApplicationClass.user = response; //this is what we do to link the user to their own list of contacts. their own tables basically.
                            Toast.makeText(Login.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Login.this, MainCategories.class));

                            Login.this.finish(); //closes off the login page when the login is successful

                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            Toast.makeText(Login.this, "Error: " +fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    }, true); //stay logged in allows them to stay logged in
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this, Register.class));

            }
        });

        tvReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(etEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter your email address in the email field", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email = etEmail.getText().toString().trim();

                    showProgress(true);
                    tvLoad.setText("Sending reset Instructions, Please check your email.");

                    Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response)
                        {
                            Toast.makeText(Login.this, "Reset instructions sent", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            Toast.makeText(Login.this, "Error: "+fault.getMessage(), Toast.LENGTH_LONG).show();
                            showProgress(false);
                        }
                    });
                }

            }
        });
//will have to move this into my main activity page so that if he is already logged in, would go straight to the main categories xml page.
        showProgress(true);
        tvLoad.setText("Checking Login Credentials. Please wait!");
        //this runs before the on click listeners
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response)
            {
                if(response) //if its a valid login from before
                {
                    //in order to get the user we do the following
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();//this is linked to the stay logged in option we chose on the login button

                    tvLoad.setText("Logging you in, Please wait!");
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response)
                        {//if it is a valid login
                            ApplicationClass.user = response; //if he is already logged in, still link the user to the contacts.
                            startActivity(new Intent(Login.this,MainCategories.class));
                            Login.this.finish();//closes the login activity
                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });
                }
                else
                {
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });
    }


  /*  public void onBackPressed()
    {
            startActivity(new Intent(Login.this, MainActivity.class));
    }
*/
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
