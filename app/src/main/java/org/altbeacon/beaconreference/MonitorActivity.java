package org.altbeacon.beaconreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class MonitorActivity extends Activity implements BeaconConsumer, MonitorNotifier {


    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final Identifier ID_1 = Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6");
    public static final Identifier ID_2 = Identifier.parse("1");
    public static final Identifier ID_3 = Identifier.parse("2");
    private static final String UNIQUE_ID = "unique_id";
    private TextView tv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        tv_log = findViewById(R.id.tv_log);
        tv_log.setMovementMethod(new ScrollingMovementMethod());
        BeaconManager.setDebug(true);
        BeaconManager.setRegionExitPeriod(3000);
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

    @NonNull
    private String time() {
        return FORMAT.format(new Date());
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            BeaconManager.getInstanceForApplication(getApplication()).startMonitoringBeaconsInRegion(new Region(UNIQUE_ID, null, null, null));
            BeaconManager.getInstanceForApplication(getApplication()).startMonitoringBeaconsInRegion(new Region(ID_1.toString(), ID_1, null, null));
            BeaconManager.getInstanceForApplication(getApplication()).startMonitoringBeaconsInRegion(new Region(ID_1.toString() + ":" + ID_2.toString(), ID_1, ID_2, null));
            BeaconManager.getInstanceForApplication(getApplication()).startMonitoringBeaconsInRegion(new Region(ID_1.toString() + ":" + ID_2.toString() + ":" + ID_3.toString(), ID_1, ID_2, ID_3));

            BeaconManager.getInstanceForApplication(getApplication()).startMonitoringBeaconsInRegion(new Region("2f234454-cf6d-4a0f-adf2-f4911ba9ffa9:9:32",
                    Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa9"),
                    Identifier.parse("9"),
                    Identifier.parse("32")));
            BeaconManager.getInstanceForApplication(getApplication()).addMonitorNotifier(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        String log = time() + ":enter region:" + region + "\n";
        Timber.i(log);
        tv_log.append(log);
    }

    @Override
    public void didExitRegion(Region region) {
        String log = time() + ":exit region:" + region + "\n";
        Timber.i(log);
        tv_log.append(log);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
