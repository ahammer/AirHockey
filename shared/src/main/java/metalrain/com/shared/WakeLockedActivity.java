package metalrain.com.shared;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

/**
 * Created by Adam on 4/24/2016.
 */
public class WakeLockedActivity extends Activity {
    PowerManager.WakeLock lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        lock  = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Airhocky");
        lock.acquire();

    }

    @Override
    protected void onPause() {
        super.onPause();
        lock.release();
    }
}
