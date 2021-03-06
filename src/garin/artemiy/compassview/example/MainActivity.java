package garin.artemiy.compassview.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import garin.artemiy.compassview.R;
import garin.artemiy.compassview.library.CompassSensorsActivity;
import garin.artemiy.compassview.library.CompassView;

import java.util.UUID;

public class MainActivity extends CompassSensorsActivity {

    private static final double DELTA = 0.5;

    private Location userLocation;
    private Location originObjectLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObjectAdapter objectAdapter = new ObjectAdapter(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocation = getBestLastKnowLocation(locationManager);
        originObjectLocation = getBestLastKnowLocation(locationManager);

        addTestData(objectAdapter);

        ((ListView) findViewById(R.id.listView)).setAdapter(objectAdapter);
    }

    private void addTestData(ObjectAdapter objectAdapter) {
        Location testObject1 = getTestObject();
        testObject1.setLongitude(testObject1.getLongitude() - DELTA);
        testObject1.setLatitude(testObject1.getLatitude() - DELTA);
        objectAdapter.add(testObject1);
    }

    private Location getTestObject() {
        Location objectLocation = new Location("");
        objectLocation.setLatitude(originObjectLocation.getLatitude());
        objectLocation.setLongitude(originObjectLocation.getLongitude());
        return objectLocation;
    }

    private class ObjectAdapter extends ArrayAdapter<Location> {

        public ObjectAdapter(Context context) {
            super(context, R.layout.list_item);
        }

        @SuppressLint("InflateParams")
        @SuppressWarnings("ConstantConditions")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);

            TextView titleView = (TextView) convertView.findViewById(R.id.titleView);
            titleView.setText(UUID.randomUUID().toString());

            CompassView compassView = (CompassView) convertView.findViewById(R.id.compassView);
            compassView.initializeCompass(userLocation, getItem(position), R.drawable.arrow);

            return convertView;
        }

    }

    private Location getBestLastKnowLocation(LocationManager locationManager) {
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) location = new Location("");
        return location;
    }

}
