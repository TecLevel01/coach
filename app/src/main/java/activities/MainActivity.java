package activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bdm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import models.myUsers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fullName, phone, email, password;
    Button singUP;
    ProgressBar myProBar;
    TextView signInTxt;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fullName = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        singUP = findViewById(R.id.signUpBtn);
        myProBar = findViewById(R.id.progressBar);
        signInTxt = findViewById(R.id.signInTxt);
        signInTxt.setOnClickListener(this);
        myAuth = FirebaseAuth.getInstance();

        singUP.setOnClickListener(view -> {

            String myFullName = fullName.getText().toString().trim();
            String myPhone = phone.getText().toString().trim();
            String myEmail = email.getText().toString().trim();
            String myPw = password.getText().toString().trim();

            if (myFullName.isEmpty()) {
                fullName.setError("full Name required!");
                fullName.requestFocus();
                return;
            }
            if (myPhone.isEmpty()) {
                phone.setError("Phone Number required!");
                phone.requestFocus();
                return;
            }
            if (myEmail.isEmpty()) {
                email.setError("Email Address required!");
                email.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(myEmail).matches()) {
                email.setError("Invalid email address!");
                email.requestFocus();
                return;
            }
            if (myPw.isEmpty()) {
                password.setError("Password required!");
                password.requestFocus();
                return;
            }
            if (myPw.length() < 6) {
                password.setError("password should be at least 6 characters!");
                password.requestFocus();
                return;
            }
            myProBar.setVisibility(View.VISIBLE);
            myAuth.createUserWithEmailAndPassword(myEmail, myPw).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()){
                    myUsers myuser = new myUsers(myFullName, myPhone, myEmail, myPw);
                    String Uid = task.getResult().getUser().getUid();
                    FirebaseFirestore.getInstance().collection("Admin")
                            .document(Uid).set(myuser).addOnCompleteListener(this, task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(this, "Admin Signed Up", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Error, something wrong!", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(this, "Admin Signed Up", Toast.LENGTH_SHORT).show();
                            });
                    startActivity(new Intent(this, Login.class));
                    finish();
                }else {
                    Toast.makeText(this, "Error, something wrong!", Toast.LENGTH_SHORT).show();
                    myProBar.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signInTxt) {
            startActivity(new Intent(this, Login.class));
        }
    }
}