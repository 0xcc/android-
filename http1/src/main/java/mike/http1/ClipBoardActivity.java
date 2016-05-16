package mike.http1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ClipBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_board);

        ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData=clipboardManager.getPrimaryClip();

        String string="";
        for (int i=0;i<clipData.getItemCount();i++){
            string+=clipData.getItemAt(i).getText()+"\n";
        }

        Toast.makeText(this,string,Toast.LENGTH_LONG).show();

        Intent intent=new Intent();
        intent.putExtra("abc","aabbcc");
        setResult(22,intent);
        //finish();
        //setResult();
        /*
        String[] mimeType={"text"};
        ClipData.Item item=new ClipData.Item("aksjdk");
        ClipData clipData=new ClipData("abc",mimeType,item);
        clipboardManager.setPrimaryClip(clipData);
        */
    }
}
