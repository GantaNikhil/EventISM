package com.example.event_ism.ui.enrolls;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_ism.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EnrollsAdapter extends FirestoreRecyclerAdapter<EnrollsModel, EnrollsAdapter.MyViewHolder> {

    public EnrollsAdapter(FirestoreRecyclerOptions<EnrollsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull EnrollsAdapter.MyViewHolder holder, int position, @NonNull @NotNull EnrollsModel enrollsmodel) {
        holder.delete.setVisibility(View.GONE);
        holder.edit.setVisibility(View.GONE);
        holder.enroll.setVisibility(View.GONE);
        holder.unenroll.setVisibility(View.VISIBLE);

        String UserID = null;
        String DocID = enrollsmodel.getEnrollID();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            UserID = user.getUid();

        FirebaseFirestore.getInstance().collection("Events").document(DocID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        EnrollsModel enrollsModel = documentSnapshot.toObject(EnrollsModel.class);
                        holder.event_disp.setText(enrollsModel.getEvent());
                        holder.by_disp.setText(enrollsModel.getBy());
                        holder.count_disp.setText(enrollsModel.getCount());
                    }
                });

        String finalUserID = UserID;
        holder.unenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> users = new HashMap<>();
                String enrollcheck = "false";
                users.put("enrollcheck", enrollcheck);            //=false if not enrolled and =true if enrolled
                DocumentReference Users = FirebaseFirestore.getInstance().collection("Events").document(DocID).collection("Enrolls").document(finalUserID);
                Users.update(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(v.getContext(), "Unenrolled Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });

                DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Users").document(finalUserID).collection("Enrolls").document(DocID);
                documentReference.delete();
                holder.enroll.setVisibility(View.VISIBLE);
                holder.unenroll.setVisibility(View.GONE);
            }
        });
        holder.enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> users = new HashMap<>();
                users.put("UserID", finalUserID);
                String enrollcheck = "true";
                users.put("enrollcheck", enrollcheck);           //=false if not enrolled and =true if enrolled
                DocumentReference Users = FirebaseFirestore.getInstance().collection("Events").document(DocID).collection("Enrolls").document(finalUserID);
                Users.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(v.getContext(), "Enrolled Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String,Object> events =new HashMap<>();
                events.put("enrollID",DocID);
                DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Users").document(finalUserID).collection("Enrolls").document(DocID);
                documentReference.set(events);


                holder.unenroll.setVisibility(View.VISIBLE);
                holder.enroll.setVisibility(View.GONE);
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public EnrollsAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_events, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView by_disp, count_disp, event_disp;
        Button enroll, edit, delete, unenroll;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            event_disp = itemView.findViewById(R.id.eventname_disp);
            by_disp = itemView.findViewById(R.id.by_disp);
            count_disp = itemView.findViewById(R.id.count);
            enroll = itemView.findViewById(R.id.enroll_disp);
            edit = itemView.findViewById(R.id.edit_disp);
            delete = itemView.findViewById(R.id.delete_disp);
            unenroll = itemView.findViewById(R.id.unenroll_disp);
        }
    }
}
