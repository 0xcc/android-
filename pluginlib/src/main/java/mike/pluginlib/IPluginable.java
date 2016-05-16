package mike.pluginlib;

import android.os.Bundle;

/**
 * Created by Administrator on 16-5-15.
 */
public interface IPluginable {
    public void onCreate(Bundle bundle);

    public void onStart();

    public void onResume();

    public void onStop();

    public void onPause();

    public void onDestroy();
}
