package mike.http1;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BroadCastSendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_send);

        Button btn_send_broadcast=(Button)findViewById(R.id.btn_send_broadcast);
        btn_send_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("my_broadcast_action");
                intent.setFlags(1);
                BroadCastSendActivity.this.sendBroadcast(intent);
            }
        });

        Button btn_send_sticky_broadcast=(Button)findViewById(R.id.btn_send_sticky_broadcast);
        btn_send_sticky_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("my_sticky_broadcast_action");
                intent.setFlags(1);
                BroadCastSendActivity.this.sendStickyBroadcast(intent);
            }
        });

        Button btn_start_activity=(Button)findViewById(R.id.btn_start);
        btn_start_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BroadCastSendActivity.this,BoradcastReceiverActivity.class);
                BroadCastSendActivity.this.startActivity(intent);

            }
        });
    }
}
