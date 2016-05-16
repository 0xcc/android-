package mike.mylauncher.utils;

import android.util.Log;

/**
 * Created by Administrator on 15-11-22.
 */
public class MyLog {
    public static void i(String tag,String msg){
        Log.i(tag,msg);
    }
    public  static void e(String tag,String msg){
        Log.e(tag,msg);
    }
}
