package mike.http1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class TweenAnimationActivity extends AppCompatActivity {

    private Button btnAlpha=null;
    private Button btnScale=null;
    private Button btnRotate=null;
    private Button btnTranslate=null;
    private Button btnMix=null;
    private ImageView imageView=null;

    //private View.OnClickListener btnAlphaListener=null;
    //private View.OnClickListener btnScaleListener=null;
    //private View.OnClickListener btnRotateListener=null;
    //private View.OnClickListener btnTranslateListener=null;
    //private View.OnClickListener btnMixListener=null;


    private Animation animationAlpha=null;
    private Animation animationScale=null;
    private Animation animationRotate=null;
    private Animation animationTranslate=null;
    private Animation animationMix=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tween_animation);

        findView();

    }

    private void findView(){
        btnAlpha=(Button)findViewById(R.id.btn_alpha);
        btnMix=(Button)findViewById(R.id.btn_mix);
        btnRotate=(Button)findViewById(R.id.btn_rotate);
        btnScale=(Button)findViewById(R.id.btn_scale);
        btnTranslate=(Button)findViewById(R.id.btn_translate);

        imageView=(ImageView)findViewById(R.id.imageView);


        btnAlpha.setOnClickListener(btnAlphaListener);
        btnScale.setOnClickListener(btnScaleListener);
        btnRotate.setOnClickListener(btnRotateListener);
        btnTranslate.setOnClickListener(btnTranslateListener);
        btnMix.setOnClickListener(btnMixListener);
    }

    private View.OnClickListener btnMixListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animationMix=AnimationUtils.loadAnimation(TweenAnimationActivity.this,R.anim.mix);
            imageView.startAnimation(animationMix);
        }
    };

    private View.OnClickListener btnTranslateListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animationTranslate=AnimationUtils.loadAnimation(TweenAnimationActivity.this,R.anim.translate);
            imageView.startAnimation(animationTranslate);
        }
    };

    private View.OnClickListener btnRotateListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animationRotate=AnimationUtils.loadAnimation(TweenAnimationActivity.this,R.anim.rotate);
            imageView.startAnimation(animationRotate);
            //rotate(imageView);
        }
    };

    private void rotate(View view){

    }


    private View.OnClickListener btnScaleListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animationScale= AnimationUtils.loadAnimation(TweenAnimationActivity.this,R.anim.scale);
            imageView.startAnimation(animationScale);
            //scale(imageView);
        }
    };

    private void scale(View view){
        Animation animation=new ScaleAnimation(2.0f,1.0f,2.0f,1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);

        animation.setDuration(2000);
        animation.setFillAfter(true);

        BounceInterpolator bounce=new BounceInterpolator();
        animation.setInterpolator(bounce);

        view.startAnimation(animation);
    }

    private View.OnClickListener btnAlphaListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animationAlpha= AnimationUtils.loadAnimation(TweenAnimationActivity.this,R.anim.alpha);
            imageView.startAnimation(animationAlpha);
            //alpha(imageView);
        }
    };

    public void alpha(View view){
        Animation animation=new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

}
