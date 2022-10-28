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

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;
    Button signIn;
    FirebaseAuth mAuth;
    TextView signTxt, forgetPw;
    ProgressBar myProBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.PwTxt);
        signIn = findViewById(R.id.loginBtn);
        signTxt = findViewById(R.id.signUpTxt);
        signTxt.setOnClickListener(this);
        forgetPw = findViewById(R.id.forgotTxt);
        myProBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(view -> {
            String myUserName = username.getText().toString().trim();
            String myPw = password.getText().toString().trim();
            if (myUserName.isEmpty()) {
                username.setError("userName required!");
                username.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(myUserName).matches()){
                username.setError("Invalid email address!");
                username.requestFocus();
                return;
            }
            if (myPw.isEmpty()) {
                password.setError("password required!");
                password.requestFocus();
                return;
            }
            if (myPw.length() < 4) {
                password.setError("password should be at least 4 characters!");
                password.requestFocus();
                return;
            }
            myProBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(myUserName, myPw).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this, Navigation.class));
                } else {
                    Toast.makeText(this, "Failed to Log In; check your credentials", Toast.LENGTH_SHORT).show();
                    myProBar.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUpTxt) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}