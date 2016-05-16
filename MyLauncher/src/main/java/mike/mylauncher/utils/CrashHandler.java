package mike.mylauncher.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Administrator on 15-11-16.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG="CrashHandler";
    private  static final boolean DEBUG=true;

    private static final String PATH= Environment.getExternalStorageDirectory().getPath()+"/CrashLog/log/";
    private static final String FILE_NAME="crash";
    private static final String FILE_NAME_SUFFIX=".crash";

    private static CrashHandler sInstance=new CrashHandler();
    private static Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler(){

    }

    public static  CrashHandler getInstance(){
        return sInstance;
    }

    public void Init(Context context){
        mDefaultCrashHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.mContext=context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        printError(ex);
    }

    private void printError(Throwable ex){
        Log.i("crashed", ex.getMessage());
        ex.printStackTrace();
        StackTraceElement[] stackTraceElements= ex.getStackTrace();
        for (StackTraceElement stack : stackTraceElements){
            String msg=stack.getClassName()+"."+stack.getMethodName()+": line: "+stack.getLineNumber();
            System.out.println(msg);
            //Log.i("stack: ",msg);

        }

    }

}
