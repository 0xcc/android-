package mike.mymvp.presenter.impl;

import mike.mymvp.model.IWeatherModel;
import mike.mymvp.model.entity.Weather;
import mike.mymvp.model.impl.WeatherModelImpl;
import mike.mymvp.presenter.IOnWeatherListener;
import mike.mymvp.presenter.IWeatherPresenter;
import mike.mymvp.ui.view.IWeatherView;

/**
 * Created by Administrator on 16-5-6.
 */
public class WeatherPresenterImpl implements IWeatherPresenter,IOnWeatherListener {
    private IWeatherView weatherView;
    private IWeatherModel weatherModel;

    public WeatherPresenterImpl(IWeatherView weatherView) {
        this.weatherView = weatherView;
        weatherModel = new WeatherModelImpl();
    }


    @Override
    public void onSuccess(Weather weather) {
        weatherView.hideLoading();
        weatherView.setWeatherInfo(weather);
    }

    @Override
    public void onError() {
        weatherView.hideLoading();
        weatherView.showError();
    }

    @Override
    public void getWeather(String cityNO) {
        weatherView.showLoading();
        weatherModel.loadWeather(cityNO,this);
    }

}
