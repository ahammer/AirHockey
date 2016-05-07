package metalrain.com.shared.client;

import android.os.Bundle;
import android.widget.EditText;

import metalrain.com.shared.R;
import metalrain.com.shared.WakeLockedActivity;

/**
 * Created by Adam on 4/24/2016.
 */
public class ClientConfigureActivity extends WakeLockedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_configure);
        findViewById(R.id.launch).setOnClickListener(event -> {
            ClientActivity.StartActivity(this, getIp());
        });
    }

    String getIp() {
        return ((EditText)findViewById(R.id.server_ip)).getText().toString();
    }
}