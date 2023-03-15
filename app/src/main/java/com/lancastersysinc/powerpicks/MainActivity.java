package com.lancastersysinc.powerpicks;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isUserLoggedIn = false;
        setTheme(R.style.Theme_PowerPicks);
        super.onCreate(savedInstanceState);
        if (!isUserLoggedIn) {
            setContentView(R.layout.activity_login);
        }
        else {
            //Navigate to Main Activity
            setContentView(R.layout.activity_main);

         }
    }
}
