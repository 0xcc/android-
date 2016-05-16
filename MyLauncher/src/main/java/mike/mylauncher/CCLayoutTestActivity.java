package mike.mylauncher;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.LinkAddress;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;

import mike.mylauncher.CCTableLayout.CCTableLayout;
import mike.mylauncher.CCTableLayout.LauncherLayout;
import mike.mylauncher.CCTableLayout.MyTextView;

public class CCLayoutTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cclayout_test);

        final CCTableLayout layout=(CCTableLayout)findViewById(R.id.mycclayout);


        MyTextView textView1=new MyTextView(this);
        textView1.setText("text view1");

        CCTableLayout.CCLayoutParams params=layout.new CCLayoutParams(1,1);
        params.setCells(0, 0, 0, 1);
        params.setMargins(20, 30, 20, 30);
        textView1.setLayoutParams(params);
        textView1.setBackgroundColor(0xffff0000);
        layout.addView(textView1);


        final  MyTextView textView2=new MyTextView(this);
        params=layout.new CCLayoutParams(1,1);
        params.setCells(1, 2, 1, 2);
        params.setMargins(10, 30, 10, 20);
        textView2.setLayoutParams(params);
        textView2.setText("TextView2");
        textView2.setBackgroundColor(0xffff0000);
        layout.addView(textView2);

        final  MyTextView textView3=new MyTextView(this);
        params=layout.new CCLayoutParams(1,1);
        params.setCells(2, 2, 2, 2);
        params.setMargins(10, 30, 10, 20);
        textView3.setLayoutParams(params);
        textView3.setText("TextView3");
        textView3.setBackgroundColor(0xffff0000);
        layout.addView(textView3);

        final  MyTextView textView4=new MyTextView(this);
        params=layout.new CCLayoutParams(1,1);
        params.setCells(3, 2, 3, 2);
        params.setMargins(10, 30, 10, 20);
        textView4.setLayoutParams(params);
        textView4.setText("TextView4");
        textView4.setBackgroundColor(0xffff0000);
        layout.addView(textView4);

//        textView4.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });


        final  MyTextView textView5=new MyTextView(this);
        params=layout.new CCLayoutParams(1,1);
        params.setCells(1, 1, 1, 1);
        params.setMargins(10,30,10,20);
        textView5.setLayoutParams(params);
        textView5.setText("TextView5");
        textView5.setBackgroundColor(0xffff0000);
        layout.addView(textView5);

//        final  MyTextView textView6=new MyTextView(this);
//        params=layout.new CCLayoutParams(1,1);
//        params.setCells(1, 0, 1, 0);
//        params.setMargins(10,30,10,20);
//        textView6.setLayoutParams(params);
//        textView6.setText("TextView6");
//        textView6.setBackgroundColor(0xffff0000);
//        layout.addView(textView6);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LauncherLayout launcherLayout
                Rect rect=new Rect(320,500,410,700);
                //layout.kickViews(rect,textView2);


//                LayoutInflater inflater = CCLayoutTestActivity.this.getLayoutInflater();
//                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.home_grid_item, layout,false);
//
//                ImageView icon = (ImageView) linearLayout.findViewById(R.id.appIcon);
//                icon.setImageResource(R.drawable.ic_search_black_24dp);
//                ((CCTableLayout.CCLayoutParams)linearLayout.getLayoutParams()).setCells(1,0,3,3);
//                layout.addView(linearLayout);

            }
        });




        Log.i("event", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("event", "onResume");
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("event", "onAttachedToWindow");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("event", "onWindowFocusChanged");
    }
}
