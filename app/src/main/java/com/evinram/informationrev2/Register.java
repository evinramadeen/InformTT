package com.evinram.informationrev2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Calendar;

public class Register extends AppCompatActivity
{
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    EditText etFirstName,etLastName, etPassword, etEmail, etReEnter,etDOB;
    Button btnRegister;
    DatePickerDialog picker;
    RadioGroup radioGroup;
    RadioButton radioButton;


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Register.this, MainActivity.class));
        Register.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etReEnter = findViewById(R.id.etReEnter);

        etDOB = findViewById(R.id.etDOB);
        etDOB.setInputType(InputType.TYPE_NULL);

        etDOB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                //date Picker dialog

                picker = new DatePickerDialog(Register.this,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                etDOB.setText((month+1)+ "/"+ (dayOfMonth)  + "/" + year);

                            }
                        },year,month,day);
                picker.show();
            }
        });

        //Below deals with the radio group used to allow the user to change the text size if they want from the start.
        radioGroup = findViewById(R.id.radioText);



        btnRegister= findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(etFirstName.getText().toString().isEmpty() ||etLastName.getText().toString().isEmpty()||etEmail.getText().toString().isEmpty()||
                        etPassword.getText().toString().isEmpty()||etReEnter.getText().toString().isEmpty()||etDOB.getText().toString().isEmpty())
                {
                    Toast.makeText(Register.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(etPassword.getText().toString().trim().equals(etReEnter.getText().toString().trim()))
                    {
                        String firstName = etFirstName.getText().toString().trim();
                        String lastName = etLastName.getText().toString().trim();
                        String password= etPassword.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String birthdate = etDOB.getText().toString().trim();
                        String textSize;
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = findViewById(selectedId);
                        String chosenText = radioButton.getText().toString().trim();

                        //The next if statement is to determine which text size did the user select. There may be a smoother way.
                        switch(chosenText)
                        {

                            case "Large Text":
                                textSize="Large";
                                break;

                                //Medium is not checked for because it is the default size.

                            case "Small Text":
                                textSize = "Small";
                                break;

                            default:
                               textSize="Medium";
                               break;
                        }


                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(password); //encrypts and saves to backendless
                        user.setProperty("firstName",firstName); //the name of the column must match the column in backendless
                        user.setProperty("lastName",lastName);
                        user.setProperty("birthdate",birthdate);
                        user.setProperty("text_size",textSize);

                        showProgress(true);
                        tvLoad.setText(R.string.registering_new_user);

                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>()
                        {
                            @Override
                            public void handleResponse(BackendlessUser response)
                            {


                                Toast.makeText(Register.this, "User successfully Registered" , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent (Register.this,Login.class)); //send it to the login page
                                Register.this.finish();//closes this activity and takes us to the previous activity

                            }

                            @Override
                            public void handleFault(BackendlessFault fault)
                            {
                                Toast.makeText(Register.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(Register.this, "Please make sure passwords match!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


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
