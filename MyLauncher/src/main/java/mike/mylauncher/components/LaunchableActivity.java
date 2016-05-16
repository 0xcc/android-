package mike.mylauncher.components;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.net.FileNameMap;

import mike.mylauncher.androidutils.ContentShare;

/**
 * Created by Administrator on 15-11-16.
 */
public class LaunchableActivity implements Comparable<LaunchableActivity> {
    private final ActivityInfo mActivityInfo;
    private final String mActivityLabel;
    private final ComponentName mComponentName;
    private Intent mLaunchIntent;
    private long lastLaunchTime;
    private boolean mShareable;
    private Drawable mActivityIcon;
    private boolean mFavorite;


    public LaunchableActivity(final ActivityInfo activityInfo, final String activityLabel,
                              final boolean isShareable) {
        this.mActivityInfo = activityInfo;
        this.mActivityLabel = activityLabel;
        mComponentName = new ComponentName(activityInfo.packageName, activityInfo.name);
        this.mShareable = isShareable;
    }
    public LaunchableActivity(final ComponentName componentName, final String label,
                              final Drawable activityIcon, final Intent launchIntent){
        this.mComponentName = componentName;
        this.mActivityLabel = label;
        this.mLaunchIntent = launchIntent;
        this.mActivityIcon = activityIcon;
        this.mActivityInfo = null;
    }

    public Intent getLaunchIntent(final String searchString) {
        if (mLaunchIntent != null)
            return mLaunchIntent;
        if (isShareable()) {
            final Intent launchIntent = ContentShare.shareTextIntent(searchString);
            launchIntent.setComponent(getComponent());
            return launchIntent;
        }
        final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.setComponent(mComponentName);
        return launchIntent;
    }

    public void setLaunchTime() {
        lastLaunchTime = System.currentTimeMillis() / 1000;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(final boolean favorite) {
        this.mFavorite = favorite;
    }

    public long getLaunchTime() {
        return lastLaunchTime;
    }

    public void setLaunchTime(long timestamp) {
        lastLaunchTime = timestamp;
    }

    public CharSequence getActivityLabel() {
        return mActivityLabel;
    }

    public boolean isIconLoaded() {
        return mActivityIcon != null;
    }

    public synchronized Drawable getActivityIcon(final PackageManager pm, final Context context,
                                                 final int iconSizePixels){
        if (!isIconLoaded()){
            Drawable _activityIcon=null;
            final ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            final int iconDpi=activityManager.getLauncherLargeIconDensity();
            try {
                _activityIcon=pm.getResourcesForActivity(mComponentName)
                        .getDrawableForDensity(mActivityInfo.getIconResource(), iconDpi);

            }catch (PackageManager.NameNotFoundException | Resources.NotFoundException e) {
            }

            if (_activityIcon==null){
                _activityIcon=Resources.getSystem().getDrawable(android.R.mipmap.sym_def_app_icon);
            }

            if (_activityIcon instanceof BitmapDrawable){
                //太大
                if (_activityIcon.getIntrinsicHeight()>iconSizePixels&&_activityIcon.getIntrinsicWidth()>iconSizePixels){
                    _activityIcon = new BitmapDrawable(
                            Bitmap.createScaledBitmap(((BitmapDrawable) _activityIcon).getBitmap()
                                    , iconSizePixels, iconSizePixels, false));
                }
            }
            mActivityIcon=_activityIcon;
        }
        return  mActivityIcon;

    }

    public boolean isShareable() {
        return mShareable;
    }

    public ComponentName getComponent() {
        return mComponentName;
    }


    @Override
    public int compareTo(LaunchableActivity another) {
        if (this.mShareable && !another.mShareable)
            return 1;
        if (!this.mShareable && another.mShareable)
            return -1;
        if (this.lastLaunchTime > another.lastLaunchTime)
            return -1;
        if (this.lastLaunchTime < another.lastLaunchTime)
            return 1;
        return this.mActivityLabel.compareTo(another.mActivityLabel);
    }
}
