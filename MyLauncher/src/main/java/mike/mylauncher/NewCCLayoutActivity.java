package mike.mylauncher;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mike.dialogplus.DialogPlus;
import mike.dialogplus.GridHolder;
import mike.dialogplus.ListHolder;
import mike.dialogplus.OnItemClickListener;
import mike.mylauncher.CCTableLayout.CCLayout;
import mike.mylauncher.CCTableLayout.HomeViewPagerAdapter;
import mike.mylauncher.CCTableLayout.MyLinerLayout;
import mike.mylauncher.androidutils.ContentShare;
import mike.mylauncher.components.LaunchableActivity;
import mike.mylauncher.utils.MyLog;

public class NewCCLayoutActivity extends Activity {



    private CCLayout layout;
    LayoutInflater inflater;
    List<View> list=new ArrayList<>();
    LinearLayout pointsLayout;

    private AppWidgetHost mAppWidgetHost = null ;

    AppWidgetManager appWidgetManager = null;

    private static final int HOST_ID = 1024 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cclayout);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        pointsLayout=(LinearLayout)findViewById(R.id.points);

        ViewPager viewPager=(ViewPager)findViewById(R.id.viewPager);
        list.add(inflateCCLayout());
        list.add(inflateCCLayout());
        list.add(inflateCCLayout());
        list.add(inflateCCLayout());
        list.add(inflateCCLayout());
        HomeViewPagerAdapter pagerAdapter=new HomeViewPagerAdapter(this,list);
        viewPager.setAdapter(pagerAdapter);
        mAppWidgetHost = new AppWidgetHost(NewCCLayoutActivity.this, HOST_ID) ;
        mAppWidgetHost.startListening() ;
        appWidgetManager = AppWidgetManager.getInstance(NewCCLayoutActivity.this) ;

        addViews();

        viewPager.setOnPageChangeListener(mPageChangerListener);

        viewPager.setCurrentItem(0);
        setSelectedPoint(0);
    }

    private void setSelectedPoint(int position){
        ImageView imageView;
        int childCnt=list.size();
        pointsLayout.removeAllViews();

        while (pointsLayout.getChildCount()<childCnt){
            imageView=new ImageView(NewCCLayoutActivity.this);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setImageResource(R.drawable.point_white);
            imageView.setLayoutParams(params);
            pointsLayout.addView(imageView);
        }


        imageView=(ImageView)pointsLayout.getChildAt(position);
        imageView.setImageResource(R.drawable.point_red);
    }

    ViewPager.OnPageChangeListener mPageChangerListener=new ViewPager.OnPageChangeListener() {

        private int mPageIdx=0;

        public int getPageIndex(){
            return mPageIdx;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setSelectedPoint(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private CCLayout inflateCCLayout(){
        inflater=(LayoutInflater)getLayoutInflater();
        CCLayout  ccLayout=(CCLayout)inflater.inflate(R.layout.workspace,null);
        return  ccLayout;
    }


    private void addViews(){
        PackageManager packageManager=getPackageManager();
        List<ResolveInfo> launchableActivities= ContentShare.getLaunchableResolveInfos(packageManager);

        int addedViewCnt=0;

        int cclayoutcnt=list.size();

        for (int k=0;k<cclayoutcnt;k++){
            layout=(CCLayout)list.get(k);
            int rows=layout.getRows();//4
            int columns=layout.getColumns(); //4

            for (int i=0;i<rows;i++){
                for (int j=0;j<columns;j++){
                    if(addedViewCnt<launchableActivities.size()){
                        ResolveInfo resolveInfo=launchableActivities.get(addedViewCnt++);
                        LaunchableActivity activityInfo = new LaunchableActivity(resolveInfo.activityInfo, resolveInfo.loadLabel(packageManager).toString(), false);
                        layout.addView(activityInfo, i, j, i + 1, j + 1);
                    }
                }
            }
        }

        List<AppWidgetProviderInfo> appWidgetProviderInfos= ContentShare.getAndroidWidget(this);

        AppWidgetProviderInfo info=appWidgetProviderInfos.get(0);

        layout=(CCLayout)list.get(list.size()-1);
        layout.setOnLongClickListener(mCCLayoutLongClick);
    }

    private static final int MY_REQUEST_APPWIDGET = 1;
    private static final int MY_CREATE_APPWIDGET = 2;


    private DialogPlus mDialogPlus;
    private DialogPlus mDialogAppSelect;

    private View.OnLongClickListener mCCLayoutLongClick=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            List<String> popItemName=new ArrayList<>();
            popItemName.add("app");
            popItemName.add("widget");
            popItemName.add("wallpaper");
            PopupDialogAdapter adapter=new PopupDialogAdapter(popItemName);
            if (mDialogPlus==null){
                mDialogPlus= DialogPlus.newDialog(NewCCLayoutActivity.this)
                        .setExpanded(false)
                        .setContentHolder(new GridHolder(2))
                        .setContentWidth(500)
                        .setAdapter(adapter)
                        .setGravity(Gravity.CENTER)
                        .create();
            }

            mDialogPlus.show();

            /*
            //选择widget
            Intent  pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK) ;
            int newAppWidgetId = mAppWidgetHost.allocateAppWidgetId() ;
            MyLog.i("newAppWidgetId", "The new allocate appWidgetId is ----> " + newAppWidgetId);
            pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, newAppWidgetId);
            startActivityForResult(pickIntent , MY_REQUEST_APPWIDGET) ;
            */
            return true;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_CANCELED)
            return ;

        switch (requestCode){
            case MY_REQUEST_APPWIDGET:
                MyLog.i("onActivityResult", "MY_REQUEST_APPWIDGET intent info is -----> " + data) ;
                int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID) ;
                Log.i("onActivityResult", "MY_REQUEST_APPWIDGET : appWidgetId is ----> " + appWidgetId) ;
                if (appWidgetId!=AppWidgetManager.INVALID_APPWIDGET_ID){
                    AppWidgetProviderInfo appWidgetProviderInfo=appWidgetManager.getAppWidgetInfo(appWidgetId);
                    if (appWidgetProviderInfo.configure!=null){
                        MyLog.i("onActivityResult", "The AppWidgetProviderInfo configure info -----> " + appWidgetProviderInfo.configure );
                        Intent intent  = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE) ;
                        intent.setComponent(appWidgetProviderInfo.configure);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        startActivityForResult(intent , MY_CREATE_APPWIDGET) ;
                    }else{
                        onActivityResult(MY_CREATE_APPWIDGET , RESULT_OK , data) ;
                    }
                }
                break;
            case MY_CREATE_APPWIDGET:
                completeAddAppWidget(data) ;
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void completeAddAppWidget(Intent data){
        Bundle extra = data.getExtras() ;
        int appWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1) ;
        MyLog.i("completeAddAppWidget", "completeAddAppWidget : appWidgetId is ----> " + appWidgetId) ;
        if(appWidgetId == -1){
            Toast.makeText(this,"invalid id",Toast.LENGTH_SHORT);
            return;
        }

        CCLayout ccLayout=(CCLayout)list.get(list.size()-1);
        ccLayout.addView(appWidgetManager,mAppWidgetHost,appWidgetId);
    }

    private class PopupDialogAdapter extends BaseAdapter {

        private List<String> mList;


        public PopupDialogAdapter(List<String> list) {
            mList=list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout layout;
            LayoutInflater inflater=(LayoutInflater)getLayoutInflater();
            layout=(LinearLayout)inflater.inflate(R.layout.popup_item,null);
            ImageView imageView=(ImageView)layout.findViewById(R.id.popdialog_item);

            String name=mList.get(position);

            switch (name){
                case "app":
                    imageView.setImageResource(R.drawable.app_add);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mDialogAppSelect==null){
                                List<ResolveInfo> resolveInfos= ContentShare.getLaunchableResolveInfos(getPackageManager());
                                mDialogAppSelect=DialogPlus.newDialog(NewCCLayoutActivity.this)
                                        .setExpanded(true)
                                        .setGravity(Gravity.CENTER)
                                        .setContentHolder(new GridHolder(2))
                                        .setContentWidth(500)
                                        .setAdapter(new AppSelectDialogAdapter(resolveInfos))
                                        .create();
                            }
                            MyLog.i("akda","asdkjfasd");
                            mDialogAppSelect.show();
                            mDialogPlus.dismiss();
                        }
                    });


                    break;
                case "widget":
                    imageView.setImageResource(R.drawable.widget_add);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                            int newAppWidgetId = mAppWidgetHost.allocateAppWidgetId();
                            MyLog.i("newAppWidgetId", "The new allocate appWidgetId is ----> " + newAppWidgetId);
                            pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, newAppWidgetId);
                            startActivityForResult(pickIntent, MY_REQUEST_APPWIDGET);
                            mDialogPlus.dismiss();
                        }
                    });

                    break;
                case "wallpaper":
                    imageView.setImageResource(R.drawable.wallpaper);
                    break;
            }


            return layout;
        }

    }


    private class AppSelectDialogAdapter extends BaseAdapter{
        List<ResolveInfo> launchableActivities;

        public AppSelectDialogAdapter(List<ResolveInfo> appList){
            launchableActivities=appList;
        }

        @Override
        public int getCount() {
            return launchableActivities.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Context context=NewCCLayoutActivity.this;
            ResolveInfo resolveInfo=launchableActivities.get(position);
            LaunchableActivity activityInfo = new LaunchableActivity(resolveInfo.activityInfo, resolveInfo.loadLabel(context.getPackageManager()).toString(), false);

            Drawable drawable = activityInfo.getActivityIcon(context.getPackageManager(), context, 100);
            String appName = activityInfo.getActivityLabel().toString();

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyLinerLayout linearLayout=(MyLinerLayout)layoutInflater.inflate(R.layout.app_item, null);

            ImageView imageView=(ImageView) linearLayout.findViewById(R.id.appImg);
            imageView.setImageDrawable(drawable);

            TextView textView = (TextView) linearLayout.findViewById(R.id.txtAppName);
            textView.setText(appName);


            return linearLayout;
        }
    }

}
