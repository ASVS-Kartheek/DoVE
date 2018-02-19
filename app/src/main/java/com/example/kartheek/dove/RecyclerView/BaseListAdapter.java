package com.example.kartheek.dove.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kartheek.dove.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by kartheek on 17/2/18.
 */

public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.BaseListViewHolder>{



    private static int mNoOfItems;
    private String[] BaseNames;

    public BaseListAdapter(int nItems){ mNoOfItems = nItems; }

    @Override
    public BaseListAdapter.BaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        BaseNames = parent.getResources().getStringArray(R.array.base_names);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item,parent,false);

        return new BaseListAdapter.BaseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseListAdapter.BaseListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 5;
    }


    class BaseListViewHolder extends RecyclerView.ViewHolder{

        TextView mBaseName, mPersonName, mTimeStamp;

        BaseListViewHolder(View itemView){
            super(itemView);

            mBaseName = itemView.findViewById(R.id.item_name);
            mPersonName = itemView.findViewById(R.id.person_name);
            mTimeStamp = itemView.findViewById(R.id.time_stamp);
        }

        void bind(final int position){
            mBaseName.setText(BaseNames[position]);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.child("Bases").child("Base"+position).child("perName").getValue(String.class);
                    mTimeStamp.setText(dataSnapshot.child("Bases").child("Base"+position).child("time").getValue(String.class));
                    mPersonName.setText(dataSnapshot.child("Users").child(value).child("name").getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
