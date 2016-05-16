package mike.http1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ToastViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_view);

        Button btn =(Button)findViewById(R.id.btnShowToast);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(ToastViewActivity.this, "hello toast", Toast.LENGTH_LONG);
                LinearLayout layout = (LinearLayout) toast.getView();

                Button button = new Button(ToastViewActivity.this);
                button.setText("akdjsjfkajd");
                layout.addView(button);
                toast.show();
                setupSpinner();

            }
        });

    }


    String[] arrayList={"abc","bcd","cde","def"};

    private void setupSpinner(){
        

        ArrayList<String> arrayList2=new ArrayList<String >();
        for (int i=0;i<arrayList.length;i++){
            arrayList2.add(arrayList[i]);
        }

        Spinner spinner1=(Spinner)findViewById(R.id.spinner1);
        Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(ToastViewActivity.this,android.R.layout.simple_spinner_item);
        arrayAdapter1.addAll(arrayList);
        spinner1.setAdapter(arrayAdapter1);

        ArrayAdapter<CharSequence> arrayAdapter2=ArrayAdapter.createFromResource(this,R.array.countries,android.R.layout.simple_spinner_item);

        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(arrayAdapter2);

        ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        String[] mimeType={"text"};

        ClipData.Item item=new ClipData.Item("aksjdk");
        ClipData clipData=  new ClipData("abc",mimeType,item);

        ClipData.Item item1=new ClipData.Item("kfddj");

        clipData.addItem(item1);

        clipboardManager.setPrimaryClip(clipData);

        Intent intent=new Intent(this,ClipBoardActivity.class);
        startActivityForResult(intent,2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==22&&requestCode==2){
            String resultdata=data.getStringExtra("abc");
        }
    }
}
