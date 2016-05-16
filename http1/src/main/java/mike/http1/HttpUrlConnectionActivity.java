package mike.http1;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;


public class HttpUrlConnectionActivity extends AppCompatActivity {

    private TextView textview;
    private void readWiki(Message msg){
        //String wikiSearchURL="https://zh.wikipedia.org/w/api.php?action=opensearch&search=Android";
        String wikiSearchURL="https://api.douban.com/v2/book/search?q=android&start=0&count=2";
        Bundle bundle=new Bundle();
        try{
            URL url=new URL(wikiSearchURL);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){

                msg.arg1=HttpURLConnection.HTTP_OK;
                InputStreamReader isr=new InputStreamReader(httpURLConnection.getInputStream());
                int i=0;
                String content="";
                while ((i=isr.read())!=-1){
                    content=content+(char)i;
                }

                bundle.putString("content",content);
                msg.setData(bundle);
                isr.close();
                handler.sendMessage(msg);
            }
        }catch (Exception e){
            bundle.putString("error",e.getMessage());
            msg.setData(bundle);
            //e.printStackTrace();
            Log.e("error",e.getMessage());
            handler.sendMessage(msg);
        }

    }


    private void readWiki2(Message msg){
        String wikiSearchURL="https://api.douban.com/v2/book/search?q=大数据&start=0&count=10";
        OkHttpClient httpClient=new OkHttpClient();

        Request request=new Request.Builder().url(wikiSearchURL).build();
        try {
            Response response=httpClient.newCall(request).execute();
            Bundle bundle=new Bundle();
            if (response.isSuccessful()){
                msg.arg1=HttpURLConnection.HTTP_OK;
                bundle.putString("content",response.body().string());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }else{

                bundle.putString("error", response.message());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }catch (IOException e){

        }
        //DefaultHttpClient
    }


    private void readPostData(Message msg){
        try{
            String postUrl="http://192.168.1.103/postdata.php";
            URL url=new URL(postUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.connect();

            DataOutputStream outputStream=new DataOutputStream(httpURLConnection.getOutputStream());
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("name="+ URLEncoder.encode("myname","utf-8"));
            stringBuilder.append("&province=" + URLEncoder.encode("浙江", "utf-8"));
            outputStream.write(stringBuilder.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            int response=httpURLConnection.getResponseCode();
            if (response==HttpURLConnection.HTTP_OK){
                InputStream inputStream=httpURLConnection.getInputStream();
                Map<String,List<String>> headerFields = httpURLConnection.getHeaderFields();
                Set<String> keys= headerFields.keySet();
                for (Iterator<String> keyIter=keys.iterator();keyIter.hasNext();){
                    String k=keyIter.next();
                    if (k==null){
                        continue;
                    }
                    if (k.equals("Set-Cookie")){
                        List<String> values=headerFields.get(k);

                        for (int i=0;i<values.size();i++){
                            String value=values.get(i);
                            Log.i("value",value);
                        }

                    }
                }

                //StringBuilder result= new StringBuilder();
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[] data=new byte[1024];
                int len=0;
                while ((len=inputStream.read(data))!=-1){
                    byteArrayOutputStream.write(data);
                }
                String result=new String(byteArrayOutputStream.toByteArray());

                JSONObject jsonObject=new JSONObject(result);

               // JSONObject nameObject=jsonObject.getJSONObject("name");
                //JSONObject provinceObject=jsonObject.getJSONObject("province");

                String name=jsonObject.getString("name");
                String province=jsonObject.getString("province");

                Log.i("value",name);
                Log.i("value",province);
                
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    Handler handler=new Handler(){

        public void handleMessage(Message msg){
            if (msg.arg1==HttpURLConnection.HTTP_OK){
                Toast.makeText(getApplicationContext(),"链接成功", Toast.LENGTH_SHORT).show();

                String content=msg.getData().getString("content");
                try{
                    JSONObject jsonObject=new JSONObject(content);
                    StringBuilder result=new StringBuilder();
                    int count=jsonObject.getInt("count");
                    int start=jsonObject.getInt("start");
                    int total=jsonObject.getInt("total");
                    result.append("count:").append(count).append("\n")
                            .append(" start:").append(start).append("\n")
                            .append(" total:").append(total).append("\n\n");

                    JSONArray jsonArray=jsonObject.getJSONArray("books");

                    StringBuilder stringBuilder=new StringBuilder();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject book=jsonArray.getJSONObject(i);
                        String title=book.getString("title");
                        String pubdate=book.getString("pubdate");
                        String price=book.getString("price");

                        JSONArray authors=book.getJSONArray("author");
                        String author="";
                        for (int a=0;a<authors.length();a++){
                            if (a==authors.length()-1){
                                author=author+authors.getString(a);
                            }else{
                                author=author+authors.getString(a)+",";
                            }
                        }


                        stringBuilder.append("title:").append(title).append("\n")
                                .append("pubdate:").append(pubdate).append("\n")
                                .append("价格：").append(price).append("\n")
                                .append("author:").append(author).append("\n\n");
                    }

                    result.append(stringBuilder);

                    textview.setText(result.toString());
                }catch (JSONException err){

                }
           }else{
                Toast.makeText(getApplicationContext(),"链接失败",Toast.LENGTH_SHORT).show();
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview=(TextView)findViewById(R.id.showWiki);

        //new Thread(networkTask).start();
        //readWiki(msg);
        //new Thread(okhttpRequest).start();
        new Thread(postHttp).start();
    }

    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            Message msg=new Message();
            readWiki(msg);
        }
    };

    Runnable okhttpRequest=new Runnable() {

        @Override
        public void run() {
            Message msg=new Message();
            readWiki2(msg);
        }
    };

    String posturl="192.168.1.103/postdata.php";
    Runnable postHttp=new Runnable() {
        @Override
        public void run() {

            Message msg=new Message();
            readPostData(msg);
        }
    };

}
