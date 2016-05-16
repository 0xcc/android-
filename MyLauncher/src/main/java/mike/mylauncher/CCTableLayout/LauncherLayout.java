package mike.mylauncher.CCTableLayout;

import android.content.ClipData;
import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import mike.mylauncher.utils.MyLog;

/**
 * Created by Administrator on 15-11-21.
 */
public class LauncherLayout extends CCTableLayout implements View.OnLongClickListener {

    public LauncherLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public LauncherLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public void addView(View child) {
        super.addView(child);
        child.setOnLongClickListener(this);


    }


    private View mDragingView;
    //private View.OnLongClickListener mChildLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            mDragingView=view;
            MyTextView textView=(MyTextView)view;
            MyLog.i("LongClick",textView.getText().toString());
            return true;

        }
   // };


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {



        //dispatchTouchEvent()
        int action=ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                MyLog.i("onInterceptTouchEvent"," down");
                return false;
            case MotionEvent.ACTION_MOVE:
                if (mDragingView==null){
                    MyLog.i("onInterceptTouchEvent"," move before");
                    return false;
                }else {
                    MyLog.i("onInterceptTouchEvent"," move");
                    return false;
                }
            case MotionEvent.ACTION_UP:
                mDragingView=null;
                return false;
        }

        return false;
    }


//    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDragingView!=null && event.getAction()==MotionEvent.ACTION_MOVE) {
            CCLayoutParams params=(CCLayoutParams)mDragingView.getLayoutParams();
            int eventX=(int)event.getX();
            int eventY=(int)event.getY();

            //dispatchTouchEvent()
            MyLog.i("onTouchEvent", " move mDragingView=" + (mDragingView != null ? true : false));
            return true;
        }
        //this.mFirst
        MyLog.i("onTouchEvent"," x:"+event.getX());
        return true;
    }
}