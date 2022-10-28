package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bdm.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import models.Driver;

public class DriverDetails extends AppCompatActivity {

    private EditText name, etPhone, etEmail, etPassword;
    public Driver driver;
    private FirebaseFirestore db;
    private HashMap<String, Object> map;
    TextView deleteBtn;


    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        deleteBtn = findViewById(R.id.deleteDriver);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

        mInit();
        getData();

        //Delete Driver Details
        deleteBtn.setOnClickListener(view -> {
            getFields();
            DocumentReference driverDoc = db.collection("drivers")
                    .document(driver.getUid());

            driverDoc.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void getFields() {
        String uname = name.getText().toString().trim(),
                phone = etPhone.getText().toString().trim(),
                dEmail = etEmail.getText().toString().trim(),
                dPw = etPassword.getText().toString().trim();
        map = new HashMap<>();
        map.put("name", uname);
        map.put("phone", phone);
        map.put("email", dEmail);
        map.put("password", dPw);
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
        name = findViewById(R.id.etUname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.dEmail);
        etPassword = findViewById(R.id.dPw);
        db = FirebaseFirestore.getInstance();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // get Driver Data
            driver = (Driver) bundle.getSerializable("driver");

            // set data
            name.setText(driver.getName());
            etPhone.setText(driver.getPhone());
            etEmail.setText(driver.getEmail());
            etPassword.setText(driver.getPassword());
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

    public void tDetails(View view) {
        startActivity(new Intent(this, trips.class));
    }
}