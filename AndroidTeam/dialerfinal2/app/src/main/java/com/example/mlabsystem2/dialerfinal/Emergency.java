package com.example.mlabsystem2.dialerfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.ACTION_CALL;
import static android.content.Intent.ACTION_DIAL;

public class Emergency extends AppCompatActivity implements View.OnClickListener {


    String TAG = "MEEEE";
    FirebaseFirestore db;
    String uid;
    public static ArrayList<String> emNumbers = new ArrayList<>();
    Button em1, em2, em3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        em1 = findViewById(R.id.em1);
        em2 = findViewById(R.id.em2);
        em3 = findViewById(R.id.em3);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");

        Log.d(TAG, "Uid: " + uid);

        loadContacts();

        em1.setOnClickListener(this);
        em2.setOnClickListener(this);
        em3.setOnClickListener(this);
    }

    private void loadContacts() {
        loadContacts(true);
    }

    private void loadContacts(final boolean fromCache) {

        //final Source source = (fromCache) ? Source.CACHE : Source.DEFAULT;

        final DocumentReference docRef = db.collection("Users").document("na30eHlGYodk0qPUK56tCh1Pq5s2");

        docRef.get(Source.DEFAULT)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("MEEEEEE", "DocumentSnapshot data: " + document.getData());

                                Map<String, Object> contacts = (Map<String, Object>) document.get("EmergencyContacts");
                                Log.d("MEEEE", "onComplete: " + contacts);
                                //if (contacts != null) {
                                    emNumbers.add(contacts.get("num1").toString());
                                    emNumbers.add(contacts.get("num2").toString());
                                    emNumbers.add(contacts.get("num3").toString());
                                //}
                            } else {
                                if (fromCache) {
                                    loadContacts(false);
                                }
                                Log.d("MEEEEEE", "No such document");
                            }

                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.em1: {
                Log.d(TAG, emNumbers.get(0));
                Intent call1 = new Intent();
                call1.setAction(ACTION_CALL);
                call1.setData(Uri.parse("tel:" + emNumbers.get(0)));
                startActivity(call1);
            }
            case R.id.em2: {
                Intent call1 = new Intent();
                call1.setAction(ACTION_CALL);
                call1.setData(Uri.parse("tel:" + emNumbers.get(1)));
                startActivity(call1);
            }
            case R.id.em3: {
                Intent call1 = new Intent();
                call1.setAction(ACTION_CALL);
                call1.setData(Uri.parse("tel:" + emNumbers.get(2)));
                startActivity(call1);

            }
        }

    }
}