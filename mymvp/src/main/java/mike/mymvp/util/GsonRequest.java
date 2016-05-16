package mike.mymvp.util;

import android.content.pm.ResolveInfo;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 16-5-6.
 */
public class GsonRequest<T> extends Request<T> {
  private  final Response.Listener<T> mListener;

  private static Gson mGson=new Gson();

  private Class<T> mClass;
  private TypeToken<T> mTypeToken;

      public GsonRequest(int method,String url,Class<T> clazz,Response.Listener<T> listener,Response.ErrorListener errorListener){
        super(method,url,errorListener);
        mClass=clazz;
        mListener=listener;
      }

     public GsonRequest(int method, String url, TypeToken<T> typeToken, Response.Listener<T> listener,
                        Response.ErrorListener errorListener) {
      super(method, url, errorListener);
      mTypeToken = typeToken;
      mListener = listener;
     }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    public GsonRequest(String url, TypeToken<T> typeToken, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mTypeToken = typeToken;
        mListener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString=new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));
            if (mTypeToken==null){
               T obj= mGson.fromJson(jsonString,mClass);
                return Response.success(obj,HttpHeaderParser.parseCacheHeaders(response));
            }else{
                return (Response<T>) Response.success(mGson.fromJson(jsonString,mTypeToken.getType()),HttpHeaderParser.parseCacheHeaders(response));
            }

            //Thread.currentThread().interrupt();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
