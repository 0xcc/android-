package mike.pluginlib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;
import mike.pluginlib.consts.Constants;
import mike.pluginlib.proxy.ActivityProxy;

/**
 * Created by Administrator on 16-5-15.
 */
public class PluginManager {
    static  class PluginMgrHolder{
        static PluginManager sManager=new PluginManager();
    }

    static Context mContext;
    Map<String ,PluginApk> sMap=new HashMap<String,PluginApk>();

    public static void init(Context context){
        mContext=context.getApplicationContext();
    }

    public static PluginManager getInstance(){
        return PluginMgrHolder.sManager;
    }

    public PluginApk getPluginApk(String packageName){
        return sMap.get(packageName);
    }

    /*
    * 加载插件APK
    * @parm apkPath 插件的绝对路径
    * */

    public final void loadApk(String apkPath){
        PackageInfo packageInfo=queryPackageInfo(apkPath);
        if (packageInfo==null || TextUtils.isEmpty(packageInfo.packageName)){
            return ;
        }

        PluginApk pluginApk=sMap.get(packageInfo.packageName);

        if (pluginApk==null){
            pluginApk=createApk(apkPath);
            if (pluginApk!=null){
                pluginApk.packageInfo=packageInfo;
                sMap.put(packageInfo.packageName,pluginApk);
            }else{
                throw new NullPointerException("PluginApk is null!");
            }
        }

    }

    private PackageInfo queryPackageInfo(String apkPath){
        PackageInfo packageInfo=mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES|PackageManager.GET_SERVICES);
        return packageInfo;
    }

    public void startActivity(Intent intent){
        Intent myIntent =new Intent(mContext, ActivityProxy.class);
        Bundle extra=intent.getExtras();

        if (extra==null
                || !extra.containsKey(Constants.PLUGIN_CLASS_NAME)
                && !extra.containsKey(Constants.PACKAGE_NAME)){
            throw new IllegalArgumentException("没有设置插件的类路径和包名");
        }

        myIntent.putExtras(intent);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //启动apk
        mContext.startActivity(myIntent);
    }

    private PluginApk createApk(String apkPath){
        PluginApk pluginApk=null;

        try {
            AssetManager assetManager= AssetManager.class.newInstance();

            Method addAssetPath=assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,apkPath);

            Resources pluginRes=new Resources(assetManager,
                                                mContext.getResources().getDisplayMetrics(),
                                                mContext.getResources().getConfiguration()
                                            );
            pluginApk=new PluginApk(pluginRes);
            pluginApk.classLoader=createDexClassLoader(apkPath);

        }catch (Exception e){

        }
        return pluginApk;
    }

    private DexClassLoader createDexClassLoader(String apkPath){
        File dexOutputDir=mContext.getDir("dex",Context.MODE_PRIVATE);
        DexClassLoader loader=new DexClassLoader(apkPath,dexOutputDir.getAbsolutePath(),null,mContext.getClassLoader());
        return loader;
    }

}
