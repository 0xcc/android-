package mike.mymvp.model.impl;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import mike.mymvp.app.MyApp;
import mike.mymvp.model.IWeatherModel;
import mike.mymvp.model.entity.Weather;
import mike.mymvp.presenter.IOnWeatherListener;
import mike.mymvp.util.VolleyRequest;

/**
 * Created by Administrator on 16-5-6.
 */
public class WeatherModelImpl implements IWeatherModel {
    @Override
    public void loadWeather(String cityNo, final IOnWeatherListener listener) {

        try {
            Request request= VolleyRequest.newInstance().newGsonRequest("http://www.weather.com.cn/data/sk/" + cityNo + ".html", Weather.class, new Response.Listener<Weather>() {
                @Override
                public void onResponse(Weather weather) {
                    if (weather!=null){
                        listener.onSuccess(weather);
                    }else{
                        listener.onError();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
