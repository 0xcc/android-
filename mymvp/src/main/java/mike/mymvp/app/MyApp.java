package mike.mymvp.app;

import android.app.Application;

import mike.mymvp.util.VolleyRequest;


/**
 * Created by Administrator on 16-5-6.
 */
public class MyApp extends Application implements Thread.UncaughtExceptionHandler {
    private static MyApp instance;

    public MyApp(){
        instance=this;
    }
    public static Application getContext(){
        return instance;
    }

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
