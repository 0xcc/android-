package mike.mylauncher.components;

/**
 * Created by Administrator on 16-1-5.
 */
public class AppItemsFactory {

    public static IAppSaveConfig IAppSaveConfig(boolean fromFile){
        if (fromFile==true){
            return new FileAppSaveConfig("");
        }else {
            return null;
        }
    }

}
