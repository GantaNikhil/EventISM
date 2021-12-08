package com.example.event_ism.EventsPage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.event_ism.DisplayActivity;
import com.example.event_ism.ui.events.EventsActivity;
import com.example.event_ism.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {
    FirebaseFirestore fstore;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);
        Button cancel = findViewById(R.id.cancel);
        Button confirm = findViewById(R.id.create_update);

        EditText eventname = findViewById(R.id.event_name);
        EditText organisedby = findViewById(R.id.by);
        TextView date = findViewById(R.id.date);

        fstore=FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            UserID = user.getUid();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String datee = day + "/" + month + "/" + year;
                        date.setText(datee);
                    }
                }, year, month, day
                );

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(CreateEventActivity.this, DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fstore.collection("Events").document();
                String DocID= documentReference.getId();
                Map<String, Object> events = new HashMap<>();
                events.put("event", eventname.getText().toString().trim());
                events.put("by", organisedby.getText().toString().trim());
                events.put("date", date.getText().toString().trim());
                events.put("eventid", UserID);
                events.put("docid",DocID);

                documentReference.set(events).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateEventActivity.this, "Event Organised!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(CreateEventActivity.this,DisplayActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
