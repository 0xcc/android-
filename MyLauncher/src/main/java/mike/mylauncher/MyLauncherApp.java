package mike.mylauncher;

import android.app.Application;

import mike.mylauncher.utils.CrashHandler;

/**
 * Created by Administrator on 15-11-17.
 */
public class MyLauncherApp extends Application {



    @Override
    public void onCreate() {

        super.onCreate();
        CrashHandler crashHandler=CrashHandler.getInstance();
        crashHandler.Init(this);
    }
}
