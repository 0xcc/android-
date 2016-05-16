package mike.pluginlib;

import android.content.pm.PackageInfo;
import android.content.res.Resources;

/**
 * Created by Administrator on 16-5-15.
 */
public class PluginApk {
    public Resources resources;
    public ClassLoader classLoader;
    public PackageInfo packageInfo;

    public PluginApk(Resources res){
        this.resources=res;
    }
}
