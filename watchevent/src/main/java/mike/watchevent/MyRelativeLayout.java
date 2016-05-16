package mike.watchevent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 15-12-21.
 */
public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int cnt=event.getPointerCount();
        
        for (int i=0;i<cnt;i++) {
            MotionEvent.PointerCoords coords=new MotionEvent.PointerCoords();
            event.getPointerCoords(i, coords);

            Log.i("dispatch", "MyRelativeLayout touchevent " + event.getPointerId(i) +" x:"+coords.getAxisValue(MotionEvent.AXIS_X));
        }
       return  super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("dispatch","MyRelativeLayout onInterceptTouchEvent");

        
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("dispatch","MyRelativeLayout onTouchEvent");
        return super.onTouchEvent(event);
    }
}
