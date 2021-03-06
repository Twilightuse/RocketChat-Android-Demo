package demo.rocketchat.example;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.rocketchat.core.RocketChatAPI;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import demo.rocketchat.example.activity.MyAdapterActivity;
import demo.rocketchat.example.application.RocketChatApplication;
import demo.rocketchat.example.utils.AppUtils;

@EActivity (R.layout.activity_room)
public class RoomActivity extends MyAdapterActivity{

    RocketChatAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = ((RocketChatApplication)getApplicationContext()).getRocketChatAPI();
        api.getConnectivityManager().register(this);
        super.onCreate(savedInstanceState);
    }


    @UiThread
    @Override
    public void onConnect(String sessionID) {
        Snackbar
                .make(findViewById(R.id.activity_room), R.string.connected, Snackbar.LENGTH_LONG)
                .show();
    }

    @UiThread
    @Override
    public void onDisconnect(boolean closedByServer) {
        AppUtils.getSnackbar(findViewById(R.id.activity_room), R.string.disconnected_from_server)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        api.reconnect();
                    }
                })
                .show();

    }

    @UiThread
    @Override
    public void onConnectError(Exception websocketException) {
        AppUtils.getSnackbar(findViewById(R.id.activity_room), R.string.connection_error)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        api.reconnect();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        api.getConnectivityManager().unRegister(this);
        super.onDestroy();
    }
}
