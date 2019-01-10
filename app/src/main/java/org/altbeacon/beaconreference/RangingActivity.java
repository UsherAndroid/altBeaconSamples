package org.altbeacon.beaconreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class RangingActivity extends Activity implements BeaconConsumer, RangeNotifier {


    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final Identifier ID_1 = Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6");
    public static final Identifier ID_2 = Identifier.parse("1");
    public static final Identifier ID_3 = Identifier.parse("2");
    private static final boolean USE_FIXED_REGION = true;
    private static final String UNIQUE_ID = "unique_id";
    private TextView tv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        tv_log = findViewById(R.id.tv_log);
        tv_log.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onResume() {
        super.onResume();
        BeaconManager.getInstanceForApplication(getApplication()).bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BeaconManager.getInstanceForApplication(getApplication()).unbind(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        StringBuilder sb = new StringBuilder();
        sb.append(time()).append(":").append("(count:").append(beacons.size()).append(")\n");
        for (Beacon beacon : beacons) {
            sb.append(beacon).append(":").append(beacon.getDistance()).append("\n");
        }
        String log = sb.toString();
        Timber.i(log);
        tv_log.append(log);

    }

    @NonNull
    private String time() {
        return FORMAT.format(new Date());
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            Region region = USE_FIXED_REGION ? fixedRegion() : allRegion();
            BeaconManager.getInstanceForApplication(getApplication()).startRangingBeaconsInRegion(region);
            BeaconManager.getInstanceForApplication(getApplication()).addRangeNotifier(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private Region allRegion() {
        return new Region(UNIQUE_ID, null, null, null);
    }

    @NonNull
    private Region fixedRegion() {
        return new Region(UNIQUE_ID, ID_1, ID_2, ID_3);
    }
}
