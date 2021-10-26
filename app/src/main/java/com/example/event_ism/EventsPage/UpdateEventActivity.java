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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateEventActivity extends AppCompatActivity {
    EditText eventname, by;
    Button update, cancel;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);

        eventname = findViewById(R.id.event_name);
        by = findViewById(R.id.by);
        date = findViewById(R.id.date);
        update = findViewById(R.id.create_update);
        cancel = findViewById(R.id.cancel);
        update.setText("Update");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateEventActivity.this, DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        if (getIntent().hasExtra("event_name")) {
//        /*not required btw it is always true.. used for checking extras*/

        String Event_name = getIntent().getStringExtra("event_name");
        String By = getIntent().getStringExtra("organised_by");
        String Date = getIntent().getStringExtra("date");
        String Docid = getIntent().getStringExtra("docid");

        eventname.setText(Event_name);
        by.setText(By);
        date.setText(Date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        date.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!(Event_name.equals(eventname.getText().toString().trim())))
                        || (!(By.equals(by.getText().toString().trim()))) ||
                        (!(Date.equals(date.getText().toString().trim())))) {

                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Events").document(Docid);
                    Map<String, Object> change = new HashMap<>();
                    change.put("event", eventname.getText().toString().trim());
                    change.put("by", by.getText().toString().trim());
                    change.put("date", date.getText().toString().trim());
                    documentReference.update(change);
                    Toast.makeText(UpdateEventActivity.this, "Changes Applied!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(v.getContext(), "No Changes!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(UpdateEventActivity.this, DisplayActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
