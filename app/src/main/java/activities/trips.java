package activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.oli.coach.R;

import java.util.ArrayList;

import adapters.tripAdapter;
import models.initTrip;

public class trips extends AppCompatActivity {
    ArrayList<initTrip> initTripArrayList = new ArrayList<>();
    tripAdapter myTripAdapter;
    FirebaseFirestore mDb;
    ProgressDialog progressDialog;
    private String dId;
    private RecyclerView tripItems;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        swipeRefreshLayout = findViewById(R.id.myRefresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::getData);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("tracking history loading...");
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
        initTripArrayList.clear();
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
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    public void backBtn(View view) {
        onBackPressed();
    }
}