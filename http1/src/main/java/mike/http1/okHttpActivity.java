package mike.http1;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;

import okio.BufferedSink;

public class okHttpActivity extends AppCompatActivity {

    Button btn_get=null;
    Button btn_post=null;
    Button btn_callback=null;
    Button btn_mediatype=null;
    Button btn_multi_part_builder=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        btn_get=(Button)findViewById(R.id.btn_get);
        btn_post=(Button)findViewById(R.id.btn_post);
        btn_callback=(Button)findViewById(R.id.btn_callback);
        btn_mediatype=(Button)findViewById(R.id.btn_mediatype);
        btn_multi_part_builder=(Button)findViewById(R.id.btn_multi_part_builder);

        btn_get.setOnClickListener(getbtnListener);
        btn_post.setOnClickListener(postbtnListener);
        btn_callback.setOnClickListener(btn_callbackListener);
        btn_mediatype.setOnClickListener(btn_mediatypeListener);
        btn_multi_part_builder.setOnClickListener(btn_multi_part_builderListener);


    }

    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    private View.OnClickListener btn_multi_part_builderListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {

                InputStream inputStream=getApplicationContext().getResources().openRawResource(R.raw.gdp2);
                int length=inputStream.available();
                byte[] buffer=new byte[length];
                inputStream.read(buffer);
                inputStream.close();

                FileOutputStream fileOutputStream= openFileOutput("a.jpg", Context.MODE_WORLD_WRITEABLE);
                fileOutputStream.write(buffer);
                fileOutputStream.close();

                File file=new File(getFilesDir().getPath(),"a.jpg");

                MultipartBuilder multipartBuilder=new MultipartBuilder();


                multipartBuilder.type(MultipartBuilder.FORM)
                        .addPart(

                                Headers.of("Content-Disposition", "form-data; name=title"),
                                RequestBody.create(null, "Square Logo"))
                        .addPart(
                                Headers.of("Content-Disposition", "form-data; name=myfile ;filename=myfile.jpg "),
                                RequestBody.create(MEDIA_TYPE_JPG, file)
                        );

                RequestBody requestBody=multipartBuilder.build();
                OkHttpClient okHttpClient=new OkHttpClient();
                Request request=new Request.Builder().url("http://192.168.1.103/postdata.php").post(requestBody).build();

                call=okHttpClient.newCall(request);

                new Thread(btn_multi_part_thread).start();
                //call.enqueue(btn_multi_part_callback);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    Call call;
    private Runnable btn_multi_part_thread=new Runnable() {
       // Call call;
        @Override
        public void run() {
            try{
                Response response=call.execute();
                ResponseBody responseBody= response.body();
                String string=responseBody.string();
                Log.i("string",string);
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    };

    private  Callback btn_multi_part_callback=new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Response response) throws IOException {

            String server=response.header("Server");
            String date=response.header("Date");
            String vary=response.header("Vary");
            Log.i("server",server);
            Log.i("date",date);
            Log.i("vary",vary);
        }
    };


    private View.OnClickListener btn_mediatypeListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                                        .url("https://api.github.com/repos/square/okhttp/issues")
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept","application/json; q=0.5")
                    .addHeader("Accept","application/vnd.github.v3+json")
                    .build();
            okHttpClient.newCall(request).enqueue(mediatypeCallback);

        }
    };

    private Callback mediatypeCallback=new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {
            String server=response.header("Server");
            String date=response.header("Date");
            String vary=response.header("Vary");
            Log.i("server",server);
            Log.i("date",date);
            Log.i("vary",vary);
        }
    };


    String posturl="http://192.168.1.103/postdata.php";
    String getUrl="http://192.168.1.103/postdata.php";
    private View.OnClickListener getbtnListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(getThread).start();
        }
    };

    Runnable getThread=new Runnable() {
        @Override
        public void run() {
            try{
                OkHttpClient okHttpClient=new OkHttpClient();
                Request.Builder requestBuilder=new Request.Builder();
                requestBuilder.url(getUrl);
                Call call= okHttpClient.newCall(requestBuilder.build());
                Response response= call.execute();

                if (response.isSuccessful()){
                    ResponseBody responseBody= response.body();
                    InputStream inputStream=responseBody.byteStream();
                    //InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

                    byte[] buffer=new byte[1024];
                    int len=0;
                    StringBuilder result=new StringBuilder();
                    while ((len=inputStream.read(buffer))!=-1){
                        result.append(new String(buffer));
                    }

                    String value=result.toString();
                    Log.i("value",value);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    private View.OnClickListener postbtnListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(postThread).start();
        }
    };

    private Runnable postThread=new Runnable() {
        @Override
        public void run() {
            try{
                OkHttpClient okHttpClient=new OkHttpClient();
                Request.Builder requestBuilder=new Request.Builder();
                requestBuilder.url(posturl);
                RequestBody requestBody=new FormEncodingBuilder().add("name","myname").add("province","浙江").build();

                requestBuilder.method("POST", requestBody);


                Request request= requestBuilder.build();

                Call call=okHttpClient.newCall(request);
                Response response= call.execute();

                if (response.isSuccessful()) {
                    ResponseBody responseBody= response.body();
                    InputStream inputStream=responseBody.byteStream();
                    byte[] bytes=new byte[1024];
                    int len=-1;
                    StringBuilder result=new StringBuilder();
                    while ((len=inputStream.read(bytes))!=-1){
                        //URLDecoder.decode( new String(bytes),"utf-8")
                        result.append(new String(bytes));
                    }

                    String string=result.toString(); //changeCharset(result.toString(),"ascii","utf-8"); //AsciiStringToString(result.toString());

                    Message msg=new Message();
                    msg.arg1=1;

                    msg.getData().putString("data",string);
                    handler.sendMessage(msg);
                    Log.i("value", string);
                }

            }catch (Exception e){

            }

        }
    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            try {
                if (msg.arg1==1){
                    TextView txtView=(TextView)findViewById(R.id.txtView);
                    String str=msg.getData().getString("data");
                    JSONObject jsonObject=new JSONObject(str);
                    String province=jsonObject.getString("province");
                    txtView.setText(province);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    };

    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    public String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    RequestBody requestBody=new RequestBody() {
        @Override
        public MediaType contentType() {
            return MediaType.parse("text");
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            //String postdata="name=myname&province="+ URLEncoder.encode("浙江","utf-8");
           // sink.writeUtf8(postdata);
        }
    };


    private View.OnClickListener  btn_callbackListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder().url("http://www.163.com").build();
            Call call= okHttpClient.newCall(request);
            call.enqueue(callback1);

        }
    };

    private  com.squareup.okhttp.Callback callback1=new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {
            String value= response.body().string()  ;
            Headers headers= response.headers();
            int size=headers.size();
            for (int i=0;i<size;i++){

            }

            Log.i("value",value);
        }
    };

}
