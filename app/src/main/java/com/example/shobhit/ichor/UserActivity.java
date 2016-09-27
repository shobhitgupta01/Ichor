package com.example.shobhit.ichor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserActivity extends AppCompatActivity {

    EditText inputName,inputPhone;
    Button btnProceed;
    Spinner spinner;
    DatabaseReference root;

    String bloodGroup;


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





        //bloodGroup
        bloodGroup = spinner.getSelectedItem().toString();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bloodGroup = parent.getItemAtPosition(0).toString();
            }
        });



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

                    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    User user = new User(inputName.getText().toString(),bloodGroup,inputPhone.getText().toString());

                   /* Map<String, Object> userData = user.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(userData);
                    */

                    //adding the data in the users node
                    root.child("users").child(userId).setValue(user);

                    root.child("bloodGroups").child(bloodGroup).child(userId).setValue(userId);
                    startActivity(new Intent(UserActivity.this,MainActivity.class));

                }
            }
        });
    }
}
