package mike.pluginhost;

import android.app.Application;

/**
 * Created by Administrator on 16-5-16.
 */
public class MyApp extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.currentThread().setUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
    }
}
