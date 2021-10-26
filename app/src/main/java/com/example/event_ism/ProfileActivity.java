package com.example.event_ism;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    //    FirebaseFirestore fstore;
    FirebaseAuth mFirebaseAuth;
    TextView Name,Email;
    ImageView Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseAuth=FirebaseAuth.getInstance();

        Name=findViewById(R.id.name_disp);
        Email=findViewById(R.id.email_disp);
        Image=findViewById(R.id.image_disp);

        FirebaseUser user =mFirebaseAuth.getCurrentUser();
        if(user!=null)
        {
            Name.setText(user.getDisplayName());
            Email.setText(user.getEmail());
            String URL = String.valueOf(user.getPhotoUrl());
            Glide.with(this)
                    .load(URL)
                    .placeholder(R.drawable.icon_refresh)
                    .into(Image);
        }
    }
}
