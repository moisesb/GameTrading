package net.moisesborges.gametrading.location;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by moises.anjos on 10/08/2016.
 */

public class FetchAddressIntentService extends IntentService {

    public static final String NAME = "FetchAddressService";
    public static final String PACKAGE_NAME = "net.moisesborges.gametrading.location";
    public static final String CLASS_NAME = "FetchAddressIntentService";
    public static final String LOCATION_ARG = PACKAGE_NAME + "." + CLASS_NAME + ".location";

    public FetchAddressIntentService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(LOCATION_ARG);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                Log.d("Adress", address.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void start(Context context, Location location) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(LOCATION_ARG, location);
        context.startService(intent);
    }
}
