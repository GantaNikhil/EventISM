package com.example.event_ism.ui.enrolls;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class EnrollActivity extends Fragment {

    RecyclerView recyclerView;
//    ProgressDialog progressDialog;
    FirebaseFirestore fstore;
    FirestoreRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enrolls, container, false);

        recyclerView=v.findViewById(R.id.recycler);

        fstore=FirebaseFirestore.getInstance();

        String User=null;
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
            User=user.getUid();
//        Log.d("UserCheck","H"+User);

        Query query= fstore.collection("Users").document(User).collection("Enrolls");

        FirestoreRecyclerOptions<EnrollsModel> options =new FirestoreRecyclerOptions.Builder<EnrollsModel>()
                .setQuery(query,EnrollsModel.class)
                .build();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new EnrollsAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        /* Progress Dialog
        progressDialog=new ProgressDialog(this.getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };
        Handler pdCanceller =new Handler();
        pdCanceller.postDelayed(runnable,3000);
        */

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
