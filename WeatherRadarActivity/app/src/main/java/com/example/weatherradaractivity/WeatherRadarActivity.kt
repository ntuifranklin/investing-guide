import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.URL;
import java.util.Locale;

public class WeatherRadarActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_radar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng tuscany = new LatLng(43.7696, 11.2558);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tuscany, 8));

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String format = String.format(Locale.US, "YOUR_RADAR_TILE_URL_FORMAT", x, y, zoom);
                URL url = null;
                try {
                    url = new URL(format);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return url;
            }
        };

        TileOverlay tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }
}