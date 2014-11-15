package wifid.cnlab.tu.wifip2p;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;


public class WiFriends extends Activity {

    public static final String TAG = "WiFriends";




    private Button startScan;
    private Switch wifiDirectOnOff;
    private ListView listPeers;

    private WifiP2pManager wfManager;
    private Channel wfChannel;

    public static int wfServerPort;


    private BroadcastReceiver wifiDirectStateChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateWifiStatus();
        }
    };

    private  BroadcastReceiver wifiDirectPeersScan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //TODO- Code displaying the List of peers that are identified with Active wifiDirect
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_peers);


        startScan = (Button)findViewById(R.id.startScan);
        wifiDirectOnOff = (Switch)findViewById(R.id.wifi_direct);
        listPeers = (ListView)findViewById(R.id.peerlistview);



        initializeServerSocket();
        startRegistration();

    }

    public void initializeServerSocket() {
        // Initialize a server socket on the next available port.

        ServerSocket wfServerSocket = null;
        try {
            wfServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the chosen port.
        wfServerPort =  wfServerSocket.getLocalPort();

    }

    public void updateWifiStatus(){

        /* Button Status */
       // startScan.setEnabled(WifiP2pManager.WIFI_P2P_STATE_ENABLED);
        Toast.makeText(WiFriends.this
                , "Working", Toast.LENGTH_SHORT).show();

    }

    private void startRegistration() {
        //  Create a string map containing information about your service.
        Map record = new HashMap();

        Log.d(TAG, "About to Create a Server Port");
       // record.put("listenport", String.valueOf(wfServerPort));
        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
        record.put("available", "visible");

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        Log.d(TAG, "Requesting for Server Port");
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.

        Log.d(TAG, "Service Info is created");


        wfManager.addLocalService(wfChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Successfully added Local Service");

            }

            @Override
            public void onFailure(int arg0) {
                Log.d(TAG, "Failure in ");
            }
        });

        //updateWifiStatus();
    }





/**********************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.discover_peers, menu);
        return true;
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
