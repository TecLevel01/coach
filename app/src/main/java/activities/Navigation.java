package activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bdm.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import adapters.DriversAdapter;
import models.Driver;

public class Navigation extends AppCompatActivity {

    ArrayList<Driver> drivers;
    private RecyclerView recView;
    private FirebaseFirestore db;
    private DriversAdapter driversAdapter;
    private CollectionReference driversRef;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mInit();
        initFirestore();
        getData();
    }

    private void getData() {
        getMethod();
//        snapshotMethod();
    }

    private void snapshotMethod() {
        driversRef.addSnapshotListener((value, error) -> {
            if (error == null) {
                if (!value.isEmpty()) {
                    // loop to iterate each document in collection
                    for (QueryDocumentSnapshot snapshot : value) {
                        // map driver to object
                        Driver driver = snapshot.toObject(Driver.class);
                        // add uid
                        driver.setUid(snapshot.getId());
                        // add driver to array List
                        drivers.add(driver);
                    }
                    driversAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getMethod() {
        driversRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // loop to iterate each document in collection
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            // map driver to object
                            Driver driver = snapshot.toObject(Driver.class);
                            // add uid
                            driver.setUid(snapshot.getId());
                            // add driver to array List
                            drivers.add(driver);
                        }
                        driversAdapter.notifyDataSetChanged();
                    }
                    // hide refresh
                    swipeRefresh.setRefreshing(false);
                });
    }

    private void initFirestore() {
        db = FirebaseFirestore.getInstance();
        driversRef = db.collection("drivers");
    }

    private void mInit() {
        recView = findViewById(R.id.recView);
        drivers = new ArrayList<>();
        driversAdapter = new DriversAdapter(drivers, this);
        recView.setAdapter(driversAdapter);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            getMethod();
        });

    }
}