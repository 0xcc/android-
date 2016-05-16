package mike.http1;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        Button btn=(Button)findViewById(R.id.btn_intent_test);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Intent intent=new Intent();
                    intent.setAction("xyz");
                    //intent.addCategory("category1");
                    intent.addCategory("category1");
                    intent.addCategory("android.intent.category.DEFAULT");
                    //intent.addCategory("android.intent.category.DEFAULT");
                    
                    //ComponentName name=new ComponentName("mike.http1","mike.http1.IntentFilterActivity");
                    //intent.setComponent(name);
                    IntentActivity.this.startActivity(intent);

                }catch (Exception err){
                    err.printStackTrace();
                }

            }
        });

    }
}
