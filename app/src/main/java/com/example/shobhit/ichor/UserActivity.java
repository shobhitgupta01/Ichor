package com.example.shobhit.ichor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    EditText inputName,inputPhone;
    Button btnProceed;
    Spinner spinner;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //EditText
        inputName=(EditText)findViewById(R.id.editText5);
        inputPhone=(EditText)findViewById(R.id.editText6);

        //Button
        btnProceed=(Button)findViewById(R.id.button8);

        //Spinner
        spinner=(Spinner)findViewById(R.id.spinner);

        //initialising the database instance
        root= FirebaseDatabase.getInstance().getReference().getRoot();

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputName.getText().toString().equals("") || inputPhone.getText().toString().equals(""))
                {
                    Toast.makeText(getBaseContext(),"Enter the details!",Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(inputName.getText().toString(),spinner.getSelectedItem().toString(),inputPhone.getText().toString());
                    String key = root.push().getKey();
                    Map<String, Object> userData = user.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/"+key,userData);

                    root.updateChildren(childUpdates);


                    startActivity(new Intent(UserActivity.this,MainActivity.class));
                }
            }
        });
    }
}
