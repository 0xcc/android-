package mike.watchevent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

public class IncludeLayoutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_include_layout);

        Button btn=(Button)findViewById(R.id.toggleBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStub viewStub=(ViewStub)findViewById(R.id.viewStub);

                if (viewStub!=null){
                   View view= viewStub.inflate();
                }else {
                    Log.i("info","None ViewStub");
                }
            }
        });

        //findViewById(R.id.viewStub).setVisibility(View.GONE);
    }

}
