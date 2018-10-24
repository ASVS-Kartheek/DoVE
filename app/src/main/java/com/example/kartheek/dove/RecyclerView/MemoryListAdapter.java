package com.example.kartheek.dove.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kartheek.dove.HistoryActivity;
import com.example.kartheek.dove.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.MemoryListViewHolder>{



    private static int mNoOfItems;
    private String[] MemoryNames;

    public MemoryListAdapter(int nItems){ mNoOfItems = nItems; }

    @Override
    public MemoryListAdapter.MemoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MemoryNames = parent.getResources().getStringArray(R.array.memory_names);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item,parent,false);

        return new MemoryListAdapter.MemoryListViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(MemoryListAdapter.MemoryListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNoOfItems;
    }


    class MemoryListViewHolder extends RecyclerView.ViewHolder{

        TextView mMemoryName, mPersonName, mTimeStamp;
        Button mCallBtn;
        Context context;

        MemoryListViewHolder(View itemView,final Context context){
            super(itemView);

            mMemoryName = itemView.findViewById(R.id.item_name);
            mPersonName = itemView.findViewById(R.id.person_name);
            mTimeStamp = itemView.findViewById(R.id.time_stamp);
            mCallBtn = itemView.findViewById(R.id.call_btn);

            this.context = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent=new Intent(context,HistoryActivity.class);
                    intent.putExtra("type","Memory");
                    intent.putExtra("number",position);
                    context.startActivity(intent);
                }
            });
        }

        void bind(final int position){
            mMemoryName.setText(MemoryNames[position]);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.child("Memory"+position).child("perName").getValue(String.class);
                    mTimeStamp.setText(dataSnapshot.child("Memory"+position).child("time").getValue(String.class));
                    assert value != null;
                    mPersonName.setText(dataSnapshot.child("Users").child(value).child("name").getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String user = dataSnapshot.child("Camera"+position).child("perName").getValue(String.class);
                            assert user != null;
                            String phone_number = dataSnapshot.child("Users").child(user).child("phone").getValue(String.class);
                            Uri uri = Uri.parse("tel:"+phone_number);
                            Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                            if (intent.resolveActivity(context.getPackageManager())!=null){
                                context.startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });

        }
    }
}
