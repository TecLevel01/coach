package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oli.coach.R;

import models.myDriver;

public class addDriver extends AppCompatActivity {
    EditText etDname, etDphone, etDemail, etDpw, etModel, etPlate;
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
        etModel = findViewById(R.id.etModel);
        etPlate = findViewById(R.id.etPlate);
        addDriver = findViewById(R.id.addDriver);
        myAuth = FirebaseAuth.getInstance();


        addDriver.setOnClickListener(view -> {
            String name = etDname.getText().toString().trim(),
                    phone = etDphone.getText().toString().trim(),
                    email = etDemail.getText().toString().trim(),
                    model = etModel.getText().toString().trim(),
                    plate = etPlate.getText().toString().trim(),
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
            if (model.isEmpty()) {
                etModel.setError("phone required");
                etModel.requestFocus();
                return;
            }
            if (plate.isEmpty()) {
                etPlate.setError("phone required");
                etPlate.requestFocus();
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
                    String Uid = task.getResult().getUser().getUid();
                    myDriver myDriver = new myDriver(name, phone, email, model, plate, password, Uid);
                    FirebaseFirestore.getInstance().collection("drivers").document(Uid).set(myDriver)
                            .addOnCompleteListener(this, task1 -> {
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


}