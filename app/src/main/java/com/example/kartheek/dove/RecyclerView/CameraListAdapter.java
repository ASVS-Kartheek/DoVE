package com.example.kartheek.dove.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kartheek.dove.HistoryActivity;
import com.example.kartheek.dove.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CameraListAdapter extends RecyclerView.Adapter<CameraListAdapter.CameraListViewHolder>{



    private static int mNoOfItems;
    private String[] camNames;

    public CameraListAdapter(int nItems){ mNoOfItems = nItems; }

    @Override
    public CameraListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        camNames = parent.getResources().getStringArray(R.array.camera_names);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item,parent,false);
        return new CameraListViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(CameraListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNoOfItems;
    }


    class CameraListViewHolder extends RecyclerView.ViewHolder{

        TextView mCameraName, mPersonName, mTimeStamp;
        Context context;

        CameraListViewHolder(final View itemView,final Context context){
            super(itemView);

            mCameraName = itemView.findViewById(R.id.item_name);
            mPersonName = itemView.findViewById(R.id.person_name);
            mTimeStamp = itemView.findViewById(R.id.time_stamp);

            this.context = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent=new Intent(context,HistoryActivity.class);
                    intent.putExtra("type","Camera");
                    intent.putExtra("number",position);
                    context.startActivity(intent);
                }
            });
        }

        void bind(final int position){
            mCameraName.setText(camNames[position]);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.child("Camera"+position).child("perName").getValue(String.class);
                    mTimeStamp.setText(dataSnapshot.child("Camera"+position).child("time").getValue(String.class));
                    assert value != null;
                    mPersonName.setText(dataSnapshot.child("Users").child(value).child("name").getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
