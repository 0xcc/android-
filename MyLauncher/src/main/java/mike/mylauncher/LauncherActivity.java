package mike.mylauncher;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import mike.mylauncher.Adapters.ActivityInfoArrayAdapter;
import mike.mylauncher.androidutils.ImageLoadingTask;
import mike.mylauncher.components.LaunchableActivity;
import mike.mylauncher.androidutils.ContentShare;
import mike.mylauncher.androidutils.StatusBarColorHelper;
import mike.mylauncher.utils.SimpleTaskConsumerManager;
import mike.mylauncher.utils.Trie;

public class LauncherActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int sInitialArrayListSize = 300;
    private final Pattern mPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private Context mContext;
    private AdapterView mAppListView;
    private ArrayAdapter<LaunchableActivity> mArrayAdapter;
    private ArrayList<LaunchableActivity> mActivityInfos;
    private ArrayList<LaunchableActivity> mShareableActivityInfos;
    private Trie<LaunchableActivity> mTrie;
    private HashMap<String, List<LaunchableActivity>> mLaunchableActivityPackageNameHashMap;

    private PackageManager mPm;
    private InputMethodManager mInputMethodManager;
    private SimpleTaskConsumerManager mImageLoadingConsumersManager;
    private ImageLoadingTask.SharedData mImageTasksSharedData;
    //private int mStatusBarHeight;

    //used only in function getAllSubwords. they are here as class fields to avoid
    // object re-allocation.
    private StringBuilder mWordSinceLastSpaceBuilder;
    private StringBuilder mWordSinceLastCapitalBuilder;
    ;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mPm=getPackageManager();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        final Resources resources=getResources();

        mAppListView = (GridView) findViewById(R.id.appsContainer);
        mContext=getApplicationContext();

        mLaunchableActivityPackageNameHashMap=new HashMap<>();
        mActivityInfos = new ArrayList<>(sInitialArrayListSize);
        mShareableActivityInfos = new ArrayList<>(sInitialArrayListSize);
        mTrie = new Trie<>();
        mWordSinceLastSpaceBuilder = new StringBuilder(64);
        mWordSinceLastCapitalBuilder = new StringBuilder(64);

        findViewById(R.id.topFillerView).getLayoutParams().height=StatusBarColorHelper.getStatusBarHeight(resources)*2;

        setupImageLoadingThreads();
        //loadShareableApps();
        loadLaunchableApps();


        setupViews();
    }

    @Override
    protected void onDestroy() {
        if (mImageLoadingConsumersManager != null)
            mImageLoadingConsumersManager.destroyAllConsumers(false);
        super.onDestroy();
    }

    public void onClickSettingsButton(View view) {
        showPopup(findViewById(R.id.overflow_button_topleft));
    }

    public void showPopup(View v) {
        final PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupEventListener());
        final MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.launcher_activity_menu, popup.getMenu());
        popup.show();
    }

    class PopupEventListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return onOptionsItemSelected(item);
        }
    }


    private void loadShareableApps() {
        List<ResolveInfo> infoList = ContentShare.getTextReceivers(mPm);

        for (ResolveInfo info : infoList) {
            final LaunchableActivity launchableActivity = new LaunchableActivity(
                    info.activityInfo, info.loadLabel(mPm).toString(), true);
            mShareableActivityInfos.add(launchableActivity);
        }

        updateApps(mShareableActivityInfos, false);
    }

    private void loadLaunchableApps(){
        List<ResolveInfo> launchableActivities= ContentShare.getLaunchableResolveInfos(mPm);
        mArrayAdapter=new ActivityInfoArrayAdapter(this,R.layout.app_grid_item,mActivityInfos,
                mImageLoadingConsumersManager,mImageTasksSharedData);

        ArrayList<LaunchableActivity>  launchablesFromResolve = new ArrayList<>(launchableActivities.size());
        for (ResolveInfo resolveInfo: launchableActivities){
            final  LaunchableActivity launchableActivity=new LaunchableActivity(resolveInfo.activityInfo,
                    resolveInfo.activityInfo.loadLabel(mPm).toString(), false);
            launchablesFromResolve.add(launchableActivity);
        }

        updateApps(launchablesFromResolve, true);
    }

    private void updateApps(final List<LaunchableActivity> updatedActivityInfos, boolean addToTrie){

        for (LaunchableActivity launchableActivity:updatedActivityInfos){
            final  String packageName=launchableActivity.getComponent().getPackageName();
            mLaunchableActivityPackageNameHashMap.remove(packageName);
        }


        for (LaunchableActivity launchableActivity: updatedActivityInfos){
            final String className = launchableActivity.getComponent().getClassName();
            //排除自己
            if (className.equals(this.getClass().getCanonicalName())) {
                continue;
            }

            if (addToTrie) {
                final String activityLabel = launchableActivity.getActivityLabel().toString();
                final List<String> subwords = getAllSubwords(stripAccents(activityLabel));
                for (String subword : subwords) {
                    mTrie.put(subword, launchableActivity);
                }
            }

            final  String packageName=launchableActivity.getComponent().getPackageName();

            List<LaunchableActivity> launchableActivitiesToUpdate=mLaunchableActivityPackageNameHashMap.remove(packageName);
            if (launchableActivitiesToUpdate==null){
                launchableActivitiesToUpdate=new LinkedList<>();
            }
            launchableActivitiesToUpdate.add(launchableActivity);
            mLaunchableActivityPackageNameHashMap.put(packageName, launchableActivitiesToUpdate);
        }

        //// TODO: 15-11-17
        // mLaunchableActivityPrefs.setAllPreferences(updatedActivityInfos);
        Log.d("SearchActivity", "updated activities: " + updatedActivityInfos.size());

        updateVisibleApps();
    }

    private void updateVisibleApps(){

        //先不过滤
        final HashSet<LaunchableActivity> infoList =
                mTrie.getAllStartingWith("");

        mActivityInfos.clear();
        mActivityInfos.addAll(infoList);
        mActivityInfos.addAll(mShareableActivityInfos);
        Collections.sort(mActivityInfos);
        Log.d("DEBUG_SEARCH", mActivityInfos.size() + "");

        mArrayAdapter.notifyDataSetChanged();
    }

    private void setupViews(){
        //背景
        ((ImageView) findViewById(R.id.backgroundView)).setImageDrawable(
                WallpaperManager.getInstance(this).getFastDrawable());
        //((ImageView)findViewById(R.id.backgroundView)).setImageDrawable(WallpaperManager.getInstance(this).getFastDrawable());
        mAppListView.setAdapter(mArrayAdapter);
        mAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchActivity(mActivityInfos.get(position - ((ActivityInfoArrayAdapter) mAppListView.getAdapter()).mColumnCount));

            }
        });


    }

    private void launchActivity(final LaunchableActivity launchableActivity){
        startActivity(launchableActivity.getLaunchIntent(""));
        launchableActivity.setLaunchTime();
    }

    private String stripAccents(final String s) {
        return mPattern.matcher(Normalizer.normalize(s, Normalizer.Form.NFKD)).replaceAll("");
    }

    private List<String> getAllSubwords(String line) {
        final ArrayList<String> subwords = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            final char character = line.charAt(i);

            if (Character.isUpperCase(character) || Character.isDigit(character)) {
                if (mWordSinceLastCapitalBuilder.length() > 1) {
                    subwords.add(mWordSinceLastCapitalBuilder.toString().toLowerCase());
                }
                mWordSinceLastCapitalBuilder.setLength(0);

            }
            if (Character.isSpaceChar(character)) {
                subwords.add(mWordSinceLastSpaceBuilder.toString().toLowerCase());
                if (mWordSinceLastCapitalBuilder.length() > 1 &&
                        mWordSinceLastCapitalBuilder.length() !=
                                mWordSinceLastSpaceBuilder.length()) {
                    subwords.add(mWordSinceLastCapitalBuilder.toString().toLowerCase());
                }
                mWordSinceLastCapitalBuilder.setLength(0);
                mWordSinceLastSpaceBuilder.setLength(0);
            } else {
                mWordSinceLastCapitalBuilder.append(character);
                mWordSinceLastSpaceBuilder.append(character);
            }
        }
        if (mWordSinceLastSpaceBuilder.length() > 0) {
            subwords.add(mWordSinceLastSpaceBuilder.toString().toLowerCase());
        }
        if (mWordSinceLastCapitalBuilder.length() > 1
                && mWordSinceLastCapitalBuilder.length() != mWordSinceLastSpaceBuilder.length()) {
            subwords.add(mWordSinceLastCapitalBuilder.toString().toLowerCase());
        }
        mWordSinceLastSpaceBuilder.setLength(0);
        mWordSinceLastCapitalBuilder.setLength(0);
        return subwords;
    }


    private void setupImageLoadingThreads(){
        final int maxThreads=Global.sMax_imageloading_threads;
        int numThreads=Runtime.getRuntime().availableProcessors()-1;
        if (numThreads<1){ numThreads=1;}
        else if (numThreads>7){numThreads=maxThreads;}

        mImageLoadingConsumersManager = new SimpleTaskConsumerManager(numThreads);
        mImageTasksSharedData = new ImageLoadingTask.SharedData(this, mPm, mContext, Global.sIconSizePixels);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
