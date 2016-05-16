package mike.http1;

import android.app.Application;

/**
 * Created by Administrator on 16-5-1.
 */
public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {


    @Override
    public void onCreate() {
        super.onCreate();
        //Thread.setDefaultUncaughtExceptionHandler(this);

        ThreadGroup group=Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup=group;
        while (group!=null){
            topGroup=group;
            group=group.getParent();
        }
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];
        int actualSize = topGroup.enumerate(slackList);
        Thread[] list = new Thread[actualSize];
        System.arraycopy(slackList, 0, list, 0, actualSize);

        for (int i=0;i<actualSize;i++){
            list[i].setUncaughtExceptionHandler(this);
        }

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
    }
}
