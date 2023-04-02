package com.lancastersysinc.powerpicks;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.lancastersysinc.powerpicks.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name,email;
    Button signOutBtn;
    ImageView user_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_PowerPicks);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        user_image = findViewById(R.id.user_image);
        signOutBtn = findViewById(R.id.signout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);


        //GET GOOGLE ACCESS TOKEN FROM GOOGLE LOGIN
        GoogleSignInAccount googleAcct =
                GoogleSignIn.getLastSignedInAccount(this);

        //SET USER DATA FROM GOOGLE LOGIN
        if(googleAcct!=null){
            String personName = googleAcct.getDisplayName();
            String personEmail = googleAcct.getEmail();
            name.setText(personName);
            email.setText(personEmail);
        }

            //GET FACEBOOK ACCESS TOKEN FROM FACEBOOK LOGIN
            AccessToken fbToken = AccessToken.getCurrentAccessToken();

            //SET USER DATA FROM FACEBOOK LOGIN
            if (fbToken != null) {
                GraphRequest request = GraphRequest.newMeRequest(
                        fbToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    String fullName = object.getString("name");
                                    //String fbEmail = object.getString("email");
                                    String url = object.getJSONObject("picture").
                                            getJSONObject("data").getString("url");
                                    name.setText(fullName);
                                   // email.setText(fbEmail);
                                    Picasso.get().load(url).into(user_image);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }


        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SIGN OUT GOOGLE
                if(googleAcct!=null){
                    signOutGoogle();
                }

                // SIGN OUT FACEBOOK
                if(fbToken != null) {
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void signOutGoogle()
    {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

            }
        });
    }
}
