package mike.pluginhost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;
import mike.pluginlib.PluginManager;
import mike.pluginlib.consts.Constants;
import mike.pluginlib2.interfaces.DemoInterface;


public class MainActivity extends Activity {

    PluginManager pluginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // test1();

        PluginManager.init(getApplicationContext());

        pluginManager=PluginManager.getInstance();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //sdcard0/emulator/myplugin.apk
                //插件路径
                String pluginApkPath= Environment.getExternalStorageDirectory()+ File.separator+"plugins"+File.separator+"mymvp-debug.apk";
                String pluginClazz="mike.mymvp.ui.activity.WeatherActivity";
                String pluginPackage="mike.mymvp";
                Log.i("", "### 插件路径 : " + pluginApkPath);

                File file=new File(pluginApkPath);
                if (file.exists()) {
                    Log.d("", "### apk存在");
                } else {
                    Log.d("", "### no apk存在");
                }

                PluginManager.getInstance().loadApk(pluginApkPath);

                Intent intent=new Intent();
                intent.putExtra(Constants.PLUGIN_CLASS_NAME,pluginClazz);
                intent.putExtra(Constants.PACKAGE_NAME, pluginPackage);
                try {
                    pluginManager.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },1000);


    }

    private  void test1(){
        DemoInterface demoInterface=null;

        String path = Environment.getExternalStorageDirectory() + "/plugins/";
        String filename = "impl_dex.jar";

        String dexpath= this.getDir("dex",MODE_PRIVATE).getAbsolutePath();

        try{
            DexClassLoader classLoader = new DexClassLoader(path + filename, dexpath,
                    null, getClassLoader());
            Class mLoadClass = classLoader.loadClass("mike.pluginlib2.impl.DemoImpl");
            Constructor constructor = mLoadClass.getConstructor(new Class[] {});
            demoInterface=(DemoInterface)constructor.newInstance(new Object[] {});
            demoInterface.init(this);
            demoInterface.sayHello();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
