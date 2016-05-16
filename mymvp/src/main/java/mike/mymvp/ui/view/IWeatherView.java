package mike.mymvp.ui.view;

import mike.mymvp.model.entity.Weather;
import mike.mymvp.model.entity.WeatherInfo;

/**
 * Created by Administrator on 16-5-6.
 */
public interface IWeatherView {
    void showLoading();
    void hideLoading();
    void showError();
    void setWeatherInfo(Weather weather);
}
