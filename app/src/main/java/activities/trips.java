package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bdm.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class trips extends AppCompatActivity {
    private RecyclerView tripItems;
    private TextView itemName, nPlate, time;
    FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        mDb = FirebaseFirestore.getInstance();
        tripItems = findViewById(R.id.mTrips);

    }

    public static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd MM yy . HH:mm");
        return simpleDateFormat.format(date);
    }
}