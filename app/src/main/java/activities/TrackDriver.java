package activities;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oli.coach.R;
import com.oli.coach.databinding.ActivityTrackDriverBinding;

import models.Driver;
import models.LocationReader;

public class TrackDriver extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityTrackDriverBinding binding;
    public Driver driver;
    Marker userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            driver = (Driver) bundle.getSerializable("driver");
            String DId = bundle.getString("getID");
            Log.i("56y", DId);
            DocumentReference driverRef = FirebaseFirestore.getInstance().collection("location")
                    .document(DId);
            driverRef.addSnapshotListener((value, error) -> {
                if (value.exists()) {
                    LocationReader locationReader = value.toObject(LocationReader.class);
                    LatLng sydney = new LatLng(locationReader.getLat(), locationReader.getLog());
                    if (userMarker == null){
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(sydney);
                        userMarker = mMap.addMarker(markerOptions.title("coach").draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        userMarker.setRotation(0.5f);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                    }else {
                        userMarker.setPosition(sydney);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                    }

                }
            });
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        getData();
    }
}