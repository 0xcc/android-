package mike.http1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BoradcastReceiverActivity extends AppCompatActivity {

    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boradcast_receiver);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("my_broadcast_action");
        mIntentFilter.addAction("my_sticky_broadcast_action");
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //System.out.println("action"+action);
            Log.i("receiver",action);
        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(mReceiver);
    }


}
