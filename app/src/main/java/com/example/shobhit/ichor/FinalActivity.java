package com.example.shobhit.ichor;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FinalActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnLogout,btnBlood;
    private String userId;
    private ToggleButton canDonate;
    private DatabaseReference root;
    private String bloodGroup;
    private Spinner spinner;
    public DataSnapshot ds;
    public ArrayList donorList;
    public StringBuffer buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        //String Buffer
        buffer = new StringBuffer();

        //Array List
        donorList=new ArrayList();


        //Firebase auth
        auth=FirebaseAuth.getInstance();

        //Database Reference
        root= FirebaseDatabase.getInstance().getReference().getRoot();

        //Buttons
        btnLogout=(Button)findViewById(R.id.buttonLogout);
        btnBlood=(Button)findViewById(R.id.button9);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        //spinner
        spinner=(Spinner)findViewById(R.id.spinner2);

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


        //Getting dataSnapshot for the first time
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ds = dataSnapshot;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Getting dataSnapshot for every change
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ds = dataSnapshot;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //userId
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Toggle Button
        canDonate=(ToggleButton)findViewById(R.id.toggleButton);


        //Changing the canDonate Status of the user
        canDonate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    root.child("users").child(userId).child("canDonate").setValue(true);
                }
                else {
                    root.child("users").child(userId).child("canDonate").setValue(false);
                }
            }
        });

        //Setting the toggle button according to the canDonate status
        root.child("users").child(userId).child("canDonate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean status = (Boolean)dataSnapshot.getValue();
                canDonate.setChecked(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Getting the donor details
        btnBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clearing the list
                donorList.clear();

                //clearing the stringBuffer
                buffer.delete(0,buffer.length());

                //parsing the donorList
                for(DataSnapshot myChild : ds.child("bloodGroups").child(bloodGroup).getChildren()){
                    String key = myChild.getKey();
                    donorList.add(key);

                }

                //parsing the eligible users for details
                for(DataSnapshot myChild : ds.child("users").getChildren()){

                    Boolean canDonate = (Boolean)myChild.child("canDonate").getValue();
                    //if the user is in the list and canDonate
                    if(donorList.contains(myChild.getKey()) && canDonate){
                        buffer.append("\nName :"+myChild.child("name").getValue());
                        buffer.append("\nPhone :"+myChild.child("contact").getValue());
                        buffer.append("\n--------------------------------\n");
                    }
                }

                //if No Donor
                if(buffer.toString().equals(""))
                {
                    buffer.append("No Donor Available");
                }

                //showing the parsed data
                showMessage("Details",buffer.toString());

            }
        });

    }

    //show message method
    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(FinalActivity.this,LoginActivity.class));
    }
}
