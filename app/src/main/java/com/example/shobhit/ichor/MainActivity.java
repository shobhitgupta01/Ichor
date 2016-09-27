package com.example.shobhit.ichor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Progress Bar
        progressBar=(ProgressBar)findViewById(R.id.progressBar4);


        //Firebase Instance
        auth = FirebaseAuth.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
            }
                else {
                    //go to final activity of the user
                    startActivity(new Intent(MainActivity.this,FinalActivity.class));
                    finish();

                }
        }
    };




    }//onCreate ends


        @Override
        protected void onResume() {
            super.onResume();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onStart() {
            super.onStart();
            auth.addAuthStateListener(authStateListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (authStateListener != null) {
                auth.removeAuthStateListener(authStateListener);
            }
        }
}
