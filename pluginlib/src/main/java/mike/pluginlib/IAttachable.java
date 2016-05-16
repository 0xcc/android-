package mike.pluginlib;

/**
 * Created by Administrator on 16-5-15.
 */
public interface IAttachable<T> {
    public void attach(T proxy,PluginApk apk);
}
