package mike.pluginlib.proxy;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Constructor;

import mike.pluginlib.IPluginable;
import mike.pluginlib.PluginActivity;
import mike.pluginlib.PluginApk;
import mike.pluginlib.PluginManager;
import mike.pluginlib.consts.Constants;

/**
 * Created by Administrator on 16-5-15.
 */
public class LifeCircleController implements IPluginable {

    Activity mProxy;

    PluginActivity mPlugin;
    //Activity mPlugin;

    Resources mResources;
    Resources.Theme mTheme;
    PluginApk mPluginApk;
    String mPluginClazz;

    public LifeCircleController(Activity activity){
        mProxy=activity;
    }

    @Override
    public void onCreate(Bundle bundle) {
        mPluginClazz =bundle.getString(Constants.PLUGIN_CLASS_NAME);
        String packageName=bundle.getString(Constants.PACKAGE_NAME);
        Log.i("", "### plugin class : " + mPluginClazz + ", package name = " + packageName);
        mPluginApk= PluginManager.getInstance().getPluginApk(packageName);

        try {
            //插件对象
            //mPlugin=new PluginActivity();

            //Activity pluginActivity =(Activity)loadPluginable(mPluginApk.classLoader,mPluginClazz);

            mPlugin=(PluginActivity)loadPluginable(PluginActivity.class.getClassLoader(),mPluginClazz);

            //关联 插件对象 代理对象
            mPlugin.attach(mProxy,mPluginApk);
            Log.i("", "### 插件对象 mPlugin: " + mPlugin + ", apk res : " + mPluginApk.resources);
            mResources=mPluginApk.resources;
            mPlugin.onCreate(bundle);
        }catch (Exception e){
            Log.e("", "### 加载插件类失败 ");
            e.printStackTrace();
        }
    }

    //new 出插件对象
    private Object loadPluginable(ClassLoader classLoader,String pluginActivityClass) throws Exception{
        Class<?> pluginClz=classLoader.loadClass(pluginActivityClass);
        Constructor<?> constructor=pluginClz.getConstructor(new Class[]{});
        constructor.setAccessible(true);
        return constructor.newInstance(new Object[] {});
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    public Resources getResources(){
        return mResources;
    }

    public Resources.Theme getTheme() {
        return mTheme;
    }

    public AssetManager getAssets() {
        return mResources.getAssets();
    }

}
