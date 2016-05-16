package mike.mylauncher.CCTableLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import mike.mylauncher.R;
import mike.mylauncher.utils.MyLog;

/**
 * Created by Administrator on 16-1-3.
 */
public class MyLinerLayout extends LinearLayout {

    public MyLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinerLayout(Context context) {
        super(context);
    }

    public MyLinerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        MyLog.i("msg","MyLinerLayout.onDraw");
        View view=this;

        String msg="MyLinerLayout width:"+view.getWidth()+" height:"+view.getHeight()+" left:"+view.getLeft()+" right:"+getRight()+" top:"+getTop()+" bottom:"+getBottom()+" x:"+getX()+" y:"+getY();
        MyLog.i("msg",msg);
    }


    private InterceptEvent interceptEvent;

    public  void setInterceptEvent(InterceptEvent event){
        interceptEvent=event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (interceptEvent!=null){
            return  interceptEvent.onInterceptEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public static interface InterceptEvent{
        public boolean onInterceptEvent(MotionEvent event);
    }

}
