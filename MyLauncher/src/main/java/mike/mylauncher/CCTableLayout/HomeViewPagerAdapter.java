package mike.mylauncher.CCTableLayout;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 16-1-4.
 */
public class HomeViewPagerAdapter extends PagerAdapter {

    private List<View> listView;
    private Context context;
    public HomeViewPagerAdapter(Context context,List<View> listView){
        this.listView=listView;
        this.context=context;
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object==view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(listView.get(position));
        return listView.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listView.get(position));
    }
}
