package mike.mylauncher.components;

/**
 * Created by Administrator on 16-1-5.
 */
public interface IAppSaveConfig {
    LaunchableActivity getList();
    void save(String componentName,boolean isApp,int row1,int col1,int row2,int col2);
    void remove(String componentName,boolean isApp);
}
