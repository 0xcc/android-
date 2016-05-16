package mike.mymvp.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 16-5-6.
 */
public class VolleyRequest {

    private  static RequestQueue mRequestQueue;

    private  VolleyRequest(){

    }

    public  static void buildRequest(Context context){
        mRequestQueue= Volley.newRequestQueue(context);
    }

    public static VolleyRequest newInstance(){
        if (mRequestQueue==null){
            throw new NullPointerException("call buildRequest before calling newInstance");
        }
        return new VolleyRequest();
    }

    public <T> GsonRequest<T> newGsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                                             Response.ErrorListener errorListener){

        GsonRequest<T> request=new GsonRequest<T>(url,clazz,listener,errorListener);
        mRequestQueue.add(request);
        return request;
    }


}
