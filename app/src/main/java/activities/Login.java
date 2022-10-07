package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bdm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button signIn;
    FirebaseAuth mAuth;
    FirebaseFirestore myAuth;
    TextView signTxt;
    ProgressBar myProBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.PwTxt);
        signIn = findViewById(R.id.loginBtn);
        signTxt = findViewById(R.id.loginTxt);
        myProBar = findViewById(R.id.progressBar);

    }
}