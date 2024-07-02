package activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.oli.coach.R;

import models.Driver;
import models.util;

public class splash extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView img;
    TextView mTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (util.isUserLoggedIn() && getIntent().getExtras() != null){
            //if there is a notification
            String uid = getIntent().getExtras().getString("uId");
            util.driverCollectionRef().document(uid).get().addOnCompleteListener(task -> {

                if (task.isSuccessful()){
                    Driver driver = task.getResult().toObject(Driver.class);

                    Intent mainIntent = new Intent(this, Navigation.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(mainIntent);

                    Intent AListIntent = new Intent(this, DriverDetails.class);
                    util.passUserID(AListIntent, driver);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(AListIntent);

                    Intent intent = new Intent(this, chatRoom.class);
                    util.passUserID(intent, driver);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            });
        }else {
            img = findViewById(R.id.logo);
            mTxt = findViewById(R.id.title);

            topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
            bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

            img.setAnimation(topAnim);
            mTxt.setAnimation(bottomAnim);

            int callAdmin = 3000;
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(splash.this, Login.class);
                startActivity(intent);
                finish();
            }, callAdmin);
        }

    }
}