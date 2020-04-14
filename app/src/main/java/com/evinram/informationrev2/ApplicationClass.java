package com.evinram.informationrev2;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "734ACFB0-309D-78CE-FF65-7C0903FC2500";
    public static final String API_KEY = "AFEA5969-0CCB-41EA-89AD-D6A1CE2C08EE";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Category> categories;
    public static List<SubCategory> subCategories;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //in order to initialize the app, i need the following lines
        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
