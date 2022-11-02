package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.bdm.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import adapters.tripAdapter;
import models.initTrip;

public class trips extends AppCompatActivity {
    ArrayList<initTrip> initTripArrayList = new ArrayList<initTrip>();
    tripAdapter myTripAdapter;
    FirebaseFirestore mDb;
    ProgressDialog progressDialog;
    private String dId;
    private RecyclerView tripItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("get drivers trips");
        progressDialog.show();

        mDb = FirebaseFirestore.getInstance();
        tripItems = findViewById(R.id.mTrips);
        tripItems.setHasFixedSize(true);


        getUserId();
    }

    private void getUserId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dId = bundle.getString("dId");
            getData();
            //Log.i("56y", dId);
        } else {
            Log.i("56y", "null");
        }

    }


    private void getData() {

        mDb.collection("history").document(dId).collection("history")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Log.i("error", error.getMessage());
                        return;
                    }
                    for (DocumentSnapshot dTrips : value.getDocuments()) {

                        initTripArrayList.add(dTrips.toObject(initTrip.class));
                    }
                    myTripAdapter = new tripAdapter(trips.this, initTripArrayList);
                    tripItems.setAdapter(myTripAdapter);
                    progressDialog.dismiss();
                });
    }

}