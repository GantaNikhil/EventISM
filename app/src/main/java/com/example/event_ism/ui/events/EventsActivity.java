package com.example.event_ism.ui.events;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_ism.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EventsActivity extends Fragment {

    FirebaseFirestore fstore;
    FirestoreRecyclerAdapter adapter;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = v.findViewById(R.id.recycler);

        fstore = FirebaseFirestore.getInstance();

        Query query = fstore.collection("Events").orderBy("event");

        FirestoreRecyclerOptions<EventsModel> options = new FirestoreRecyclerOptions.Builder<EventsModel>()
                .setQuery(query, EventsModel.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new EventsAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.post(new Runnable()
        {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    //for fetching data from firestore(adapter)
    @Override
    public void onStart() {
        super.onStart();
        /*Progress Bar
        progressDialog=new ProgressDialog(this.getContext());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Runnable progressRunner=new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };
        Handler pdCanceller=new Handler();
        pdCanceller.postDelayed(progressRunner,2000);
        End Of Progress Bar*/
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}