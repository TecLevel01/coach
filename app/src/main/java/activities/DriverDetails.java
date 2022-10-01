package activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bdm.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import models.Driver;

public class DriverDetails extends AppCompatActivity {

    private EditText etPhone, etUname;
    private Driver driver;
    private FirebaseFirestore db;
    private HashMap<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

        mInit();
        getData();

    }

    private void getFields() {
        String phone = etPhone.getText().toString().trim(),
                uname = etUname.getText().toString().trim();
        map = new HashMap<>();
        map.put("phone", phone);
        map.put("uname", uname);
    }

    private void updateData() {
        getFields();

        DocumentReference driverDoc = db
                .collection("drivers")
                .document(driver.getUid());
        // update
        driverDoc.update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mInit() {
        etUname = findViewById(R.id.etUname);
        etPhone = findViewById(R.id.etPhone);
        db = FirebaseFirestore.getInstance();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // get Driver Data
            driver = (Driver) bundle.getSerializable("driver");

            // set data
            etUname.setText(driver.getUname());
            etPhone.setText(driver.getPhone());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void runAction(View view) {
        updateData();
    }

    public void addDriver(View view) {
        getFields();
        db.collection("drivers").add(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                Toast.makeText(this, "Driver Added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}