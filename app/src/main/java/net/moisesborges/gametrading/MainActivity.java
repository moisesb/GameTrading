package net.moisesborges.gametrading;

import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.moisesborges.gametrading.location.FetchAddressIntentService;
import net.moisesborges.gametrading.location.LocationService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hello_text_view)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LocationService locationService = new LocationService(this);
        locationService.getLocation(new LocationService.LocationCallback() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mTextView.setText("latitude: " + location.getLatitude() + ", longitude: " + location.getLongitude());
                    FetchAddressIntentService.start(MainActivity.this,location);
                }else {
                    mTextView.setText("could not get location");
                }
            }

            @Override
            public void onError() {
                mTextView.setText("could not get location");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
