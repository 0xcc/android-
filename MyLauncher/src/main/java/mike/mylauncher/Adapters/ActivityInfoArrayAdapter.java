package mike.mylauncher.Adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mike.mylauncher.R;
import mike.mylauncher.androidutils.ImageLoadingTask;
import mike.mylauncher.androidutils.StatusBarColorHelper;
import mike.mylauncher.components.LaunchableActivity;
import mike.mylauncher.utils.SimpleTaskConsumerManager;

/**
 * Created by Administrator on 15-11-16.
 */
public class ActivityInfoArrayAdapter extends ArrayAdapter<LaunchableActivity> {

    private static final int sNavigationBarHeightMultiplier = 1;
    private static final int sGridViewTopRowExtraPaddingInDP = 78;
    private static final int sMarginFromNavigationBarInDp = 16;
    private static final int sGridItemHeightInDp = 96;


    final LayoutInflater inflater;
    SimpleTaskConsumerManager mImageLoadingConsumersManager;
    private ImageLoadingTask.SharedData mImageTasksSharedData;

    public int mColumnCount;

    //gridView顶部空间
    private int mGridViewTopRowHeight;

    //gridview底部空间
    private int mGridViewBottomRowHeight;

    private int mIconSizePixels;

    public ActivityInfoArrayAdapter(final Activity context,final int resource,
                                    final List<LaunchableActivity> activityInfos,
                                    SimpleTaskConsumerManager mImageLoadingConsumersManager,
                                    ImageLoadingTask.SharedData mImageTasksSharedData){

        super(context,resource,activityInfos);
        inflater=context.getLayoutInflater();
        this.mImageLoadingConsumersManager=mImageLoadingConsumersManager;
        this.mImageTasksSharedData=mImageTasksSharedData;


        Resources resources=context.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float displayDensity=displayMetrics.density;

        final int gridViewTopRowExtraPaddingInPixels = Math.round(displayDensity * sGridViewTopRowExtraPaddingInDP);
        final int marginFromNavigationBarInPixels = Math.round(displayDensity * sMarginFromNavigationBarInDp);

        final int gridItemHeightInPixels = Math.round(displayDensity * sGridItemHeightInDp);

        mGridViewTopRowHeight = StatusBarColorHelper.getStatusBarHeight(resources) + gridViewTopRowExtraPaddingInPixels;
        mGridViewBottomRowHeight = gridItemHeightInPixels + sNavigationBarHeightMultiplier *
                StatusBarColorHelper.getNavigationBarHeight(resources) + marginFromNavigationBarInPixels;

        //每行多少个item
        float dpWidth = displayMetrics.widthPixels / displayDensity;
        final float gridItemWidth = 72;//TODO remove magic number
        mColumnCount = (int) (dpWidth / gridItemWidth) - 1;
    }

    @Override
    public int getCount(){ return super.getCount()+mColumnCount;}

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        final View view=convertView !=null? convertView: inflater.inflate(R.layout.app_grid_item,parent,false);
        final AbsListView.LayoutParams params=(AbsListView.LayoutParams)view.getLayoutParams();

        //if (position<mCol)
        if (position<mColumnCount){
            params.height = mGridViewTopRowHeight;
            view.setLayoutParams(params);
            view.setVisibility(View.INVISIBLE);
            return view;
        }

        if (position == (getCount() - 1)) {
            //最后一个撑出底部空余空间
            params.height = mGridViewBottomRowHeight;
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        view.setLayoutParams(params);
        view.setVisibility(View.VISIBLE);

        final LaunchableActivity launchableActivity = getItem(position - mColumnCount);
        final CharSequence label = launchableActivity.getActivityLabel();
        final TextView appLabelView = (TextView) view.findViewById(R.id.appLabel);
        final ImageView appIconView = (ImageView) view.findViewById(R.id.appIcon);
        final View appShareIndicator = view.findViewById(R.id.appShareIndicator);

        appLabelView.setText(label);
        appIconView.setTag(launchableActivity);

        //加载图片
        if (!launchableActivity.isIconLoaded()){
            mImageLoadingConsumersManager.addTask(new ImageLoadingTask(appIconView,launchableActivity,mImageTasksSharedData));
        }else{

                Context ctx=getContext();
                appIconView.setImageDrawable(
                        launchableActivity.getActivityIcon(ctx.getPackageManager(), ctx, mIconSizePixels));
        }

        return view;
    }


}
