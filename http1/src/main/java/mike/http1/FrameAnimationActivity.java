package mike.http1;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FrameAnimationActivity extends AppCompatActivity {

    Button btn_start=null;
    Button btn_stop=null;
    ImageView imageView=null;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);

        btn_start=(Button)findViewById(R.id.btn_start_frame);
        btn_stop=(Button)findViewById(R.id.btn_stop_frame);
        imageView=(ImageView)findViewById(R.id.imageView);




        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setBackgroundResource(R.anim.frame);
                animationDrawable=(AnimationDrawable)imageView.getBackground();
                animationDrawable.start();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationDrawable.stop();

            }
        });
    }



}
