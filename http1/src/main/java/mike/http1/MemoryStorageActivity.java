package mike.http1;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;


public class MemoryStorageActivity extends AppCompatActivity {

    TextView txtMeminfo=null;
    TextView txtreadfromfile=null;
    EditText txtFileContent=null;
    Button btn_save=null;
    Button btn_read=null;
    Button btn_sdcard_save=null;
    Button btn_sdcard_read=null;
    TextView txtSDCardInfo=null;

    private final String  infoSystem="/proc/meminfo";
    private final String fileName="abcd.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_storage);

        txtMeminfo=(TextView)findViewById(R.id.txtMemInfo);
        txtreadfromfile=(TextView)findViewById(R.id.txtreadfromfile);
        txtFileContent=(EditText)findViewById(R.id.txtFileContent);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_read=(Button)findViewById(R.id.btn_read);
        btn_sdcard_read=(Button)findViewById(R.id.btn_sdcard_read);
        btn_sdcard_save=(Button)findViewById(R.id.btn_sdcard_save);
        txtSDCardInfo=(TextView)findViewById(R.id.txtSDCardInfo);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        btn_sdcard_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savesdcard();
            }
        });

        btn_sdcard_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadsdcard();
            }
        });
    }

    private String getAvailMemory(){
        ActivityManager am=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(getBaseContext(),mi.availMem);
    }

    private String getTotalMemory(){
        String str=null;
        String[] arrayOfString;
        long initial_memory=0;
        try{
            FileReader localReader=new FileReader(infoSystem);
            BufferedReader localBufferedReader=new BufferedReader(localReader,8192);
            str=localBufferedReader.readLine();
            arrayOfString=str.split("\\s+");
            initial_memory=Integer.valueOf(arrayOfString[1]).intValue()*1024;
            localBufferedReader.close();
        }catch (Exception e){

        }
        return  Formatter.formatFileSize(getBaseContext(),initial_memory);
    }

    private void save(){
        String content=txtFileContent.getText().toString();
        if (content.length()==0){
            return;
        }

        try {
            FileOutputStream outputStream=openFileOutput(fileName, Activity.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            txtMeminfo.setText("手机总内存:" + getTotalMemory() + " 可用内存" + getAvailMemory());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void load(){

        try {
            FileInputStream inputStream=this.openFileInput(fileName);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String content="";
            String data="";
            while ((data=bufferedReader.readLine())!=null){
                content+=data;
            }
            txtFileContent.setText(content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void savesdcard(){
        String content=txtFileContent.getText().toString();
        if (content.length()==0){
            return;
        }

        try {
            File file=new File(Environment.getExternalStorageDirectory(),fileName);
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            sdcardInfo();
        }catch (Exception e){

        }


    }

    private void loadsdcard(){

    }

    private void sdcardInfo(){
        String sdcardString=Environment.getExternalStorageState();
        if (sdcardString.equals(Environment.MEDIA_MOUNTED)){
            File pathFile=Environment.getExternalStorageDirectory();
            android.os.StatFs statfs=new StatFs(pathFile.getPath());
            long nTotalBlocks=statfs.getBlockCount();
            long nBlockSize=statfs.getBlockSize();
            long nAvalaBlock=statfs.getAvailableBlocks();
            long nSDTotalSize=nBlockSize*nTotalBlocks;
            long sdFreeSize=nAvalaBlock*nBlockSize;

            txtSDCardInfo.setText("blocks:"+nTotalBlocks +" blocksize:"+nBlockSize+"\n avaliable block:"+nAvalaBlock+"\n sdtotalsize:"+nSDTotalSize+"\n freesize:"+sdFreeSize);
        }

    }
}
