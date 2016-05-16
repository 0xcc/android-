package mike.http1;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.EventListener;

public class StorageDirActivity extends AppCompatActivity {
    StringBuilder value=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_dir);

        append("Environment.getDataDirectory()", Environment.getDataDirectory().getAbsolutePath());
        append("Environment.getDownloadCacheDirectory()",Environment.getDownloadCacheDirectory().getAbsolutePath());
        append("Environment.getExternalStorageDirectory()",Environment.getExternalStorageDirectory().getAbsolutePath());
        append("Environment.getRootDirectory()",Environment.getRootDirectory().getAbsolutePath());
        append("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        append("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath());
        append("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        append("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        append("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());

        append("Context.getCacheDir",getBaseContext().getCacheDir().getAbsolutePath());
        append("Context.getDatabasePath", getBaseContext().getDatabasePath(HerosDB.DATABASE_NAME).getAbsolutePath());
        append("Context.getExternalCacheDir().getAbsolutePath()",getBaseContext().getExternalCacheDir().getAbsolutePath());
        //append("Context.getCodeCacheDir().getAbsolutePath()",getBaseContext().getCodeCacheDir().getAbsolutePath());
        append("Context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)",getBaseContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        append("Context.getDir(a.txt)",getBaseContext().getDir("a.txt", Context.MODE_WORLD_READABLE).getAbsolutePath());
        append("Context.getobbDir()",getBaseContext().getObbDir().getAbsolutePath());
        append("Context.getFilesDir()",getBaseContext().getFilesDir().getAbsolutePath());
         

        TextView txtView=(TextView)findViewById(R.id.txtView);
        txtView.setText(value.toString());
    }

    private void append(String funcname,String v){
        value.append(funcname+"  : \n "+v+"\n");
    }
}
