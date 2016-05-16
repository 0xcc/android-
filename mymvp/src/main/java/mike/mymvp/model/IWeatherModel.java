package mike.mymvp.model;

import mike.mymvp.presenter.IOnWeatherListener;

/**
 * Created by Administrator on 16-5-6.
 */
public interface IWeatherModel {
    void loadWeather(String cityNo,IOnWeatherListener listener);
}
