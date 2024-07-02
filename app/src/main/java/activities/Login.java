package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.oli.coach.R;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText etEmail, etPw;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mInit();

    }



    private void mInit() {
        etEmail = findViewById(R.id.username);
        etPw = findViewById(R.id.PwTxt);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String email, password;
        email = etEmail.getText().toString().trim();
        password = etPw.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            final ProgressDialog dialog = ProgressDialog.show(this, "", "logging in...", true);
            dialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this, Navigation.class));
                    finish();
                } else {
                    String msg = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });
        } else {
            etEmail.requestFocus();
            etPw.requestFocus();
        }

    }

    public void goRegister(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}