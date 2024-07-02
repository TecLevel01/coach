package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oli.coach.R;

import java.util.HashMap;

import models.Driver;
import models.util;

public class DriverDetails extends AppCompatActivity {

    private EditText name, etPhone, etEmail, etModel, etPlate, etPassword;
    public Driver driver;
    private FirebaseFirestore db;
    private HashMap<String, Object> map;
    TextView deleteBtn;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        deleteBtn = findViewById(R.id.deleteDriver);
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
                model = etModel.getText().toString().trim(),
                plate = etPlate.getText().toString().trim(),
                dPw = etPassword.getText().toString().trim();
        map = new HashMap<>();
        map.put("name", uname);
        map.put("phone", phone);
        map.put("email", dEmail);
        map.put("model", model);
        map.put("plate", plate);
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
        etModel = findViewById(R.id.etModel);
        etPlate = findViewById(R.id.etPlate);
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
            etModel.setText(driver.getModel());
            etPlate.setText(driver.getPlate());
            etPassword.setText(driver.getPassword());
        }
    }

    private void getDId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // get Driver Data
            driver = (Driver) bundle.getSerializable("driver");

            // set data
            String dId = driver.getUid();
            Intent intent = new Intent(this, TrackDriver.class);
            intent.putExtra("getID", dId);
            Log.i("oli", "Driver" + dId);
            startActivity(intent);
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


    //Trip details

    public void tDetails(View view) {
        Intent intent = new Intent(this, trips.class);
        intent.putExtra("dId", driver.getUid());
        startActivity(intent);
    }

    public void trackBtn(View view) {
        getDId();
    }

    public void QR(View view) {
        openQrActivity();
    }

    private void openQrActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            driver = (Driver) bundle.getSerializable("driver");

            String name = driver.getName();
            String phone = driver.getPhone();
            String email = driver.getEmail();
            String model = driver.getModel();
            String plate = driver.getPlate();
            String password = driver.getPassword();

            // Generate QR code using the retrieved data
            String idToQr = "Name: " + name + "\nPhone: " + phone + "\nEmail: " + email
                    + "\nmodel: " + model + "\nplate: " + plate + "\nID: " + password;

            Intent intent = new Intent(this, qrCode.class);
            intent.putExtra("data", idToQr); // Pass the data as a String
            startActivity(intent);
        }
    }

    public void tChat(View view) {
        Intent intent = new Intent(this, chatRoom.class);
        util.passUserID(intent, driver);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}