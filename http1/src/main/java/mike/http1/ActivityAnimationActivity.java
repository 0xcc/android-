package mike.http1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_animation);

        Button btn=(Button)findViewById(R.id.btn_activity_animation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityAnimationActivity.this,BitmapOpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
            }
        });

    }
}
