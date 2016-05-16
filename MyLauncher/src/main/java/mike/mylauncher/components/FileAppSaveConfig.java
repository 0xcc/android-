package mike.mylauncher.components;

/**
 * Created by Administrator on 16-1-5.
 */
public class FileAppSaveConfig implements IAppSaveConfig {

    String fileName;
    public FileAppSaveConfig(String fileName){
        this.fileName=fileName;
    }

    @Override
    public LaunchableActivity getList() {
        return null;
    }

    @Override
    public void save(String componentName, boolean isApp, int row1, int col1, int row2, int col2) {

    }

    @Override
    public void remove(String componentName, boolean isApp) {

    }
}
