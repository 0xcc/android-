package mike.mymvp.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mike.mymvp.R;
import mike.mymvp.model.entity.Weather;
import mike.mymvp.model.entity.WeatherInfo;
import mike.mymvp.presenter.IWeatherPresenter;
import mike.mymvp.presenter.impl.WeatherPresenterImpl;
import mike.mymvp.ui.view.IWeatherView;
import mike.mymvp.util.VolleyRequest;
import mike.pluginlib.IAttachable;
import mike.pluginlib.IPluginable;
import mike.pluginlib.PluginActivity;



public class WeatherActivity extends /*Activity*/ PluginActivity  implements IWeatherView,View.OnClickListener {
    private Dialog loadingDialog;
    private EditText cityNOInput;
    private TextView city;
    private TextView cityNO;
    private TextView temp;
    private TextView wd;
    private TextView ws;
    private TextView sd;
    private TextView wse;
    private TextView time;
    private TextView njd;

    private IWeatherPresenter weatherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        VolleyRequest.buildRequest(this);
        init();
    }

    private void init(){
        cityNOInput = (EditText)findViewById(R.id.et_city_no);
        city = findView(R.id.tv_city);
        cityNO = findView(R.id.tv_city_no);
        temp = findView(R.id.tv_temp);
        wd = findView(R.id.tv_WD);
        ws = findView(R.id.tv_WS);
        sd = findView(R.id.tv_SD);
        wse = findView(R.id.tv_WSE);
        time = findView(R.id.tv_time);
        njd = findView(R.id.tv_njd);


        findViewById(R.id.btn_go).setOnClickListener(this);

        weatherPresenter=new WeatherPresenterImpl(this);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("加载天气中...");
    }

    private  TextView findView(int resid){
        TextView view=(TextView)findViewById(resid);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                weatherPresenter.getWeather(cityNOInput.getText().toString().trim());
                break;
        }
    }


    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showError() {
        //Do something
        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        WeatherInfo info = weather.getWeatherInfo();
        city.setText(info.getCity());
        cityNO.setText(info.getCityid());
        temp.setText(info.getTemp());
        wd.setText(info.getWD());
        ws.setText(info.getWS());
        sd.setText(info.getSD());
        wse.setText(info.getWS());
        time.setText(info.getTemp());
        njd.setText(info.getNjd());
    }
}
