package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.oli.coach.R;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import adapters.DriversAdapter;
import models.Driver;
import models.util;

public class Navigation extends AppCompatActivity {

    ArrayList<Driver> drivers;
    private RecyclerView recView;
    private DriversAdapter driversAdapter;
    private CollectionReference driversRef;
    private SwipeRefreshLayout swipeRefresh;
    FloatingActionButton fab;
    FrameLayout myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        myLayout = findViewById(R.id.navigator);
        fab = findViewById(R.id.fab);

        getBiometric();
        mInit();
        initFirestore();
        getData();


        fab.setOnClickListener(view -> startActivity(new Intent(this, addDriver.class)));


    }
    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String token = task.getResult();
                util.adminRef().update("token", token);
            }
        });
    }


    private void getBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "sensor error!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "no fingerprint on device", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "sensor not found!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Toast.makeText(this, "wrong fingerprint!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Toast.makeText(this, "update required!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Toast.makeText(this, "fingerprint not supported!", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(Navigation.this, "Authentication succeeded", Toast.LENGTH_SHORT).show();
                myLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                startActivity(new Intent(Navigation.this, Login.class));
            }
        });

        //fingerprint dialog

//        Drawable iconDrawable = ContextCompat.getDrawable(this, R.drawable.coach);
//
//// Create a SpannableString with the desired title and icon
//        SpannableString spannableString = new SpannableString("  Global Coaches");
//        iconDrawable.setBounds(0, 0, iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight());
//        ImageSpan imageSpan = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BOTTOM);
//        spannableString.setSpan(imageSpan, 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the custom title in the promptInfo
        BiometricPrompt.PromptInfo.Builder promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Global Coaches")
                .setDescription("Use your fingerprint to access data")
                .setNegativeButtonText("Not an Admin! go to logout");

        biometricPrompt.authenticate(promptInfo.build());

    }


    private void getData() {
        getMethod();
    }


    private void getMethod() {
        drivers.clear(); // clear old list
        driversRef.get().addOnCompleteListener(task -> {
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

                driversAdapter = new DriversAdapter(drivers, this);
                recView.setAdapter(driversAdapter);
            }
            // hide refresh
            swipeRefresh.setRefreshing(false);
        });
    }

    private void initFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        driversRef = db.collection("drivers");
    }

    private void mInit() {
        recView = findViewById(R.id.recView);
        drivers = new ArrayList<>();
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setOnRefreshListener(this::getMethod);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            Logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.reload();
            getFCMToken();
        } else {
            Intent intent = new Intent(this, splash.class);
            startActivity(intent);
            finish();
        }
    }

    private void Logout() {
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Do you want to log out?", Snackbar.LENGTH_LONG).setAction("YES", view1 -> {
                   FirebaseAuth.getInstance().signOut();
                   startActivity(new Intent(this, Login.class));
                   finish();
               });
               snackbar.show();
           }
        });
    }

}