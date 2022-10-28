package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bdm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import models.myDriver;

public class addDriver extends AppCompatActivity implements View.OnClickListener {
    EditText etDname, etDphone, etDemail, etDpw;
    Button addDriver;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        etDname = findViewById(R.id.etDname);
        etDphone = findViewById(R.id.etDphone);
        etDemail = findViewById(R.id.etDemail);
        etDpw = findViewById(R.id.etDpw);
        addDriver = findViewById(R.id.addDriver);
        myAuth = FirebaseAuth.getInstance();


        addDriver.setOnClickListener(view -> {
            String name = etDname.getText().toString().trim(),
                    phone = etDphone.getText().toString().trim(),
                    email = etDemail.getText().toString().trim(),
                    password = etDpw.getText().toString().trim();

            if (name.isEmpty()) {
                etDname.setError("username required");
                etDname.requestFocus();
                return;
            }
            if (phone.isEmpty()) {
                etDphone.setError("phone required");
                etDphone.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                etDemail.setError("email required");
                etDemail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etDemail.setError("invalid email address");
                etDemail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                etDpw.setError("Driver ID required!");
                etDpw.requestFocus();
                return;
            }
            if (password.length() < 6) {
                etDpw.setError("Driver ID should be at least 6 characters!");
                etDpw.requestFocus();
                return;
            }
            myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    myDriver myDriver = new myDriver(name, phone, email, password);
                    FirebaseFirestore.getInstance().collection("drivers").document(task.getResult().getUser()
                            .getUid()).set(myDriver).addOnCompleteListener(this, task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(this, "driver added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(this, "driver added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Navigation.class));
                } else {
                    Toast.makeText(this, "try again, something wrong", Toast.LENGTH_SHORT).show();
                }

            });


        });
    }

    @Override
    public void onClick(View view) {

    }
}