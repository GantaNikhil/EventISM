package com.example.event_ism.ui.events;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_ism.EventsPage.UpdateEventActivity;
import com.example.event_ism.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventsAdapter extends FirestoreRecyclerAdapter<EventsModel, EventsAdapter.MyViewHolder> {
    public EventsAdapter(@NonNull FirestoreRecyclerOptions<EventsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull EventsModel eventsModel) {
        String UserID = null;
        String DocID = eventsModel.getDocid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            UserID = user.getUid();

        if (UserID.equals(eventsModel.getEventid())) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.enroll.setVisibility(View.GONE);
            holder.unenroll.setVisibility(View.GONE);
        } else {
            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);

            FirebaseFirestore.getInstance().collection("Events").document(DocID).collection("Enrolls").document(UserID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            Log.d("MSHHHHH","JJ");
                            EventsModel Enrollcheck=documentSnapshot.toObject(EventsModel.class);
                            String enrollcheck ;
                                enrollcheck= Enrollcheck != null ? Enrollcheck.getEnrollcheck() : null;
//                            Log.d("enrollcheck", " " + enrollcheck);

                            if (enrollcheck != null && enrollcheck.equals("true")) {
                                holder.enroll.setVisibility(View.GONE);
                                holder.unenroll.setVisibility(View.VISIBLE);
                            } else {
                                holder.enroll.setVisibility(View.VISIBLE);
                                holder.unenroll.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        holder.event_disp.setText(eventsModel.getEvent());
        holder.by_disp.setText(eventsModel.getBy());
        holder.count_disp.setText(eventsModel.getCount());

        final EventsModel item = getItem(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateEventActivity.class);
                intent.putExtra("event_name", eventsModel.getEvent());
                intent.putExtra("organised_by", eventsModel.getBy());
                intent.putExtra("date", eventsModel.getDate());
                intent.putExtra("docid", eventsModel.getDocid());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(v -> DeleteEvent(v.getContext(), item));

        String finalUserID = UserID;
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
                events.put("enrollID",eventsModel.getDocid());
                DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Users").document(finalUserID).collection("Enrolls").document(eventsModel.getDocid());
                documentReference.set(events);


                holder.unenroll.setVisibility(View.VISIBLE);
                holder.enroll.setVisibility(View.GONE);
            }
        });
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

                DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Users").document(finalUserID).collection("Enrolls").document(eventsModel.getDocid());
                documentReference.delete();
                holder.enroll.setVisibility(View.VISIBLE);
                holder.unenroll.setVisibility(View.GONE);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_events, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView by_disp, count_disp, event_disp;
        Button enroll, edit, delete, unenroll;

        public MyViewHolder(View itemView) {
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

    //Delete Event Function
    private void DeleteEvent(Context mcontext, EventsModel eventsModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        builder.setMessage("This process cannot be undone!")
                .setCancelable(true)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Docid = eventsModel.getDocid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Events").document(Docid);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mcontext, "Deleted " + eventsModel.getEvent(), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(mcontext, "Try Again!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }




}
