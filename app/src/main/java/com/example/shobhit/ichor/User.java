package com.example.shobhit.ichor;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shobhit on 26/9/16.
 */

public class User {

    public String name;
    public String bloodGroup;
    public String contact;
    public Boolean canDonate;


    User()
    {

    }

    User(String name , String bloodGroup, String contact)
    {

        this.name=name;
        this.bloodGroup=bloodGroup;
        this.contact=contact;
        this.canDonate=true;
    }

   /* @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("bloodGroup", bloodGroup);
        result.put("contact",contact);
        result.put("canDonate",canDonate);

        return result;
    }
    */
}
