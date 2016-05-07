package metalrain.com.shared.server;

import android.app.Application;

/**
 * Created by Adam on 4/21/2016.
 */
public class AirHockeyApplication extends Application {
    private static AirHockeyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
