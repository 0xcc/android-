package mike.mylauncher.androidutils;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-11-16.
 */
public class ContentShare {


    public static List<AppWidgetProviderInfo> getAndroidWidget(Context context){

        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);

        List<AppWidgetProviderInfo> appWidgetProviderInfoList= appWidgetManager.getInstalledProviders();
        List<AppWidgetProviderInfo> result=new ArrayList<>();

        for (AppWidgetProviderInfo temp:appWidgetProviderInfoList){
            if (temp.minHeight>0 || temp.minWidth>0){
                result.add(temp);
            }
        }

        return  result;
    }

    public static List<ResolveInfo> getShortcutResolveInfos(PackageManager packageManager){
        final Intent intent =new Intent(Intent.ACTION_CREATE_SHORTCUT);
        return packageManager.queryIntentActivities(intent,0);
    }

    public  static List<ResolveInfo> getLaunchableResolveInfos(PackageManager packageManager){
        final Intent intent =new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return packageManager.queryIntentActivities(intent,0);
    }

    public static List<ResolveInfo> getTextReceivers(PackageManager pm) {


        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        final List<ResolveInfo> resolveInfos =
                pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfos;
    }

    public static Intent shareTextIntent(String text) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

}
