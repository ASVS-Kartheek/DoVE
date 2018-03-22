package com.example.kartheek.dove;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kartheek.dove.RecyclerView.CameraListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //This part is for scanning QR Code
        final View view = inflater.inflate(R.layout.fragment_camera, container, false);

        //This part is for adding Name
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String uid = mAuth.getCurrentUser().getUid();
                    String name = dataSnapshot.child("Users").child(uid).child("name").getValue(String.class);
                    String text = "Hi! " + name;
                    TextView mText = view.findViewById(R.id.tv_camera);
                    mText.setText(text);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        FloatingActionButton mTakeCamBtn = view.findViewById(R.id.take_camera_btn);
        mTakeCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator( (Activity) getContext());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("SCAN");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        RecyclerView mCameraList = view.findViewById(R.id.rV_camera);

        mCameraList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mCameraList.setLayoutManager(layoutManager);


        //Change the adapter according to item
        CameraListAdapter mAdapter = new CameraListAdapter(2);
        mCameraList.setAdapter(mAdapter);

        return view;
    }


}
