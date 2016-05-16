package mike.pluginlib.proxy;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * Created by Administrator on 16-5-15.
 */
public class ActivityProxy  extends Activity {

    LifeCircleController mPluginController=new LifeCircleController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPluginController.onCreate(getIntent().getExtras());
    }

    @Override
    public Resources getResources(){
        Resources resources=mPluginController.getResources();
        return resources==null ? super.getResources(): resources;
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = mPluginController.getTheme();
        return theme == null ? super.getTheme() : mPluginController.getTheme();
    }

    @Override
    public AssetManager getAssets() {
        return mPluginController.getAssets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPluginController.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPluginController.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPluginController.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPluginController.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPluginController.onDestroy();
    }
}
